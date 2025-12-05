package com.comp2042.ui;

import com.comp2042.JavaFxTestBase;
import org.mockito.Mock;
import com.comp2042.logic.scoring.LeaderboardManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LeaderboardControllerTest extends JavaFxTestBase {

    private LeaderboardController controller;
    private VBox leaderboardContainer;

    @Mock
    private AudioManager mockAudioManager;

    @TempDir
    Path tempDir;
    private String testLeaderboardFile;

    @BeforeEach
    public void setUp() throws Exception {
        AudioManager.setInstanceForTest(mockAudioManager);
        testLeaderboardFile = tempDir.resolve("test_leaderboard.dat").toString();
        LeaderboardManager.setLeaderboardFileForTesting(testLeaderboardFile);
        LeaderboardManager.clearLeaderboard(); // Ensure a clean slate
        controller = new LeaderboardController();
        leaderboardContainer = new VBox(); // Create a mock VBox
        // Add a dummy header to simulate FXML structure
        leaderboardContainer.getChildren().add(new Label("Header Row"));

        // Use reflection to inject the FXML field
        setField(controller, "leaderboardContainer", leaderboardContainer);

        // Add some test data
        LeaderboardManager.saveScore("PlayerA", 100, 2, 5);
        LeaderboardManager.saveScore("PlayerB", 200, 3, 10);
        LeaderboardManager.saveScore("PlayerC", 50, 1, 2);
    }

    @AfterEach
    public void tearDown() {
        LeaderboardManager.clearLeaderboard(); // Clean up after each test
        AudioManager.resetInstance();
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private void invokePrivateMethod(Object target, String methodName, Object... args) throws Exception {
        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
            // Handle primitive types if necessary, though ActionEvent is an object
            if (argTypes[i] == ActionEvent.class) { // specific handling for ActionEvent to avoid issues with subclasses
                argTypes[i] = ActionEvent.class;
            }
        }
        java.lang.reflect.Method method = target.getClass().getDeclaredMethod(methodName, argTypes);
        method.setAccessible(true);
        method.invoke(target, args);
    }

    @Test
    public void initialize_displaysLeaderboardCorrectly() throws InterruptedException {
        // Platform.runLater is needed because initialize calls JavaFX components which must be on FX thread.
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            controller.initialize(null, null);
            latch.countDown();
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));

        // Expect header + 3 entries
        assertEquals(4, leaderboardContainer.getChildren().size());

        // Verify content of first entry (PlayerB, as scores are sorted desc)
        HBox row1 = (HBox) leaderboardContainer.getChildren().get(1);
        assertEquals("1", ((Label) row1.getChildren().get(0)).getText()); // Rank
        assertEquals("PlayerB", ((Label) row1.getChildren().get(1)).getText()); // Name
        assertEquals("200", ((Label) row1.getChildren().get(2)).getText()); // Score

        // Verify content of second entry (PlayerA)
        HBox row2 = (HBox) leaderboardContainer.getChildren().get(2);
        assertEquals("2", ((Label) row2.getChildren().get(0)).getText());
        assertEquals("PlayerA", ((Label) row2.getChildren().get(1)).getText());
        assertEquals("100", ((Label) row2.getChildren().get(2)).getText());

        // Verify content of third entry (PlayerC)
        HBox row3 = (HBox) leaderboardContainer.getChildren().get(3);
        assertEquals("3", ((Label) row3.getChildren().get(0)).getText());
        assertEquals("PlayerC", ((Label) row3.getChildren().get(1)).getText());
        assertEquals("50", ((Label) row3.getChildren().get(2)).getText());
    }

    @Test
    public void clearLeaderboard_clearsScores() throws Exception { // Changed to throws Exception for reflection
        // Initialize first to display scores
        CountDownLatch latchInit = new CountDownLatch(1);
        Platform.runLater(() -> {
            controller.initialize(null, null);
            latchInit.countDown();
        });
        assertTrue(latchInit.await(5, TimeUnit.SECONDS));

        // Verify scores are initially present
        assertEquals(4, leaderboardContainer.getChildren().size());
        assertEquals(3, LeaderboardManager.loadLeaderboard().size());

        // Clear leaderboard using reflection
        CountDownLatch latchClear = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                invokePrivateMethod(controller, "clearLeaderboard", new ActionEvent());
            } catch (Exception e) {
                e.printStackTrace();
                // Fail the test if reflection fails
                throw new RuntimeException(e);
            }
            latchClear.countDown();
        });
        assertTrue(latchClear.await(5, TimeUnit.SECONDS));

        // Expect only the header row
        assertEquals(1, leaderboardContainer.getChildren().size());
        assertTrue(LeaderboardManager.loadLeaderboard().isEmpty());
    }
}
