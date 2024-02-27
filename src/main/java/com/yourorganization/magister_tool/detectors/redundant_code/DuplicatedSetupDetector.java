package com.yourorganization.magister_tool.detectors.redundant_code;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DuplicatedSetupDetector extends VoidVisitorAdapter<Void> {


    private List<Set<String>> relatedTests = new ArrayList<>();
    private int issueCount = 0;
    private ArrayList<String> testsWithIncidence = new ArrayList<String>();

    public  int getIssueCount() {
        return issueCount;
    }

    public ArrayList getIssueList() {
        testsWithIncidence.sort(String::compareToIgnoreCase);
        return testsWithIncidence;
    }

    private void addIssue(String testCase) {
        if (!testsWithIncidence.contains(testCase)) {
            testsWithIncidence.add(testCase);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
        List<MethodDeclaration> testMethods = new ArrayList<>();
        classDeclaration.getMethods().forEach(method -> {
            if (method.getNameAsString().startsWith("test") || method.getNameAsString().startsWith("Test")) {
                testMethods.add(method);
            }
        });

        Map<String, Set<String>> commonStatements = new HashMap<>();
        int testMethodCount = testMethods.size();

        for (int i = 0; i < testMethodCount; i++) {
            for (int j = i + 1; j < testMethodCount; j++) {
                MethodDeclaration method1 = testMethods.get(i);
                MethodDeclaration method2 = testMethods.get(j);
                List<Statement> statements1 = method1.getBody().orElse(null).getStatements();
                List<Statement> statements2 = method2.getBody().orElse(null).getStatements();
                // Get each line of code in the method
                

                // Now check for if the first 2 statements are the same
                if (statements1.size() >= 2 && statements2.size() >= 2) {
                    Statement statement1 = statements1.get(0);
                    Statement statement2 = statements2.get(0);
                    if (statement1.toString().equals(statement2.toString())) {
                        Statement statement3 = statements1.get(1);
                        Statement statement4 = statements2.get(1);
                        if (statement3.toString().equals(statement4.toString())) {
                            Set<String> methods = commonStatements.getOrDefault(statement1.toString(), new HashSet<>());
                            methods.add(method1.getNameAsString());
                            methods.add(method2.getNameAsString());
                            commonStatements.put(statement1.toString(), methods);
                        }
                    }
                }


                
            }
        }

        commonStatements.forEach((statement, methods) -> {
            if (methods.size() >= 2) {
                relatedTests.add(methods);
                for (String method : methods) {
                    addIssue(method);
                }

            }
        });
        issueCount = relatedTests.size();
        super.visit(classDeclaration, arg);
    }
}
