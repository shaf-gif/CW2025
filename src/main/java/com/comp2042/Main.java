package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Ensure custom font is loaded before creating the scene
        Font.loadFont(getClass().getResource("/digital.ttf").toExternalForm(), 12);

        // Load the FXML file
        URL location = getClass().getResource("/menuLayout.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);

        // Load the FXML content and create the scene
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 510);

        // Set up the window
        primaryStage.setTitle("Tetris JFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
