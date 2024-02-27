package com.yourorganization.magister_tool.detectors.failed_setup;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExceptionsDueToExternalDependenciesDetector extends VoidVisitorAdapter<Void> {

    private int issueCount = 0;

    public int getIssueCount() {
        return issueCount;
    }

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

    private static final List<String> EXTERNAL_DEPENDENCY_EXCEPTIONS = Arrays.asList(
            "HeadlessException",
            "SQLException",
            "NotYetConnectedException",
            "NoClassDefFoundError",
            "ClassNotFoundException",
            "NoSuchFieldException",
            "NoRouteToHostException",
            "UnknownHostException",
            "ConnectException",
            "BindException",
            "SocketException",
            "SocketTimeoutException",
            "FileNotFoundException",
            "IOException",
            "EOFException",
            "InterruptedIOException",
            "ObjectStreamException",
            "InvalidClassException",
            "OptionalDataException",
            "StreamCorruptedException");

    @Override
    public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
        List<MethodDeclaration> testMethods = classDeclaration.getMethods().stream()
                .filter(method -> method.getNameAsString().startsWith("test")
                        || method.getNameAsString().startsWith("Test"))
                .collect(Collectors.toList());
        Set<String> problematicTests = new HashSet<>();
        for (MethodDeclaration testMethod : testMethods) {
            List<CatchClause> catchClauses = testMethod.findAll(CatchClause.class);
            for (CatchClause catchClause : catchClauses) {
                String caughtException = catchClause.getParameter().getTypeAsString();
                if (EXTERNAL_DEPENDENCY_EXCEPTIONS.contains(caughtException)) {
                    // System.out.println("Exception due to external dependencies detected in test:
                    // " + testMethod.getNameAsString() + " Exception: " + caughtException);
                    problematicTests.add(testMethod.getNameAsString());
                }
            }
        }
        issueCount = problematicTests.size();
        for (String test : problematicTests) {
            addIssue(test);
        }
        super.visit(classDeclaration, arg);
    }
}