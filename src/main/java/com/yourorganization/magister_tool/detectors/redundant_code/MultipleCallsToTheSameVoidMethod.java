package com.yourorganization.magister_tool.detectors.redundant_code;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MultipleCallsToTheSameVoidMethod extends VoidVisitorAdapter<Void> {

    // Must recieve a string parameter in constructor
    private List<String> sideEffectTests;
    public MultipleCallsToTheSameVoidMethod(List<String> testsAlreadyDetected) {
        this.sideEffectTests = testsAlreadyDetected;
    }



    private int issueCount = 0;
    private List<Set<String>> relatedTests = new ArrayList<>();
    private ArrayList<String> testsWithIncidence = new ArrayList<String>();

    public  int getIssueCount() {
        return issueCount;
    }

    public ArrayList getIssueList() {
        // sort the list
        testsWithIncidence.sort(String::compareToIgnoreCase);
        return testsWithIncidence;
    }

    private void addIssue(String testCase) {
        if (!testsWithIncidence.contains(testCase) && sideEffectTests.contains(testCase)) { // NUEVA LINEA
            testsWithIncidence.add(testCase);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
        
        List<MethodDeclaration> testMethods = classDeclaration.getMethods().stream()
                .filter(method -> method.getNameAsString().startsWith("test") || method.getNameAsString().startsWith("Test"))
                .collect(Collectors.toList());

        Map<String, Set<String>> methodToTestsMap = new HashMap<>();

        for (MethodDeclaration testMethod : testMethods) {
            // We must verify if it has a try statement. If so, we must ignore it
            if (testMethod.findAll(TryStmt.class).size() > 0) {
                continue;
            }
            
            List<MethodCallExpr> methodCalls = testMethod.findAll(MethodCallExpr.class);

            for (MethodCallExpr methodCall : methodCalls) {
                // Dont detect it if is an assert call
                if (isVoidMethod(methodCall) && !isAssertMethod(methodCall) ) {
                    methodToTestsMap.putIfAbsent(methodCall.getNameAsString(), new HashSet<>());
                    methodToTestsMap.get(methodCall.getNameAsString()).add(testMethod.getNameAsString());
                }
            }
        }

        methodToTestsMap.forEach((methodName, testNames) -> {
            if (testNames.size() > 1) { // 2 or more tests call the same void method
                // System.out.println("Same void method called in tests: " + String.join(", ", testNames) + " Method name: " + methodName);
                relatedTests.add(testNames);
            }
        });
        issueCount = relatedTests.size(); 
        for (Set<String> testNames : relatedTests) {
            for (String testName : testNames) {
                addIssue(testName);
            }
        }
        super.visit(classDeclaration, arg);
    }

    private boolean isVoidMethod(MethodCallExpr methodCall) {
        return methodCall.resolve().getReturnType().isVoid();
    }

    private boolean isAssertMethod(MethodCallExpr methodCall) {
        return methodCall.getNameAsString().toLowerCase().startsWith("assert");
    }
}
