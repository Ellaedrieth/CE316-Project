package com.example.ce316project;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Comparison {
    public static void compareFiles(File outputF, File expectedOutputF) throws IOException{
        //to check for both file existing
        if(!outputF.exists() || !expectedOutputF.exists()){
            System.out.println("One or both files do not exist!");
            throw new IllegalArgumentException("One or both files do not exist!");
        }
        //to check if the files are not directory
        if(!outputF.isFile() || !expectedOutputF.isFile()){
            System.out.println("One or both files are directory.");
            throw new IllegalArgumentException("One or both files are directory.");
        }
        //Comparison process
        try(FileInputStream fileInputStream1 = new FileInputStream(outputF);
            FileInputStream fileInputStream2 = new FileInputStream(expectedOutputF)){//to read as a byte type from the file
            while ((fileInputStream1.read() != -1) && (fileInputStream2.read() != -1)){ //if it is not empty.
                if(fileInputStream1.read() == fileInputStream2.read()){
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Comparison is successfully completed!");
                    successAlert.showAndWait();
                }
                else{
                    Alert successAlert = new Alert(Alert.AlertType.WARNING);
                    successAlert.setTitle("Failure");
                    successAlert.setHeaderText("Comparison is not completed!");
                    successAlert.showAndWait();
                }
            }
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

}
