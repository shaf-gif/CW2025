package com.comp2042.ui;

import com.comp2042.GameController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu implements Initializable {
    @FXML
    private TextField nameField;

    @FXML
    private Button resumeButton;

    // Static variable to store the active game scene
    private static Scene activeGameScene = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Show resume button only if there's an active game
        if (activeGameScene != null && resumeButton != null) {
            resumeButton.setVisible(true);
            resumeButton.setManaged(true);
        }
    }

    public void startGame(ActionEvent event) throws Exception {
        String playerName = nameField.getText().trim();
        if (playerName.isEmpty()) {
            playerName = "Player"; // Will be auto-numbered if needed
        }

        Stage primaryStage = (Stage) ((Parent) event.getSource()).getScene().getWindow();

        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        GuiController c = fxmlLoader.getController();

        // Pass player name to GUI controller
        c.setPlayerName(playerName);

        Scene gameScene = new Scene(root, 700, 495);
        primaryStage.setScene(gameScene);

        // Store the game scene as active
        activeGameScene = gameScene;

        new GameController(c);
    }

    public void resumeGame(ActionEvent event) {
        if (activeGameScene != null) {
            Stage primaryStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(activeGameScene);
            primaryStage.show();
        }
    }

    public void showLeaderboard(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        URL location = getClass().getClassLoader().getResource("leaderboardLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 600, 510);
        stage.setScene(scene);
        stage.show();
    }

    public void showSettings(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        URL location = getClass().getClassLoader().getResource("settingsLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 600, 510);
        stage.setScene(scene);
        stage.show();
    }

    public void exitGame(ActionEvent event) {
        Platform.exit();
    }

    // Static method to clear active game when game is over or new game starts
    public static void clearActiveGame() {
        activeGameScene = null;
    }

    // Static method to set active game scene
    public static void setActiveGameScene(Scene scene) {
        activeGameScene = scene;
    }

    // 🌟 NEW: Static method to return to the main menu (used by sub-controllers like LeaderboardController)
    /**
     * Loads the main menu FXML and switches the scene back to the main menu.
     * @param stage The primary stage of the application.
     */
    public static void returnToMainMenu(Stage stage) throws IOException {
        URL location = MainMenu.class.getClassLoader().getResource("menuLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        // Ensure the size matches the menu's standard size
        Scene scene = new Scene(root, 600, 510);
        stage.setScene(scene);
        stage.show();
    }
}