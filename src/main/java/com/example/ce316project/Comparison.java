package com.example.ce316project;

import javafx.scene.control.Alert;

import java.io.*;
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
                        System.out.println(Compilation_Interpretation.zipFileExtraction().get(i)+ "was here 1");
                        System.out.println(line_output);
                        System.out.println(line_expect);
                        result_student=false;
                        break;
                    }else{
                        System.out.println(Compilation_Interpretation.zipFileExtraction().get(i)+ "was here 2");
                        count--;
                        if(count==0){
                            result_student=true;
                        }else{
                            result_student=false;
                        }
                    }

                }else if(line_output.startsWith(Compilation_Interpretation.zipFileExtraction().get(i)) && ((line_expect=expect.readLine())== null)){
                    System.out.println(Compilation_Interpretation.zipFileExtraction().get(i)+ "was here 3");
                    result_student=false;
                    break;
                }
            }
            result.add(result_student);

        }
        for(Boolean bool : result){
            System.out.println(bool);
        }

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

}
