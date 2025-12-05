package com.comp2042.ui;

import com.comp2042.logic.board.SimpleBoard;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

// Explicit imports for classes in the same package, as a workaround
import com.comp2042.JavaFxTestBase;
import com.comp2042.ui.GuiController;
import com.comp2042.ui.MainMenu;
import com.comp2042.ui.AudioManager;
import com.comp2042.ui.GameController;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // Suppress UnnecessaryStubbingException
class MainMenuTest extends JavaFxTestBase {

    @Mock private TextField mockNameField;
    @Mock private Button mockResumeButton;
    @Mock private ActionEvent mockActionEvent;
    @Mock private Stage mockStage;
    @Mock private Parent mockRoot;
    @Mock private Scene mockGameScene;
    @Mock private GuiController mockGuiController;
    @Mock private URL mockUrl;
    @Mock private ResourceBundle mockResourceBundle;
    @Mock private AudioManager mockAudioManager; // Reinstated @Mock private AudioManager mockAudioManager;

    private MainMenu mainMenu;
    private MockedStatic<AudioManager> mockedStaticAudioManager;
    private MockedStatic<Platform> mockedStaticPlatform;
    private AutoCloseable openMocks; // For manual Mockito setup

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        openMocks = MockitoAnnotations.openMocks(this); // Initialize mocks manually

        // Mock static AudioManager.getInstance()
        mockedStaticAudioManager = mockStatic(AudioManager.class);
        mockedStaticAudioManager.when(AudioManager::getInstance).thenReturn(mockAudioManager);

        mockedStaticPlatform = mockStatic(Platform.class); // Mock Platform.exit()

        mainMenu = new MainMenu();
        // Inject @FXML mocks
        injectMocksIntoMainMenu(mainMenu);

        // Explicitly call initialize to set up audioManager in MainMenu
        mainMenu.initialize(mock(URL.class), mock(ResourceBundle.class));

        // Reset static activeGameScene
        Field activeGameSceneField = MainMenu.class.getDeclaredField("activeGameScene");
        activeGameSceneField.setAccessible(true);
        activeGameSceneField.set(null, null); // Set to null before each test
        
        when(mockActionEvent.getSource()).thenReturn(mock(javafx.scene.Parent.class));
        when(((javafx.scene.Node) mockActionEvent.getSource()).getScene()).thenReturn(mockGameScene);
        when(mockGameScene.getWindow()).thenReturn(mockStage);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockedStaticAudioManager.close();
        mockedStaticPlatform.close();
        openMocks.close(); // Close mocks manually
    }

    private void injectMocksIntoMainMenu(MainMenu menu) throws NoSuchFieldException, IllegalAccessException {
        Field nameField = MainMenu.class.getDeclaredField("nameField");
        nameField.setAccessible(true);
        nameField.set(menu, mockNameField);

        Field resumeButtonField = MainMenu.class.getDeclaredField("resumeButton");
        resumeButtonField.setAccessible(true);
        resumeButtonField.set(menu, mockResumeButton);
    }

    @Test
    void initializePlaysMenuMusicAndSetsResumeButtonVisibility() throws NoSuchFieldException, IllegalAccessException {
        // audioManager.playBackgroundMusic("menu") is already verified by @BeforeEach.
        // So, we only need to verify if the initial call was made.
        verify(mockAudioManager).playBackgroundMusic("menu");

        // Test case where activeGameScene is null (initial state from setUp)
        verify(mockResumeButton, never()).setVisible(true);
        verify(mockResumeButton, never()).setManaged(true);

        // Now, set activeGameScene and call initialize again to test that branch
        MainMenu.setActiveGameScene(mockGameScene);
        clearInvocations(mockAudioManager); // Clear previous invocations for a clean test of the second initialize call
        mainMenu.initialize(mock(URL.class), mock(ResourceBundle.class)); // Re-initialize to check resume button visibility
        verify(mockResumeButton).setVisible(true);
        verify(mockResumeButton).setManaged(true);
        verify(mockAudioManager, times(1)).playBackgroundMusic("menu"); // Expect one more call here
    }

    @Test
    void startGameInitializesGameAndNavigates() throws Exception {
        when(mockNameField.getText()).thenReturn("TestPlayer");

        try (MockedConstruction<FXMLLoader> mockedConstructionFXMLLoader = mockConstruction(FXMLLoader.class,
                     (mock, context) -> {
                         when(mock.load()).thenReturn(mockRoot);
                         when(mock.getController()).thenReturn(mockGuiController);
                     });
             MockedConstruction<Scene> mockedConstructionScene = mockConstruction(Scene.class);
             MockedConstruction<GameController> mockedConstructionGameController = mockConstruction(GameController.class);
             MockedConstruction<SimpleBoard> mockedConstructionSimpleBoard = mockConstruction(SimpleBoard.class)
        ) {
            mainMenu.startGame(mockActionEvent);

            verify(mockAudioManager, times(1)).playButtonClick();
            verify(mockNameField).getText();
            verify(mockGuiController).setPlayerName("TestPlayer");
            verify(mockStage).setScene(any(Scene.class));
            verify(mockStage).show();
            verify(mockAudioManager).playBackgroundMusic("game");

            assertEquals(1, mockedConstructionFXMLLoader.constructed().size());
            assertEquals(1, mockedConstructionScene.constructed().size());
            assertEquals(1, mockedConstructionGameController.constructed().size());
            assertEquals(1, mockedConstructionSimpleBoard.constructed().size());
        }
    }

    @Test
    void resumeGameResumesActiveGame() {
        MainMenu.setActiveGameScene(mockGameScene);
        when(mockGameScene.getRoot()).thenReturn(mockRoot);
        when(mockRoot.getUserData()).thenReturn(mockGuiController);

        mainMenu.resumeGame(mockActionEvent);

        verify(mockAudioManager).playButtonClick();
        verify(mockStage).setScene(mockGameScene);
        verify(mockStage).show();
        verify(mockAudioManager).playBackgroundMusic("game");
        verify(mockGuiController).resumeFromMenu();
    }

    @Test
    void showLeaderboardLoadsLeaderboardScene() throws IOException {
        try (MockedConstruction<FXMLLoader> mockedConstructionFXMLLoader = mockConstruction(FXMLLoader.class,
                     (mock, context) -> {
                         when(mock.load()).thenReturn(mockRoot);
                     });
             MockedConstruction<Scene> mockedConstructionScene = mockConstruction(Scene.class)
        ) {
            mainMenu.showLeaderboard(mockActionEvent);

            verify(mockAudioManager).playButtonClick();
            assertEquals(1, mockedConstructionFXMLLoader.constructed().size());
            assertEquals(1, mockedConstructionScene.constructed().size());
            verify(mockStage).setScene(any(Scene.class));
            verify(mockStage).show();
        }
    }

    @Test
    void showSettingsLoadsSettingsScene() throws IOException {
        try (MockedConstruction<FXMLLoader> mockedConstructionFXMLLoader = mockConstruction(FXMLLoader.class,
                     (mock, context) -> {
                         when(mock.load()).thenReturn(mockRoot);
                     });
             MockedConstruction<Scene> mockedConstructionScene = mockConstruction(Scene.class)
        ) {
            mainMenu.showSettings(mockActionEvent);

            verify(mockAudioManager).playButtonClick();
            assertEquals(1, mockedConstructionFXMLLoader.constructed().size());
            assertEquals(1, mockedConstructionScene.constructed().size());
            verify(mockStage).setScene(any(Scene.class));
            verify(mockStage).show();
        }
    }

    @Test
    void exitGameDisposesAudioManagerAndExitsPlatform() {
        mainMenu.exitGame(mockActionEvent);

        verify(mockAudioManager).playButtonClick();
        verify(mockAudioManager).dispose();
        mockedStaticPlatform.verify(Platform::exit);
    }

    @Test
    void clearActiveGameSetsActiveGameSceneToNull() throws NoSuchFieldException, IllegalAccessException {
        Field activeGameSceneField = MainMenu.class.getDeclaredField("activeGameScene");
        activeGameSceneField.setAccessible(true);
        activeGameSceneField.set(null, mockGameScene);

        MainMenu.clearActiveGame();

        assertNull(activeGameSceneField.get(null));
    }

    @Test
    void setActiveGameSceneSetsScene() throws NoSuchFieldException, IllegalAccessException {
        Field activeGameSceneField = MainMenu.class.getDeclaredField("activeGameScene");
        activeGameSceneField.setAccessible(true);

        MainMenu.setActiveGameScene(mockGameScene);

        assertEquals(mockGameScene, activeGameSceneField.get(null));
    }

    @Test
    void returnToMainMenuNavigatesAndPlaysMenuMusic() throws IOException {
        try (MockedConstruction<FXMLLoader> mockedConstructionFXMLLoader = mockConstruction(FXMLLoader.class,
                     (mock, context) -> {
                         when(mock.load()).thenReturn(mockRoot);
                     });
             MockedConstruction<Scene> mockedConstructionScene = mockConstruction(Scene.class)
        ) {
            clearInvocations(mockAudioManager); // Clear invocations from setUp()

            MainMenu.returnToMainMenu(mockStage);

            verify(mockAudioManager).playButtonClick();
            verify(mockStage).setScene(any(Scene.class));
            verify(mockStage).show();
            verify(mockAudioManager).playBackgroundMusic("menu");
            assertEquals(1, mockedConstructionFXMLLoader.constructed().size());
            assertEquals(1, mockedConstructionScene.constructed().size());
        }
    }
}