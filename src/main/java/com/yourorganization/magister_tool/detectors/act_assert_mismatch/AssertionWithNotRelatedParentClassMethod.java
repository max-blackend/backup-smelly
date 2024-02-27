package com.yourorganization.magister_tool.detectors.act_assert_mismatch;

import java.util.ArrayList;
import java.util.Optional;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;

import com.github.javaparser.ast.Node;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;

public class AssertionWithNotRelatedParentClassMethod extends VoidVisitorAdapter<Void> {

    private final JavaSymbolSolver symbolSolver;
    private int issueCount = 0;
    private ArrayList<String> testsWithIncidence = new ArrayList<String>();

    public AssertionWithNotRelatedParentClassMethod(TypeSolver typeSolver) {
        this.symbolSolver = new JavaSymbolSolver(typeSolver);
    }

    public int getIssueCount() {
        return issueCount;
    }

    public ArrayList getIssueList() {
        return testsWithIncidence;
    }

    private void addIssue(String testCase) {
        if (!testsWithIncidence.contains(testCase)) {
            testsWithIncidence.add(testCase);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration cid, Void arg) {
        if (cid.isTopLevelType()) {
            super.visit(cid, arg);
        }
    }

    @Override
    public void visit(MethodDeclaration md, Void arg) {
        try {
            md.findAll(MethodCallExpr.class).forEach(methodCall -> {
                ResolvedMethodDeclaration resolvedMethod = methodCall.resolve();
                String qualifiedName = resolvedMethod.getQualifiedName();
                if (isAssertionMethod(qualifiedName)) {
                    methodCall.getArguments().forEach(argument -> {
                        if (argument.isMethodCallExpr()) {
                            String objectVariable = argument.asMethodCallExpr().getScope().get().toString();
                            String objectVariableClass = null;

                            for (VariableDeclarator variable : md.findAll(VariableDeclarator.class)) {
                                if (variable.getNameAsString().equals(objectVariable)) {
                                    objectVariableClass = variable.getTypeAsString();
                                }
                            }
                            try {
                                if (!argument.asMethodCallExpr().resolve().declaringType().getQualifiedName().toString()
                                    .contains(objectVariableClass.toString().split("<")[0]) && (!argument.asMethodCallExpr().resolve().declaringType().getQualifiedName().toString().contains("java.util"))) {
                                issueCount++;
                                addIssue(md.getNameAsString());
                                    }
                            
                            } catch (Exception e) {
                                System.out.println("Error in AssertionWithNotRelatedParentClassMethod1.java");
                                System.out.println(e.getCause().toString());
                            }
                            
                        }
                        try{
                            if (argument.isNameExpr()) { 
                                for (VariableDeclarator variable : md.findAll(VariableDeclarator.class)) {
                                    if (variable.getNameAsString().equals(argument.asNameExpr().getNameAsString())) {
                                        String objectVariableClass = variable.getTypeAsString();
                                        if (argument.isMethodCallExpr()) {
                                            try {
                                                ResolvedMethodDeclaration resolvedMethod2 = argument.asMethodCallExpr().resolve();
                                                String methodDeclaringClass = resolvedMethod2.declaringType().getQualifiedName();
                                                if (!methodDeclaringClass.contains(objectVariableClass.split("<")[0]) && !methodDeclaringClass.contains("java.util")) {
                                                    issueCount++;
                                                    addIssue(md.getNameAsString());
                                                }
                                            } catch (Exception e) {
                                                System.out.println("Error in AssertionWithNotRelatedParentClassMethod2.java");
                                                if (e.getCause() != null) {
                                                    System.out.println(e.getCause().toString());
                                                } else {
                                                    System.out.println(e.toString());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            
                    } catch (Exception e) {
                        System.out.println("Error in AssertionWithNotRelatedParentClassMethod3.java");
                        System.out.println(e.getCause().toString());
                    } 
                    });
                }
                

            });

        } catch (Exception e) {
            System.out.println("Error in AssertionWithNotRelatedParentClassMethod.java");
            System.out.println(e.getCause().toString());

        }
        
    }

    private boolean isAssertionMethod(String qualifiedName) {
        return qualifiedName.startsWith("org.junit.Assert.assertFalse")
                || qualifiedName.startsWith("org.junit.Assert.assertTrue")
                || qualifiedName.startsWith("org.junit.Assert.assertEquals")
                || qualifiedName.startsWith("org.junit.Assert.assertNotEquals")
                || qualifiedName.startsWith("org.junit.Assert.assertSame")
                || qualifiedName.startsWith("org.junit.Assert.assertNotSame")
                || qualifiedName.startsWith("org.junit.Assert.assertNull")
                || qualifiedName.startsWith("org.junit.Assert.assertNotNull");
    }
}


