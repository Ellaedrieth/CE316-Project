package com.example.ce316project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 420, 650);
        stage.setTitle("Integrated Assignment Environment");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(actionEvent -> {
            Platform.exit();
            System.exit(0);
        });
    }
    public static void main(String[] args) {
        launch();
    }
}