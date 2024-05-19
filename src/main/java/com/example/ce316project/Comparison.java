package com.example.ce316project;

import org.apache.commons.io.FileUtils;
import java.io.*;
import java.util.ArrayList;

import static com.example.ce316project.MainController.takenValue;

public class Comparison {
    public static ArrayList<Boolean> compareFileContents(File outputF, File expectedOutputF) throws IOException {
        ArrayList<Boolean> result = new ArrayList<>();

        int size = Compilation_Interpretation.zipFileExtraction().size();
        for (int i = 0; i < size; i++) {
            boolean result_student = true;
            boolean contains = false;

            try (BufferedReader output = new BufferedReader(new FileReader(outputF));
                 BufferedReader expect = new BufferedReader(new FileReader(expectedOutputF))) {

                String line_output;
                String line_expect = expect.readLine();

                while ((line_output = output.readLine()) != null) {
                    if (line_output.startsWith(Compilation_Interpretation.zipFileExtraction().get(i))) {
                        contains = true;
                        line_output = line_output.substring(line_output.indexOf(":") + 2);

                        if (line_expect != null) {
                            if (!line_output.equals(line_expect)) {
                                result_student = false;
                                break;
                            } else {
                                line_expect = expect.readLine();
                            }
                        } else {
                            result_student = false;
                            break;
                        }
                    }
                }

                // Check if expect has more lines remaining
                if (line_expect != null) {
                    result_student = false;
                }

                if (!contains) {
                    result_student = false;
                }

                result.add(result_student);
            }
        }

        result_file(result);
        return result;
    }

    public static void result_file(ArrayList<Boolean> result_list) throws IOException {
        ArrayList<String> names =Compilation_Interpretation.zipFileExtraction();
        File outputFile = new File("src/main/java/com/example/ce316project/results.txt");
        BufferedWriter writer= new BufferedWriter(new FileWriter(outputFile));
        for (int i=0;i< result_list.size();i++ ){
                writer.append(names.get(i)+" "+result_list.get(i)+"\n");
                writer.flush();
        }
    }

}
