package com.yourorganization.magister_tool.helpers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class CoverageJsonFileHelper {
    public static Map<String, Map<String, Map<String, List<Integer>>>> getAllCoverageData() {
        return allCoverageData;
    }

    static Map<String, Map<String, Map<String, List<Integer>>>> allCoverageData;

    public static void readCoverageFile(String jsonFilePath){
        System.out.println(jsonFilePath);
        File jsonFile = new File(jsonFilePath);

        if (!jsonFile.exists()) {
            System.err.println("Error: JSON file not found at " + jsonFilePath);
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            TypeReference<Map<String, Map<String, Map<String, List<Integer>>>>> typeRef = new TypeReference<Map<String, Map<String, Map<String, List<Integer>>>>>() {};
            allCoverageData = objectMapper.readValue(jsonFile, typeRef);
            System.out.println("Successfully read JSON data from: " + jsonFilePath);

        } catch (IOException e) {
            System.err.println("Error reading or parsing JSON: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets a Set of all top-level Test Class Names found in the loaded data.
     * @return A Set of Strings representing test class names, or an empty set if no data is loaded.
     */
    public static Set<String> getAllTestClassNames() {
        if (allCoverageData == null) {
            return Collections.emptySet();
        }
        return allCoverageData.keySet();
    }

    /**
     * Gets a Set of Test Method Names for a specific Test Class.
     * @param testClassName The full name of the test class (e.g., "net.kencochrane.a4j.beans.BrowseNode_ESTest").
     * @return A Set of Strings representing test method names, or an empty set if the test class is not found.
     */
    public static Set<String> getTestMethodNamesForClass(String testClassName) {
        if (allCoverageData == null || !allCoverageData.containsKey(testClassName)) {
            return Collections.emptySet();
        }
        Map<String, Map<String, List<Integer>>> testMethods = allCoverageData.get(testClassName);
        return testMethods != null ? testMethods.keySet() : Collections.emptySet();
    }

    /**
     * Gets a Set of Class Under Test names covered by a specific test method within a test class.
     * @param testClassName The full name of the test class.
     * @param testMethodName The name of the test method (e.g., "test27").
     * @return A Set of Strings representing class under test names, or an empty set if the test class or method is not found.
     */
    public static Set<String> getClassUnderTestNamesForTest(String testClassName, String testMethodName) {
        if (allCoverageData == null || !allCoverageData.containsKey(testClassName)) {
            return Collections.emptySet();
        }
        Map<String, Map<String, List<Integer>>> testMethods = allCoverageData.get(testClassName);
        if (testMethods == null || !testMethods.containsKey(testMethodName)) {
            return Collections.emptySet();
        }
        Map<String, List<Integer>> classesUnderTest = testMethods.get(testMethodName);
        return classesUnderTest != null ? classesUnderTest.keySet() : Collections.emptySet();
    }

    /**
     * Gets the list of executed line numbers for a specific Class Under Test,
     * covered by a specific test method within a test class.
     * @param testClassName The full name of the test class.
     * @param testMethodName The name of the test method.
     * @param classUnderTestName The name of the class under test (e.g., "BrowseNode.java").
     * @return A List of Integers representing executed line numbers, or an empty list if data is not found.
     */
    public static List<Integer> getExecutedLines(String testClassName, String testMethodName, String classUnderTestName) {
        if (allCoverageData == null || !allCoverageData.containsKey(testClassName)) {
            return Collections.emptyList();
        }
        Map<String, Map<String, List<Integer>>> testMethods = allCoverageData.get(testClassName);
        if (testMethods == null || !testMethods.containsKey(testMethodName)) {
            return Collections.emptyList();
        }
        Map<String, List<Integer>> classesUnderTest = testMethods.get(testMethodName);
        if (classesUnderTest == null || !classesUnderTest.containsKey(classUnderTestName)) {
            return Collections.emptyList();
        }
        return classesUnderTest.get(classUnderTestName);
    }

    /**
     * Gets a Map of all classes covered by a specific test method within a test class,
     * along with their executed lines.
     * @param testClassName The full name of the test class.
     * @param testMethodName The name of the test method.
     * @return A Map where keys are Class Under Test names (String) and values are Lists of executed lines (List<Integer>),
     * or an empty map if the test class or method is not found.
     */
    public static Map<String, List<Integer>> getCoverageForTestMethod(String testClassName, String testMethodName) {
        System.out.println(allCoverageData.keySet());
        for(String key: allCoverageData.keySet()){
            if(key.contains(testClassName)){
                System.out.println("Tenemos la llave");
                Map<String, Map<String, List<Integer>>> testMethods = allCoverageData.get(key);
                return testMethods.get(testMethodName);
            }
        }
        return Collections.emptyMap();
    }


    /**
     * Gets a Map of all classes covered by a specific test method within a test class,
     * along with their executed lines.
     * @param testClassName The full name of the test class.
     * @param testMethodName The name of the test method.
     * @param coveredClass The name of the test covered class
     * @return A Map where keys are Class Under Test names (String) and values are Lists of executed lines (List<Integer>),
     * or an empty map if the test class or method is not found.
     */

    public static boolean hasCoverageInformation(String testClassName) {
        for (String key : allCoverageData.keySet()) {
            if (key.endsWith(testClassName + "_ESTest"))
                return true;
        }
        return false;
    }

    /**
     * Gets a List of all executed lines from a test method in reference to a covered class. 
     * @param testClassName The full name of the test class.
     * @param testMethodName The name of the test method.
     * @param coveredClass The name of the test covered class
     * @return A (List<Integer>) of the executed lines of a test in reference of a covered class,
     * or an empty map if the test class or method is not found.
     */
    public static List<Integer> getCoveredLinesForTest(String testClassName, String testMethodName, String coveredClass){
        for(String key: allCoverageData.keySet()){
            if(key.endsWith(testClassName + "_ESTest")){
                Map<String, Map<String, List<Integer>>> testMethods = allCoverageData.get(key);
                Map<String, List<Integer>> coveredClasses = testMethods.get(testMethodName);
                if(coveredClasses == null){
                    return Collections.emptyList();
                }
                return coveredClasses.get(coveredClass);
            }
        }
        return Collections.emptyList();
    }
}
