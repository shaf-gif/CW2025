package com.comp2042.ui;

// Import the specific inner class defined in your LeaderboardManager
import com.comp2042.logic.scoring.LeaderboardManager.LeaderboardEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LeaderboardController implements Initializable {

    // --- FXML Components ---
    // This VBox will hold the dynamically generated HBox score rows
    @FXML private VBox leaderboardContainer;
    // -----------------------

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load and display the scores when the view initializes
        displayLeaderboard();
    }

    private void displayLeaderboard() {
        // Clear existing dynamic entries, keeping the FXML-defined header (first child).
        if (leaderboardContainer.getChildren().size() > 1) {
            leaderboardContainer.getChildren().remove(1, leaderboardContainer.getChildren().size());
        }

        List<LeaderboardEntry> scores = com.comp2042.logic.scoring.LeaderboardManager.loadLeaderboard();

        int rank = 1;
        for (LeaderboardEntry entry : scores) {
            leaderboardContainer.getChildren().add(createScoreRow(rank++, entry));
        }
    }

    private HBox createScoreRow(int rank, LeaderboardEntry entry) {
        HBox row = new HBox(10); // Spacing matches the header
        row.getStyleClass().add("leaderboard-entry");

        Label rankLabel = new Label(String.valueOf(rank));
        rankLabel.setPrefWidth(50);
        rankLabel.getStyleClass().add("nextBrickLabel");

        Label nameLabel = new Label(entry.getPlayerName());
        nameLabel.setPrefWidth(150);
        nameLabel.getStyleClass().add("nextBrickLabel");

        Label scoreLabel = new Label(String.valueOf(entry.getScore()));
        scoreLabel.setPrefWidth(80);
        scoreLabel.getStyleClass().add("nextBrickLabel");

        Label levelLabel = new Label(String.valueOf(entry.getLevel()));
        levelLabel.setPrefWidth(60);
        levelLabel.getStyleClass().add("nextBrickLabel");

        Label rowsLabel = new Label(String.valueOf(entry.getRowsCleared()));
        rowsLabel.setPrefWidth(60);
        rowsLabel.getStyleClass().add("nextBrickLabel");

        row.getChildren().addAll(rankLabel, nameLabel, scoreLabel, levelLabel, rowsLabel);
        return row;
    }

    @FXML
    private void clearLeaderboard(ActionEvent event) {
        // Play button click sound
        com.comp2042.logic.AudioManager.getInstance().playButtonClick();
        // Call the static method in your manager
        com.comp2042.logic.scoring.LeaderboardManager.clearLeaderboard();

        // Refresh the displayed list immediately
        displayLeaderboard();
    }

    @FXML
    private void backToMenu(ActionEvent event) throws IOException {
        // Get the current stage from the button
        Stage primaryStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        // Assuming MainMenu is the class with the static returnToMainMenu method
        MainMenu.returnToMainMenu(primaryStage);
    }
}