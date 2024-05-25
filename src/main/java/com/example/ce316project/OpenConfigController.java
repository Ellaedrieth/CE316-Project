package com.example.ce316project;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class OpenConfigController implements Initializable {
    @FXML
    Button openButton, deleteButton, exportButton;
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
    Button editButton;
    @FXML
    TextField execFileBar;
    String configName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        openButton.setOnAction(actionEvent -> getConfigInfo());
        editButton.setOnAction(actionEvent -> setNewConfigInfo());
        deleteButton.setOnAction(actionEvent -> deleteConfiguration());
        exportButton.setOnAction(actionEvent -> export());
    }

    public void getConfigInfo() {
        configName = inputBar.getText() + ".dat";
        String path = "src/main/resources/com/example/ce316project/" + configName;

        File configFile = new File(path);
        if (!configFile.exists()) {
            Alert successAlert = new Alert(Alert.AlertType.WARNING);
            successAlert.setTitle("Configuration File Name Error");
            successAlert.setHeaderText("File name is not valid.");
            successAlert.showAndWait();
            configNameBar.setText(null);
            inputBar.setText(null);
        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(configFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Configuration Name: ")) {
                        String configName = line.substring("Configuration Name: ".length()).trim();
                        configNameBar.setText(configName);
                    }
                    if (line.startsWith("Executable File Extension: ")) {
                        String configName = line.substring("Executable File Extension: ".length()).trim();
                        execFileBar.setText(configName);
                    }
                    if (line.startsWith("Programming Language: ")) {
                        String configName = line.substring("Programming Language: ".length()).trim();
                        progLangBar.setText(configName);
                    }
                    if (line.startsWith("Compiler Parameters: ")) {
                        String configName = line.substring("Compiler Parameters: ".length()).trim();
                        compParamBar.setText(configName);
                    }
                    if (line.startsWith("Execution Parameter: ")) {
                        String configName = line.substring("Execution Parameter: ".length()).trim();
                        execParamBar.setText(configName);
                    }
                }
                inputBar.setText(null);
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setNewConfigInfo() {
        //FOR THE VALUES FROM THE CONFIG SCREEN
        String configurationNameVal = configNameBar.getText();
        String executionParameter = execParamBar.getText();
        String compilerParametersVal = compParamBar.getText();
        String extensionVal = execFileBar.getText();
        String languageVal = progLangBar.getText();
        if (configurationNameVal.isEmpty() || executionParameter.isEmpty() || compilerParametersVal.isEmpty() || extensionVal.isEmpty() || languageVal.isEmpty()) {
            Alert validationError = new Alert(Alert.AlertType.ERROR);
            validationError.setTitle("Validation Error");
            validationError.setHeaderText("Please fill in all required fields.");
            validationError.showAndWait();
        } else {
            //save configuration
            editConfiguration(configurationNameVal, extensionVal, languageVal, compilerParametersVal, executionParameter);
            //deleting the entering values on the open config screen
            configNameBar.setText(null);
            execFileBar.setText(null);
            progLangBar.setText(null);
            compParamBar.setText(null);
            execParamBar.setText(null);
        }
    }

    public void editConfiguration(String configName, String extension, String language, String compilerParameter, String executionParameter) {
        try (FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/com/example/ce316project/" + configName + ".dat")) {
            StringBuilder newConfigData = new StringBuilder(); //using StringBuilder is more effective from the point of memory usage
            newConfigData.append("Configuration Name: ").append(configName).append("\n");
            newConfigData.append("Executable File Extension: ").append(extension).append("\n");
            newConfigData.append("Programming Language: ").append(language).append("\n");
            newConfigData.append("Compiler Parameters: ").append(compilerParameter).append("\n");
            newConfigData.append("Execution Parameter: ").append(executionParameter).append("\n");


            fileOutputStream.write(newConfigData.toString().getBytes(StandardCharsets.UTF_8));

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("Configuration saved successfully!");
            successAlert.showAndWait();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("An error occurred while saving the configuration.");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }

    public void deleteConfiguration() {
        configName = inputBar.getText() + ".dat";
        String path = "src/main/resources/com/example/ce316project/" + configName;
        File file = new File(path);
        String name;
        if (file.exists()) {
            int dotIndex = file.getName().indexOf(".");
            name = file.getName().substring(0, dotIndex);
            if (file.delete()) {
                System.out.println("File deleted.");
                inputBar.setText("CONFIGURATION IS DELETED!");
                ProjectController.newLangItems.remove(name);
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("An error occurred while saving the configuration.");
                errorAlert.setContentText("Hey!");
                errorAlert.showAndWait();
            }
        }
    }

    public void export() {
        configName = inputBar.getText() + ".dat";
        String path = "src/main/resources/com/example/ce316project/" + configName;
        File file = new File(path);

        if (file.exists()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT files (*.dat)", "*.dat");
            fileChooser.getExtensionFilters().add(extFilter);

            File selectedFile = fileChooser.showSaveDialog(null);

            if (selectedFile != null) {
                String selectedFilePath = selectedFile.getAbsolutePath();
                if (!selectedFilePath.endsWith(".dat")) {
                    selectedFile = new File(selectedFilePath + ".dat");
                }

                try {
                    Files.copy(file.toPath(), selectedFile.toPath());
                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                    infoAlert.setTitle("Information");
                    infoAlert.setHeaderText("Export Success");
                    infoAlert.setContentText("The file is exported successfully at: " + selectedFile.getAbsolutePath());
                    infoAlert.showAndWait();
                    inputBar.setText(null);
                } catch (IOException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Exporting Error");
                    errorAlert.setContentText("The file cannot be exported: " + e.getMessage());
                    errorAlert.showAndWait();
                    inputBar.setText(null);
                }
            }
        }
        else if (!file.exists()){
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Information");
            infoAlert.setHeaderText("File Not Available");
            infoAlert.setContentText("The file is not available at: " + file.getAbsolutePath());
            infoAlert.showAndWait();
            inputBar.setText(null);
        }
        else {
            Alert infoAlert = new Alert(Alert.AlertType.WARNING);
            infoAlert.setTitle("Error");
            infoAlert.setHeaderText("File Not Available");
            infoAlert.setContentText("Write a file name.");
            infoAlert.showAndWait();
            inputBar.setText(null);
        }
    }

}

