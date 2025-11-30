package com.comp2042.ui;

import com.comp2042.GameController;
import com.comp2042.logic.AudioManager;
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

    private static Scene activeGameScene = null;
    private AudioManager audioManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        audioManager = AudioManager.getInstance();

        // Start menu music when main menu loads
        audioManager.playBackgroundMusic("menu");

        if (activeGameScene != null && resumeButton != null) {
            resumeButton.setVisible(true);
            resumeButton.setManaged(true);
        }
    }

    public void startGame(ActionEvent event) throws Exception {
        audioManager.playButtonClick();

        String playerName = nameField.getText().trim();
        if (playerName.isEmpty()) {
            playerName = "Player";
        }

        Stage primaryStage = (Stage) ((Parent) event.getSource()).getScene().getWindow();

        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        GuiController c = fxmlLoader.getController();

        c.setPlayerName(playerName);

        Scene gameScene = new Scene(root, 700, 495);
        primaryStage.setScene(gameScene);

        activeGameScene = gameScene;

        // Switch to game music
        audioManager.playBackgroundMusic("game");

        new GameController(c);
    }

    public void resumeGame(ActionEvent event) {
        audioManager.playButtonClick();

        if (activeGameScene != null) {
            Stage primaryStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            Parent root = activeGameScene.getRoot();
            GuiController controller = (GuiController) root.getUserData();

            primaryStage.setScene(activeGameScene);
            primaryStage.show();

            // Switch back to game music
            audioManager.playBackgroundMusic("game");

            if (controller != null) {
                controller.resumeFromMenu();
            }
        }
    }

    public void showLeaderboard(ActionEvent event) throws IOException {
        audioManager.playButtonClick();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        URL location = getClass().getClassLoader().getResource("leaderboardLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 600, 510);
        stage.setScene(scene);
        stage.show();
    }

    public void showSettings(ActionEvent event) throws IOException {
        audioManager.playButtonClick();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        URL location = getClass().getClassLoader().getResource("settingsLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 600, 510);
        stage.setScene(scene);
        stage.show();
    }

    public void exitGame(ActionEvent event) {
        audioManager.playButtonClick();
        audioManager.dispose();
        Platform.exit();
    }

    public static void clearActiveGame() {
        activeGameScene = null;
    }

    public static void setActiveGameScene(Scene scene) {
        activeGameScene = scene;
    }

    public static void returnToMainMenu(Stage stage) throws IOException {
        // Play button click sound
        AudioManager.getInstance().playButtonClick();

        URL location = MainMenu.class.getClassLoader().getResource("menuLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 600, 510);
        stage.setScene(scene);
        stage.show();

        // Switch back to menu music
        AudioManager.getInstance().playBackgroundMusic("menu");
    }
}