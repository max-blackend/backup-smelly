package com.yourorganization.magister_tool.detectors.redundant_code;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;

import javassist.expr.MethodCall;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;

public class AssertingObjectInitializationMultipleTimes extends VoidVisitorAdapter<Void> {

    private int issueCount = 0;
    private ArrayList<String> testsWithIncidence = new ArrayList<String>();
    private final JavaSymbolSolver symbolSolver;

    public int getIssueCount() {
        return issueCount;
    }

    public AssertingObjectInitializationMultipleTimes(TypeSolver typeSolver) {
        this.symbolSolver = new JavaSymbolSolver(typeSolver);
    }

    public ArrayList getIssueList() {
        testsWithIncidence.sort(String::compareToIgnoreCase);
        return testsWithIncidence;
    }

    private void addIssue(String testCase) {
        if (!testsWithIncidence.contains(testCase)) {
            testsWithIncidence.add(testCase);
        }
    }

    private List<MethodDeclaration> getAllTestMethods(ClassOrInterfaceDeclaration classDeclaration) {
        List<MethodDeclaration> testMethods = classDeclaration.getMethods().stream()
                .filter(method -> method.getNameAsString().startsWith("test")
                        || method.getNameAsString().startsWith("Test"))
                .collect(Collectors.toList());
        return testMethods;
    }

    private boolean isCheckingFieldsOnlyChangedInInitialization(MethodDeclaration testMethod) {
        List<List<String>> objectsCreatedNames = new ArrayList<>();
        for (VariableDeclarationExpr variableDeclarationExpr : testMethod.findAll(VariableDeclarationExpr.class)) {
            if (variableDeclarationExpr.getVariable(0).getInitializer().isPresent()) {
                String variableName = variableDeclarationExpr.getVariable(0).getNameAsString();
                Node variableValue = variableDeclarationExpr.getVariable(0).getInitializer().get();
                if (variableValue instanceof ObjectCreationExpr) {
                    objectsCreatedNames.add(new ArrayList<String>());
                    objectsCreatedNames.get(objectsCreatedNames.size() - 1).add(variableName);
                }

            }
        }

        // Now we check all the other lines of the test method where each object created
        // is calling a method
        for (MethodCallExpr methodCallExpr : testMethod.findAll(MethodCallExpr.class)) {
            // Check if the method call is calling a method of an object created in the test
            // method
            if (methodCallExpr.getScope().isPresent()) {
                // Get the name of the object created
                String objectCreatedName = methodCallExpr.getScope().get().toString();
                // Check if the object created is in the list of objects created without
                // parameters
                if (objectsCreatedNames.contains(objectCreatedName)) {
                    // Check if the method called is not a getter or setter

                    if (!methodCallExpr.getNameAsString().startsWith("get")
                            && !methodCallExpr.getNameAsString().startsWith("set")) {
                        return false;
                    } else if (methodCallExpr.getNameAsString().startsWith("get")
                            || methodCallExpr.getNameAsString().startsWith("is")) {
                        // If this line is inside an assert method call, we return true
                        if (methodCallExpr.getParentNode().isPresent()
                                && methodCallExpr.getParentNode().get() instanceof MethodCallExpr) {
                            MethodCallExpr parentMethodCallExpr = (MethodCallExpr) methodCallExpr.getParentNode().get();
                            if (parentMethodCallExpr.getNameAsString().startsWith("assert")) {
                                String nameOfObjectCallingMethod = methodCallExpr.getScope().get().toString();
                                for (MethodCallExpr methodCallExpr2 : testMethod.findAll(MethodCallExpr.class)) {
                                    if (methodCallExpr2.getScope().isPresent()) {
                                        if (methodCallExpr2.getScope().get().toString()
                                                .equals(nameOfObjectCallingMethod)) {
                                            if (!methodCallExpr2.getNameAsString().startsWith("get")) {
                                                return false;
                                            }
                                        }
                                    }
                                }
                                return true;
                            }
                        }
                        // If not, then we check if its being stored in a variable
                        else if (methodCallExpr.getParentNode().isPresent() && methodCallExpr.getParentNode().get()
                                .getClass().getName().contains("VariableDeclarator")) {
                            VariableDeclarator parentVariableDeclarator = (VariableDeclarator) methodCallExpr
                                    .getParentNode().get();
                            // Now we get the right side of the assignment
                            if (parentVariableDeclarator.getInitializer().isPresent()) {
                                Node rightSideOfAssignment = parentVariableDeclarator.getInitializer().get();
                                // If the right side of the assignment is a method call, then we check if it is
                                // a getter
                                if (rightSideOfAssignment instanceof MethodCallExpr) {
                                    MethodCallExpr rightSideMethodCallExpr = (MethodCallExpr) rightSideOfAssignment;
                                    // Now we get the name of the variable that calls the method
                                    String variableName = rightSideMethodCallExpr.getScope().get().toString();
                                    for (MethodCallExpr methodCallExpr2 : testMethod.findAll(MethodCallExpr.class)) {
                                        if (methodCallExpr2.getScope().isPresent()) {
                                            if (methodCallExpr2.getScope().get().toString().equals(variableName)) {
                                                if (!methodCallExpr2.getNameAsString().startsWith("get")) {
                                                    return false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            // if the variable is using a method that is not a getter, then we return false
                            for (MethodCallExpr methodCallExpr2 : testMethod.findAll(MethodCallExpr.class)) {
                                if (methodCallExpr2.getScope().isPresent()) {
                                    if (methodCallExpr2.getScope().get().toString()
                                            .equals(parentVariableDeclarator.getNameAsString())) {
                                        if (!methodCallExpr2.getNameAsString().startsWith("get")) {
                                            // We check if the call is inside an assert method call
                                            if (methodCallExpr2.getParentNode().isPresent() && methodCallExpr2
                                                    .getParentNode().get() instanceof MethodCallExpr) {
                                                MethodCallExpr parentMethodCallExpr = (MethodCallExpr) methodCallExpr2
                                                        .getParentNode().get();
                                                if (parentMethodCallExpr.getNameAsString().startsWith("assert")) {
                                                    return true;
                                                }
                                            }

                                            return false;
                                        }
                                    }
                                }
                            }
                            // If it is, then we check if the variable is being used in an assert method
                            // call
                            for (MethodCallExpr assertMethodCall : testMethod.findAll(MethodCallExpr.class).stream()
                                    .filter(methodCall -> methodCall.getNameAsString().toLowerCase()
                                            .startsWith("assert"))
                                    .collect(Collectors.toList())) {
                                List<String> assertMethodCallParameters = assertMethodCall.getArguments().stream()
                                        .map(parameter -> parameter.toString())
                                        .collect(Collectors.toList());
                                if (assertMethodCallParameters.contains(parentVariableDeclarator.getNameAsString())) {
                                    for (MethodCallExpr methodCallExpr2 : testMethod.findAll(MethodCallExpr.class)) {
                                        if (methodCallExpr2.getScope().isPresent() && !methodCallExpr2.getNameAsString()
                                                .toLowerCase().startsWith("assert")) {
                                            if (methodCallExpr2.getScope().get().toString()
                                                    .equals(parentVariableDeclarator.getNameAsString())) {
                                                if (!methodCallExpr2.getNameAsString().startsWith("get")) {
                                                    return false;
                                                }
                                            }
                                        }

                                    }

                                    return true;
                                }
                            }
                        }

                    }
                }
            }
        }
        return false;

    }

    // This method attempts to find the class declaration within the AST
    // Traverse up to the root of the AST and then search for the class
    @Override
    public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
        List<MethodDeclaration> testMethods = getAllTestMethods(classDeclaration);
        Map<String, Set<String>> initAssertsToTestsMap = new HashMap<>();
        Set<String> problematicTests = new HashSet<>();

        for (MethodDeclaration testMethod : testMethods) {
            HashMap<String, String> variableAssignments = new HashMap<>();
            List<ExpressionStmt> expressionStatements = testMethod.findAll(ExpressionStmt.class);

            if (isCheckingFieldsOnlyChangedInInitialization(testMethod)) {
                addIssue(testMethod.getNameAsString());

            }

            for (VariableDeclarationExpr variableDeclarationExpr : testMethod.findAll(VariableDeclarationExpr.class)) {
                // Check if the expression statement is an assignment
                if (variableDeclarationExpr.getVariable(0).getInitializer().isPresent()) {
                    // Get the variable name
                    String variableName = variableDeclarationExpr.getVariable(0).getNameAsString();
                    // Get the variable value
                    String variableValue = variableDeclarationExpr.getVariable(0).getInitializer().get().toString();
                    variableAssignments.put(variableName, variableValue);
                }
            }
            // Check all object creations
            List<ObjectCreationExpr> objectCreations = testMethod.findAll(ObjectCreationExpr.class);
            // Check all assert method calls
            List<MethodCallExpr> assertMethodCalls = testMethod.findAll(MethodCallExpr.class).stream()
                    .filter(methodCall -> methodCall.getNameAsString().toLowerCase().startsWith("assert"))
                    .collect(Collectors.toList());

            // Get all the parameters of the assert method calls used in the test method
            List<String> assertParameters = assertMethodCalls.stream()
                    .map(assertMethodCall -> assertMethodCall.getArguments().stream()
                            .map(parameter -> parameter.toString())
                            .collect(Collectors.toList()))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            for (ObjectCreationExpr objectCreation : objectCreations) {
                // Get the class name of the object creation
                String className = objectCreation.getTypeAsString();
                // Get parameters of the object creation
                List<String> parameters = objectCreation.getArguments().stream()
                        .map(parameter -> parameter.toString())
                        .collect(Collectors.toList());
                for (MethodCallExpr assertMethodCall : assertMethodCalls) {
                    // Get the assert method name
                    String assertMethodName = assertMethodCall.getNameAsString();
                    // Get the parameters of the assert method call
                    List<String> assertMethodCallParameters = assertMethodCall.getArguments().stream()
                            .map(parameter -> parameter.toString())
                            .collect(Collectors.toList());
                    // Check if the assert method call parameters are the same as the object
                    // creation parameters
                    // If any of the assert method call parameters is from the same class as the
                    // class being tested, then it is not an assert for the object creation

                    for (String assertMethodCallParameter : assertMethodCallParameters) {
                        if (parameters.contains(assertMethodCallParameter)
                                && parameters.indexOf(assertMethodCallParameter) != -1) {
                            // Now we check if assertMethodCallParameter is a variable, if it is, then we
                            // need to get the actual value of the variable
                            // Get the index of the parameter in the object creation
                            int parameterIndex = parameters.indexOf(assertMethodCallParameter);
                            // String assertKey = className + ":" + assertMethodName + ":" + parameterIndex
                            // + ":" + assertMethodCallParameter;
                            String assertKey = className + ":" + assertMethodName + ":" + parameterIndex + ":";
                            initAssertsToTestsMap.putIfAbsent(assertKey, new HashSet<>());
                            initAssertsToTestsMap.get(assertKey).add(testMethod.getNameAsString());
                        } else if (variableAssignments.containsKey(assertMethodCallParameter)) {
                            // Now we check if assertMethodCallParameter is a variable, if it is, then we
                            // need to get the actual value of the variable
                            // Get the index of the parameter in the object creation
                            ;
                            String variableValue = variableAssignments.get(assertMethodCallParameter);
                            String assertKey = assertMethodName + ":" + variableValue + ":" + assertMethodCallParameter;
                            initAssertsToTestsMap.putIfAbsent(assertKey, new HashSet<>());
                            initAssertsToTestsMap.get(assertKey).add(testMethod.getNameAsString());
                        }
                        // Now we track the real assignation of the variables used in the assert method
                        // call
                        // Check if the assert method call parameter is a variable
                        if (variableAssignments.containsKey(assertMethodCallParameter)
                                && parameters.indexOf(assertMethodCallParameter) != -1) {
                            // Get the variable value
                            String variableValue = variableAssignments.get(assertMethodCallParameter);
                            // Get the index of the parameter in the object creation
                            int parameterIndex = parameters.indexOf(variableValue);
                            String assertKey = className + ":" + assertMethodName + ":" + parameterIndex + ":"
                                    + variableValue;
                            initAssertsToTestsMap.putIfAbsent(assertKey, new HashSet<>());
                            initAssertsToTestsMap.get(assertKey).add(testMethod.getNameAsString());
                        }

                    }
                    // Check if the type of assert is not a assertNull or assertNotNull
                    // if (!assertMethodName.equals("assertNull") &&
                    // !assertMethodName.equals("assertNotNull")) {
                    if (!assertMethodName.equals("assertNotNull")) {
                        // Get the parameters used in the assert from assertMethodCall in its original
                        // format, not string
                        List<Expression> assertMethodCallParametersOriginalFormat = assertMethodCall.getArguments()
                                .stream().map(parameter -> parameter).collect(Collectors.toList());
                        for (Expression assertMethodCallParameterOriginalFormat : assertMethodCallParametersOriginalFormat) {
                            // Check if the assert method call parameter is a variable
                            if (variableAssignments
                                    .containsKey(assertMethodCallParameterOriginalFormat.toString().split("\\.")[0])) {
                                // Get the variable value
                                String variableValue = variableAssignments
                                        .get(assertMethodCallParameterOriginalFormat.toString().split("\\.")[0]);
                                // Get the index of the parameter in the object creation
                                int parameterIndex = parameters.indexOf(variableValue);
                                String assertKey = className + ":" + assertMethodName + ":" + parameterIndex + ":"
                                        + variableValue;
                                initAssertsToTestsMap.putIfAbsent(assertKey, new HashSet<>());
                                initAssertsToTestsMap.get(assertKey).add(testMethod.getNameAsString());
                            }
                        }

                        String assertKey = className + ":" + assertMethodName + ":"
                                + String.join(",", assertMethodCallParameters);
                        initAssertsToTestsMap.putIfAbsent(assertKey, new HashSet<>());
                        initAssertsToTestsMap.get(assertKey).add(testMethod.getNameAsString());
                    }

                }
            }

        }

        initAssertsToTestsMap.forEach((assertKey, testNames) -> {
            if (testNames.size() > 1) {
                problematicTests.addAll(testNames);
                for (String testName : testNames) {
                    addIssue(testName);
                }
            }
        });

        // Now we try to kill false positives:
        // 1) we look for the tests that are problematic and then we check if they have
        // any setter method call
        for (MethodDeclaration testMethod : testMethods) {
            if (testsWithIncidence.contains(testMethod.getNameAsString())) {
                for (MethodCallExpr methodCallExpr : testMethod.findAll(MethodCallExpr.class)) {
                    if (methodCallExpr.getNameAsString().startsWith("set")
                            || methodCallExpr.getNameAsString().startsWith("add")
                            || methodCallExpr.getNameAsString().startsWith("remove")
                            || methodCallExpr.getNameAsString().startsWith("toString")) {
                        testsWithIncidence.remove(testMethod.getNameAsString());
                    }
                }
            }

            List<MethodCallExpr> methodCalls = testMethod.findAll(MethodCallExpr.class);
            boolean anyMethodCallIsASetter = false;
            for (int i = 0; i < methodCalls.size(); i++) {
                if (methodCalls.get(i).getNameAsString().startsWith("assert")
                        || methodCalls.get(i).getNameAsString().startsWith("get")
                        || methodCalls.get(i).getNameAsString().startsWith("is")
                        || methodCalls.get(i).getNameAsString().startsWith("has")
                        || methodCalls.get(i).getNameAsString().startsWith("contains")) {
                } else {
                    anyMethodCallIsASetter = true;
                    break;
                }

            }
            if (!anyMethodCallIsASetter) {
                addIssue(testMethod.getNameAsString());
            }

        }
        for (MethodDeclaration testMethod: testMethods){
            List<String> argumentsObjectCreation = new ArrayList<>();
        for (ObjectCreationExpr objectCreation : testMethod.findAll(ObjectCreationExpr.class)) {

            for (Expression argument : objectCreation.getArguments()) {
                argumentsObjectCreation.add(argument.toString());
            }
        }
        
        for (MethodCallExpr methodCallExpr : testMethod.findAll(MethodCallExpr.class)) {
            // If is an assert method call
            if (methodCallExpr.getNameAsString().toLowerCase().startsWith("assert")) {
                // If the method call has arguments
                if (methodCallExpr.getArguments().size() > 0) {
                    for (Expression param : methodCallExpr.getArguments()) {
                        if (argumentsObjectCreation.size() > 0 && argumentsObjectCreation.contains(param.toString())) {
                            addIssue(testMethod.getNameAsString());
                            // System.out.println("adding" + testMethod.getNameAsString() + " because of "
                            //         + methodCallExpr.toString() + " in test " + testMethod.getNameAsString() + " param "
                            //         + param.toString() + " argumentsObjectCreation " + argumentsObjectCreation);
                        }
                    }
                }
            }
        }}
        issueCount = problematicTests.size();
        super.visit(classDeclaration, arg);
    }
}
