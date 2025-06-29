import os
import re
import argparse

def clean_line_for_brace_counting(line):
    line = re.sub(r'//.*', '', line)
    line = re.sub(r'/\*.*?\*/', '', line)
    line = re.sub(r'".*?"', '', line)
    line = re.sub(r"'.*?'", '', line)
    return line

def split_java_test_class(input_java_file_path, output_base_dir="."):
    try:
        with open(input_java_file_path, 'r') as f:
            lines = f.readlines()
    except FileNotFoundError:
        print(f"Error: Input file '{input_java_file_path}' not found.")
        return
    except Exception as e:
        print(f"Error reading file '{input_java_file_path}': {e}")
        return

    package_line = ""
    imports = []
    class_annotations = []
    class_declaration_prefix = "" 
    class_extends_implements = "" 
    original_class_name = ""
    
    test_methods = []
    current_method_lines = []
    in_method = False
    brace_level = 0
    
    in_main_class_scope = False
    main_class_brace_level = 0 

    for i, line in enumerate(lines):
        stripped_line = line.strip()
        cleaned_line = clean_line_for_brace_counting(line)
        if stripped_line.startswith("package ") and not package_line:
            package_line = stripped_line
            continue
        if stripped_line.startswith("import "):
            imports.append(stripped_line)
            continue
        if not original_class_name and re.match(r'@(?:RunWith|EvoRunnerParameters)\b', stripped_line):
            class_annotations.append(stripped_line)
            continue
        
        class_match = re.search(r'(public|private|protected)\s+class\s+(\w+)\s*(.*?)\s*\{', line)
        if class_match and not original_class_name:
            original_class_name = class_match.group(2)
            class_declaration_prefix = line[:class_match.start(2)] 
            class_extends_implements = class_match.group(3).strip() 
            in_main_class_scope = True 
            main_class_brace_level += cleaned_line.count('{') 
            continue
        
        if in_main_class_scope:
            main_class_brace_level += cleaned_line.count('{') - cleaned_line.count('}')
            if main_class_brace_level == 0:
                in_main_class_scope = False

        if in_main_class_scope:
            if re.search(r'@Test', stripped_line) and not in_method:
                in_method = True
                current_method_lines.append(line)
                brace_level = cleaned_line.count('{') - cleaned_line.count('}')
                continue

            if in_method:
                current_method_lines.append(line)
                brace_level += cleaned_line.count('{') - cleaned_line.count('}')
                if brace_level == 0 and '}' in cleaned_line and len(cleaned_line.strip()) == 1:
                    test_methods.append("".join(current_method_lines).strip())
                    current_method_lines = []
                    in_method = False
                    continue

    if not original_class_name:
        print(f"Error: Could not find original class declaration in '{input_java_file_path}'. Is it a valid Java test file?")
        return

    output_package_path = ""
    if package_line:
        package_name = package_line.replace("package ", "").replace(";", "").strip()
        output_package_path = os.path.join(output_base_dir, *package_name.split('.'))
    else:
        output_package_path = output_base_dir 

    os.makedirs(output_package_path, exist_ok=True)

    print(f"Found {len(test_methods)} test methods in {original_class_name}.")

    for method_content in test_methods:
        method_name_match = re.search(r'public\s+void\s+(\w+)\s*\(.*\)', method_content)
        if method_name_match:
            method_name = method_name_match.group(1)
            new_class_name = f"{original_class_name}_{method_name}"
            new_file_name = f"{new_class_name}.java"
            new_file_path = os.path.join(output_package_path, new_file_name)
            new_file_lines = []
            if package_line:
                new_file_lines.append(package_line + ";\n\n") 
            
            for imp in imports:
                new_file_lines.append(imp + "\n")
            new_file_lines.append("\n") 

            for ann in class_annotations:
                new_file_lines.append(ann + "\n")
            
            new_file_lines.append(f"{class_declaration_prefix}{new_class_name} {class_extends_implements} {{\n")
            
            indented_method_content = "  " + method_content.replace("\n", "\n  ")
            indented_method_content = re.sub(r' +\n', '\n', indented_method_content)
            
            new_file_lines.append(f"{indented_method_content}\n")
            new_file_lines.append("}\n")

            try:
                with open(new_file_path, 'w') as out_f:
                    out_f.writelines(new_file_lines)
                print(f"Created: {new_file_path}")
            except IOError as e:
                print(f"Error writing to file {new_file_path}: {e}")
        else:
            print(f"Warning: Could not extract method name from test method content: \n{method_content[:200]}...") # Print first 200 chars


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Program to split a test suite to its tests")

    parser.add_argument("-inputTestSuite", type=str, help="The path to the test suite to split", required=True)
    parser.add_argument("-outputDir", type=str, help="The path to the output directory", required=True)

    args = parser.parse_args()
    split_java_test_class(args.inputTestSuite, args.outputDir)
