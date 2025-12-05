package com.comp2042.ui;

import com.comp2042.JavaFxTestBase;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ControlsControllerTest extends JavaFxTestBase {

    private ControlsController controller;

    @Mock
    private ActionEvent mockActionEvent;
    @Mock
    private Node mockNode;
    @Mock
    private Scene mockScene;
    @Mock
    private Stage mockStage;
    @Mock
    private AudioManager mockAudioManager; // Mock AudioManager for this test

    @BeforeEach
    public void setUp() {
        controller = new ControlsController();

        // Clear any previous AudioManager instance
        AudioManager.resetInstance();
        // Inject the mock AudioManager instance for testing static calls
        // This makes AudioManager.getInstance() return mockAudioManager
        AudioManager.setInstanceForTest(mockAudioManager);

        // Mock the event source hierarchy for navigation
        when(mockActionEvent.getSource()).thenReturn(mockNode);
        when(mockNode.getScene()).thenReturn(mockScene);
        when(mockScene.getWindow()).thenReturn(mockStage);
    }

    @Test
    public void backToSettings_playsButtonClickSound() throws Exception {
        controller.backToSettings(mockActionEvent);

        verify(mockAudioManager, times(1)).playButtonClick();

        // Reset the instance after the test
        AudioManager.resetInstance();
    }
}
