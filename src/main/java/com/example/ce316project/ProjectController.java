package com.example.ce316project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    @FXML
    AnchorPane projectPaneScreen;
    //to access the object from the ConfigController class; reference
    HashSet<String> progLanItems = new HashSet<>();
    static HashSet<String> newLangItems = new HashSet<>();
    static HashSet<String> ProjectItems = new HashSet<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //projectConfigMenu.getItems().addAll(defaultLang());
        projectConfigMenu.getItems().addAll(scanConfigFiles());
        projectConfigMenu.setValue("Select Configuration Name");
        submissionZipBtn.setOnAction(event -> chooseSubmissionPath());
        outputBtn.setOnAction(event -> chooseOutputFile());
        projectGenBtn.setOnAction(event -> {
            projectGenerateAct();

           }
        );
    }
    public static String takeConfigName(String newConfigFileName){
        String languageValue = "";
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(newConfigFileName))){
            String line;
            while((line = bufferedReader.readLine()) != null){
                if(line.startsWith("Configuration Name: ")){
                    languageValue = line.substring("Configuration Name: ".length()).trim();
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
                String langTaken = takeConfigName(datFile);
                newLangItems.add(langTaken);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return newLangItems;
    }

//    public ObservableList<String>  defaultLang(){
//        progLanItems.add("java");
//        progLanItems.add("python");
//        return FXCollections.observableArrayList(progLanItems);
//    }

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

        if (project_name.isEmpty() || projectConfigMenu.getSelectionModel().getSelectedItem()==null || projectConfigMenu.getSelectionModel().getSelectedItem().equals("Select Configuration Name") | zip_file_path.isEmpty() || output_file.isEmpty()) {
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

            //deleting the entering values on the new project screen
            projectNameTextField.setText(null);
            projectConfigMenu.setValue("Select Configuration Name");
            submissionTextField.setText(null);
            outputTextField.setText(null);
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

    public void newConfigurationButtonOnProjectPane(ActionEvent event) {
        System.out.println("You clicked New Configuration on the Project Pane screen");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("new_config.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Disables main window until this one is closed
            stage.setTitle("New Configuration");
            stage.setScene(new Scene(root1));
            stage.show();

            //close the current window
            Stage currentStage = (Stage) projectPaneScreen.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            System.out.println("Can't open new window.");
        }
    }


}