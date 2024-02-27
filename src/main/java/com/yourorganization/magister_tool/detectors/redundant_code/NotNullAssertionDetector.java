package com.yourorganization.magister_tool.detectors.redundant_code;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotNullAssertionDetector extends VoidVisitorAdapter<Void> {

    private int issueCount = 0;
    private ArrayList<String> testsWithIncidence = new ArrayList<String>();

    public ArrayList getIssueList() {
        // sort the list
        testsWithIncidence.sort(String::compareToIgnoreCase);
        return testsWithIncidence;
    }

    private void addIssue(String testCase) {
        if (!testsWithIncidence.contains(testCase)) {
            testsWithIncidence.add(testCase);
        }
    }
    
    public  int getIssueCount() {
        return issueCount;
    }


    @Override
    public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
        List<MethodDeclaration> testMethods = new ArrayList<>();
        classDeclaration.getMethods().forEach(method -> {
            if (method.getNameAsString().startsWith("test") || method.getNameAsString().startsWith("Test")) {
                testMethods.add(method);
            }
        });

        for (MethodDeclaration testMethod : testMethods) {
            testMethod.accept(new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(ExpressionStmt stmt, Void arg) {
                    if (stmt.getExpression() instanceof MethodCallExpr) {
                        MethodCallExpr methodCall = (MethodCallExpr) stmt.getExpression();
                        if (methodCall.getNameAsString().equals("assertNotNull")) {
                            handleAssertNotNull(testMethod, stmt, methodCall);
                        }
                    }
                    super.visit(stmt, arg);
                }
            }, null);
        }
        super.visit(classDeclaration, arg);
    }

    private void handleAssertNotNull(MethodDeclaration testMethod, ExpressionStmt stmt, MethodCallExpr methodCall) {
        if (methodCall.getArguments().isEmpty()) {
            return;
        }
        String variableName = methodCall.getArgument(0).toString();
        boolean isRedundant = false;
        // Case (i): Check if the assertNotNull is used to verify a recently created object
        if (isNewlyCreatedObject(testMethod, variableName)) {
            // System.out.println("(isNewlyCreatedObject) not null assertion in " + testMethod.getNameAsString() + ": " + stmt.toString());
            isRedundant = true;
        }

        // Case (ii): Check if there's another assertion that already verifies the variable is not null
        if (isNotNullAssertionRedundant(testMethod, variableName, stmt)) {
            // System.out.println("(isNotNullAssertionRedundant) not null assertion in " + testMethod.getNameAsString() + ": " + stmt.toString());
            isRedundant = true;
        }

        // Case (iii): Check if the not null assertion tests a method that did not mean to return null
        // if (isNotNullAssertionRedundantTestNotVoidMethod(testMethod, variableName)) {
        //     System.out.println("(isNotNullAssertionRedundant) not null assertion in " + testMethod.getNameAsString() + ": " + stmt.toString());
        //     isRedundant = true;
        // }
        if(isRedundant) {
            issueCount++;
            addIssue(testMethod.getNameAsString());
        }
    }

    private boolean isNewlyCreatedObject(MethodDeclaration testMethod, String variableName) {
        return testMethod.findAll(VariableDeclarationExpr.class)
                .stream()
                .anyMatch(v -> v.getVariables().stream()
                        .anyMatch(var -> var.getNameAsString().equals(variableName) && var.getInitializer().isPresent() && var.getInitializer().get().isObjectCreationExpr()));
    }

    private boolean isNotNullAssertionRedundant(MethodDeclaration testMethod, String variableName, ExpressionStmt stmt) {
        return (testMethod.findAll(MethodCallExpr.class)
                .stream()
                .anyMatch(m -> m.getNameAsString().startsWith("assert") &&  
                // We must check if the statement is different from the current one, because the current one is the one we are checking
                // and it's not redundant to check the same statement
                (!m.getParentNode().get().equals(stmt)) &&

                (m.getNameAsString().contains("Equals") ||
                 m.getNameAsString().contains("True") || 
                    m.getNameAsString().contains("False") ||
                    m.getNameAsString().contains("NotNull") ||
                    m.getNameAsString().contains("Null") ||
                    m.getNameAsString().contains("Same") ||
                    m.getNameAsString().contains("NotSame")
                 ) && m.getArguments().stream()
                        .anyMatch(arg -> arg.toString().equals(variableName))));
    }

    private boolean isNotNullAssertionRedundantTestNotVoidMethod(MethodDeclaration testMethod, String variableName) {
        return testMethod.findAll(MethodCallExpr.class)
                .stream()
                .anyMatch(m -> m.getNameAsString().startsWith("assert") && m.getNameAsString().contains("Equals") && m.getArguments().stream()
                        .anyMatch(arg -> arg.toString().equals(variableName)));
    }
}
