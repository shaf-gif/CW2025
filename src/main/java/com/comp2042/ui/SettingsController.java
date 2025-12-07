package com.comp2042.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the settings view, managing audio settings (volume, mute) and navigation to controls.
 */
public class SettingsController implements Initializable {

    /**
     * Constructs a new SettingsController.
     * This class manages the application's settings, including audio and navigation to controls.
     */
    public SettingsController() {
        // Default constructor
    }

    /** Slider for controlling background music volume, managed by FXML. */
    @FXML
    private Slider musicVolumeSlider;

    /** Slider for controlling sound effects volume, managed by FXML. */
    @FXML
    private Slider sfxVolumeSlider;

    /** CheckBox for toggling background music on/off, managed by FXML. */
    @FXML
    private CheckBox musicToggle;

    /** CheckBox for toggling sound effects on/off, managed by FXML. */
    @FXML
    private CheckBox sfxToggle;

    /** Label displaying the current music volume percentage, managed by FXML. */
    @FXML
    private Label musicVolumeLabel;

    /** Label displaying the current SFX volume percentage, managed by FXML. */
    @FXML
    private Label sfxVolumeLabel;

    /** The AudioManager instance used for controlling audio playback and settings. */
    private AudioManager audioManager;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up initial UI states based on current audio settings and adds listeners to update settings.
     * @param location The URL location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        audioManager = AudioManager.getInstance();

        // Initialize sliders with current values
        musicVolumeSlider.setValue(audioManager.getMusicVolume() * 100);
        sfxVolumeSlider.setValue(audioManager.getSfxVolume() * 100);

        // Initialize checkboxes
        musicToggle.setSelected(audioManager.isMusicEnabled());
        sfxToggle.setSelected(audioManager.isSfxEnabled());

        // Update labels
        updateMusicVolumeLabel();
        updateSfxVolumeLabel();

        // Add listeners
        musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            audioManager.setMusicVolume(newVal.doubleValue() / 100.0);
            updateMusicVolumeLabel();
        });

        sfxVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            audioManager.setSfxVolume(newVal.doubleValue() / 100.0);
            updateSfxVolumeLabel();
            // Play sample sound when adjusting
            audioManager.playButtonClick();
        });

        musicToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            audioManager.setMusicEnabled(newVal);
            musicVolumeSlider.setDisable(!newVal);
            if (newVal) {
                audioManager.playBackgroundMusic("menu");
            }
        });

        sfxToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            audioManager.setSfxEnabled(newVal);
            sfxVolumeSlider.setDisable(!newVal);
            if (newVal) {
                audioManager.playButtonClick();
            }
        });

        // Disable sliders if audio is disabled
        musicVolumeSlider.setDisable(!audioManager.isMusicEnabled());
        sfxVolumeSlider.setDisable(!audioManager.isSfxEnabled());
    }

    /**
     * Updates the music volume label to display the current percentage value from the slider.
     */
    private void updateMusicVolumeLabel() {
        musicVolumeLabel.setText(String.format("Music: %d%%", (int) musicVolumeSlider.getValue()));
    }

    /**
     * Updates the sound effects volume label to display the current percentage value from the slider.
     */
    private void updateSfxVolumeLabel() {
        sfxVolumeLabel.setText(String.format("SFX: %d%%", (int) sfxVolumeSlider.getValue()));
    }

    /**
     * Resets all audio settings (music volume, SFX volume, music enabled, SFX enabled) to their default values.
     * Triggers a button click sound effect.
     */
    @FXML
    public void resetAudioSettings() {
        audioManager.playButtonClick();

        // Reset to default values
        musicVolumeSlider.setValue(50);
        sfxVolumeSlider.setValue(70);
        musicToggle.setSelected(true);
        sfxToggle.setSelected(true);

        audioManager.setMusicVolume(0.5);
        audioManager.setSfxVolume(0.7);
        audioManager.setMusicEnabled(true);
        audioManager.setSfxEnabled(true);
    }

    /**
     * Navigates to the controls settings screen.
     * Plays a button click sound, loads the controls layout FXML, and displays it.
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException if the FXML file for the controls layout cannot be loaded.
     */
    public void showControls(ActionEvent event) throws IOException {
        audioManager.playButtonClick();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        URL location = getClass().getClassLoader().getResource("controlsLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 600, 510);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates back to the main menu screen.
     * Plays a button click sound, loads the main menu layout FXML, and displays it.
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException if the FXML file for the main menu layout cannot be loaded.
     */
    public void backToMenu(ActionEvent event) throws IOException {
        audioManager.playButtonClick();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        URL location = getClass().getClassLoader().getResource("menuLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 600, 510);
        stage.setScene(scene);
        stage.show();
    }
}