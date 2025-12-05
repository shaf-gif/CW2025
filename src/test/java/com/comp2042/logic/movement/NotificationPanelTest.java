package com.comp2042.logic.movement;

import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.comp2042.JavaFxTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NotificationPanelTest extends JavaFxTestBase {

    @Test
    void testConstructorSetsTextAndStyle() {
        String testText = "Test Score";
        NotificationPanel panel = new NotificationPanel(testText);

        assertNotNull(panel.getCenter(), "Center node should not be null");
        assert(panel.getCenter() instanceof Label);

        Label scoreLabel = (Label) panel.getCenter();
        assertEquals(testText, scoreLabel.getText(), "Label text should match constructor input");
        assert(scoreLabel.getStyleClass().contains("bonusStyle"));
        assertNotNull(scoreLabel.getEffect(), "Label should have an effect (Glow)");
        assertEquals(Color.WHITE, scoreLabel.getTextFill(), "Label text color should be WHITE");
    }

    @Test
    void testShowScoreDoesNotThrowException() {
        String testText = "1000";
        NotificationPanel panel = new NotificationPanel(testText);
        ObservableList<Node> mockList = Mockito.spy(FXCollections.observableArrayList());

        try {
            panel.showScore(mockList);
            // This test primarily checks that no exceptions are thrown
            // Further assertions would require a more complex JavaFX testing setup or checking internal state
            // For example, verifying that transitions are playing or that the item is eventually removed from the list
            // However, that goes beyond simple unit testing for this component's public API without a UI thread.
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail("showScore method threw an exception: " + e.getMessage());
        }
    }
}