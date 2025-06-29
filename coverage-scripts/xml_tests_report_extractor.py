import xml.etree.ElementTree as ET
import sys
import os
import argparse
import json

def get_executed_lines(xml_file_path):
    executed_lines_by_class = {}

    try:
        tree = ET.parse(xml_file_path)
        root = tree.getroot()

        for package_elem in root.findall('package'):
            for class_elem in package_elem.findall('sourcefile'):
                class_name = class_elem.get('name')
                current_executed_lines = []
                for line_elem in class_elem.findall('line'):
                    line_number = int(line_elem.get('nr'))
                    covered_instructions = int(line_elem.get('ci'))
                    missed_instructions = int(line_elem.get('mi')) 
                    missed_branches = int(line_elem.get('mb')) 
                    
                    if covered_instructions > 0 and missed_instructions == 0 and missed_branches == 0:
                        current_executed_lines.append(line_number)
                
                if current_executed_lines:
                    executed_lines_by_class[class_name] = sorted(current_executed_lines)

    except FileNotFoundError:
        print(f"Error: XML file not found at {xml_file_path}", file=sys.stderr)
    except ET.ParseError:
        print(f"Error: Could not parse XML from {xml_file_path}. Check if it's valid JaCoCo XML.", file=sys.stderr)
    except Exception as e:
        print(f"An unexpected error occurred while processing {xml_file_path}: {e}", file=sys.stderr)

    return executed_lines_by_class

def process_jacoco_reports_in_folder(folder_path):
    # Final structure:
    # { "TestClassName": { "TestMethodName": { "ClassUnderTest.java": [lines] } } }
    reorganized_data = {}

    if not os.path.isdir(folder_path):
        print(f"Error: Folder '{folder_path}' not found or is not a directory.", file=sys.stderr)
        return reorganized_data

    print(f"Searching for JaCoCo XML reports in: '{folder_path}'...")
    
    for root, _, files in os.walk(folder_path):
        for file in files:
            if file.endswith(".xml"):
                xml_file_path = os.path.join(root, file)
                print(f"Processing report: '{xml_file_path}'")
                
                full_test_identifier = os.path.splitext(os.path.basename(xml_file_path))[0]
                parts = full_test_identifier.rsplit('_', 1)
                
                test_class_name = full_test_identifier
                test_method_name = "full_report"

                if len(parts) == 2:
                    test_class_name = parts[0]
                    test_method_name = parts[1]
                else:
                    print(f"Warning: Could not reliably split '{full_test_identifier}' into test class and method. Using '{test_class_name}' as class and '{test_method_name}' as method.", file=sys.stderr)

                executed_lines_for_this_test = get_executed_lines(xml_file_path)
                
                if executed_lines_for_this_test:
                    test_class_entry = reorganized_data.setdefault(test_class_name, {})
                    test_class_entry[test_method_name] = executed_lines_for_this_test
    
    return reorganized_data

if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        description="Extract fully executed line numbers from JaCoCo XML reports and organize them by Test Class -> Test Method -> Class Under Test -> Executed Lines."
    )
    parser.add_argument(
        "folder_path",
        type=str,
        help="Path to the folder containing JaCoCo XML reports (will process subfolders recursively)."
    )
    parser.add_argument(
        "--json",
        type=str,
        help="Optional: Path to a JSON file to save the reorganized data."
    )

    args = parser.parse_args()

    reorganized_processed_data = process_jacoco_reports_in_folder(args.folder_path)

    if reorganized_processed_data:
        if args.json:
            try:
                with open(args.json, 'w') as json_file:
                    json.dump(reorganized_processed_data, json_file, indent=4)
                print(f"\nSuccessfully exported reorganized data to JSON file: '{args.json}'")
            except IOError as e:
                print(f"Error writing to JSON file '{args.json}': {e}", file=sys.stderr)
            except Exception as e:
                print(f"An unexpected error occurred while writing JSON: {e}", file=sys.stderr)
        else:
            print("\n--- Summary of Executed Lines (Organized by Test Class -> Test Method) ---")
            
            for test_class, test_methods_data in reorganized_processed_data.items():
                print(f"\nTest Class: {test_class}")
                if test_methods_data:
                    for test_method, classes_coverage_data in test_methods_data.items():
                        print(f"  Test Method: {test_method}")
                        if classes_coverage_data:
                            for class_name, lines in classes_coverage_data.items():
                                print(f"    Class Under Test: {class_name}")
                                print(f"    Executed Lines: {', '.join(map(str, lines))}")
                        else:
                            print("    No classes covered by this test method.")
                else:
                    print("  No test methods found for this test class.")
                print("-" * 60)
    else:
        print(f"No JaCoCo XML reports found or no data extracted/reorganized from '{args.folder_path}'.")