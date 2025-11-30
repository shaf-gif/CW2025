package com.comp2042.ui;

import com.comp2042.logic.AudioManager;
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

public class SettingsController implements Initializable {

    @FXML
    private Slider musicVolumeSlider;

    @FXML
    private Slider sfxVolumeSlider;

    @FXML
    private CheckBox musicToggle;

    @FXML
    private CheckBox sfxToggle;

    @FXML
    private Label musicVolumeLabel;

    @FXML
    private Label sfxVolumeLabel;

    private AudioManager audioManager;

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

    private void updateMusicVolumeLabel() {
        musicVolumeLabel.setText(String.format("Music: %d%%", (int) musicVolumeSlider.getValue()));
    }

    private void updateSfxVolumeLabel() {
        sfxVolumeLabel.setText(String.format("SFX: %d%%", (int) sfxVolumeSlider.getValue()));
    }

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