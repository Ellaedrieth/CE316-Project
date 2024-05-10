package com.example.ce316project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class ProjectController implements Initializable {
    public final String CONFIG_FILE_PATH = "src/main/resources/com/example/ce316project/";
    @FXML
    Button submissionZipBtn, outputBtn;
    @FXML
    TextField submissionTextField,outputTextField;
    @FXML
    ChoiceBox<String> projectConfigMenu;
    //to access the object from the ConfigController class; reference
    HashSet<String> progLanItems = new HashSet<>();
    static HashSet<String> newLangItems = new HashSet<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        projectConfigMenu.getItems().addAll(defaultLang());
        projectConfigMenu.getItems().addAll(scanConfigFiles());
        submissionZipBtn.setOnAction(event -> chooseSubmissionPath());
        outputBtn.setOnAction(event -> chooseOutputFile());
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
}