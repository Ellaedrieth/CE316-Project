package com.example.ce316project;

import javafx.scene.control.Alert;

import java.io.*;

public class Comparison {
    public static boolean compareFileContents(File outputF, File expectedOutputF) throws IOException {
        int i=0;
        boolean isEqual = true;
        for(int j = 0;j<Compilation_Interpretation.size;j++){
            try (BufferedReader reader1 = new BufferedReader(new FileReader(outputF));
                 BufferedReader reader2 = new BufferedReader(new FileReader(expectedOutputF))) {
                String line1, line2;
                System.out.println("size: " + Compilation_Interpretation.size);
                while (((line1 = reader1.readLine()) != null) && (line2 = reader2.readLine()) != null && i<Compilation_Interpretation.size) {
                    if (line1.startsWith(Compilation_Interpretation.zipFileExtraction().get(j))) {
                        String content1 = line1.substring(line1.indexOf(":") + 2);
                        System.out.println(j+1+"Content: " + content1);
                        System.out.println("Expected output line : " + line2);
                        reader1.readLine();
                    }
                }
            }
        }
        return isEqual;
    }
    public static void compareFiles(File outputF, File expectedOutputF) throws IOException {
        boolean result = compareFileContents(outputF, expectedOutputF);
        Alert alert = new Alert(result ? Alert.AlertType.INFORMATION : Alert.AlertType.WARNING);
        alert.setTitle(result ? "Success" : "Failure");
        alert.setHeaderText(result ? "Comparison is successfully completed!" : "Comparison is not completed!");
        alert.showAndWait();
    }

}
