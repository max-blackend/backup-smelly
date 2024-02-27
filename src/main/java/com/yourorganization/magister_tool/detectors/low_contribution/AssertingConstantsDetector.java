package com.yourorganization.magister_tool.detectors.low_contribution;

import java.util.ArrayList;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;

public class AssertingConstantsDetector extends VoidVisitorAdapter<Void> {

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

    private final SymbolResolver symbolResolver;

    public AssertingConstantsDetector(SymbolResolver typeSolver) {
        this.symbolResolver = typeSolver;
    }

    @Override
    public void visit(MethodDeclaration md, Void arg) {
        md.getBody().ifPresent(body -> {
            body.getStatements().forEach(statement -> {
                if (statement.isExpressionStmt()) {
                    ExpressionStmt expressionStmt = statement.asExpressionStmt();
                    if (expressionStmt.getExpression().isMethodCallExpr()) {
                        MethodCallExpr methodCallExpr = expressionStmt.getExpression().asMethodCallExpr();
                        if (methodCallExpr.getNameAsString().startsWith("assert")) {
                            for (Expression argument : methodCallExpr.getArguments()) {
                                if (argument.isFieldAccessExpr()) {
                                    FieldAccessExpr fieldAccessExpr = argument.asFieldAccessExpr();
                                    ResolvedValueDeclaration resolvedValueDeclaration = symbolResolver
                                            .resolveDeclaration(fieldAccessExpr, ResolvedValueDeclaration.class);

                                    if (resolvedValueDeclaration.isField()) {
                                        ResolvedFieldDeclaration resolvedFieldDeclaration = resolvedValueDeclaration
                                                .asField();
                                        if (resolvedFieldDeclaration.isStatic()
                                                && resolvedFieldDeclaration.accessSpecifier() == AccessSpecifier.PUBLIC) {
                                            if (!isConstantRelatedToMethod(methodCallExpr, resolvedFieldDeclaration)) {
                                                // System.out.println("Found assertion of a constant in test: "
                                                //         + methodCallExpr);
                                                addIssue(md.getNameAsString());
                                                issueCount++;
                                            }
                                        }
                                    }
                                }
                            
                            else if (argument.isNameExpr()) {
                                try {
                                    ResolvedValueDeclaration resolvedValueDeclaration = symbolResolver
                                        .resolveDeclaration(argument.asNameExpr(), ResolvedValueDeclaration.class);
                                    if (resolvedValueDeclaration.isField()) {
                                        ResolvedFieldDeclaration resolvedFieldDeclaration = resolvedValueDeclaration.asField();
                                        if (resolvedFieldDeclaration.isStatic()
                                                && resolvedFieldDeclaration.accessSpecifier() == AccessSpecifier.PUBLIC) {
                                            // Check if the constant is unrelated to the method being called in the test
                                            if (!isConstantRelatedToMethod(methodCallExpr, resolvedFieldDeclaration)) {
                                                // System.out.println("Found assertion of a constant in test: "
                                                //         + methodCallExpr);
                                                addIssue(md.getNameAsString());
                                                issueCount++;
                                            }
                                        }
                                    }
                                } catch (UnsolvedSymbolException e) {
                                    // this might happen if the symbol can't be resolved, ignore it
                                }
                            }
                            }
                        }
                    }
                }
            });
        });
    }
    


    private boolean isConstantRelatedToMethod(MethodCallExpr methodCallExpr, ResolvedFieldDeclaration constantField) {
        if (constantField.declaringType().getQualifiedName().startsWith("java.")){
            return true;
        }
        // Method got deprecated in paper
        return false; // We always return false for now

    }
}
