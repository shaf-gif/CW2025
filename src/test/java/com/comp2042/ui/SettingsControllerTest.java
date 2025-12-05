package com.comp2042.ui;

import com.comp2042.JavaFxTestBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SettingsControllerTest extends JavaFxTestBase {

    private SettingsController controller;

    @Mock
    private AudioManager mockAudioManager;

    private Slider musicVolumeSlider;
    private Slider sfxVolumeSlider;
    private CheckBox musicToggle;
    private CheckBox sfxToggle;
    private Label musicVolumeLabel;
    private Label sfxVolumeLabel;

    @BeforeEach
    public void setUp() throws Exception {
        AudioManager.setInstanceForTest(mockAudioManager);

        controller = new SettingsController();

        musicVolumeSlider = new Slider();
        sfxVolumeSlider = new Slider();
        musicToggle = new CheckBox();
        sfxToggle = new CheckBox();
        musicVolumeLabel = new Label();
        sfxVolumeLabel = new Label();

        // Use reflection to inject the FXML fields
        setField(controller, "musicVolumeSlider", musicVolumeSlider);
        setField(controller, "sfxVolumeSlider", sfxVolumeSlider);
        setField(controller, "musicToggle", musicToggle);
        setField(controller, "sfxToggle", sfxToggle);
        setField(controller, "musicVolumeLabel", musicVolumeLabel);
        setField(controller, "sfxVolumeLabel", sfxVolumeLabel);
    }

    @AfterEach
    public void tearDown() {
        AudioManager.resetInstance();
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    public void initialize_setsInitialValues() {
        // Set specific values to test against
        when(mockAudioManager.getMusicVolume()).thenReturn(0.8);
        when(mockAudioManager.getSfxVolume()).thenReturn(0.6);
        when(mockAudioManager.isMusicEnabled()).thenReturn(false);
        when(mockAudioManager.isSfxEnabled()).thenReturn(true);

        controller.initialize(null, null);

        assertEquals(80, musicVolumeSlider.getValue());
        assertEquals(60, sfxVolumeSlider.getValue());
        assertFalse(musicToggle.isSelected());
        assertTrue(sfxToggle.isSelected());
        assertEquals("Music: 80%", musicVolumeLabel.getText());
        assertEquals("SFX: 60%", sfxVolumeLabel.getText());
    }

    @Test
    public void musicVolumeSlider_updatesAudioManager() {
        controller.initialize(null, null);
        clearInvocations(mockAudioManager);
        musicVolumeSlider.setValue(45);
        verify(mockAudioManager).setMusicVolume(0.45);
        assertEquals("Music: 45%", musicVolumeLabel.getText());
    }

    @Test
    public void sfxVolumeSlider_updatesAudioManager() {
        controller.initialize(null, null);
        clearInvocations(mockAudioManager);
        sfxVolumeSlider.setValue(85);
        verify(mockAudioManager).setSfxVolume(0.85);
        assertEquals("SFX: 85%", sfxVolumeLabel.getText());
    }

    @Test
    public void musicToggle_updatesAudioManager() {
        controller.initialize(null, null);
        clearInvocations(mockAudioManager);
        musicToggle.fire();
        verify(mockAudioManager).setMusicEnabled(true);
        assertFalse(musicVolumeSlider.isDisable());
    }

    @Test
    public void sfxToggle_updatesAudioManager() {
        controller.initialize(null, null);
        clearInvocations(mockAudioManager);
        sfxToggle.fire();
        verify(mockAudioManager).setSfxEnabled(true);
        assertFalse(sfxVolumeSlider.isDisable());
    }

    @Test
    public void resetAudioSettings_resetsValues() {
        controller.initialize(null, null);
        clearInvocations(mockAudioManager);

        // Change some settings first
        musicVolumeSlider.setValue(10);
        sfxVolumeSlider.setValue(20);
        musicToggle.setSelected(false);
        sfxToggle.setSelected(false);

        controller.resetAudioSettings();

        assertEquals(50, musicVolumeSlider.getValue());
        assertEquals(70, sfxVolumeSlider.getValue());
        assertTrue(musicToggle.isSelected());
        assertTrue(sfxToggle.isSelected());
        verify(mockAudioManager, atLeastOnce()).setMusicVolume(0.5);
        verify(mockAudioManager, atLeastOnce()).setSfxVolume(0.7);
        verify(mockAudioManager, atLeastOnce()).setMusicEnabled(true);
        verify(mockAudioManager, atLeastOnce()).setSfxEnabled(true);
    }
}