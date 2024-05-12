package com.example.ce316project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class OpenConfigController implements Initializable {
    @FXML
    Button openButton;
    @FXML
    TextField inputBar;
    @FXML
    TextField configNameBar;
    @FXML
    TextField execParamBar;
    @FXML
    TextField progLangBar;
    @FXML
    TextField compParamBar;
    @FXML
    Button genButton;
    @FXML
    TextField execFileBar;




    String configName;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        openButton.setOnAction(actionEvent -> getConfigInfo());
    }
    public void getConfigInfo(){
        configName = inputBar.getText() + ".dat";
        String path = "src/main/resources/com/example/ce316project/"+configName;

        File configFile = new File(path);
        if(!configFile.exists()){
            Alert successAlert = new Alert(Alert.AlertType.WARNING);
            successAlert.setTitle("Configuration File Name Error");
            successAlert.setHeaderText("File name is not valid.");
            successAlert.showAndWait();
            configNameBar.setText("");
        }
        else{
            try {
                BufferedReader reader = new BufferedReader(new FileReader(configFile));
                String line;
                while((line = reader.readLine()) != null){
                    if(line.startsWith("Configuration Name: ")){
                        String configName = line.substring("Configuration Name: ".length()).trim();
                        configNameBar.setText(configName);
                    }
                    if(line.startsWith("Executable File Extension: ")){
                        String configName = line.substring("Executable File Extension: ".length()).trim();
                        execFileBar.setText(configName);
                    }
                    if(line.startsWith("Programming Language: ")){
                        String configName = line.substring("Programming Language: ".length()).trim();
                        progLangBar.setText(configName);
                    }
                    if(line.startsWith("Compiler Parameters: ")){
                        String configName = line.substring("Compiler Parameters: ".length()).trim();
                        compParamBar.setText(configName);
                    }
                    if(line.startsWith("Execution Parameter: ")){
                        String configName = line.substring("Execution Parameter: ".length()).trim();
                        execParamBar.setText(configName);
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }





}
