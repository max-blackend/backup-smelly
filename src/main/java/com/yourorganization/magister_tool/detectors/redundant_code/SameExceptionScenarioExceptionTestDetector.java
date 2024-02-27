package com.yourorganization.magister_tool.detectors.redundant_code;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SameExceptionScenarioExceptionTestDetector extends VoidVisitorAdapter<Void> {

    private int issueCount = 0;
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
        if (!testsWithIncidence.contains(testCase)) {
            testsWithIncidence.add(testCase);
        }
    }
    
    private List<Set<String>> relatedTests = new ArrayList<>();

    @Override
    public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
        List<MethodDeclaration> testMethods = new ArrayList<>();
        classDeclaration.getMethods().forEach(method -> {
            if (method.getNameAsString().startsWith("test") || method.getNameAsString().startsWith("Test")) {
                testMethods.add(method);
            }
        });

        int testMethodCount = testMethods.size();

        for (int i = 0; i < testMethodCount; i++) {
            for (int j = i + 1; j < testMethodCount; j++) {
                MethodDeclaration method1 = testMethods.get(i);
                MethodDeclaration method2 = testMethods.get(j);
                if (isSameExceptionScenario(method1, method2)) {
                    addRelatedTests(method1.getNameAsString(), method2.getNameAsString());
                }
            }
        }

        printRelatedTests();
        issueCount = relatedTests.size();
        relatedTests.forEach(set -> {
            set.forEach(test -> {
                addIssue(test);
            });
        });
        super.visit(classDeclaration, arg);
    }

    // The rest of the class remains the same
    private boolean isSameExceptionScenario(MethodDeclaration method1, MethodDeclaration method2) {
        List<Statement> statements1 = method1.getBody().orElse(null).getStatements();
        List<Statement> statements2 = method2.getBody().orElse(null).getStatements();

        String exception1 = getExceptionType(method1);
        String exception2 = getExceptionType(method2);

        if (exception1 == null || exception2 == null || !exception1.equals(exception2)) {
            return false;
        }

        // List<Statement> diff = statements1.stream()
        //         .filter(statement -> !statements2.contains(statement))
        //         .collect(Collectors.toList());
        List<Statement> diff = new ArrayList<>();
        if (statements1.size() != statements2.size()) {
            return false;
        }
        
        for (int i = 0; i < statements1.size(); i++) {
            if (!statements1.get(i).toString().equals(statements2.get(i).toString())) {
                diff.add(statements1.get(i));
            }
        }

        return diff.size() <= 1;
        // return true;
    }

    private String getExceptionType(MethodDeclaration method) {
        return method.findAll(CatchClause.class)
                .stream()
                .findFirst()
                .map(catchClause -> catchClause.getParameter().getType().asString())
                .orElse(null);
    }

    private void addRelatedTests(String testName1, String testName2) {
        Optional<Set<String>> existingSet = relatedTests.stream()
                .filter(set -> set.contains(testName1) || set.contains(testName2))
                .findFirst();

        if (existingSet.isPresent()) {
            existingSet.get().add(testName1);
            existingSet.get().add(testName2);
        } else {
            Set<String> newSet = new HashSet<>();
            newSet.add(testName1);
            newSet.add(testName2);
            relatedTests.add(newSet);
        }
    }

    private void printRelatedTests() {
        relatedTests.forEach(set -> {
            String tests = String.join(", ", set);
            // System.out.println("Same exception scenario detected between: " + tests);
        });
    }
}