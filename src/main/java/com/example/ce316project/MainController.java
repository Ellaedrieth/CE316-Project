package com.example.ce316project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class MainController implements Initializable {
    @FXML
    public MenuButton projectMenuBtn;
    @FXML
    public MenuButton configMenuBtn;
    @FXML
    public Button runBtn;
    @FXML
    public ChoiceBox<String> openProject;
    public static final String Existing_Project_File_Path = "src/main/resources/com/example/ce316project/";
    @FXML
    Button refreshBtn;
    static HashSet<String> updateProjectItems = new HashSet<>();
    static String takenValue;
    String selectedConfigExtension = "";

    @FXML
    Button helpButton;
    @FXML
    private TableView<Student> resultTable;

    @FXML
    private TableColumn<Student,String> resultCol;

    @FXML
    private TableColumn<Student, Long> studentCol;

    ObservableList<Student> stu_list= FXCollections.observableArrayList();
    @FXML
    public Button resultBtn, zipBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        openProject.getItems().addAll(getProjectName());
        openProject.setValue("Open Projects");
        refreshBtn.setOnAction(event -> refreshProjectList());
        runBtn.setOnAction(event -> {
            takenValue = openProject.getValue();
            try {
                File file = new File(MainController.getDirectoryPathWithoutZip() + "\\project_result_output.txt");
                if(file.exists()){file.delete();}
                Comparison.compareFileContents(Compilation_Interpretation.compile(Compilation_Interpretation.compilationFileReg(Compilation_Interpretation.zipFileExtraction()),
                        Compilation_Interpretation.executionFileReg(Compilation_Interpretation.zipFileExtraction())),getOutputFilePathItem(takenValue) );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        resultBtn.setOnAction(actionEvent -> {
            resultTable.getItems().clear();
            set_list();
            Alert addedSuccess = new Alert(Alert.AlertType.INFORMATION);
            addedSuccess.setTitle("Results Information");
            addedSuccess.setHeaderText("Results are being displayed!");
            addedSuccess.showAndWait();
            studentCol.setCellValueFactory(new PropertyValueFactory<Student,Long>("student_id"));
            resultCol.setCellValueFactory(new PropertyValueFactory<Student,String>("result"));
            resultTable.setItems(stu_list);
            resultTable.refresh();
            try {
                FileUtils.cleanDirectory(new File(MainController.getDirectoryPathWithoutZip() + "\\extractedFiles"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        zipBtn.setOnAction(actionEvent -> {
            String zipFilePath = getDirectoryPathWithoutZip()+"\\students_zip";
            File zipDirectory = new File(zipFilePath);
            if(!zipDirectory.exists()){
                zipDirectory.mkdir();
            }
            try {
                FileUtils.cleanDirectory(new File(MainController.getDirectoryPathWithoutZip() + "\\students_zip"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            convertToZip();
        });
    }
    public void openConfigurationButton(ActionEvent event) {
        System.out.println("You clicked New Configuration");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("open_config.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Disables main window until this one is closed
            stage.setTitle("Open Configuration");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't open new window.");
        }

    }
    public void helpButtonClicked(ActionEvent event) {
        System.out.println("You clicked Help");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("help_pane.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Disables main window until this one is closed
            stage.setTitle("Help");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't open new window.");
        }
    }

    public void refreshProjectList(){
        openProject.getItems().clear();
        openProject.getItems().addAll(getProjectName());
    }
    public static String takeExistingProjectName(String newProjectFileName){
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
    public HashSet<String> getProjectName(){
        FileScanner fileScannerForProject = new FileScanner();
        try {
            HashSet<String> projectFilesList = fileScannerForProject.scanProjectFiles(Existing_Project_File_Path);
            for(String txtFile : projectFilesList){
                String p_nameTaken = takeExistingProjectName(txtFile);
                updateProjectItems.add(p_nameTaken);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return updateProjectItems;
    }
    //to open a project, first the path of the project file must choose
    public static String getProjectPath(String takenValue){
        return Existing_Project_File_Path+takenValue+".txt";
    }


    //to get the configuration name using the project path in the getProjectPath() method
    public String getConfigExtensionItem() {
        String selectedConfigName = "";
        //to represent the current file path
        File file = new File(getProjectPath(takenValue));
        if (file.exists()) {
            //Runtime.getRuntime().exec("cmd /c start " + getProjectPath());
            try (BufferedReader reader = new BufferedReader(new FileReader(getProjectPath(takenValue)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    //to search the "Configuration Name" field in the file
                    if (line.startsWith("Configuration Name:")) {
                        selectedConfigName = line.substring(line.indexOf(":")+2);
                        BufferedReader readerExtension = new BufferedReader(new FileReader(Existing_Project_File_Path+selectedConfigName+".dat"));
                        String lineExtension;
                            try {
                                while ((lineExtension = readerExtension.readLine()) != null){
                                    if(lineExtension.startsWith("Executable File Extension:")){
                                        selectedConfigExtension = lineExtension.substring(lineExtension.indexOf(":")+2);

                                    }
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("File cannot be found!");
        }
        return selectedConfigExtension;
    }

    //to get the zip file path name using the project path in the getProjectPath() method
    public static String getZipFilePathItem(String takenValue) {
        String zipFilePath = "";

        //to represent the current file path
        File file = new File(getProjectPath(takenValue));
        if (file.exists()) {
            //Runtime.getRuntime().exec("cmd /c start " + getProjectPath());
            try (BufferedReader reader = new BufferedReader(new FileReader(getProjectPath(takenValue)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    //to search the "Zip File Path" field in the file
                    if (line.startsWith("Zip File Path:")) {
                        zipFilePath = line.substring(line.indexOf(":")+2);
                    }
                }
            } catch (IOException exception) {
                System.out.println("An error occurred while searching for the specified value");
                exception.printStackTrace();
            }
        } else {
            System.out.println("File cannot be found!");
        }
        return zipFilePath;
    }
    public static String getOnlyZipName(){
        String fullZipPath = getZipFilePathItem(takenValue);
        return fullZipPath.substring(fullZipPath.lastIndexOf('\\')+ 1);
    }
    public static String getDirectoryPathWithoutZip() {
        String fullZipPath = getZipFilePathItem(takenValue);
        int lastIndex = fullZipPath.lastIndexOf('\\');
        if (lastIndex != -1) {
            return fullZipPath.substring(0, lastIndex);
        } else {
            return "";
        }
    }
    //to get the zip file path name using the project path in the getProjectPath() method
    public static File getOutputFilePathItem(String takenValue) {
        String outputFilePath = "";
        //to represent the current file path
        File file = new File(getProjectPath(takenValue));
        if (file.exists()) {
            //Runtime.getRuntime().exec("cmd /c start " + getProjectPath());
            try (BufferedReader reader = new BufferedReader(new FileReader(getProjectPath(takenValue)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    //to search the "Output File" field in the file
                    if (line.startsWith("Output File:")) {
                        outputFilePath = line.substring(line.indexOf(":")+2);
                    }
                }
            } catch (IOException exception) {
                System.out.println("An error occurred while searching for the specified value");
                exception.printStackTrace();
            }
        } else {
            System.out.println("File cannot be found!");
        }
        return new File(outputFilePath);
    }


    //SWITCHING FUNCTIONS BETWEEN SCREENS
    public void newProjectButton(ActionEvent event) {
        System.out.println("New Project");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("project_pane.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Disables main window until this one is closed
            stage.setTitle("New Project");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't open new window.");
        }
    }


    public void newConfigurationButton(ActionEvent event) {
        System.out.println("You clicked New Configuration");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("new_config.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Disables main window until this one is closed
            stage.setTitle("New Configuration");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't open new window.");
        }
    }
    public void set_list() {
        try {
            String line;
            File result_file = new File("src/main/java/com/example/ce316project/results.txt");
            if (result_file.exists()) {
                try{
                    BufferedReader result = new BufferedReader(new FileReader(result_file));
                    while ((line = result.readLine()) != null) {
                        String[] data = line.split(" ");
                        stu_list.add(new Student(data[1], data[0]));
                    }

                } catch(Exception e){
                    System.out.println("Error occured while getting the result.Please check the file.");
                }
            }
        }
        catch(Exception e){
            System.out.println("Result file did not found please check the folder");
        }
    }
    public void convertToZip(){
        if(stu_list.isEmpty()){
            Alert emptyWarning = new Alert(Alert.AlertType.WARNING);
            emptyWarning.setTitle("Not Any Student Value");
            emptyWarning.setHeaderText("The table view of the students' results is empty!");
            emptyWarning.showAndWait();
        }
        else{
            for(Student students : stu_list){
                String zipFileName = MainController.getDirectoryPathWithoutZip()+"\\students_zip" + "\\" + students.getStudent_id() + ".zip";
                try(ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileName))) {
                    ZipEntry zipEntry = new ZipEntry(students.getStudent_id()+".txt");
                    zipOutputStream.putNextEntry(zipEntry);

                    byte[] studentData = students.getResult().getBytes();
                    zipOutputStream.write(studentData,0, studentData.length);
                    zipOutputStream.closeEntry();

                    System.out.println("Zipped the file: " + zipFileName);

                } catch (IOException e) {
                    System.out.println("There is an error while creating the zip.");
                    throw new RuntimeException(e);
                }
            }
            Alert emptyWarning = new Alert(Alert.AlertType.INFORMATION);
            emptyWarning.setTitle("ZIP file process of each student");
            emptyWarning.setHeaderText("The ZIP file process of each student is completed successfully!");
            emptyWarning.showAndWait();
        }
    }
}