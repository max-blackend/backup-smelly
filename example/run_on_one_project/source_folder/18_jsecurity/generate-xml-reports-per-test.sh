#!/bin/bash

# Adjust these paths to match your project setup
JACOCO_AGENT_JAR="./jacocoagent.jar"
JACOCO_CLI_JAR="./jacococli.jar"

# Directory containing your application's compiled .class files
APP_CLASSES_DIR="./jsecurity.jar" 

# Directory containing your compiled test .class files (e.g., Evosuite tests)
TEST_CLASSES_DIR="output_individual_tests/" 

# Directory containing your application's .java source files
SOURCE_CODE_DIR="src/main/java/" 

# Base directory for all generated reports and .exec files
REPORT_BASE_DIR="jacoco-individual-reports"
EXEC_DIR="$REPORT_BASE_DIR/execs"
XML_REPORT_DIR="$REPORT_BASE_DIR/xml"

# --- Create report directories ---
mkdir -p "$EXEC_DIR"
mkdir -p "$XML_REPORT_DIR"

# --- Prepare Classpath ---
# This classpath must contain everything needed for JUnit to run,
# your application's classes, and your test classes.
# Example: "junit-platform-console-standalone.jar:lib/*:$APP_CLASSES_DIR:$TEST_CLASSES_DIR"
CP="./hamcrest-core-1.3.jar:./junit-4.11.jar:lib/*:$APP_CLASSES_DIR:$TEST_CLASSES_DIR"

echo "Using Classpath: $CP"
echo "Collecting test classes from: $TEST_CLASSES_DIR"

# --- Find all Evosuite test classes and process them ---
# This 'find' command looks for compiled .class files matching the pattern.
# We then convert the file path into a fully qualified class name.
find "$TEST_CLASSES_DIR" -name "*_ESTest_test*.class" | while read -r class_file_path; do
    # Extract the relative path from TEST_CLASSES_DIR
    relative_path=${class_file_path#"$TEST_CLASSES_DIR"/}
    
    # Remove the .class extension
    class_name_with_slashes=${relative_path%.class}
    
    # Convert slashes to dots to get the fully qualified class name
    fully_qualified_class_name=${class_name_with_slashes//\//.}

    echo "--------------------------------------------------------"
    echo "Processing test class: $fully_qualified_class_name"

    # Define unique .exec and XML report file names for this test
    EXEC_FILE="$EXEC_DIR/${fully_qualified_class_name}.exec"
    XML_REPORT_FILE="$XML_REPORT_DIR/${fully_qualified_class_name}.xml"

    # --- Step 1: Run individual test class in a separate JVM to generate a unique .exec file ---
    echo "Running JUnitCore for $fully_qualified_class_name to generate $EXEC_FILE..."
    java -javaagent:"$JACOCO_AGENT_JAR=destfile=$EXEC_FILE,append=false,dumponexit=true" \
         -cp "$CP" \
         org.junit.runner.JUnitCore "$fully_qualified_class_name"

    # Check the exit status of the JUnitCore command
    if [ $? -eq 0 ]; then
        echo "JUnitCore execution for $fully_qualified_class_name completed successfully. Generating JaCoCo report."
        
        # --- Step 2: Generate XML report for this specific .exec file ---
        java -jar "$JACOCO_CLI_JAR" report "$EXEC_FILE" \
             --classfiles "$APP_CLASSES_DIR" \
             --sourcefiles "$SOURCE_CODE_DIR" \
             --xml "$XML_REPORT_FILE"

        if [ $? -eq 0 ]; then
            echo "Successfully generated XML report: $XML_REPORT_FILE"
        else
            echo "ERROR: Failed to generate XML report for $fully_qualified_class_name."
        fi
    else
        echo "WARNING: JUnitCore execution for $fully_qualified_class_name failed or encountered issues. Skipping report generation for this test."
        # You might want to remove the .exec file here if it's incomplete
        # rm -f "$EXEC_FILE"
    fi
    echo "--------------------------------------------------------"
done

echo "All individual JaCoCo XML reports are located in: $XML_REPORT_DIR"
echo "All individual JaCoCo .exec files are located in: $EXEC_DIR"