package com.example.ce316project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.ResourceBundle;

public class ProjectController implements Initializable {
    public final String CONFIG_FILE_PATH = "src/main/resources/com/example/ce316project/";
    public final String Project_File_Path = "src/main/resources/com/example/ce316project/";
    @FXML
    Button submissionZipBtn, outputBtn , projectGenBtn;
    @FXML
    TextField submissionTextField,outputTextField,projectNameTextField;
    @FXML
    ChoiceBox<String> projectConfigMenu;
    //to access the object from the ConfigController class; reference
    HashSet<String> progLanItems = new HashSet<>();
    static HashSet<String> newLangItems = new HashSet<>();
    static HashSet<String> ProjectItems = new HashSet<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        projectConfigMenu.getItems().addAll(defaultLang());
        projectConfigMenu.getItems().addAll(scanConfigFiles());
        submissionZipBtn.setOnAction(event -> chooseSubmissionPath());
        outputBtn.setOnAction(event -> chooseOutputFile());
        projectGenBtn.setOnAction(event -> projectGenerateAct());

    }
    public static String takeLang(String newConfigFileName){
        String languageValue = "";
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(newConfigFileName))){
            String line;
            while((line = bufferedReader.readLine()) != null){
                if(line.startsWith("Programming Language: ")){
                    languageValue = line.substring("Programming Language: ".length()).trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return languageValue;
    }

    public HashSet<String> scanConfigFiles(){
        FileScanner fileScannerForConfig = new FileScanner();
        try {
            HashSet<String> configFilesList = fileScannerForConfig.scanConfigFiles(CONFIG_FILE_PATH);
            for(String datFile : configFilesList){
                String langTaken = takeLang(datFile);
                newLangItems.add(langTaken);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return newLangItems;
    }

    public ObservableList<String>  defaultLang(){
        progLanItems.add("java");
        progLanItems.add("python");
        return FXCollections.observableArrayList(progLanItems);
    }

    public void chooseSubmissionPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Submission File");

        //only for the submission zip files (for ZIP)
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("ZIP Files (*.zip)", "*.zip");
        fileChooser.getExtensionFilters().add(extensionFilter);

        //to open the computer files screen
        Stage stage = (Stage) submissionZipBtn.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            submissionTextField.setText(selectedFile.getAbsolutePath());
        }
    }
    public void chooseOutputFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Submission File");

        //only for the submission zip files (for ZIP)
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);

        //to open the computer files screen
        Stage stage = (Stage) outputBtn.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            outputTextField.setText(selectedFile.getAbsolutePath());
        }
    }

    public void saveProject(String projectName, String configName, String zipFilePath, String outputFile) {
        try (FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/com/example/ce316project/"+ projectName + ".txt")) {
            StringBuilder newProjectData = new StringBuilder(); //using StringBuilder is more effective from the point of memory usage
            newProjectData.append("Project Name: ").append(projectName).append("\n");
            newProjectData.append("Configuration Name: ").append(configName).append("\n");
            newProjectData.append("Zip File Path: ").append(zipFilePath).append("\n");
            newProjectData.append("Output File: ").append(outputFile).append("\n");

            fileOutputStream.write(newProjectData.toString().getBytes(StandardCharsets.UTF_8));

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("Project saved successfully!");
            successAlert.showAndWait();

        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("An error occurred while saving the project.");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }
    public void projectGenerateAct() {
        //Values taken from new project screen
        String project_name = projectNameTextField.getText();
        String config_name = projectConfigMenu.getSelectionModel().getSelectedItem();
        String zip_file_path = submissionTextField.getText();
        String output_file = outputTextField.getText();
        scanProjectFiles();

        if (project_name.isEmpty() || projectConfigMenu.getValue()==null || zip_file_path.isEmpty() || output_file.isEmpty()) {
            Alert validationError = new Alert(Alert.AlertType.ERROR);
            validationError.setTitle("Validation Error");
            validationError.setHeaderText("Please fill in all required fields.");
            validationError.showAndWait();
        } else if (ProjectItems.contains(project_name)) {
            Alert validationError = new Alert(Alert.AlertType.ERROR);
            validationError.setTitle("Duplicate Error");
            validationError.setHeaderText("This project already exists.");
            validationError.showAndWait();
        } else{
            //save project
            saveProject(project_name, config_name, zip_file_path, output_file);
        }
    }
    public static String takeProjectName(String newProjectFileName){
        String ProjectVal = "";
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(newProjectFileName))){
            String line;
            while((line = bufferedReader.readLine()) != null){
                if(line.startsWith("Project Name: ")){
                    ProjectVal = line.substring("Project Name: ".length()).trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ProjectVal;
    }

    public HashSet<String> scanProjectFiles(){
        FileScanner fileScannerForProject = new FileScanner();
        try {
            HashSet<String> projectFilesList = fileScannerForProject.scanProjectFiles(Project_File_Path);
            for(String txtFile : projectFilesList){
                String p_nameTaken = takeProjectName(txtFile);
                ProjectItems.add(p_nameTaken);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return ProjectItems;
    }


}