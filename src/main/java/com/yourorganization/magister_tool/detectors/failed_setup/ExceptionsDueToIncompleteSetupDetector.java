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
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ExceptionsDueToIncompleteSetupDetector extends VoidVisitorAdapter<Void> {
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


    @Override
    public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
        List<MethodDeclaration> testMethods = classDeclaration.getMethods().stream()
                .filter(method -> method.getNameAsString().startsWith("test")
                        || method.getNameAsString().startsWith("Test"))
                .collect(Collectors.toList());

        for (MethodDeclaration testMethod : testMethods) {
            boolean hasTryCatch = false;
            for (Statement statement : testMethod.getBody().get().getStatements()) {
                if (statement.isTryStmt()) {
                    hasTryCatch = true;
                    break;
                }
            }
            Optional<Statement> tryStatement = testMethod.getBody().get().getStatements().stream()
                    .filter(statement -> statement.isTryStmt())
                    .findFirst();
            boolean nullPointerException = tryStatement.isPresent()
                    && tryStatement.get().asTryStmt().getCatchClauses().size() == 1
                    && (tryStatement.get().asTryStmt().getCatchClauses().get(0).getParameter().getType().asString()
                            .contains("NullPointerException"));

            if (hasTryCatch && nullPointerException) {

            List<Statement> statements = testMethod.getBody().get().getStatements();
            Set<String> calledMethods = new HashSet<>();
            Set<String> modifiedVariables = new HashSet<>();
            Set<String> declaredVariablesButNotInitialized = new HashSet<>();

            for (Statement statement : statements) {
                if (!statement.isTryStmt()) {
                    List<MethodCallExpr> methodCalls = statement.findAll(MethodCallExpr.class);
                    for (MethodCallExpr methodCall : methodCalls) {
                        try {
                            Optional<Node> optCalledMethodNode = methodCall.resolve().toAst();
                            if (!optCalledMethodNode.isPresent()) {
                                continue;
                            }
                            MethodDeclaration calledMethod = (MethodDeclaration) optCalledMethodNode.get();

                            if (calledMethod != null) {
                                String calledMethodName = calledMethod.getNameAsString();
                                if (calledMethods.contains(calledMethodName)) {
                                    continue;
                                }
                                calledMethods.add(calledMethodName);
                                List<AssignExpr> assignments = calledMethod.findAll(AssignExpr.class);
                                if (!assignments.isEmpty() && calledMethod.getType() instanceof VoidType) {
                                    for (AssignExpr assignment : assignments) {
                                        if (assignment.getTarget().isFieldAccessExpr()) {
                                            modifiedVariables
                                                    .add(assignment.getTarget().asFieldAccessExpr().getNameAsString());
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Exception EDTIS1" + e);
                        }
                    }
                    for (ObjectCreationExpr objectCreation : statement.findAll(ObjectCreationExpr.class)) {
                        try {
                            Optional<Node> optCalledMethodNode = objectCreation.resolve().toAst();
                            if (!optCalledMethodNode.isPresent()) {
                                continue;
                            }
                            ConstructorDeclaration calledMethod = (ConstructorDeclaration) optCalledMethodNode.get();
                            if (calledMethod != null) {
                                String calledMethodName = calledMethod.getNameAsString();
                                if (calledMethods.contains(calledMethodName)) {
                                    continue;
                                }
                                calledMethods.add(calledMethodName);
                                List<AssignExpr> assignments = calledMethod.findAll(AssignExpr.class);
                                if (!assignments.isEmpty()) {
                                    for (AssignExpr assignment : assignments) {
                                        if (assignment.getTarget().isFieldAccessExpr()) {
                                            modifiedVariables
                                                    .add(assignment.getTarget().asFieldAccessExpr().getNameAsString());
                                        }
                                        if (assignment.getTarget().isNameExpr()) {
                                            modifiedVariables
                                                    .add(assignment.getTarget().asNameExpr().getNameAsString());
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Exception EDTIS2" + e);
                        }
                    }
                    // Finally, we must consider field declarations that are and are not initialized
                    // within the class

                }
            }
            // Now we already have the called methods and the modified variables
            // We need to check if the exception is caused by a variable that was not
            // modified by any of the called methods.
            // For this, we check the methods called inside the try block and check if they
            // are stored in the modified variables
            for (Statement statement : statements) {
                if (statement.isTryStmt()) {
                    List<MethodCallExpr> methodCalls = statement.findAll(MethodCallExpr.class);
                    for (MethodCallExpr methodCall : methodCalls) {
                        try {
                            Optional<Node> optCalledMethodNode = methodCall.resolve().toAst();
                            if (optCalledMethodNode.isPresent()) {

                                for (Parameter parameter : optCalledMethodNode.get().findAll(Parameter.class)) {
                                    modifiedVariables.add(parameter.getNameAsString());
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Exception " + e);
                        }

                        try {
                            Optional<Node> optCalledMethodNode = methodCall.resolve().toAst();
                            if (!optCalledMethodNode.isPresent()) {
                                continue;
                            }
                            Optional<Node> optClassNode = methodCall.resolve().declaringType()
                                    .toAst();
                            // Now we remove all comments from the class
                            optClassNode.get().findAll(Comment.class).forEach(Node::remove);
                            // Then we find all the fields of the class
                            List<FieldDeclaration> fields = optClassNode.get().findAll(FieldDeclaration.class);
                            List<FieldDeclaration> fields2 = optClassNode.get().findAll(FieldDeclaration.class);
                            // We remove comments from the fields and we add them fields2
                            for (FieldDeclaration field : fields) {
                                if (field.getComment().isPresent()) {
                                    fields2.remove(field);
                                }
                            }
                            try {
                                // Then we check if the variable is declared within the class
                                for (FieldDeclaration field : fields) {
                                    for (VariableDeclarator variable : optClassNode.get()
                                            .findAll(VariableDeclarator.class)) {
                                        // We must check if these variable were assigned with a value
                                        if (variable.getInitializer().isPresent() &&
                                                !variable.getInitializer().get().isNullLiteralExpr()) {
                                            modifiedVariables.add(variable.getNameAsString());
                                        } else {
                                            declaredVariablesButNotInitialized.add(variable.getNameAsString());
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                // System.out.println("Exception " + e);
                            }
                            for (ImportDeclaration importStatement : optClassNode.get().findCompilationUnit().get()
                                    .getImports()) {
                                try {
                                    modifiedVariables.add(importStatement.getNameAsString()
                                            .split("\\.")[importStatement.getNameAsString().split("\\.").length - 1]);
                                } catch (Exception e) {
                                    // System.out.println("Exception " + e);
                                }
                            }
                            // Now we must look into the for loops and check for variables declared there
                            List<ForStmt> forStmts = optCalledMethodNode.get().findAll(ForStmt.class);
                            for (ForStmt forStmt : forStmts) {
                                List<VariableDeclarator> variableDeclarators = forStmt
                                        .findAll(VariableDeclarator.class);
                                for (VariableDeclarator variableDeclarator : variableDeclarators) {
                                    modifiedVariables.add(variableDeclarator.getNameAsString());
                                }
                            }
                            // Now we check for Variables declared in the method
                            List<VariableDeclarator> variableDeclarators = optCalledMethodNode.get()
                                    .findAll(VariableDeclarator.class);
                            for (VariableDeclarator variableDeclarator : variableDeclarators) {
                                // System.out.println("variableDeclarator inside method " + variableDeclarator);
                                modifiedVariables.add(variableDeclarator.getNameAsString());
                            }

                            MethodDeclaration calledMethod = (MethodDeclaration) optCalledMethodNode.get();
                            List<AssignExpr> assignments = calledMethod.findAll(AssignExpr.class);
                            boolean hasUnmodifiedVariables = false;
                            for (AssignExpr assignment : assignments) {
                                if (assignment.getTarget().isFieldAccessExpr()) {
                                    String variableName = assignment.getTarget().asFieldAccessExpr().getNameAsString();
                                    if (declaredVariablesButNotInitialized.contains(variableName)) {
                                        // Now we check if the variable was assigned a value in the method
                                        List<AssignExpr> assignExprs = calledMethod.findAll(AssignExpr.class);
                                        for (AssignExpr assignExpr : assignExprs) {
                                            String targetName = "";
                                            if (assignExpr.getTarget().isFieldAccessExpr()) {
                                                targetName = assignExpr.getTarget().asFieldAccessExpr()
                                                        .getNameAsString();
                                            }
                                            if (assignExpr.getTarget().isNameExpr()) {
                                                targetName = assignExpr.getTarget().asNameExpr().getNameAsString();
                                            }
                                            if (!targetName.equals("")) {
                                                if (targetName.equals(variableName)
                                                        || (targetName.split("this.")[1].equals(variableName))) {
                                                    // The variable was assigned a value in the method
                                                    declaredVariablesButNotInitialized.remove(variableName);
                                                    modifiedVariables.add(variableName);
                                                }
                                            }
                                        }
                                    }
                                    if (!modifiedVariables.contains(variableName)) {
                                        // System.out.println("All modified variables " + modifiedVariables);
                                        // System.out.println("variableName Not initialized 1" + variableName);
                                        hasUnmodifiedVariables = true;
                                        break;
                                    }
                                }

                            }
                            // We also must check variables that are not assigned but are used in the method
                            List<NameExpr> nameExprs = calledMethod.findAll(NameExpr.class);

                            for (NameExpr nameExpr : nameExprs) {
                                String variableName = nameExpr.getNameAsString();

                                if (declaredVariablesButNotInitialized.contains(variableName)) {
                                    // Now we check if the variable was assigned a value in the method
                                    List<AssignExpr> assignExprs = calledMethod.findAll(AssignExpr.class);
                                    for (AssignExpr assignExpr : assignExprs) {
                                        String targetName = "";
                                        if (assignExpr.getTarget().isFieldAccessExpr()) {
                                            targetName = assignExpr.getTarget().asFieldAccessExpr().getNameAsString();
                                        }
                                        if (assignExpr.getTarget().isNameExpr()) {
                                            targetName = assignExpr.getTarget().asNameExpr().getNameAsString();
                                        }
                                        if (assignExpr.getTarget().isNameExpr()) {
                                            if (targetName.equals(variableName)
                                                    || (targetName.replace("this.", "").equals(variableName))) {
                                                // The variable was assigned a value in the method
                                                declaredVariablesButNotInitialized.remove(variableName);
                                                modifiedVariables.add(variableName);
                                                // break;
                                                // continue;
                                            }
                                        }
                                    }
                                }
                                if (!modifiedVariables.contains(variableName)) {
                                    // System.out.println("testmethod: " + testMethod.getNameAsString()
                                    //         + " unmodified variable " + variableName);
                                    hasUnmodifiedVariables = true;
                                    break;
                                }
                            }

                            if (hasUnmodifiedVariables) {
                                // Add the test case to the issue list if the method has unmodified variables
                                addIssue(testMethod.getNameAsString());
                                issueCount++;
                                break;
                            }

                        } catch (Exception e) {
                            System.out.println("Exception EDTIS3" + e);
                        }
                    }
                }
            }
        }
    }}
}
