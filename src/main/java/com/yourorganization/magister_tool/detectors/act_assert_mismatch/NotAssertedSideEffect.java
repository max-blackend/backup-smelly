package com.yourorganization.magister_tool.detectors.act_assert_mismatch;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.TryStmt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.xalan.xsltc.compiler.sym;

public class NotAssertedSideEffect extends VoidVisitorAdapter<Void> {

    private int issueCount = 0;
    private Set<String> countedTests = new HashSet<>();
    private ArrayList<String> testsWithIncidence = new ArrayList<String>();
    private final TypeSolver symbolSolver;

    public NotAssertedSideEffect(TypeSolver typeSolver) {
        this.symbolSolver = typeSolver;
    }

    public int getIssueCount() {
        return issueCount;
    }

    public ArrayList getIssueList() {
        testsWithIncidence.addAll(countedTests);
        // Sort the list
        testsWithIncidence.sort(String::compareTo);
        return testsWithIncidence;
    }

    @Override
    public void visit(MethodDeclaration md, Void arg) {
        md.getBody().ifPresent(body -> {
            if (body.findAll(TryStmt.class).size() > 0) {
                return;
            }
            String testName = md.getNameAsString();
            Set<String> calledMethods = new HashSet<>();
            body.findAll(MethodCallExpr.class).forEach(methodCall -> {
                try {
                    Optional<Node> optCalledMethodNode = methodCall.resolve().toAst();
                    ResolvedMethodDeclaration resolvedMethodDeclaration = methodCall.resolve();

                    if (!optCalledMethodNode.isPresent()) {
                        return;
                    }
                    MethodDeclaration calledMethod = (MethodDeclaration) optCalledMethodNode.get();

                    if (calledMethod != null) {
                        String calledMethodName = calledMethod.getNameAsString();
                        if (calledMethods.contains(calledMethodName)) {
                            return;
                        }
                        calledMethods.add(calledMethodName);
                        //List<AssignExpr> assignments = ((JavaParserMethodDeclaration) resolvedMethodDeclaration).getWrappedNode().findAll(AssignExpr.class);
                        List<AssignExpr> assignments = calledMethod.findAll(AssignExpr.class);
                        if (!assignments.isEmpty() && calledMethod.getType() instanceof VoidType ) {
                            Set<String> modifiedVariables = new HashSet<>();
                            for (AssignExpr assignment : assignments) {
                                if (assignment.getTarget().isFieldAccessExpr()) {
                                    modifiedVariables.add(assignment.getTarget().asFieldAccessExpr().getNameAsString());
                                }                            
                                //Add fix, if this.x and x are not the same but refer to the same field. 
                                if (assignment.getTarget().isNameExpr()){
                                    NameExpr nameExpr = assignment.getTarget().asNameExpr();
                                    try {
                                        ResolvedValueDeclaration resolved = JavaParserFacade.get(symbolSolver).solve(nameExpr).getCorrespondingDeclaration();
                                        if (resolved instanceof ResolvedFieldDeclaration) {
                                            modifiedVariables.add(nameExpr.getNameAsString());
                                        }
                                    } catch (Exception e) {
                                        System.out.println(nameExpr.getNameAsString() + " could not be resolved: " + e.getMessage());
                                    }
                                }
                            }
                            boolean isAsserted = false;
                            ArrayList<String> modifiedVariablesList = new ArrayList<String>();
                            modifiedVariablesList.addAll(modifiedVariables);
                            for (String modifiedVariable : modifiedVariables) {
                                Boolean isStoredInVariable = false;
                                VariableDeclarator variableUsedForStorage = null;
                                for (VariableDeclarator variableDeclarator : body.findAll(VariableDeclarator.class)) {
                                    try {
                                        variableUsedForStorage = variableDeclarator;
                                        Optional<Node> optGetterMethodNode = null;
                                        if (variableDeclarator.getInitializer().get().isMethodCallExpr()) {
                                            optGetterMethodNode = variableDeclarator.getInitializer().get()
                                                    .asMethodCallExpr().resolve().toAst();
                                        } else if (variableDeclarator.getInitializer().get().isObjectCreationExpr()) {
                                            optGetterMethodNode = variableDeclarator.getInitializer().get()
                                                    .asObjectCreationExpr().resolve().toAst();
                                            continue;
                                        } else if (variableDeclarator.getInitializer().get().isNameExpr()) {
                                            optGetterMethodNode = variableDeclarator.getInitializer().get()
                                                    .asNameExpr().resolve().toAst();
                                        } else if (variableDeclarator.getInitializer().get().isFieldAccessExpr()) {
                                            optGetterMethodNode = variableDeclarator.getInitializer().get()
                                                    .asFieldAccessExpr().resolve().toAst();
                                        }
                                        if (!optGetterMethodNode.isPresent()) {
                                            System.err.println("Unable to resolve symbol: "
                                                    + variableDeclarator.getInitializer().get().asMethodCallExpr()
                                                            .getName());
                                            continue;
                                        }
                                        MethodDeclaration getterMethod = null;
                                        try {
                                            getterMethod = (MethodDeclaration) optGetterMethodNode.get();
                                        } catch (Exception e) {
                                            // System.out
                                            //         .println("PROBLEM WITH THE METHOD DECLARATION : " + e.getMessage());
                                            continue;
                                        }
                                        List<ReturnStmt> returnStmts = getterMethod.findAll(ReturnStmt.class);
                                        for (ReturnStmt returnStmt : returnStmts) {
                                            try {
                                                if (FieldAccessExpr.class
                                                        .isInstance(returnStmt.getExpression().get())) {
                                                    if (returnStmt.getExpression().get().asFieldAccessExpr()
                                                            .getNameAsString().equals(modifiedVariable)) {
                                                        isStoredInVariable = true;
                                                    }
                                                }
                                            } catch (Exception e) {
                                                System.out.println("Exception  NASE1: " + e);
                                            }
                                            try {
                                                if (NameExpr.class.isInstance(returnStmt.getExpression().get())) {
                                                    if (returnStmt.getExpression().get().asNameExpr().getNameAsString()
                                                            .equals(modifiedVariable)) {
                                                        isStoredInVariable = true;
                                                    }
                                                }
                                            } catch (Exception e) {
                                                System.out.println("Exception NASE2: " + e);
                                            }

                                        }

                                    } catch (Exception e) {
                                        System.out.println("Exception NASE3: " + e.getMessage());
                                    }
                                }

                                if (isStoredInVariable) {
                                    for (MethodCallExpr assertMethodCall : body.findAll(MethodCallExpr.class)) {
                                        if (assertMethodCall.getNameAsString().startsWith("assert")) {
                                            List<Expression> assertMethodCallArguments = assertMethodCall
                                                    .getArguments();
                                            for (Expression argument : assertMethodCallArguments) {
                                                if (argument.isNameExpr()) {
                                                    if (argument.asNameExpr().getNameAsString()
                                                            .equals(variableUsedForStorage.getNameAsString())) {
                                                        isAsserted = true;
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                                for (MethodCallExpr assertMethodCall : body.findAll(MethodCallExpr.class)) {
                                    if (assertMethodCall.getNameAsString().startsWith("assert")) {
                                        List<Expression> assertMethodCallArguments = assertMethodCall.getArguments();
                                        for (Expression argument : assertMethodCallArguments) {
                                            if (argument.isMethodCallExpr()) {
                                                Optional<Node> optGetterMethodNode = argument.asMethodCallExpr()
                                                        .resolve().toAst();
                                                if (!optGetterMethodNode.isPresent()) {
                                                    System.err.println("Unable to resolve symbol: "
                                                            + argument.asMethodCallExpr().getName());
                                                    continue;
                                                }
                                                MethodDeclaration getterMethod = (MethodDeclaration) optGetterMethodNode
                                                        .get();

                                                List<ReturnStmt> returnStmts = getterMethod.findAll(ReturnStmt.class);
                                                for (ReturnStmt returnStmt : returnStmts) {
                                                    if (returnStmt.getExpression().isPresent()
                                                            && returnStmt.getExpression().get().isFieldAccessExpr()
                                                            && returnStmt.getExpression().get().asFieldAccessExpr()
                                                                    .getNameAsString().equals(modifiedVariable)) {
                                                        isAsserted = true;
                                                        break;
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }

                            }
                            if (!isAsserted && !countedTests.contains(testName)) {
                                // System.out.println(
                                // "Method " + testName + " contains an unverified side effect: "
                                // + methodCall + "attr" + modifiedVariablesList + "var" + modifiedVariables);
                                issueCount++;
                                countedTests.add(testName);
                            }
                        }
                    }
                } catch (UnsolvedSymbolException e) {
                    System.err.println("Unable to resolve symbol: " + methodCall.getName());
                }
            });
        });
    }
}
