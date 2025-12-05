package com.comp2042.ui;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;
import com.comp2042.JavaFxTestBase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // Suppress UnnecessaryStubbingException
class GameOverPanelTest extends JavaFxTestBase {

    private GameOverPanel gameOverPanel;

    @Mock
    private Label mockLabel;

    @Test
    void constructorInitializesCorrectly() {
        try (MockedConstruction<Label> mockedConstructionLabel = mockConstruction(Label.class,
                (mock, context) -> {
                    when(mock.getStyleClass()).thenReturn(mock(ObservableList.class));
                    // Check if the constructor argument for Label is "GAME OVER"
                    assertEquals("GAME OVER", context.arguments().get(0));
                })) {

            gameOverPanel = new GameOverPanel();

            // Verify a Label was constructed
            assertEquals(1, mockedConstructionLabel.constructed().size());
            Label constructedLabel = mockedConstructionLabel.constructed().get(0);

            // Verify the Label's style class was set
            verify(constructedLabel.getStyleClass()).add("gameOverStyle");

            // Verify the constructed label was set as the center of the BorderPane
            assertEquals(constructedLabel, gameOverPanel.getCenter());
        }
    }
}
