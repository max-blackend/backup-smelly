package com.yourorganization.magister_tool.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.reflect.TypeToken;
public class DetectorResult {
    public boolean resumeAnalisis;
    public String outputFilePath;
    public String outputFileName;

    private ArrayList<String> detectors = new ArrayList<>();
    private HashMap<String, HashMap<String, Integer>> incidencesPerDetector = new HashMap<>();
    private HashMap<String, HashMap<String, List<String>>> incidencesPerClassTest = new HashMap<>();
    private String[] detectorsArray = { "Duplicated Setup", "Not asserted return values",
            "Asserting object initialization multiple times", "Testing only field accesors", "Not asserted side effects",
            "Test without assertions", "Multiple calls to the same void method",
            "Assertion with not related parent class method", "Exceptions due to external dependencies",
            "Exceptions due to null arguments", "Asserting Constants", "Testing the same exception scenario",
            "Not null assertion", "Exceptions due to incomplete setup" };

    public DetectorResult(String outputFilePath, String outputFileName, boolean resumeAnalisis) {
        this.resumeAnalisis = resumeAnalisis;
        this.outputFilePath = outputFilePath;
        this.outputFileName = outputFileName;

        initializeDetectors();
        // loadPreviousResults();
    }

    private void initializeDetectors() {
        for (String detector : detectorsArray) {
            detectors.add(detector);
        }
    }

    public void addIncidence(String testFile, String issue, Integer issueCount) {
        ensureTestFileEntryExists(testFile);
        incidencesPerDetector.get(testFile).put(issue, issueCount);
    }

    private void ensureTestFileEntryExists(String testFile) {
        if (!incidencesPerDetector.containsKey(testFile)) {
            HashMap<String, Integer> detectorMap = new HashMap<>();
            for (String detector : detectors) {
                detectorMap.put(detector, 0);
            }
            incidencesPerDetector.put(testFile, detectorMap);
        }
    }

    public void addIncidencesPerClassTest(String testfile, String issue, ArrayList<String> issueRegistry) {
        ensureTestClassEntryExists(testfile);
        incidencesPerClassTest.get(testfile).put(issue, issueRegistry);
        System.out.println("[DONE] " + issue);
    }

    private void ensureTestClassEntryExists(String testfile) {
        if (!incidencesPerClassTest.containsKey(testfile)) {
            HashMap<String, List<String>> detectorMap = new HashMap<>();
            for (String detector : detectors) {
                detectorMap.put(detector, new ArrayList<>());
            }

            incidencesPerClassTest.put(testfile, detectorMap);
        }
    }

    public void printToConsoleCurrentStatus(String testFile) {
        System.out.println("Test File: " + testFile);
        for (String detector : detectors) {
            System.out.println(detector + ": " + incidencesPerDetector.get(testFile).get(detector));
        }
    }

    public void printToCsvFile() {
        writeEachClassOcurrences();
        // String csvFile = "C:/Users/maxim/OneDrive/Escritorio/Code/Tool-Magister/smelly/results_FULL_JTExpert.csv";
        // String csvFile = "C:/Users/maxim/OneDrive/Escritorio/CONTEO/Final_samples_standard/EVALUATION_REVISITED_FISTANDARD.csv";
        // try (PrintWriter writer = new PrintWriter(new File(csvFile))) {
        //     writer.write(createCsvHeader());
        //     writer.write(createCsvData());
        // } catch (FileNotFoundException e) {
        //     System.out.println(e.getMessage());
        // }
    }

    private String createCsvHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("File");
        for (String detector : detectors) {
            sb.append(',');
            sb.append(detector);
        }
        sb.append('\n');
        return sb.toString();
    }

    private String createCsvData() {
        StringBuilder sb = new StringBuilder();
        for (String testFile : incidencesPerDetector.keySet()) {
            sb.append(testFile);
            for (String detector : detectors) {
                sb.append(',');
                sb.append(incidencesPerDetector.get(testFile).get(detector));
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public void writeEachClassOcurrences() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(incidencesPerClassTest);


        try (FileWriter fileWriter = new FileWriter(
                // "C:/Users/maxim/OneDrive/Escritorio/Code/Tool-Magister/smelly/incidencesPerClassTest_FULL_JTExpert.json")) {
                    // "C:/Users/maxim/OneDrive/Escritorio/CONTEO/Final_samples_jtexpert/EVALUATION_REVISITED_FINAL_JTEXPERT.json")) {
                        // "C:/Users/maxim/OneDrive/Escritorio/s0/stats.json")) {
                            this.outputFilePath + "/" + this.outputFileName)) {

            fileWriter.write(jsonOutput);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean alreadyParsed(String testFile) {
        return incidencesPerDetector.containsKey(testFile);
    }

    public void loadPreviousResults() {
        Gson gson = new Gson();
        String json = "";
        try {
            // json = new String(Files.readAllBytes(Paths.get("C:/Users/maxim/OneDrive/Escritorio/Code/Tool-Magister/smelly/incidencesPerClassTest_FULL_JTExpert.json")));
            // json = new String(Files.readAllBytes(Paths.get("C:/Users/maxim/OneDrive/Escritorio/CONTEO/Final_samples_standard/EVALUATION_REVISITED_FINAL_STANDARD.json")));
            // json = new String(Files.readAllBytes(Paths.get("C:/Users/maxim/OneDrive/Escritorio/s0/stats.json")));
            if (resumeAnalisis == true) {
                json = new String(Files.readAllBytes(Paths.get(this.outputFilePath + "/" + this.outputFileName)));
            } else {
                json = new String("{}");
            }
        } catch (Exception e) {
            System.out.println("LOADING PREVIOUS RESULTS "+ e.getMessage());
        }
        incidencesPerDetector = gson.fromJson(json, new TypeToken<HashMap<String, HashMap<String, List<String>>>>() {
        }.getType());
    }

    public List<String> getTestsWithSideEffects(String testFile) {
        return incidencesPerClassTest.get(testFile).get("Not asserted side effects");
    }
}
