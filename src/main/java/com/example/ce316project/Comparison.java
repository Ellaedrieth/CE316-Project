package com.example.ce316project;

import javafx.scene.control.Alert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.example.ce316project.MainController.takenValue;

public class Comparison {
    public static ArrayList<Boolean> compareFileContents(File outputF, File expectedOutputF) throws IOException {
        ArrayList<Boolean> result =new ArrayList<Boolean>();
        for(int i=0;i<Compilation_Interpretation.size;i++){
            int count= line_count(MainController.getOutputFilePathItem(takenValue));
            boolean result_student=true;
            BufferedReader output = new BufferedReader(new FileReader(outputF));
            BufferedReader expect = new BufferedReader(new FileReader(expectedOutputF));
            String line_output ,line_expect;
            while(((line_output=output.readLine())!= null)){
                if(line_output.startsWith(Compilation_Interpretation.zipFileExtraction().get(i)) && ((line_expect=expect.readLine())!= null)){
                    line_output=line_output.substring(line_output.indexOf(":") + 2);
                    if(!(line_output.equals(line_expect))){
                        result_student=false;
                        break;
                    }else{
                        count--;
                        if(count==0){
                            result_student=true;
                        }else{
                            result_student=false;
                        }
                    }

                }else if(line_output.startsWith(Compilation_Interpretation.zipFileExtraction().get(i)) && ((line_expect=expect.readLine())== null)){
                    result_student=false;
                    break;
                }
            }
            result.add(result_student);

        }
        result_file(result);
        return result;
    }
    public static int line_count(File file) {
        int lineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return lineCount;
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
