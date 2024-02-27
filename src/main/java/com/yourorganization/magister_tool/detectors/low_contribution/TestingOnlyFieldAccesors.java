package com.yourorganization.magister_tool.detectors.low_contribution;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;

public class TestingOnlyFieldAccesors extends VoidVisitorAdapter<Void> {

    private final JavaSymbolSolver symbolSolver;

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

    public int getIssueCount() {
        return issueCount;
    }

    public TestingOnlyFieldAccesors(TypeSolver typeSolver) {
        this.symbolSolver = new JavaSymbolSolver(typeSolver);
    }

    private boolean isTryStmtPresent(MethodDeclaration md) {
        return md.findAll(TryStmt.class).size() > 0;
    }

    private boolean isGetterOrSetter(ResolvedMethodDeclaration method) {

        boolean isGetter = isGetter(method);
        boolean isSetter = isSetter(method);

        return isGetter || isSetter;
    }

    private boolean isSetter(ResolvedMethodDeclaration method) {
        boolean isSetter = method.getNumberOfParams() == 1 && method.getReturnType().isVoid() &&
                method.getName().startsWith("set");
        return isSetter;
    }

    private boolean isGetter(ResolvedMethodDeclaration method) {
        boolean isGetter = method.getNumberOfParams() == 0 && !method.getReturnType().isVoid() &&
                (method.getName().startsWith("get") || method.getName().startsWith("is"));
        return isGetter;
    }

    private boolean isGetterOrSetterPurist(MethodCallExpr methodCall) {

        boolean isGetter = isPureGetter(methodCall);
        boolean isSetter = isPureSetter(methodCall);

        return isGetter || isSetter;
    }

    /**
     * Checks if the method is a pure getter, i.e., it has only one line and it is
     * returnig a field
     * 
     * @param method the method to be checked
     * @return true if the method is a pure getter, false otherwise
     */
    private boolean isPureGetter(MethodCallExpr method) {
        try {
            ResolvedMethodDeclaration resolvedMethod = method.resolve();
            Optional<Node> astNode = resolvedMethod.toAst();
            if (astNode.isPresent() && astNode.get() instanceof MethodDeclaration) {
                MethodDeclaration methodDeclaration = (MethodDeclaration) astNode.get();
                return methodDeclaration.getBody().isPresent() &&
                        methodDeclaration.getBody().get().getStatements().size() == 1 &&
                        methodDeclaration.getBody().get().getStatements().get(0).isReturnStmt() &&
                        methodDeclaration.getBody().get().getStatements().get(0).asReturnStmt().getExpression()
                                .isPresent()
                        &&
                        (methodDeclaration.getBody().get().getStatements().get(0).asReturnStmt().getExpression().get()
                                .isNameExpr() ||
                                methodDeclaration.getBody().get().getStatements().get(0).asReturnStmt().getExpression()
                                        .get().isFieldAccessExpr());
            }
            return false;

        } catch (UnsolvedSymbolException | UnsupportedOperationException e) {
            // Handle the exception, maybe the method couldn't be resolved
            return false;
        }
    }

    /**
     * Checks if the given method is a pure setter.
     * A pure setter is a method that has a single assignment statement in its body,
     * where the left-hand side of the assignment is either a variable or a field
     * access expression.
     * 
     * @param method the method to check
     * @return true if the method is a pure setter, false otherwise
     */
    private boolean isPureSetter(MethodCallExpr method) {
        try {
            ResolvedMethodDeclaration resolvedMethod = method.resolve();
            Optional<Node> astNode = resolvedMethod.toAst();
            if (astNode.isPresent() && astNode.get() instanceof MethodDeclaration) {
                MethodDeclaration methodDeclaration = (MethodDeclaration) astNode.get();
                return methodDeclaration.getBody().isPresent() &&
                        methodDeclaration.getBody().get().getStatements().size() == 1 &&
                        methodDeclaration.getBody().get().getStatements().get(0).isExpressionStmt() &&
                        methodDeclaration.getBody().get().getStatements().get(0).asExpressionStmt().getExpression()
                                .isAssignExpr()
                        &&
                        (methodDeclaration.getBody().get().getStatements().get(0).asExpressionStmt().getExpression()
                                .asAssignExpr().getTarget().isNameExpr() ||
                                methodDeclaration.getBody().get().getStatements().get(0).asExpressionStmt()
                                        .getExpression().asAssignExpr().getTarget().isFieldAccessExpr());

            }
            return false;
        

        } catch (UnsolvedSymbolException | UnsupportedOperationException e) {
            // Handle the exception, maybe the method couldn't be resolved
            return false;
        }
    }

    @Override
    public void visit(MethodDeclaration md, Void arg) {
        md.getBody().ifPresent(body -> {
            // if (isTryStmtPresent(md)) {
            // return;
            // }
            List<MethodCallExpr> methodCalls = body.findAll(MethodCallExpr.class);
            // Now we count the number of method calls that are not assertions
            Integer nonAssertionMethodCalls = (int) methodCalls.stream()
                    .filter(methodCall -> !methodCall.getNameAsString().startsWith("assert")).count();
            
            boolean allMethodsAreGettersOrSetters = methodCalls.stream()
                    .allMatch(methodCall -> {
                        if (methodCall.getNameAsString().startsWith("assert")) {
                            return true;
                        }

                        try {
                            return isGetterOrSetterPurist(methodCall);
                        } catch (Exception e) {
                            return false;
                        }
                    });

            if (allMethodsAreGettersOrSetters && nonAssertionMethodCalls > 0) {
                issueCount++;
                addIssue(md.getNameAsString());
            }
        });
    }
}