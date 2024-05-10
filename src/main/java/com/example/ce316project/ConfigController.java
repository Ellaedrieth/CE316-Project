package com.example.ce316project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class ConfigController implements Initializable {
    @FXML
    public TextField configNameField, compilerPathInput, compilerParametersInput, languageInput;
    @FXML
    public Button generateConfigBtn, chooseCompilerPathButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseCompilerPathButton.setOnAction(event -> chooseCompilerPath());
        generateConfigBtn.setOnAction(event -> configGenAct());

    }
    public void configGenAct() {
        //FOR THE VALUES FROM THE CONFIG SCREEN
        String configurationNameVal = configNameField.getText();
        String compilerPathVal = compilerPathInput.getText();
        String compilerParametersVal = compilerParametersInput.getText();
        String languageVal = languageInput.getText();

        if (configurationNameVal.isEmpty() || compilerPathVal.isEmpty() || compilerParametersVal.isEmpty() || languageVal.isEmpty()) {
            Alert validationError = new Alert(Alert.AlertType.ERROR);
            validationError.setTitle("Validation Error");
            validationError.setHeaderText("Please fill in all required fields.");
            validationError.showAndWait();
        }
        else{
            //save configuration
            saveConfiguration(configurationNameVal, languageVal, compilerPathVal, compilerParametersVal);
        }
    }


    public void chooseCompilerPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Compiler Path");

        //only for the executable files (to compile)
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Executable Files (*.exe)", "*.exe");
        fileChooser.getExtensionFilters().add(extensionFilter);

        //to open the computer files screen
        Stage stage = (Stage) chooseCompilerPathButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            compilerPathInput.setText(selectedFile.getAbsolutePath());
        }
    }

    public void saveConfiguration(String configName, String language, String compilerPath, String compilerParameter) {
        try (FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/com/example/ce316project/"+configName + ".dat")) {
            StringBuilder newConfigData = new StringBuilder(); //using StringBuilder is more effective from the point of memory usage
            newConfigData.append("Configuration Name: ").append(configName).append("\n");
            newConfigData.append("Programming Language: ").append(language).append("\n");
            newConfigData.append("Compiler Path: ").append(compilerPath).append("\n");
            newConfigData.append("Compiler Parameters: ").append(compilerParameter).append("\n");

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
}