package com.example.ce316project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    ArrayList<Project> projects;
    ArrayList<Configuration> configurations;
    Path tempDir;
    int selectedProjectIndex = 0;

    @FXML
    public MenuButton projectMenuB;

    @FXML
    private Button runBtn;

    @FXML
    public ChoiceBox<String> projectTitle;

    /** TODO run
     * "tableView"ı disable et. <-- Önemli
     * her bir "zip" dosyasının adını "tableView"ın sol sütununa yaz.
     * "project"te tanımlanmış olan her bir zip dosyası için (paralel olarak):
     *     "zip"in açılmış halini temp klasörüne at
     *     klasörün içinden projeyi çalıştır
     *     "configuration"dan dönen sonuca göre "tableView"ı düzenle
     *     Hepsi bir sonuç döndürünce veya "timeOut" olmasını bekle
     * "tableView"ı aktive et
     * butona basıldığında sonuçların olduğu zip dosyasınının nereye konulacağını sor
     */
    public void run() {

    }

    // TODO dosyalardan project'ler okunacak
    private ArrayList<Project> getSavedProjects() {
        return new ArrayList<Project>();
    }

    // TODO dosyalardan configuration'lar okunacak
    private ArrayList<Configuration> getSavedConfigurations() {
        return new ArrayList<Configuration>();
    }

    // TODO Yeni oluşturulmuş projeyi "projects" arrayList'ine ekle
    public void newProject(ActionEvent event) {
        System.out.println("New Project");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("project_pane.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Disables main window until this one is closed
            stage.setTitle("New Project");
            stage.setScene(new Scene(root1));
            stage.show();
            stage.close();
        } catch (Exception e) {
            System.out.println("Can't open new window.");
        }
    }

    // TODO openProject
    public void openProject(ActionEvent event) {
        System.out.println("You clicked Open Project");
    }

    // TODO editProject
    public void editProject(ActionEvent event) {
        System.out.println("You clicked Edit Project");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurations = getSavedConfigurations();
        projects = getSavedProjects();

        // Asıl zip dosyalarının üzerinde çalışmak için geçici bir klasör oluştur
        try {
            tempDir = Files.createTempDirectory("");
            // System.out.println(tempDir.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Dosya seçme fonksiyonu
    public void selectAFile(ActionEvent event) {
        if(event.getSource() ==  runBtn) {
            FileChooser fc = new FileChooser();
            File selectedFile = fc.showOpenDialog(null);
            if (selectedFile != null) {
                System.out.println(selectedFile.getAbsolutePath());
            }
        }
    }
     */
            /* Zipten çıkarma fonksiyonu
        File dir = new File("temp");
        if (!dir.exists()) boolean t = dir.mkdir();
        String zipPath = "javaHelloAssignments.zip";
        try {
            ZipFile zipFile = new ZipFile(zipPath);
            zipFile.extractAll("temp");
        } catch (ZipException e) {
            e.printStackTrace();
        }
         */
        /* Geçici klasör oluşturma fonksiyonu
        try {
            Path tempDir = Files.createTempDirectory("temporary");
            System.out.println(tempDir.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
}