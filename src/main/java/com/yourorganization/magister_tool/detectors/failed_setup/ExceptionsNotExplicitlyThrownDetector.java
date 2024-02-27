package com.yourorganization.magister_tool.detectors.failed_setup;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.google.gwt.dev.util.collect.HashMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ExceptionsNotExplicitlyThrownDetector extends VoidVisitorAdapter<Void> {

    private int issueCount = 0;
    private ArrayList<String> testsWithIncidence = new ArrayList<String>();

    public ArrayList getIssueList() {
        testsWithIncidence.sort(String::compareToIgnoreCase);
        return testsWithIncidence;
    }

    private void addIssue(String testCase) {
        if (!testsWithIncidence.contains(testCase)) {
            testsWithIncidence.add(testCase);
        }
    }

    public int getIssueCount() {
        return issueCount;
    }

    private boolean testHasTryCatchBlock(MethodDeclaration testMethod) {
        Optional<Statement> tryStatement = testMethod.getBody().get().getStatements().stream()
                .filter(statement -> statement.isTryStmt())
                .findFirst();
        // We check if we are catching a NullPointerException or an Exception
        boolean hasCatch = tryStatement.isPresent() && tryStatement.get().asTryStmt().getCatchClauses().size() == 1
                && (tryStatement.get().asTryStmt().getCatchClauses().get(0).getParameter().getType().asString()
                        .contains("NullPointerException")
                        ||
                        tryStatement.get().asTryStmt().getCatchClauses().get(0).getParameter().getType()
                                .asString().contains("Exception"));
        return tryStatement.isPresent() && hasCatch;
    }

    private boolean baseCaseTestFullyContainedInsideTryClause(MethodDeclaration testMethod) {
        // If a null value is being used as parameter inside a constructor call or
        // method call
        boolean hasNullValueAsParameter = false;
        for (Statement statement : testMethod.getBody().get().getStatements()) {
            if (statement.isExpressionStmt()) {
                Expression expression = statement.asExpressionStmt().getExpression();
                if (expression.isMethodCallExpr()) {
                    MethodCallExpr methodCallExpr = expression.asMethodCallExpr();
                    for (Expression argument : methodCallExpr.getArguments()) {
                        if (argument instanceof NullLiteralExpr
                                || argument.toString().contains("null")) {
                            hasNullValueAsParameter = true;
                            break;
                        }
                    }
                } else if (expression.isAssignExpr()) {
                    AssignExpr assignExpr = expression.asAssignExpr();
                    if (assignExpr.getValue().isObjectCreationExpr()) {
                        ObjectCreationExpr objectCreationExpr = assignExpr.getValue().asObjectCreationExpr();
                        for (Expression argument : objectCreationExpr.getArguments()) {
                            if (argument instanceof NullLiteralExpr
                                    || argument.toString().contains("null")) {
                                hasNullValueAsParameter = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (testMethod.getBody().get().getStatements().get(0).isTryStmt()
                && testMethod.getBody().get().getStatements().get(0).asTryStmt().getCatchClauses().size() == 1
                && (testMethod.getBody().get().getStatements().get(0).asTryStmt().getCatchClauses().get(0)
                        .getParameter()
                        .getType().asString().contains("NullPointerException")
                        && hasNullValueAsParameter

                        || testMethod.getBody().get()
                                .getStatements().get(0).asTryStmt().getCatchClauses().get(0).getParameter().getType()
                                .asString().contains("Exception"))) {
            addIssue(testMethod.getNameAsString());
            issueCount++;
            return true;
        }
        return false;

    }

    private List<String> getFieldsFromObjectCreationBeingAsignedWithArgumentIndex(
            ResolvedConstructorDeclaration constructorDeclaration, int argumentIndex) {
        List<String> fields = new ArrayList<String>();
        // First we get the code of the constructor declaration
        Optional<Node> constructorDeclarationOptional = constructorDeclaration.toAst();

        if (!constructorDeclarationOptional.isPresent()
                || !(constructorDeclarationOptional.get() instanceof ConstructorDeclaration)) {
            // Handle error, or return an empty list if this condition is not a problem
            return fields;
        }

        ConstructorDeclaration constructorDeclarationNode = (ConstructorDeclaration) constructorDeclarationOptional
                .get();

        // Now we get the name of the parameter
        // constructorDeclaration.getParam(argumentIndex)
        ResolvedParameterDeclaration parameter = constructorDeclaration.getParam(argumentIndex);
        String parameterName = parameter.getName();

        // Now we iterate over the lines of code inside the constructor and check the
        // name of the fields that are being initialized with the null values
        for (Statement statement : constructorDeclarationNode.getBody().getStatements()) {
            if (statement.isExpressionStmt()) {
                Expression expression = statement.asExpressionStmt().getExpression();
                if (expression.isAssignExpr()) {
                    AssignExpr assignExpr = expression.asAssignExpr();
                    if (assignExpr.getTarget().isFieldAccessExpr()) {
                        if (assignExpr.getTarget().asFieldAccessExpr().getNameAsString().equals(parameterName)) {
                            fields.add(assignExpr.getTarget().asFieldAccessExpr().getNameAsString());
                        }
                    }
                }
            }
        }

        return fields; // Don't forget to return your result!
    }

    private List<String> getFieldsFromMethodCallBeingAssignedWithArgumentIndex(MethodCallExpr methodCallExpr,
            int argumentIndex) {
        List<String> fields = new ArrayList<String>();

        // Resolve the MethodCallExpr to a ResolvedMethodDeclaration
        ResolvedMethodDeclaration methodDeclaration = methodCallExpr.resolve();

        // Convert the ResolvedMethodDeclaration to a MethodDeclaration
        Optional<Node> methodDeclarationNodeOptional = methodDeclaration.toAst();

        // Check the Optional and instance of the Node
        if (!methodDeclarationNodeOptional.isPresent()) {
            // Handle error, or return an empty list if this condition is not a problem
            return fields;
        }

        Optional<MethodDeclaration> methodDeclarationOptional = methodDeclarationNodeOptional.get()
                .findAncestor(MethodDeclaration.class);

        // Check if the Optional<MethodDeclaration> is present
        if (!methodDeclarationOptional.isPresent()) {
            // Handle error, or return an empty list if this condition is not a problem
            return fields;
        }

        MethodDeclaration methodDeclarationNode = methodDeclarationOptional.get();

        // Get the parameter of the method call at the index
        ResolvedParameterDeclaration parameter = methodDeclaration.getParam(argumentIndex);
        String parameterName = parameter.getName();

        // Iterate over the lines of code inside the method and check the name of the
        // fields that are being initialized with the null values
        for (Statement statement : methodDeclarationNode.getBody().get().getStatements()) {
            if (statement.isExpressionStmt()) {
                Expression expression = statement.asExpressionStmt().getExpression();
                if (expression.isAssignExpr()) {
                    AssignExpr assignExpr = expression.asAssignExpr();
                    if (assignExpr.getTarget().isFieldAccessExpr()) {
                        if (assignExpr.getTarget().asFieldAccessExpr().getNameAsString().equals(parameterName)) {
                            fields.add(assignExpr.getTarget().asFieldAccessExpr().getNameAsString());
                        }
                    }
                }
            }
        }

        return fields;
    }

    private boolean iterateOverObjectInitializations(MethodDeclaration testMethod,
            HashMap<String, List<String>> objectInitializations) {
        boolean containedNulls = false;
        for (Statement statement : testMethod.getBody().get().getStatements()) {
            if (statement.isExpressionStmt()) {
                Expression expression = statement.asExpressionStmt().getExpression();
                if (expression.isVariableDeclarationExpr()) {
                    VariableDeclarationExpr variableDeclarationExpr = expression.asVariableDeclarationExpr();
                    for (VariableDeclarator variableDeclarator : variableDeclarationExpr.getVariables()) {
                        if (variableDeclarator.getInitializer().isPresent()) {
                            if (variableDeclarator.getInitializer().get().isObjectCreationExpr()) {
                                ObjectCreationExpr objectCreationExpr = variableDeclarator.getInitializer().get()
                                        .asObjectCreationExpr();
                                // Now we resolve the constructor declaration
                                ResolvedConstructorDeclaration constructorDeclaration = objectCreationExpr.resolve();

                                if (objectCreationExpr.getArguments().size() > 0) {
                                    for (Expression argument : objectCreationExpr.getArguments()) {
                                        // Nulls can come varius ways. Like this: Parser parser0 = new Parser((Scanner)
                                        // null, (SQLSchema) null);
                                        if (argument instanceof NullLiteralExpr
                                                || argument.toString().contains("null")) {
                                            containedNulls = true;
                                            // name of the variable storing the
                                            // object
                                            List<String> fieldsContainer = getFieldsFromObjectCreationBeingAsignedWithArgumentIndex(
                                                    constructorDeclaration,
                                                    objectCreationExpr.getArguments().indexOf(argument));
                                            if (objectInitializations
                                                    .containsKey(variableDeclarator.getNameAsString())) {
                                                objectInitializations.get(variableDeclarator.getNameAsString())
                                                        .addAll(fieldsContainer);
                                            } else {
                                                objectInitializations.put(variableDeclarator.getNameAsString(),
                                                        fieldsContainer);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return containedNulls;
    }

    private boolean containsParameterName(Node node, String parameterName) {
        if (node instanceof NameExpr && ((NameExpr) node).getNameAsString().equals(parameterName)) {
            return true;
        }

        for (Node child : node.getChildNodes()) {
            if (containsParameterName(child, parameterName)) {
                return true;
            }
        }

        return false;
    }

    public boolean processStatements(HashMap<String, List<String>> objectInitializations,
            MethodDeclaration testMethod) {
        boolean containedNulls = false;
        for (Statement statement : testMethod.getBody().get().getStatements()) {
            if (statement.isTryStmt()) {
                for (Statement tryStatement : statement.asTryStmt().getTryBlock().getStatements()) {
                    if (tryStatement.isExpressionStmt()) {
                        Expression expression = tryStatement.asExpressionStmt().getExpression();
                        if (expression.isMethodCallExpr()) {
                            MethodCallExpr methodCallExpr = expression.asMethodCallExpr();
                            if (methodCallExpr.getScope().isPresent()) {
                                if (methodCallExpr.getScope().get().isNameExpr()) {
                                    NameExpr nameExpr = methodCallExpr.getScope().get().asNameExpr();
                                    if (objectInitializations.containsKey(nameExpr.getNameAsString())) {
                                        for (Expression argument : methodCallExpr.getArguments()) {
                                            if (argument instanceof NullLiteralExpr
                                                    || argument.toString().contains("null")) {
                                                containedNulls = true;

                                                List<String> fieldsContainer = getFieldsFromMethodCallBeingAssignedWithArgumentIndex(
                                                        methodCallExpr,
                                                        methodCallExpr.getArguments().indexOf(argument));
                                                if (objectInitializations.containsKey(nameExpr.getNameAsString())) {
                                                    objectInitializations.get(nameExpr.getNameAsString())
                                                            .addAll(fieldsContainer);
                                                } else {
                                                    objectInitializations.put(nameExpr.getNameAsString(),
                                                            fieldsContainer);
                                                }
                                            }
                                        }
                                        ResolvedMethodDeclaration methodDeclaration = methodCallExpr.resolve();
                                        Optional<Node> methodDeclarationNodeOptional = methodDeclaration.toAst();
                                        if (!methodDeclarationNodeOptional.isPresent()) {
                                            continue;
                                        }
                                        Optional<MethodDeclaration> methodDeclarationOptional = methodDeclarationNodeOptional
                                                .get().findAncestor(MethodDeclaration.class);
                                        if (!methodDeclarationOptional.isPresent()) {
                                            continue;
                                        }
                                        MethodDeclaration methodDeclarationNode = methodDeclarationOptional.get();
                                        for (Statement methodStatement : methodDeclarationNode.getBody().get()
                                                .getStatements()) {
                                            if (methodStatement.isExpressionStmt()) {
                                                Expression methodExpression = methodStatement.asExpressionStmt()
                                                        .getExpression();
                                                if (methodExpression.isAssignExpr()) {
                                                    AssignExpr assignExpr = methodExpression.asAssignExpr();
                                                    if (assignExpr.getTarget().isFieldAccessExpr()) {
                                                        if (objectInitializations.get(nameExpr.getNameAsString())
                                                                .contains(assignExpr.getTarget().asFieldAccessExpr()
                                                                        .getNameAsString())) {
                                                            addIssue(testMethod.getNameAsString());
                                                            issueCount++;
                                                            break;
                                                        }
                                                    }
                                                } else if (methodExpression.isMethodCallExpr()) {
                                                    MethodCallExpr methodCallExpr2 = methodExpression
                                                            .asMethodCallExpr();
                                                    if (methodCallExpr2.getScope().isPresent()) {
                                                        if (methodCallExpr2.getScope().get().isFieldAccessExpr()) {
                                                            if (objectInitializations.get(nameExpr.getNameAsString())
                                                                    .contains(methodCallExpr2.getScope().get()
                                                                            .asFieldAccessExpr().getNameAsString())) {
                                                                addIssue(testMethod.getNameAsString());
                                                                issueCount++;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return containedNulls;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration classDeclarationOriginal, Void arg) {
        List<MethodDeclaration> testMethods = classDeclarationOriginal.getMethods().stream()
                .filter(method -> method.getNameAsString().startsWith("test")
                        || method.getNameAsString().startsWith("Test"))
                .collect(Collectors.toList());
        boolean containedNullValuesAnywhere = false;
        for (MethodDeclaration testMethod : testMethods) {
            HashMap<String, List<String>> objectInitializations = new HashMap<String, List<String>>();
            if (!testHasTryCatchBlock(testMethod)) {
                continue;
            }

            if (baseCaseTestFullyContainedInsideTryClause(testMethod)) {
                continue;
            }

            // If any of the previous conditions is not met, we continue with the analysis.
            // Now we check for the object initializations. We will store the name of the
            // variable storing the object as key and a List of strings as value. The list
            // will contain the name of the fields that are being initialized with the null
            // values
            // passed as arguments to the constructor.
            containedNullValuesAnywhere = iterateOverObjectInitializations(testMethod, objectInitializations);

            // Now we review the try block. We will check if any of the fields that were
            // initialized with null values
            // are being used inside the try block by any method call made to the object. If
            // so, we flag the test as an issue.

            containedNullValuesAnywhere = containedNullValuesAnywhere
                    || processStatements(objectInitializations, testMethod);
            boolean auxNullFinder = false;
            for (MethodCallExpr methodCall : testMethod.findAll(MethodCallExpr.class)) {
                for (Expression argument : methodCall.getArguments()) {
                    if (argument.toString().contains("null")) {
                        auxNullFinder = true;
                        break;
                    }
                }
            }

            for (ObjectCreationExpr objectCreation : testMethod.findAll(ObjectCreationExpr.class)) {
                for (Expression argument : objectCreation.getArguments()) {
                    if (argument.toString().contains("null")) {
                        auxNullFinder = true;
                        break;
                    }
                }
            }

            // Now we have to check for any other method call made inside the try block that
            // is using as argument a null value.
            // We will iterate over the statements of the test method, resolve the method
            // calls, and check if any of the arguments is a null value.
            for (Statement statement : testMethod.getBody().get().getStatements()) {
                if (statement.isTryStmt()) {
                    for (Statement tryStatement : statement.asTryStmt().getTryBlock().getStatements()) {
                        if (tryStatement.isExpressionStmt()) {
                            Expression expression = tryStatement.asExpressionStmt().getExpression();
                            if (expression.isAssignExpr()) {
                                // Now we get the right part and check if is a objectCreationExpr
                                if (expression.asAssignExpr().getValue().isObjectCreationExpr()) {
                                    ObjectCreationExpr objectCreationExpr = expression.asAssignExpr().getValue()
                                            .asObjectCreationExpr();
                                    // Now we resolve the constructor declaration
                                    ResolvedConstructorDeclaration constructorDeclaration = objectCreationExpr
                                            .resolve();
                                    Optional<Node> constructorDeclarationOptional = constructorDeclaration.toAst();
                                    boolean resolvedMethodhasTryCatchblock = false;
                                    if (!constructorDeclarationOptional.isPresent()
                                            || !(constructorDeclarationOptional
                                                    .get() instanceof ConstructorDeclaration)) {
                                        // Handle error, or return an empty list if this condition is not a problem
                                        addIssue(testMethod.getNameAsString());
                                        issueCount++;

                                        continue;

                                    }
                                    // Now we iterate over the statements of the constructor to check if it has a
                                    // try catch block
                                    for (Statement constructorStatement : constructorDeclarationOptional.get()
                                            .findRootNode().findAll(Statement.class)) {
                                        if (constructorStatement.isTryStmt()) {
                                            resolvedMethodhasTryCatchblock = true;
                                            break;
                                        }
                                    }
                                    if (!resolvedMethodhasTryCatchblock) {
                                        addIssue(testMethod.getNameAsString());
                                        issueCount++;
                                    }
                                }
                            }
                            // If is a constructor call
                            if (expression.isObjectCreationExpr()) {
                                ObjectCreationExpr objectCreationExpr = expression.asObjectCreationExpr();
                                // Now we resolve the constructor declaration
                                ResolvedConstructorDeclaration constructorDeclaration = objectCreationExpr.resolve();
                                Optional<Node> constructorDeclarationOptional = constructorDeclaration.toAst();
                                boolean resolvedMethodhasTryCatchblock = false;
                                if (!constructorDeclarationOptional.isPresent()
                                        || !(constructorDeclarationOptional.get() instanceof ConstructorDeclaration)) {
                                    // Handle error, or return an empty list if this condition is not a problem
                                    addIssue(testMethod.getNameAsString());
                                    issueCount++;

                                    continue;

                                }
                                ConstructorDeclaration constructorDeclarationNode = (ConstructorDeclaration) constructorDeclarationOptional
                                        .get();
                                // Now we iterate over the statements of the constructor to check if it has a
                                // try catch block
                                for (Statement constructorStatement : constructorDeclarationOptional.get()
                                        .findRootNode().findAll(Statement.class)) {
                                    if (constructorStatement.isTryStmt()) {
                                        resolvedMethodhasTryCatchblock = true;
                                        break;
                                    }
                                }
                                if (!resolvedMethodhasTryCatchblock) {
                                    addIssue(testMethod.getNameAsString());
                                    issueCount++;
                                }

                            }

                            if (expression.isMethodCallExpr()) {
                                MethodCallExpr methodCallExpr = expression.asMethodCallExpr();
                                if (containedNullValuesAnywhere) {
                                    ResolvedMethodDeclaration methodDeclaration = methodCallExpr.resolve();
                                    Optional<Node> methodDeclarationNodeOptional = methodDeclaration.toAst();
                                    boolean resolvedMethodhasTryCatchblock2 = false;

                                    if (!methodDeclarationNodeOptional.isPresent()) {
                                        addIssue(testMethod.getNameAsString());
                                        issueCount++;
                                        continue;
                                    }
                                    // Now we get the method declaration node because we need to iterate over the
                                    // statements of the method
                                    for (Statement methodStatement : methodDeclarationNodeOptional.get()
                                            .findRootNode().findAll(Statement.class)) {
                                        if (methodStatement.isTryStmt()) {
                                            resolvedMethodhasTryCatchblock2 = true;
                                            break;
                                        }
                                    }
                                    if (!resolvedMethodhasTryCatchblock2) {
                                        addIssue(testMethod.getNameAsString());
                                        issueCount++;
                                    }

                                    if (methodDeclarationNodeOptional.isPresent()) {
                                        // Now we check if the body of the method contains a try catch block
                                        // for (Node node : methodCallExpr.getChildNodes()) {
                                        // if (((Statement) node).isTryStmt()) {
                                        // hasTry = true;
                                        // break;
                                        // }
                                        // }

                                    }
                                }
                                int argumentIndex = 0;
                                for (Expression argument : methodCallExpr.getArguments()) {
                                    boolean hasTry = false;
                                    if (argument instanceof NullLiteralExpr ||
                                            (argument.toString().contains("null"))) {
                                        ResolvedMethodDeclaration methodDeclaration = methodCallExpr.resolve();
                                        Optional<Node> methodDeclarationNodeOptional = methodDeclaration.toAst();
                                        // If the resolved method does not have a trycatch block, we mark the test as an
                                        // issue
                                        if (methodDeclarationNodeOptional.isPresent()) {
                                            List<Statement> methodStatements = methodDeclarationNodeOptional.get()
                                                    .findRootNode().findAll(Statement.class);

                                            for (Statement statement2 : methodStatements) {
                                                if (statement2.isTryStmt()) {
                                                    hasTry = true;
                                                    break;
                                                }
                                            }
                                            if (!hasTry) {
                                                addIssue(testMethod.getNameAsString());
                                                issueCount++;
                                            }
                                        }
                                        if (!methodDeclarationNodeOptional.isPresent()) {
                                            continue;
                                        }
                                        // Now we get the method declaration node because we need to iterate over the
                                        // statements of the method
                                        // First we get the name of the parameter of the method call at the index we are
                                        // iterating
                                        ResolvedParameterDeclaration parameter = methodDeclaration
                                                .getParam(argumentIndex);
                                        String parameterName = parameter.getName();
                                        // Now we iterate over the lines of code inside the method and check the name of
                                        // the fields that are being initialized with the null values
                                        MethodDeclaration methodDeclarationNode = (MethodDeclaration) methodDeclarationNodeOptional
                                                .get();
                                        for (Statement methodStatement : methodDeclarationNode.getBody().get()
                                                .getStatements()) {
                                            
                                            if (methodStatement.isExpressionStmt()) {
                                                Expression methodExpression = methodStatement.asExpressionStmt()
                                                        .getExpression();

                                                if (methodExpression.isAssignExpr()) {
                                                    AssignExpr assignExpr = methodExpression.asAssignExpr();
                                                    // If the null value was passed to another variable, we check if the
                                                    // variable is being used in the method
                                                    if (assignExpr.getTarget().isNameExpr()) {
                                                        if (assignExpr.getTarget().asNameExpr().getNameAsString()
                                                                .equals(parameterName)) {
                                                            // Now we obtain the name of the variable that is being
                                                            // assigned with the null value
                                                            String variableName = assignExpr.getTarget().asNameExpr()
                                                                    .getNameAsString();

                                                            // Now we must iterate over the method statements again to
                                                            // check if the variable is being used
                                                            for (Statement methodStatement2 : methodDeclarationNode
                                                                    .getBody().get().getStatements()) {
                                                                if (methodStatement2.isExpressionStmt()) {
                                                                    Expression methodExpression2 = methodStatement2
                                                                            .asExpressionStmt().getExpression();
                                                                    if (methodExpression2.isMethodCallExpr()) {
                                                                        MethodCallExpr methodCallExpr2 = methodExpression2
                                                                                .asMethodCallExpr();
                                                                        if (methodCallExpr2.getScope().isPresent()) {
                                                                            if (methodCallExpr2.getScope().get()
                                                                                    .isNameExpr()) {
                                                                                if (methodCallExpr2.getScope().get()
                                                                                        .asNameExpr().getNameAsString()
                                                                                        .equals(parameterName) ||
                                                                                        methodCallExpr2.getScope().get()
                                                                                                .asNameExpr()
                                                                                                .getNameAsString()
                                                                                                .equals(variableName)) {
                                                                                    addIssue(testMethod
                                                                                            .getNameAsString());
                                                                                    issueCount++;
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else if (methodExpression.isMethodCallExpr()) {
                                                    MethodCallExpr methodCallExpr2 = methodExpression
                                                            .asMethodCallExpr();
                                                    if (methodCallExpr2.getScope().isPresent()) {
                                                        if (methodCallExpr2.getScope().get().isNameExpr()) {
                                                            if (methodCallExpr2.getScope().get().asNameExpr()
                                                                    .getNameAsString().equals(parameterName)) {
                                                                addIssue(testMethod.getNameAsString());
                                                                issueCount++;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                // Now we check if is being used in cast expression

                                                else if (methodExpression.isVariableDeclarationExpr()) {
                                                    VariableDeclarationExpr variableDeclarationExpr = methodExpression
                                                            .asVariableDeclarationExpr();
                                                    for (VariableDeclarator variableDeclarator : variableDeclarationExpr
                                                            .getVariables()) {
                                                        if (variableDeclarator.getInitializer().isPresent()
                                                                && variableDeclarator.getInitializer().get()
                                                                        .isMethodCallExpr()) {
                                                            MethodCallExpr methodCallExpr2 = variableDeclarator
                                                                    .getInitializer().get().asMethodCallExpr();
                                                            // Now for example, if we have table:
                                                            // table.getColumnModel().getColumn(columnIndex), we need to
                                                            // check if the object that is being used is the same as the
                                                            // one that is being passed as argument
                                                            while (true) {
                                                                if (!methodCallExpr2.getScope().isPresent()) {
                                                                    break;
                                                                }

                                                                Expression scope = methodCallExpr2.getScope().get();

                                                                if (scope.isMethodCallExpr()) {
                                                                    methodCallExpr2 = scope.asMethodCallExpr();
                                                                } else if (scope.isNameExpr()) {
                                                                    NameExpr nameExpr = scope.asNameExpr();
                                                                    if (nameExpr.getNameAsString()
                                                                            .equals(parameterName)) {
                                                                        addIssue(testMethod.getNameAsString());
                                                                        issueCount++;
                                                                    }
                                                                    break; // Break the loop if it's a NameExpr, whether
                                                                           // it matches parameterName or not.
                                                                } else {
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                for (Node node : methodStatement.getChildNodes()) {
                                                    // if the parameter is being used in any kind of expression, we flag
                                                    // the test as an issue
                                                    if (containsParameterName(node, parameterName)) {
                                                        addIssue(testMethod.getNameAsString());
                                                        issueCount++;
                                                        break;
                                                    }
                                                }
                                            }

                                        }
                                    }
                                    argumentIndex++;
                                }
                                // If the method has no arguments, we mark the test as an issue
                                if (argumentIndex == 0 && auxNullFinder) {
                                    addIssue(testMethod.getNameAsString());
                                    issueCount++;
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}
