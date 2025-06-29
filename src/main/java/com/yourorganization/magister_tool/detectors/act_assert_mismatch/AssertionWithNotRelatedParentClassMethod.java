package com.yourorganization.magister_tool.detectors.act_assert_mismatch;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.*;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionMethodDeclaration;
import com.github.javaparser.ast.stmt.CatchClause;

import com.github.javaparser.ast.stmt.ReturnStmt;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.yourorganization.magister_tool.helpers.CoverageJsonFileHelper;

public class AssertionWithNotRelatedParentClassMethod extends VoidVisitorAdapter<Void> {

    private final TypeSolver symbolSolver;
    private int issueCount = 0;
    private ArrayList<String> testsWithIncidence = new ArrayList<String>();

    //Helpers with sequenciality of calls
    public static boolean reachedAssertions = false;
    public static String currentTest = "undefined";
    public static String testFileName;
    public static ResolvedMethodDeclaration lastCall = null;

    public AssertionWithNotRelatedParentClassMethod(TypeSolver typeSolver, String testFileName) {
        this.symbolSolver = typeSolver;
        this.testFileName = testFileName;
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

    private boolean isAncestor(ResolvedReferenceTypeDeclaration type, ResolvedReferenceTypeDeclaration cutClass){
        AtomicBoolean isAncestor = new AtomicBoolean(false);
        cutClass.getAllAncestors().forEach(father -> {
            if(type.getQualifiedName().equals(father.getQualifiedName())){
                isAncestor.set(true);
            }
        });
        return isAncestor.get();
    }

    private boolean testContainsTryCatch(MethodDeclaration md){
        AtomicBoolean hasTryCatch = new AtomicBoolean(false);
        md.getBody().ifPresent(body -> {
            if (body.findAll(TryStmt.class).size() > 0) {
                hasTryCatch.set(true);
                return;
            }
        });
        return hasTryCatch.get();
    }

    @Override
    public void visit(MethodDeclaration md, Void arg) {
        //Avoid trycatch methods
        if(testContainsTryCatch(md)){
            return;
        }
        // Checks the tests change
        if(!currentTest.equals(md.getNameAsString())){
            currentTest = md.getNameAsString();
            reachedAssertions = false;
        }
        try {
            List<AssignExpr> variablesChangedDuringTest = new ArrayList<>();
            md.findAll(MethodCallExpr.class).forEach(methodCall -> {
                ResolvedMethodDeclaration resolvedMethod = methodCall.resolve();
                if(methodCall.getName().toString().contains("assert")){
                    reachedAssertions = true;
                } else {
                    // Collect all assignations from the methods of the classes involved
                    if(!reachedAssertions){
                        lastCall = resolvedMethod;
                        resolvedMethod.declaringType().getAllMethods().forEach(methodFromClass -> {
                            if(methodFromClass.getDeclaration() instanceof JavaParserMethodDeclaration){
                                MethodDeclaration declaration = ((JavaParserMethodDeclaration) methodFromClass.getDeclaration()).getWrappedNode();
                                //Check for all assignations
                                List<AssignExpr> assignations = declaration.findAll(AssignExpr.class);
                                for (AssignExpr assignExpr : assignations){
                                    List<Integer> executedLines = CoverageJsonFileHelper.getCoveredLinesForTest(testFileName, md.getNameAsString(), testFileName + ".java");
                                    Integer assignationLine = assignExpr.getRange().get().begin.line;
                                    if(executedLines.contains(assignationLine)) {
                                        variablesChangedDuringTest.add(assignExpr);
                                    }
                                }
                                //Check for all method call expressions
                                declaration.findAll(MethodCallExpr.class).forEach(call -> {
                                    try {
                                        ResolvedMethodDeclaration methodResolved = JavaParserFacade.get(symbolSolver).solve(call).getCorrespondingDeclaration();
                                        if(isAncestor(methodResolved.declaringType(), resolvedMethod.declaringType())){
                                            //System.out.println(methodResolved.getName() + " is from class: " + methodResolved.getClassName() + " and caller is from: " + resolvedMethod.getClassName());
                                            if(methodResolved instanceof JavaParserMethodDeclaration){
                                                methodResolved.declaringType().getAllMethods().forEach(m -> {
                                                    if(m.getName().equals(methodResolved.getName())){
                                                        MethodDeclaration fatherCallDeclaration = ((JavaParserMethodDeclaration) m.getDeclaration()).getWrappedNode();
                                                        List<AssignExpr> fatherAssignations = fatherCallDeclaration.findAll(AssignExpr.class);
                                                        for(AssignExpr assignExpr: fatherAssignations){
                                                            List<Integer> executedLines = CoverageJsonFileHelper.getCoveredLinesForTest(testFileName, md.getNameAsString(), fatherCallDeclaration.getName().asString() + ".java");
                                                            Integer assignationLine = assignExpr.getRange().get().begin.line;
                                                            if(executedLines.contains(assignationLine)) {
                                                                variablesChangedDuringTest.add(assignExpr);
                                                            }
                                                        }



                                                        variablesChangedDuringTest.addAll(fatherCallDeclaration.findAll(AssignExpr.class));
                                                    }
                                                });
                                                
                                            }
                                        }
                                    } catch (Exception e) {
                                        System.out.println(e.getCause());
                                    }
                                });
                            }
                        });
                    } else {
                        // We assume that every last call before the assertions belongs to the class under test
                        if(lastCall == null){
                            //The test is focused in constructors
                            return;
                        }
                        if(!lastCall.getClassName().equals(resolvedMethod.getClassName())){
                            ResolvedReferenceTypeDeclaration cutClass = lastCall.declaringType();
                            if(isAncestor(resolvedMethod.declaringType(), cutClass)){
                                resolvedMethod.declaringType().getAllMethods().forEach(fatherMethod -> {
                                    if(fatherMethod.getName().equals(methodCall.getNameAsString())){
                                        //We've found a possibility of ARPM
                                        ResolvedMethodDeclaration candidate = fatherMethod.getDeclaration();
                                        //We are sure because it comes from an external library
                                        if(candidate instanceof ReflectionMethodDeclaration){
                                            issueCount++;
                                            addIssue(currentTest);
                                        }
                                        //We must evaluate if the method called in the assertion returns something changed during the test
                                        if(candidate instanceof JavaParserMethodDeclaration){
                                            MethodDeclaration resolvedCandidate = ((JavaParserMethodDeclaration) candidate).getWrappedNode();
                                            resolvedCandidate.findAll(ReturnStmt.class).forEach(stmt -> {
                                                stmt.getExpression().ifPresent(expr -> {
                                                    try {
                                                        NameExpr returnName = expr.asNameExpr();
                                                        AtomicBoolean containsVariable = new AtomicBoolean(false);
                                                        variablesChangedDuringTest.forEach(assignExpr -> {
                                                            if(assignExpr.getTarget().toString().equals(returnName.getNameAsString())){
                                                                containsVariable.set(true);
                                                            }
                                                        });
                                                        if(!containsVariable.get()){
                                                            issueCount++;
                                                            addIssue(currentTest);
                                                        }
                                                    } catch (Exception e) {
                                                        issueCount++;
                                                        addIssue(currentTest);
                                                    }
                                                    
                                                });
                                            });

                                        }
                                    }
                                });
                            }
                        } 
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Error in AssertionWithNotRelatedParentClassMethod.java");
            System.out.println(e.getCause().toString());
            e.printStackTrace();
        }
    }
}


