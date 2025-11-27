package com.comp2042.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize if needed
    }

    public void showControls(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        URL location = getClass().getClassLoader().getResource("controlsLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 600, 510);
        stage.setScene(scene);
        stage.show();
    }

    public void backToMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        URL location = getClass().getClassLoader().getResource("menuLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 600, 510);
        stage.setScene(scene);
        stage.show();
    }
}