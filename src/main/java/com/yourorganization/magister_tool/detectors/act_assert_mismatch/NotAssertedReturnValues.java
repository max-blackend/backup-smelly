package com.yourorganization.magister_tool.detectors.act_assert_mismatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class NotAssertedReturnValues extends VoidVisitorAdapter<Void> {

    private int issueCount = 0;
    private ArrayList<String> testsWithIncidence = new ArrayList<String>();

    public int getIssueCount() {
        return issueCount;
    }

    public ArrayList getIssueList() {
        return testsWithIncidence;
    }

    private void checkMethodCall(MethodDeclaration md, MethodCallExpr mce) {
        if (mce.toString().contains("doReturn") || mce.toString().contains("when")) {
            return;
        }
        ResolvedType returnType = null;
        boolean isVoid = false;
        try {
            isVoid = mce.resolve().getReturnType().isVoid();
            returnType = mce.resolve().getReturnType();

        } catch (UnsolvedSymbolException e) {
            System.err.println("Unable to resolve symbol: " + mce.getName());
        }
        if (returnType != null && !(isVoid || returnType.isVoid())) {
            boolean isAsserted = false;
            for (AssertStmt assertStmt : md.findAll(AssertStmt.class)) {
                if (assertStmt.getCheck().toString().contains(mce.toString())) {
                    isAsserted = true;
                    break;
                }
            }
            // We must:
            // 0. The returned value is not being stored in a variable
            // 1. Check that the returned value is not stored in a variable
            // 2. If the returned value is stored in a variable, check that the variable is
            // not used as argument in an assert
            // 3. If the returned value is stored in a variable, check that the variable is
            // not used as argument in another method call

            Boolean forgottenReturn = false;
            Boolean isStoredInVariable = false;
            Boolean isUsedAsArgument = false;
            String variableName = "";

            try {
                Integer amountOfReturns = md.findAll(MethodCallExpr.class).size();
                for (MethodCallExpr methodCallExpr : md.findAll(MethodCallExpr.class)) {
                    if (methodCallExpr.getArguments().toString().contains(mce.toString())) {
                        amountOfReturns--;
                        if (amountOfReturns > 0) {
                            forgottenReturn = true;
                        }
                        if (amountOfReturns <= 0) {
                            forgottenReturn = false;
                        }
                    }
                }

            } catch (Exception e) {
                System.err.println("Unable to resolve symbol: " + mce.getName());
            }
            try {
                for (VariableDeclarator variableDeclarator : md.findAll(VariableDeclarator.class)) {
                    if (variableDeclarator.getInitializer().toString().contains(mce.toString())) {
                        isStoredInVariable = true;
                        variableName = variableDeclarator.getNameAsString();
                        break;
                    }
                }

            } catch (Exception e) {
                System.err.println("Unable to resolve symbol: " + mce.getName());
            }

            if (isStoredInVariable) {
                for (AssertStmt assertStmt : md.findAll(AssertStmt.class)) {
                    if (assertStmt.getCheck().toString().contains(variableName)) {
                        isAsserted = true;
                        break;
                    }
                }
                for (MethodCallExpr methodCallExpr : md.findAll(MethodCallExpr.class)) {
                    if (methodCallExpr.getArguments().toString().contains(variableName)) {
                        isUsedAsArgument = true;
                        break;
                    }
                }
                for (ObjectCreationExpr objectCreationExpr : md.findAll(ObjectCreationExpr.class)) {
                    if (objectCreationExpr.getArguments().toString().contains(variableName)) {
                        isUsedAsArgument = true;
                        break;
                    }
                }
                for (VariableDeclarator variableDeclarator : md.findAll(VariableDeclarator.class)) {
                    if (variableDeclarator.getInitializer().toString().contains(variableName)) {
                        isUsedAsArgument = true;
                        break;
                    }
                }
                
            }
            Boolean isUsingTryCatch = false;
            try {
                List<TryStmt> tryStmts = md.findAll(TryStmt.class);
                if (tryStmts.size() > 0) {
                    isUsingTryCatch = true;
                }

                // for (TryStmt tryStmt : md.findAll(TryStmt.class)) {
                //     if (tryStmt.getTryBlock().toString().contains(mce.toString())) {
                //         isUsingTryCatch = true;
                //         break;
                //     }
                // }
            } catch (Exception e) {
                System.err.println("Unable to resolve symbol: " + mce.getName());
            }

            if (!isAsserted && !isUsedAsArgument && !forgottenReturn && !isUsingTryCatch) {
                if (!testsWithIncidence.contains(md.getNameAsString())) {
                    testsWithIncidence.add(md.getNameAsString());
                }
                issueCount++;
            }
        }
    }

    @Override
    public void visit(MethodDeclaration md, Void arg) {
        md.getBody().ifPresent(body -> {
            body.findAll(MethodCallExpr.class).forEach(mce -> {
                checkMethodCall(md, mce);
            });
        });
    }
}
