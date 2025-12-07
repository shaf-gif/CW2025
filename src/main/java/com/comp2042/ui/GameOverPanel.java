package com.comp2042.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;


/**
 * A simple JavaFX BorderPane that displays a "GAME OVER" message.
 */
public class GameOverPanel extends BorderPane {

    /**
     * Constructs a new GameOverPanel, initializing it with a styled "GAME OVER" label centered.
     */
    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        setCenter(gameOverLabel);
    }

}

