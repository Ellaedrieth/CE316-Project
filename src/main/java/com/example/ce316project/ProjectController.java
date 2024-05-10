package com.example.ce316project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class ProjectController implements Initializable {

    @FXML
    ChoiceBox<String> projectConfigMenu;
    //to access the object from the ConfigController class; reference
    HashSet<String> progLanItems = new HashSet<>();
    static HashSet<String> newLangItems = new HashSet<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        projectConfigMenu.getItems().addAll(defaultLang());
        projectConfigMenu.getItems().addAll(newLangItems);

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
    public static void keepNewLangItems(String newSavedFile){
        newSavedFile = takeLang(newSavedFile);
        newLangItems.add(newSavedFile);
    }


    public ObservableList<String>  defaultLang(){
        progLanItems.add("java");
        progLanItems.add("python");
        return FXCollections.observableArrayList(progLanItems);
    }
}