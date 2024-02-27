package com.yourorganization.magister_tool;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.SymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.yourorganization.magister_tool.detectors.act_assert_mismatch.AssertionWithNotRelatedParentClassMethod;
import com.yourorganization.magister_tool.detectors.act_assert_mismatch.NotAssertedReturnValues;
import com.yourorganization.magister_tool.detectors.act_assert_mismatch.NotAssertedSideEffect;
import com.yourorganization.magister_tool.detectors.failed_setup.ExceptionsDueToExternalDependenciesDetector;
import com.yourorganization.magister_tool.detectors.failed_setup.ExceptionsDueToIncompleteSetupDetector;
import com.yourorganization.magister_tool.detectors.failed_setup.ExceptionsNotExplicitlyThrownDetector;
import com.yourorganization.magister_tool.detectors.low_contribution.AssertingConstantsDetector;
import com.yourorganization.magister_tool.detectors.low_contribution.NoAssertionsDetector;
import com.yourorganization.magister_tool.detectors.low_contribution.TestingOnlyFieldAccesors;
import com.yourorganization.magister_tool.detectors.redundant_code.AssertingObjectInitializationMultipleTimes;
import com.yourorganization.magister_tool.detectors.redundant_code.DuplicatedSetupDetector;
import com.yourorganization.magister_tool.detectors.redundant_code.MultipleCallsToTheSameVoidMethod;
import com.yourorganization.magister_tool.detectors.redundant_code.SameExceptionScenarioExceptionTestDetector;
import com.yourorganization.magister_tool.detectors.redundant_code.NotNullAssertionDetector;

import com.yourorganization.magister_tool.helpers.*;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainDetector {

    // public static final String generalPath =
    // "C:/Users/maxim/OneDrive/Escritorio/Code/Functional-Evosuite-tests/subjects";
    // public static final String generalTestsPath =
    // "C:/Users/maxim/OneDrive/Escritorio/s0";
    // public static final String junitPath =
    // "C:/Users/maxim/OneDrive/Escritorio/Code/Tool-Magister/smelly/junit-4.11.jar";
    // public static final String evosuiteStandaloneRuntimePath =
    // "C:/Users/maxim/OneDrive/Escritorio/Code/Tool-Magister/smelly/evosuite-standalone-runtime-1.2.0.jar";

    public static String generalPath;
    public static String generalTestsPath;
    public static String junitPath;
    public static String evosuiteStandaloneRuntimePath;
    public static Integer mode; // 0: evosuite tests, 1: JTExpert tests
    public static Integer detectors; // 0: all, 1: low contribution, 2: act-assert mismatch, 3: redundant code, 4:
                                     // failed
                                     // setup
    public static boolean resumeAnalisis;
    public static String outputFilePath;
    public static String outputFileName;
    public static String sufix;

    public static void main(String[] args) {

        Options options = new Options();
        consoleBehavior(options);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            setSourcePath(cmd.getOptionValue("sourcePath"));
            setTestPath(cmd.getOptionValue("testPath"));
            setJunitPath(cmd.getOptionValue("junitPath"));
            setEvosuiteStandaloneRuntimePath(cmd.getOptionValue("evosuitePath"));
            modeSetter(Integer.parseInt(cmd.getOptionValue("mode")));
            detectorsSetter(Integer.parseInt(cmd.getOptionValue("detectors")));
            outputSetter(cmd.getOptionValue("outputFilePath"));
            outputFileNameSetter(cmd.getOptionValue("outputFileName"));
            sufixSetter(cmd.getOptionValue("sufix"));
            resumeAnalisis = Boolean.parseBoolean(cmd.getOptionValue("resumeAnalisis"));
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }

        DetectorResult detectorResults = new DetectorResult(outputFilePath, outputFileName, resumeAnalisis);
        File[] listOfFiles = getFiles(generalPath);

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isDirectory()) {
                String realName = "";
                String projectName = listOfFiles[i].getName();
                try {
                    realName = getProjectRealName(projectName);
                    System.out.println("Processing project: " + realName);

                    String[] paths = innerPathsProject(projectName);
                    String jarPath = paths[0];
                    String testPath = paths[3];
                    String libPath = paths[1];
                    String sourcePath = paths[2];

                    List<File> javaFiles = getJavaSourceFiles(testPath);
                    for (File javaFile : javaFiles) {
                        if (javaFile.getName().endsWith(".java")) {

                            String originalName = processOriginalName(javaFile);
                            if (originalName == null) {
                                continue;
                            }

                            if (isSkippeableFile(javaFile, detectorResults, realName, originalName)) {
                                System.out.println("File Skipped");
                                continue;
                            } else {
                                // if (true || originalName.equals("CompleteMetadataBean")) {
                                tryProjectProcessing(realName, jarPath, libPath, sourcePath, testPath,
                                        getTestFilePath(javaFile), detectorResults,
                                        originalName);

                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("EXCEPTION: " + e.getMessage());
                    System.out.println(
                            "Error fatal VINCULANTE: " + projectName + " path:" + listOfFiles[i].getAbsolutePath());
                }
            }
        }
    }

    /**
     * Returns the list of test files.
     * 
     * @param testPath Path to the test folder
     * @return List of test files
     */
    private static List<File> getJavaSourceFiles(String testPath) {
        return findJavaFiles(new File(testPath), mode);
    }

    /**
     * Returns the original name of the test file.
     * 
     * @param javaFile Test file
     * @return Original name of the test file
     */
    public static String processOriginalName(File javaFile) {
        if (javaFile.getName().contains("_ESTest")) {
            return javaFile.getName().split("_ESTest")[0].replace("EvoSuiteTest", "");
        } else if (javaFile.getName().contains("JTETest")) {
            return javaFile.getName().split("JTETest")[0];
        } else {
            System.out.println("File Skipped NOT TEST: " + javaFile.getName());
            return null;
        }
    }

    private static boolean isSkippeableFile(File javaFile, DetectorResult detectorResults, String realName,
            String originalName) {
        return javaFile.getName().contains("scaffold") || detectorResults.alreadyParsed(realName + "." + originalName);
    }

    /**
     * Tries to process the project.
     * 
     * @param realName        Real name of the project
     * @param jarPath         Path to the jar file
     * @param libPathInput    Path to the lib folder
     * @param sourcePath      Path to the source folder
     * @param testPath        Path to the test folder
     * @param testFilePath    Path to the test file
     * @param detectorResults
     * @param testFileName    Name of the test file
     */
    private static void tryProjectProcessing(String realName, String jarPath, String libPathInput, String sourcePath,
            String testPath, String testFilePath, DetectorResult detectorResults, String testFileName) {
                System.out.println("Processing test file: " + testFileName);
        try {
            processProject(realName, jarPath, libPathInput, sourcePath, testPath, testFilePath, detectorResults,
                    testFileName);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
            System.out.println("Error fatal contenido: " + realName + " path:" + testFilePath);
        }
    }

    /**
     * Returns the path of the test file.
     * 
     * @param javaFile Test file
     * @return Path of the test file
     */
    private static String getTestFilePath(File javaFile) {
        return javaFile.getAbsolutePath().toString().replace("\\", "/");
    }

    private static void processProject(String realName, String jarPath, String libPathInput, String sourcePath,
            String testPath, String testFilePath, DetectorResult detectorResults, String testFileName) {
        // Log.setAdapter(new Log.SilentAdapter());
        TypeSolver typeSolver = setGenericJarTypeSolver(junitPath);
        TypeSolver typeSolverEvosuite = setGenericJarTypeSolver(evosuiteStandaloneRuntimePath);

        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());

        addSourceFilesToTypeSolver(sourcePath, combinedTypeSolver);
        addLibFilesToTypeSolver(libPathInput, combinedTypeSolver);
        addJarTypeSolver(jarPath, combinedTypeSolver);

        combinedTypeSolver.add(typeSolver);
        combinedTypeSolver.add(typeSolverEvosuite);
        combinedTypeSolver.add(new JavaParserTypeSolver(sourcePath));

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        ParserConfiguration parserConfiguration = new ParserConfiguration().setAttributeComments(false);
        parserConfiguration.setSymbolResolver(symbolSolver);
        StaticJavaParser.setConfiguration(parserConfiguration);

        Path sourceRootPath = CodeGenerationUtils.mavenModuleRoot(MainDetector.class).resolve(testPath);
        SourceRoot sourceRoot = null;

        try {
            sourceRoot = new SourceRoot(sourceRootPath);
            Path javaFilePath = sourceRootPath
                    .resolve(testFilePath);
            CompilationUnit cu = StaticJavaParser.parse(new File(javaFilePath.toString()));
            if (cu != null) {
                runDetectors(realName, detectorResults, testFileName, typeSolver, symbolSolver, cu);
            } else {
                System.out.println("Error: Could not parse the Java file");
            }
        } catch (IOException e) {
            System.out.println("Error: Failed to read the Java file");
            e.printStackTrace();
        }
    }

    /**
     * Runs the detectors over the test file.
     * 
     * @param realName        Real name of the project
     * @param detectorResults
     * @param testFileName    Name of the test file
     * @param typeSolver
     * @param cu              Compilation unit of the test file
     */

    private static void runDetectors(String realName, DetectorResult detectorResults, String testFileName,
            TypeSolver typeSolver, SymbolResolver symbolSolver, CompilationUnit cu) {
        System.out.println("Initializaing detectors for " + testFileName);
        handleAllowedDetectors(realName, detectorResults, testFileName, typeSolver, symbolSolver, cu);
        endFileProcessing(detectorResults);
    }

    /**
     * Runs the following detectors over the test file:
     * <ul>
     * <li>Asserting Object Initialization Multiple Times</li>
     * <li>Duplicated Setup</li>
     * <li>Testing the Same Exception Scenario</li>
     * <li>Multiple Calls to the Same Void Method</li>
     * <li>Redundant not null assertions</li>
     * 
     */
    private static void runRedundantCodeDetectors(
            String realName, String testFileName, DetectorResult detectorResults, CompilationUnit cu,
            TypeSolver typeSolver) {
        runAssertingObjectInitializationMultipleTimesDetector(realName, detectorResults, testFileName, typeSolver, cu);
        runDuplicatedSetupDetector(realName, detectorResults, testFileName, cu);
        runSameExceptionScenarioExceptionTestDetector(realName, detectorResults, testFileName, cu);
        runMultipleCallsToTheSameVoidMethod(realName, testFileName, detectorResults, cu);
        runNotNullAssertionDetector(realName, detectorResults, testFileName, cu);
    }

    /**
     * Runs the following detectors over the test file:
     * <ul>
     * <li>Exceptions due to external dependencies</li>
     * <li>Exceptions due to incomplete setup</li>
     * <li>Exceptions due to null arguments</li>
     * 
     */
    private static void runFailedSetupDetectors(
            String realName, String testFileName, DetectorResult detectorResults, CompilationUnit cu,
            TypeSolver typeSolver) {
        runExceptionsDueToExternalDependenciesDetector(realName, detectorResults, testFileName, cu);
        runExceptionsDueToIncompleteSetupDetector(realName, detectorResults, testFileName, cu);
        runExceptionsDueToNullArgumentsDetector(realName, detectorResults, testFileName, cu);
    }

    /**
     * Runs the following detectors over the test file:
     * <ul>
     * <li>Testing only field accesors</li>
     * <li>Asserting Constants</li>
     * 
     */
    private static void runTestingFieldAccessorsAndConstantsDetectors(
            String realName, String testFileName, DetectorResult detectorResults, CompilationUnit cu,
            TypeSolver typeSolver, SymbolResolver symbolSolver) {
        runTestingOnlyFieldAccesors(realName, detectorResults, testFileName, typeSolver, cu);
        runAssertingConstantsDetector(realName, detectorResults, testFileName, symbolSolver, cu);
    }

    /**
     * Runs the following detectors over the test file:
     * <ul>
     * <li>Not Asserted Side Effects</li>
     * <li>Not Asserted Return Values</li>
     * <li>Assertion with Not Related Parent Class Method</li>
     */
    private static void runActAssertMismatchDetectors(String realName, String testFileName,
            DetectorResult detectorResults,
            CompilationUnit cu, TypeSolver typeSolver) {
        runNotAssertedSideEffect(realName, testFileName, detectorResults, cu);
        runNotAssertedReturnValue(realName, detectorResults, testFileName, cu);
        runAssertionWithNotRelatedParentClassMethod(realName, testFileName, detectorResults, cu, typeSolver);
    }

    private static void runExceptionsDueToNullArgumentsDetector(String realName, DetectorResult detectorResults,
            String testFileName, CompilationUnit cu) {
        ExceptionsNotExplicitlyThrownDetector exceptionsDueToNullArgumentsDetector = new ExceptionsNotExplicitlyThrownDetector();
        cu.accept(exceptionsDueToNullArgumentsDetector, null);
        detectorResults.addIncidence(realName + "." + testFileName,
                "Exceptions due to null arguments",
                exceptionsDueToNullArgumentsDetector.getIssueCount());
        detectorResults.addIncidencesPerClassTest(realName + "." + testFileName,
                "Exceptions due to null arguments",
                exceptionsDueToNullArgumentsDetector.getIssueList());
    }

    private static void runExceptionsDueToIncompleteSetupDetector(String realName, DetectorResult detectorResults,
            String testFileName, CompilationUnit cu) {
        ExceptionsDueToIncompleteSetupDetector exceptionsDueToIncompleteSetupDetector = new ExceptionsDueToIncompleteSetupDetector();
        cu.accept(exceptionsDueToIncompleteSetupDetector, null);
        detectorResults.addIncidence(realName + "." + testFileName,
                "Exceptions due to incomplete setup",
                exceptionsDueToIncompleteSetupDetector.getIssueCount());
        detectorResults.addIncidencesPerClassTest(realName + "." + testFileName,
                "Exceptions due to incomplete setup",
                exceptionsDueToIncompleteSetupDetector.getIssueList());
    }

    private static void runNotNullAssertionDetector(String realName, DetectorResult detectorResults,
            String testFileName, CompilationUnit cu) {
        NotNullAssertionDetector notNullAssertionDetector = new NotNullAssertionDetector();
        cu.accept(notNullAssertionDetector, null);
        detectorResults.addIncidence(realName + "." + testFileName, "Not null assertion",
                notNullAssertionDetector.getIssueCount());
        detectorResults.addIncidencesPerClassTest(realName + "." + testFileName,
                "Not null assertion",
                notNullAssertionDetector.getIssueList());
    }

    private static void runSameExceptionScenarioExceptionTestDetector(String realName, DetectorResult detectorResults,
            String testFileName, CompilationUnit cu) {
        SameExceptionScenarioExceptionTestDetector redundantExceptionTestDetector = new SameExceptionScenarioExceptionTestDetector();
        cu.accept(redundantExceptionTestDetector, null);
        detectorResults.addIncidence(realName + "." + testFileName,
                "Testing the same exception scenario",
                redundantExceptionTestDetector.getIssueCount());
        detectorResults.addIncidencesPerClassTest(realName + "." + testFileName,
                "Testing the same exception scenario",
                redundantExceptionTestDetector.getIssueList());
    }

    private static void runAssertingConstantsDetector(String realName, DetectorResult detectorResults,
            String testFileName, SymbolResolver symbolResolver, CompilationUnit cu) {
        AssertingConstantsDetector assertingConstantsDetector = new AssertingConstantsDetector(
                symbolResolver);
        cu.accept(assertingConstantsDetector, null);
        detectorResults.addIncidence(realName + "." + testFileName, "Asserting Constants",
                assertingConstantsDetector.getIssueCount());
        detectorResults.addIncidencesPerClassTest(realName + "." + testFileName,
                "Asserting Constants",
                assertingConstantsDetector.getIssueList());
    }

    private static void runExceptionsDueToExternalDependenciesDetector(String realName, DetectorResult detectorResults,
            String testFileName, CompilationUnit cu) {
        ExceptionsDueToExternalDependenciesDetector exceptionsDueToExternalDependenciesDetector = new ExceptionsDueToExternalDependenciesDetector();
        cu.accept(exceptionsDueToExternalDependenciesDetector, null);
        detectorResults.addIncidence(realName + "." + testFileName, "Exceptions due to external dependencies",
                exceptionsDueToExternalDependenciesDetector.getIssueCount());
        detectorResults.addIncidencesPerClassTest(realName + "." + testFileName,
                "Exceptions due to external dependencies",
                exceptionsDueToExternalDependenciesDetector.getIssueList());
    }

    private static void runTestingOnlyFieldAccesors(String realName, DetectorResult detectorResults,
            String testFileName, TypeSolver typeSolver, CompilationUnit cu) {
        TestingOnlyFieldAccesors testingOnlyFieldAccesors = new TestingOnlyFieldAccesors(typeSolver);
        cu.accept(testingOnlyFieldAccesors, null);
        detectorResults.addIncidence(realName + "." + testFileName, "Testing only field accesors",
                testingOnlyFieldAccesors.getIssueCount());
        detectorResults.addIncidencesPerClassTest(realName + "." + testFileName,
                "Testing only field accesors",
                testingOnlyFieldAccesors.getIssueList());
    }

    private static void runAssertingObjectInitializationMultipleTimesDetector(String realName,
            DetectorResult detectorResults,
            String testFileName, TypeSolver typeSolver, CompilationUnit cu) {
        AssertingObjectInitializationMultipleTimes assertingObjectInitializationMultipleTimes = new AssertingObjectInitializationMultipleTimes(
                typeSolver);
        cu.accept(assertingObjectInitializationMultipleTimes, null);
        detectorResults.addIncidence(realName + "." + testFileName,
                "Asserting object initialization multiple times",
                assertingObjectInitializationMultipleTimes.getIssueCount());
        detectorResults.addIncidencesPerClassTest(realName + "." + testFileName,
                "Asserting object initialization multiple times",
                assertingObjectInitializationMultipleTimes.getIssueList());
    }

    private static void runDuplicatedSetupDetector(String realName, DetectorResult detectorResults, String testFileName,
            CompilationUnit cu) {
        DuplicatedSetupDetector duplicatedSetupDetector = new DuplicatedSetupDetector();
        cu.accept(duplicatedSetupDetector, null);
        detectorResults.addIncidence(realName + "." + testFileName, "Duplicated Setup",
                duplicatedSetupDetector.getIssueCount());
        detectorResults.addIncidencesPerClassTest(realName + "." + testFileName,
                "Duplicated Setup",
                duplicatedSetupDetector.getIssueList());
    }

    private static void runNotAssertedReturnValue(String realName, DetectorResult detectorResults, String testFileName,
            CompilationUnit cu) {
        NotAssertedReturnValues notAssertedReturnValues = new NotAssertedReturnValues();
        cu.accept(notAssertedReturnValues, null);
        detectorResults.addIncidence(realName + "." + testFileName, "Not asserted return values",
                notAssertedReturnValues.getIssueCount());
        detectorResults.addIncidencesPerClassTest(realName + "." + testFileName,
                "Not asserted return values",
                notAssertedReturnValues.getIssueList());
    }

    /**
     * Returns a List of the test files to be analyzed.
     * 
     * @param dir           Directory to be analyzed
     * @param finding_class 0: Tests from evosuite, 1: JTExpert tests
     * @return List of test files
     */
    private static List<File> findJavaFiles(File dir, Integer finding_class) {
        List<File> javaFiles = new ArrayList<>();
        if (!dir.exists() || !dir.isDirectory() || !dir.canRead()) {
            return javaFiles;
        }

        findJavaFilesRecursive(dir, finding_class, javaFiles);

        return javaFiles;
    }

    /**
     * Recursive method to find the test files to be analyzed.
     * 
     * @param dir           Directory to be analyzed
     * @param finding_class 0: Tests from evosuite, 1: JTExpert tests
     * @param javaFiles     List of test files
     */
    private static void findJavaFilesRecursive(File dir, Integer finding_class, List<File> javaFiles) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                findJavaFilesRecursive(file, finding_class, javaFiles);
            } else if (file.getName().endsWith(".java")) {
                if ((finding_class == 0) && file.getName().contains("_ESTest")) {
                    javaFiles.add(file);
                } else if (finding_class == 1 && file.getName().contains("JTETestCases")) {
                    javaFiles.add(file);
                }
            }
        }
    }

    /**
     * Returns the files in the given path.
     * 
     * @param path Path to be analyzed
     * @return Files in the given path
     */
    public static File[] getFiles(String path) {
        return new File(path).listFiles();
    }

    /**
     * Returns the real name of the project.
     * 
     * @param projectName
     * @return Real name of the project
     */
    public static String getProjectRealName(String projectName) {
        return projectName.substring(projectName.indexOf("_") + 1);
    }

    /**
     * Returns the paths of the project.
     * 
     * @param projectName
     * @return Paths of the project
     */
    public static String[] innerPathsProject(String projectName) {
        String jarPath = generalPath + "/" + projectName + "/" + getProjectRealName(projectName) + ".jar";
        String libPath = generalPath + "/" + projectName + "/lib";
        String sourcePath = generalPath + "/" + projectName + "/src/main/java";
        String testPath = sufixGenerator(generalTestsPath, projectName);
        String[] paths = { jarPath, libPath, sourcePath, testPath };
        return paths;
    }

    private static String sufixGenerator(String generalTestsPath, String projectName) {
        switch (sufix) {
            case "dynamosa":
                return generalTestsPath + "/" + projectName + "/evosuite-tests";
            case "mosa":
                return generalTestsPath + "/" + projectName + "/evosuite-tests-mosa";
            case "standard":
                return generalTestsPath + "/" + projectName + "/evosuite-tests-STANDARD_GA";
            case "jtexpert":
                return generalTestsPath + "/" + projectName + "/jteOutput/testcases";
            default:
                return generalTestsPath + "/" + projectName ;
        }
    }

    private static TypeSolver setGenericJarTypeSolver(String path) {
        TypeSolver typeSolver = null;
        try {
            typeSolver = new JarTypeSolver(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return typeSolver;
    }

    private static void addSourceFilesToTypeSolver(String sourcePath, CombinedTypeSolver combinedTypeSolver) {
        try {
            combinedTypeSolver.add(new JavaParserTypeSolver(sourcePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addJarTypeSolver(String jarPath, CombinedTypeSolver combinedTypeSolver) {
        try {
            combinedTypeSolver.add(new JarTypeSolver(jarPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addLibFilesToTypeSolver(String libPathInput, CombinedTypeSolver combinedTypeSolver) {
        Path libPath = CodeGenerationUtils.mavenModuleRoot(MainDetector.class).resolve(libPathInput);
        for (File path : libPath.toFile().listFiles()) {
            if (path.getName().endsWith(".jar")) {
                try {
                    combinedTypeSolver.add(new JarTypeSolver(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void runAssertionWithNotRelatedParentClassMethod(String realName, String testFileName,
            DetectorResult detectorResults, CompilationUnit cu, TypeSolver typeSolver) {
        AssertionWithNotRelatedParentClassMethod assertionWithNotRelatedParentClassMethod = new AssertionWithNotRelatedParentClassMethod(
                typeSolver);
        cu.accept(assertionWithNotRelatedParentClassMethod, null);
        detectorResults.addIncidence(realName + "." + testFileName,
                "Assertion with not related parent class method",
                assertionWithNotRelatedParentClassMethod.getIssueCount());
        detectorResults.addIncidencesPerClassTest(realName + "." + testFileName,
                "Assertion with not related parent class method",
                assertionWithNotRelatedParentClassMethod.getIssueList());
    }

    private static void runMultipleCallsToTheSameVoidMethod(String realName, String testFileName,
            DetectorResult detectorResults, CompilationUnit cu) {
        MultipleCallsToTheSameVoidMethod multipleCallsToTheSameVoidMethod = new MultipleCallsToTheSameVoidMethod(
                detectorResults.getTestsWithSideEffects(realName + "." + testFileName));
        cu.accept(multipleCallsToTheSameVoidMethod, null);
        detectorResults.addIncidence(realName + "." + testFileName,
                "Multiple calls to the same void method",
                multipleCallsToTheSameVoidMethod.getIssueCount());
        detectorResults.addIncidencesPerClassTest(realName + "." + testFileName,
                "Multiple calls to the same void method",
                multipleCallsToTheSameVoidMethod.getIssueList());
    }

    private static void runNotAssertedSideEffect(String realName, String testFileName, DetectorResult detectorResults,
            CompilationUnit cu) {
        NotAssertedSideEffect notAssertedSideEffect = new NotAssertedSideEffect();
        cu.accept(notAssertedSideEffect, null);
        detectorResults.addIncidence(realName + "." + testFileName, "Not asserted side effects",
                notAssertedSideEffect.getIssueCount());
        detectorResults.addIncidencesPerClassTest(realName + "." + testFileName, "Not asserted side effects",
                notAssertedSideEffect.getIssueList());
    }

    private static void endFileProcessing(DetectorResult detectorResults) {
        detectorResults.writeEachClassOcurrences();
        System.out.println("[DONE] Printing to CSV file");
    }

    public static void consoleBehavior(Options options) {

        Option sourcePath = new Option("sp", "sourcePath", true, "path to folder of java projects");
        sourcePath.setRequired(true);
        options.addOption(sourcePath);

        Option testPath = new Option("tp", "testPath", true,
                "path to folder of test files in their respective project folders");
        testPath.setRequired(true);
        options.addOption(testPath);

        Option junitPath = new Option("jp", "junitPath", true, "path to junit-4.11.jar file");
        junitPath.setRequired(true);
        options.addOption(junitPath);

        Option evosuitePath = new Option("ep", "evosuitePath", true, "path to evosuite standalone-1.2.0.jar file");
        evosuitePath.setRequired(true);
        options.addOption(evosuitePath);

        Option outputFileName = new Option("ofn", "outputFileName", true, "name of the output file");
        outputFileName.setRequired(true);
        options.addOption(outputFileName);

        Option outputFilePath = new Option("ofp", "outputFilePath", true, "path to output file");
        outputFilePath.setRequired(true);
        options.addOption(outputFilePath);

        Option resumeAnalisis = new Option("r", "resumeAnalisis", true, "resume analisis from a previous file and don't start from the beginning");
        resumeAnalisis.setRequired(false);
        options.addOption(resumeAnalisis);

        Option mode = new Option("m", "mode", true, "mode of execution: 0 for evosuite tests, 1 for only JTExpert");
        mode.setRequired(false);
        options.addOption(mode);

        Option detectors = new Option("d", "detectors", true,
                "detectors to be executed: 0 for all, 1 only act-assert mismatch, 2 for only redundant code, 3 for only failed setup, 4 for only low contribution");
        detectors.setRequired(true);
        options.addOption(detectors);

        Option sufix = new Option("s", "sufix", true, "sufix of the test folder (for develop purposes)");
        sufix.setRequired(true);
        options.addOption(sufix);

        Option help = new Option("h", "help", false, "prints help");
        help.setRequired(false);
        options.addOption(help);

    }

    public static void setSourcePath(String sourcePathFlag) {
        if (sourcePathFlag != null) {
            generalPath = sourcePathFlag;
        } else {
            generalPath = "C:/Users/maxim/OneDrive/Escritorio/Code/Functional-Evosuite-tests/subjects";
        }
    }

    public static void setTestPath(String testPathFlag) {
        if (testPathFlag != null) {
            generalTestsPath = testPathFlag;
        } else {
            generalTestsPath = "C:/Users/maxim/OneDrive/Escritorio/s0";
        }
    }

    public static void sufixSetter(String sufixFlag) {
        if (sufixFlag != null) {
            sufix = sufixFlag;
        } else {
            sufix = "";
        }
    }

    public static void setJunitPath(String junitPathFlag) {
        if (junitPathFlag != null) {
            junitPath = junitPathFlag;
        } else {
            junitPath = "C:/Users/maxim/OneDrive/Escritorio/Code/Tool-Magister/smelly/junit-4.11.jar";
        }
    }

    public static void setEvosuiteStandaloneRuntimePath(String evosuiteStandaloneRuntimePathFlag) {
        if (evosuiteStandaloneRuntimePathFlag != null) {
            evosuiteStandaloneRuntimePath = evosuiteStandaloneRuntimePathFlag;
        } else {
            evosuiteStandaloneRuntimePath = "C:/Users/maxim/OneDrive/Escritorio/Code/Tool-Magister/smelly/evosuite-standalone-runtime-1.2.0.jar";
        }
    }

    public static void modeSetter(Integer modeFlag) {
        if (modeFlag != null) {
            mode = modeFlag;
        } else {
            mode = 0;
        }
    }

    public static void detectorsSetter(Integer detectorsFlag) {
        if (detectorsFlag != null) {
            detectors = detectorsFlag;
        } else {
            detectors = 0;
        }
    }

    public static void outputSetter(String outputFlag) {
        if (outputFlag != null) {
            outputFilePath = outputFlag;
        } else {
            outputFilePath = "C:/Users/maxim/OneDrive/Escritorio/s0";
        }
    }

    public static void outputFileNameSetter(String outputFileNameFlag) {
        if (outputFileNameFlag != null) {
            outputFileName = outputFileNameFlag + ".json";
        } else {
            outputFileName = "stats.json";
        }
    }

    public static void handleAllowedDetectors(String realName, DetectorResult detectorResults, String testFileName,
            TypeSolver typeSolver, SymbolResolver symbolSolver, CompilationUnit cu) {
        switch (detectors) {
            case 0:
                runActAssertMismatchDetectors(realName, testFileName, detectorResults, cu, typeSolver);
                runRedundantCodeDetectors(realName, testFileName, detectorResults, cu, typeSolver);
                runFailedSetupDetectors(realName, testFileName, detectorResults, cu, typeSolver);
                runTestingFieldAccessorsAndConstantsDetectors(realName, testFileName, detectorResults, cu, typeSolver,
                        symbolSolver);
                break;
            case 1:
                runActAssertMismatchDetectors(realName, testFileName, detectorResults, cu, typeSolver);
                break;
            case 2:
                runRedundantCodeDetectors(realName, testFileName, detectorResults, cu, typeSolver);
                break;
            case 3:
                runFailedSetupDetectors(realName, testFileName, detectorResults, cu, typeSolver);
                break;
            case 4:
                runTestingFieldAccessorsAndConstantsDetectors(realName, testFileName, detectorResults, cu, typeSolver,
                        symbolSolver);
                break;
            default:
                break;
        }
    }

}
