package com.yourorganization.magister_tool.detectors.low_contribution;

import java.util.ArrayList;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class NoAssertionsDetector extends VoidVisitorAdapter<Void> {

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
    public void visit(MethodDeclaration n, Void arg) {
        super.visit(n, arg);

        // Check if the method is a test method by checking its name
        if (n.getNameAsString().startsWith("test") || n.getNameAsString().startsWith("Test")) {
            boolean hasAssertion = n.findAll(MethodCallExpr.class).stream()
                    .anyMatch(m -> m.getNameAsString().startsWith("assert") || m.getNameAsString().startsWith("fail"));

            if (!hasAssertion) {
                // System.out.println("Found a test method without assertions: " + n.getNameAsString());
                issueCount++;
                addIssue(n.getNameAsString());
            }
        }
    }
}
