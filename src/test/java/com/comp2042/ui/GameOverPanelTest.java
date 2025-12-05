package com.comp2042.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.comp2042.JavaFxTestBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // Suppress UnnecessaryStubbingException
class GameOverPanelTest extends JavaFxTestBase {

    private GameOverPanel gameOverPanel;

    @Test
    void constructorInitializesCorrectly() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            gameOverPanel = new GameOverPanel();
            // To properly initialize the panel, it needs to be in a scene and a stage
            Stage stage = new Stage();
            Scene scene = new Scene(gameOverPanel, 200, 200);
            stage.setScene(scene);
            // Do not call stage.show() to prevent the window from appearing during tests.
            // The component is sufficiently initialized for testing by being in a scene.

            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));

        assertNotNull(gameOverPanel);
        assertTrue(gameOverPanel.getCenter() instanceof Label);
        Label label = (Label) gameOverPanel.getCenter();
        assertEquals("GAME OVER", label.getText());
        assertTrue(label.getStyleClass().contains("gameOverStyle"));
    }
}
