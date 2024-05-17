package com.example.ce316project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class ConfigController implements Initializable {
    @FXML
    public TextField configNameField, compilerParametersInput, extensionInput, languageInput, executionParameterInput;
    @FXML
    public Button generateConfigBtn, chooseCompilerPathButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        chooseCompilerPathButton.setOnAction(event -> chooseCompilerPath());
        generateConfigBtn.setOnAction(event -> configGenAct());
    }
    public void configGenAct() {
        //FOR THE VALUES FROM THE CONFIG SCREEN
        String configurationNameVal = configNameField.getText();
        String executionParameter = executionParameterInput.getText();
        String compilerParametersVal = compilerParametersInput.getText();
        String extensionVal = extensionInput.getText();
        String languageVal = languageInput.getText();

        if (configurationNameVal.isEmpty() || executionParameter.isEmpty() || compilerParametersVal.isEmpty() || extensionVal.isEmpty() || languageVal.isEmpty()) {
            Alert validationError = new Alert(Alert.AlertType.ERROR);
            validationError.setTitle("Validation Error");
            validationError.setHeaderText("Please fill in all required fields.");
            validationError.showAndWait();
        }
        else{
            //save configuration
            saveConfiguration(configurationNameVal, extensionVal, languageVal, compilerParametersVal,executionParameter);

            //deleting the entering values on the new config screen
            configNameField.setText(null);
            extensionInput.setText(null);
            languageInput.setText(null);
            executionParameterInput.setText(null);
            compilerParametersInput.setText(null);
        }
    }

    public void saveConfiguration(String configName, String extension, String language,String compilerParameter, String executionParameter) {
        try (FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/com/example/ce316project/"+configName + ".dat")) {
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
}