package com.comp2042.ui;

import com.comp2042.logic.Constants;
import com.comp2042.logic.movement.DownData;
import com.comp2042.logic.movement.InputEventListener;
import com.comp2042.logic.movement.MoveEvent;
import com.comp2042.logic.scoring.LeaderboardManager;
import com.comp2042.logic.scoring.Score;
import com.comp2042.model.ViewData;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;
import com.comp2042.JavaFxTestBase;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // Suppress UnnecessaryStubbingException
class GuiControllerTest extends JavaFxTestBase {

    // @FXML fields
    @Mock private Pane mockGameContainer;
    @Mock private Button mockShadowButton;
    @Mock private BorderPane mockGameBoard;
    @Mock private GridPane mockGamePanel;
    @Mock private Group mockGroupNotification;
    @Mock private GridPane mockBrickPanel;
    @Mock private GameOverPanel mockGameOverPanel;
    @Mock private Label mockScoreLabel;
    @Mock private Label mockLevelLabel;
    @Mock private Button mockPauseButton;
    @Mock private GridPane mockNextPanel1;
    @Mock private GridPane mockNextPanel2;
    @Mock private GridPane mockNextPanel3;
    @Mock private GridPane mockHoldPanel;
    @Mock private Rectangle mockOverlay;

    // Other dependencies
    @Mock private InputEventListener mockInputEventListener;
    @Mock private BrickViewManager mockBrickViewManager;
    @Mock private PreviewPanelManager mockPreviewPanelManager;
    @Mock private Score mockScore;
    @Mock private ReadOnlyIntegerProperty mockScoreProperty;
    @Mock private ReadOnlyIntegerProperty mockLevelProperty;
    @Mock private KeyEvent mockKeyEvent;
    @Mock private ViewData mockViewData;

    @Captor
    private ArgumentCaptor<EventHandler<KeyEvent>> keyEventHandlerCaptor;

    private GuiController guiController;

    @BeforeEach
    void setUp() throws IOException {
        // Mock default behavior for common JavaFX components
        when(mockGamePanel.getStyleClass()).thenReturn(mock(ObservableList.class));
        when(mockGamePanel.getChildren()).thenReturn(mock(ObservableList.class));
        when(mockGamePanel.focusedProperty()).thenReturn(mock(BooleanProperty.class)); // Added for requestFocus()
        
        when(mockScoreLabel.textProperty()).thenReturn(mock(javafx.beans.property.StringProperty.class));
        when(mockLevelLabel.textProperty()).thenReturn(mock(javafx.beans.property.StringProperty.class));

        // Stub mockOverlay's properties
        when(mockOverlay.layoutXProperty()).thenReturn(mock(javafx.beans.property.DoubleProperty.class));
        when(mockOverlay.layoutYProperty()).thenReturn(mock(javafx.beans.property.DoubleProperty.class));
        when(mockOverlay.widthProperty()).thenReturn(mock(javafx.beans.property.DoubleProperty.class));
        when(mockOverlay.heightProperty()).thenReturn(mock(javafx.beans.property.DoubleProperty.class));

        // Stub mockGameBoard's properties for binding
        when(mockGameBoard.layoutXProperty()).thenReturn(mock(DoubleProperty.class));
        when(mockGameBoard.layoutYProperty()).thenReturn(mock(DoubleProperty.class));
        when(mockGameBoard.widthProperty()).thenReturn(mock(DoubleProperty.class));
        when(mockGameBoard.heightProperty()).thenReturn(mock(DoubleProperty.class));

        // Stub mockGameContainer's properties for listeners
        when(mockGameContainer.widthProperty()).thenReturn(mock(javafx.beans.property.ReadOnlyDoubleProperty.class));
        when(mockGameContainer.heightProperty()).thenReturn(mock(javafx.beans.property.ReadOnlyDoubleProperty.class));

        // Inject mocks into GuiController (simulating FXML loading)
        guiController = new GuiController();
        injectMocksIntoGuiController(guiController); // Helper to set @FXML fields

        // General mock setup for things called in initialize
        when(mockScore.scoreProperty()).thenReturn(mockScoreProperty);
        when(mockScore.levelProperty()).thenReturn(mockLevelProperty);
        
        // Mock getParent for brickPanel
        when(mockBrickPanel.getParent()).thenReturn(mock(Pane.class));
    }

    // Helper method to inject @Mock fields into the GuiController, simulating FXML loader behavior
    private void injectMocksIntoGuiController(GuiController controller) {
        try {
            java.lang.reflect.Field field;

            field = GuiController.class.getDeclaredField("gameContainer");
            field.setAccessible(true);
            field.set(controller, mockGameContainer);
            
            field = GuiController.class.getDeclaredField("shadowButton");
            field.setAccessible(true);
            field.set(controller, mockShadowButton);

            field = GuiController.class.getDeclaredField("gameBoard");
            field.setAccessible(true);
            field.set(controller, mockGameBoard);

            field = GuiController.class.getDeclaredField("gamePanel");
            field.setAccessible(true);
            field.set(controller, mockGamePanel);

            field = GuiController.class.getDeclaredField("groupNotification");
            field.setAccessible(true);
            field.set(controller, mockGroupNotification);

            field = GuiController.class.getDeclaredField("brickPanel");
            field.setAccessible(true);
            field.set(controller, mockBrickPanel);

            field = GuiController.class.getDeclaredField("gameOverPanel");
            field.setAccessible(true);
            field.set(controller, mockGameOverPanel);

            field = GuiController.class.getDeclaredField("scoreLabel");
            field.setAccessible(true);
            field.set(controller, mockScoreLabel);

            field = GuiController.class.getDeclaredField("levelLabel");
            field.setAccessible(true);
            field.set(controller, mockLevelLabel);

            field = GuiController.class.getDeclaredField("pauseButton");
            field.setAccessible(true);
            field.set(controller, mockPauseButton);

            field = GuiController.class.getDeclaredField("nextPanel1");
            field.setAccessible(true);
            field.set(controller, mockNextPanel1);

            field = GuiController.class.getDeclaredField("nextPanel2");
            field.setAccessible(true);
            field.set(controller, mockNextPanel2);

            field = GuiController.class.getDeclaredField("nextPanel3");
            field.setAccessible(true);
            field.set(controller, mockNextPanel3);

            field = GuiController.class.getDeclaredField("holdPanel");
            field.setAccessible(true);
            field.set(controller, mockHoldPanel);

            field = GuiController.class.getDeclaredField("overlay");
            field.setAccessible(true);
            field.set(controller, mockOverlay);

            field = GuiController.class.getDeclaredField("brickViewManager");
            field.setAccessible(true);
            field.set(controller, mockBrickViewManager);

            field = GuiController.class.getDeclaredField("previewPanelManager");
            field.setAccessible(true);
            field.set(controller, mockPreviewPanelManager);


        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to inject mocks into GuiController: " + e.getMessage());
        }
    }

    @Test
    void initializeSetsUpUIAndManagers() throws IOException {
        try (MockedStatic<Font> mockedStaticFont = mockStatic(Font.class);
             MockedConstruction<BrickViewManager> mockedConstructionBVM = mockConstruction(BrickViewManager.class,
                     (mock, context) -> when(mock.isShadowEnabled()).thenReturn(true)); // Mock BrickViewManager
             MockedConstruction<PreviewPanelManager> mockedConstructionPPM = mockConstruction(PreviewPanelManager.class);
             MockedConstruction<Reflection> mockedConstructionReflection = mockConstruction(Reflection.class)
        ) {
            // Mock Font.loadFont to prevent actual font loading
            mockedStaticFont.when(() -> Font.loadFont(anyString(), anyDouble())).thenReturn(mock(Font.class));

            guiController.initialize(mock(URL.class), mock(java.util.ResourceBundle.class));

            // Verify Font.loadFont is called
            mockedStaticFont.verify(() -> Font.loadFont(anyString(), eq(38.0)), times(1));

            // Verify gamePanel setup
            verify(mockGamePanel).setFocusTraversable(true);
            verify(mockGamePanel).requestFocus();
            verify(mockGamePanel).setOnKeyPressed(any(EventHandler.class));

            // Verify BrickViewManager and PreviewPanelManager instantiation
            assertEquals(1, mockedConstructionBVM.constructed().size());
            assertEquals(1, mockedConstructionPPM.constructed().size());
            
            // Verify gameOverPanel and overlay setup
            verify(mockGameOverPanel).setVisible(false);
            verify(mockOverlay).setManaged(false);
            verify(mockOverlay).setVisible(false);
            verify(mockOverlay).setOpacity(Constants.OVERLAY_OPACITY);
            verify(mockOverlay).setMouseTransparent(true);
            
            // Verify Reflection is instantiated
            assertEquals(1, mockedConstructionReflection.constructed().size());

            // Further verifications for property listeners can be added if needed, but they are hard to verify directly
            // without custom mocks for Property objects.
        }
    }
}
