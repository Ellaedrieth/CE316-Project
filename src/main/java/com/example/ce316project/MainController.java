package com.example.ce316project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class MainController {
    @FXML
    public MenuButton projectMenuBtn;

    @FXML
    private Button runBtn;

    @FXML
    public ChoiceBox<String> projectTitle;
    @FXML
    public TextArea submissionPathArea, outputPathArea;
    @FXML
    public MenuButton configMenuBtn;
    String selectedItemForConfigBtn;
    @FXML
    public MenuButton projectMenuB;

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

    public void openProjectButton(ActionEvent event) {
        System.out.println("You clicked Open Project");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("open_project.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Disables main window until this one is closed
            stage.setTitle("Open Project");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't open new window.");
        }
    }

    public void editProjectButton(ActionEvent event) {
        System.out.println("You clicked Edit Project");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit_project.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Disables main window until this one is closed
            stage.setTitle("Edit Project");
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





}