package com.comp2042.ui;


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

/**
 * Controller for the main menu of the Tetris JFX application.
 * Handles navigation to different game screens (start game, resume game, leaderboard, settings)
 * and manages the main menu's audio and player name input.
 */
public class MainMenu implements Initializable {
    /**
     * Constructs a new MainMenu controller.
     * This class manages the main menu interactions, including navigation and game state.
     */
    public MainMenu() {
        // Default constructor
    }
    /** The text field where the player can enter their name, managed by FXML. */
    @FXML
    private TextField nameField;

    /** The button to resume an active game, managed by FXML. */
    @FXML
    private Button resumeButton;

    /** Stores the {@code Scene} of the active game, allowing it to be resumed. */
    private static Scene activeGameScene = null;
    /** The AudioManager instance for handling sound effects and music. */
    private AudioManager audioManager;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Starts playing menu music and sets the visibility of the resume button based on an active game.
     * @param location The URL location of the FXML file, or null if not applicable.
     * @param resources The ResourceBundle for localization, or null if not applicable.
     */
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

    /**
     * Starts a new game.
     * Reads the player's name, loads the game layout, sets up the {@code GameController},
     * switches to game music, and displays the game scene.
     * @param event The {@code ActionEvent} triggered by the button click.
     * @throws Exception if an error occurs during FXML loading or game setup.
     */
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

        Scene gameScene = new Scene(root, 700, 520);
        primaryStage.setScene(gameScene);
        primaryStage.show();

        activeGameScene = gameScene;

        // Switch to game music
        audioManager.playBackgroundMusic("game");

        new GameController(c, new com.comp2042.logic.board.SimpleBoard());
    }

    /**
     * Resumes a previously active game.
     * Switches to the stored game scene and calls {@code resumeFromMenu()} on the {@code GuiController}.
     * Plays a button click sound and switches to game music.
     * @param event The {@code ActionEvent} triggered by the button click.
     */
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

    /**
     * Displays the leaderboard screen.
     * Plays a button click sound, loads the leaderboard FXML, and displays it.
     * @param event The {@code ActionEvent} triggered by the button click.
     * @throws IOException if the FXML file for the leaderboard cannot be loaded.
     */
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

    /**
     * Displays the settings screen.
     * Plays a button click sound, loads the settings FXML, and displays it.
     * @param event The {@code ActionEvent} triggered by the button click.
     * @throws IOException if the FXML file for the settings cannot be loaded.
     */
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

    /**
     * Exits the application.
     * Plays a button click sound, disposes of audio resources, and then exits the JavaFX platform.
     * @param event The {@code ActionEvent} triggered by the button click.
     */
    public void exitGame(ActionEvent event) {
        audioManager.playButtonClick();
        audioManager.dispose();
        Platform.exit();
    }

    /**
     * Clears the reference to the active game scene, effectively making it un-resumable.
     * This is typically called when a game ends or a new game is started.
     */
    public static void clearActiveGame() {
        activeGameScene = null;
    }

    /**
     * Sets the {@code Scene} of the currently active game. This allows the game to be resumed later.
     * @param scene The {@code Scene} object of the active game.
     */
    public static void setActiveGameScene(Scene scene) {
        activeGameScene = scene;
    }

    /**
     * Returns to the main menu from any other screen.
     * Loads the main menu FXML, sets it as the current scene on the provided stage,
     * and switches to menu background music.
     * @param stage The {@code Stage} to display the main menu on.
     * @throws IOException if the FXML file for the main menu cannot be loaded.
     */
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