package com.comp2042.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Controller for the controls settings view.
 * Manages navigation back to the main settings page.
 */
public class ControlsController {

    /**
     * Constructs a new ControlsController.
     * This class handles user interactions on the controls settings screen.
     */
    public ControlsController() {
        // Default constructor
    }

    /**
     * Handles the action of navigating back to the settings menu.
     * Plays a button click sound, loads the settings layout, and sets it as the current scene.
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException if the FXML file for the settings layout cannot be loaded.
     */
    public void backToSettings(ActionEvent event) throws IOException {
        AudioManager.getInstance().playButtonClick();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        URL location = getClass().getClassLoader().getResource("settingsLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 600, 510);
        stage.setScene(scene);
        stage.show();
    }
} 