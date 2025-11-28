package com.comp2042.ui;

import com.comp2042.InputEventListener;
import com.comp2042.logic.Constants;
import com.comp2042.logic.movement.*;
import com.comp2042.model.ViewData;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import com.comp2042.logic.scoring.Score; // Import the Score class
import com.comp2042.logic.scoring.LeaderboardManager; // Import the LeaderboardManager

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    // --- NEW: Player & Score Fields ---
    private String playerName = "Player";
    private Score scoreTracker; // Store reference to score
    // ------------------------------------

    // --- View Components (FXML) ---
    @FXML private javafx.scene.control.Button shadowButton;
    @FXML private BorderPane gameBoard;
    @FXML private GridPane gamePanel; // Background grid
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel; // Active piece layer
    @FXML private GameOverPanel gameOverPanel;
    @FXML private javafx.scene.control.Label scoreLabel;
    @FXML private javafx.scene.control.Label levelLabel; // NEW: Level display
    @FXML private javafx.scene.control.Button pauseButton;
    // Next previews
    @FXML private GridPane nextPanel1;
    @FXML private GridPane nextPanel2;
    @FXML private GridPane nextPanel3;
    // Hold preview
    @FXML private GridPane holdPanel;
    @FXML private Rectangle overlay;

    // --- Internal State & Logic ---
    private Rectangle[][] displayMatrix; // Background tiles
    private InputEventListener eventListener;
    private Timeline timeline;

    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    // --- Refactored Managers ---
    private BrickViewManager brickViewManager;
    private PreviewPanelManager previewPanelManager;

    // --- NEW: Setter Methods ---
    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public void setScoreTracker(Score score) {
        this.scoreTracker = score;
    }
    // ---------------------------

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load font
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        // Input setup
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPress);

        // Initialize Managers
        // Pass brickPanel's parent to the BrickViewManager for correct GhostPanel insertion
        Pane brickPanelContainer = brickPanel.getParent() instanceof Pane ? (Pane) brickPanel.getParent() : null;
        brickViewManager = new BrickViewManager(brickPanel, gamePanel, shadowButton, brickPanelContainer);
        previewPanelManager = new PreviewPanelManager(nextPanel1, nextPanel2, nextPanel3, holdPanel);

        // Visual initial states setup
        gameOverPanel.setVisible(false);
        setupOverlay();

        // Enable board grid if requested
        if (Constants.SHOW_GRID && gamePanel != null) {
            gamePanel.setGridLinesVisible(true);
        }

        // Reserved: optional reflection effect for future polish
        Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    private void setupOverlay() {
        if (overlay != null) {
            overlay.setManaged(false);
            overlay.setVisible(false);
            overlay.setOpacity(Constants.OVERLAY_OPACITY);
            overlay.setMouseTransparent(true);
            // Bind overlay to the game board's bounds
            if (gameBoard != null) {
                overlay.layoutXProperty().bind(gameBoard.layoutXProperty());
                overlay.layoutYProperty().bind(gameBoard.layoutYProperty());
                overlay.widthProperty().bind(gameBoard.widthProperty());
                overlay.heightProperty().bind(gameBoard.heightProperty());
            }
            overlay.setArcWidth(Constants.OVERLAY_CORNER_RADIUS);
            overlay.setArcHeight(Constants.OVERLAY_CORNER_RADIUS);
        }
    }

    private void handleKeyPress(KeyEvent keyEvent) {

        if (!isPause.get() && !isGameOver.get()) {

            switch (keyEvent.getCode()) {
                // Delegate to handlers, which refresh the view via managers
                case LEFT, A -> handleMove(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                case RIGHT, D -> handleMove(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                case UP, W -> handleMove(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                case DOWN, S -> moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                case SPACE -> hardDrop(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                case C -> handleMove(eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER)));
                case H -> brickViewManager.toggleShadow(null); // Delegate to manager
            }
        }

        if (keyEvent.getCode() == KeyCode.N) newGame(null);

        keyEvent.consume();

        // Use pauseGame method for consistent logic
        if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
            pauseGame(null);
        }
    }

    /**
     * Helper to refresh view after a non-down/hard-drop event (Left, Right, Rotate, Hold).
     */
    private void handleMove(ViewData brick) {
        if (isPause.get()) return;
        brickViewManager.refreshBrick(brick);
        previewPanelManager.renderAllPreviews(brick);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {

        // Draw background grid (skip hidden rows)
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        for (int row = Constants.HIDDEN_ROWS; row < boardMatrix.length; row++) {
            for (int col = 0; col < boardMatrix[row].length; col++) {

                Rectangle rect = new Rectangle(Constants.TILE_SIZE, Constants.TILE_SIZE);
                rect.setArcHeight(Constants.TILE_ROUNDING);
                rect.setArcWidth(Constants.TILE_ROUNDING);
                TileStyleUtility.applyTileStyle(rect, 0); // Use the shared utility

                displayMatrix[row][col] = rect;
                gamePanel.add(rect, col, row - Constants.HIDDEN_ROWS);
            }
        }

        // Delegate drawing of active and ghost bricks
        brickViewManager.initDrawBricks(brick);

        // Delegate rendering of previews
        previewPanelManager.renderAllPreviews(brick);

        // Start game loop
        timeline = new Timeline(new KeyFrame(
                Duration.millis(Constants.FALL_DELAY_MS),
                e -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void refreshGameBackground(int[][] board) {
        for (int r = Constants.HIDDEN_ROWS; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                TileStyleUtility.applyTileStyle(displayMatrix[r][c], board[r][c]);
            }
        }
    }

    private void moveDown(MoveEvent e) {
        if (isPause.get()) return;
        DownData data = eventListener.onDownEvent(e);
        handlePostDropUpdates(data);
    }

    private void hardDrop(MoveEvent e) {
        if (isPause.get()) return;
        DownData data = eventListener.onHardDropEvent(e);
        handlePostDropUpdates(data);
    }

    /**
     * Handles UI updates common to both soft and hard drops (score, notifications, piece/preview refresh).
     */
    private void handlePostDropUpdates(DownData data) {
        if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel np = new NotificationPanel("+" + data.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(np);
            np.showScore(groupNotification.getChildren());
        }

        // Delegate brick and preview updates
        ViewData brick = data.getViewData();
        brickViewManager.refreshBrick(brick);
        previewPanelManager.renderAllPreviews(brick);

        gamePanel.requestFocus();
    }

    // --- Game State Management & UI Effects ---

    public void bindScore(IntegerProperty scoreProp) {
        scoreLabel.textProperty().bind(Bindings.format("Score: %d", scoreProp));
    }

    /**
     * NEW: Bind the level property to the level label
     */
    public void bindLevel(IntegerProperty levelProp) {
        levelLabel.textProperty().bind(Bindings.format("Level: %d", levelProp));
    }

    /**
     * NEW: Update game speed based on current level
     * Speed formula: baseSpeed × (0.75 ^ (level - 1))
     * Each level increases speed by 25%, with minimum of 50ms
     */
    public void updateGameSpeed(int level) {
        if (timeline != null) {
            timeline.stop();

            // Calculate new speed based on level
            int baseSpeed = Constants.FALL_DELAY_MS;
            // Changed from 0.85 to 0.75 for much faster progression (25% speed increase per level)
            double speedMultiplier = Math.pow(0.75, level - 1);
            int newSpeed = Math.max(50, (int)(baseSpeed * speedMultiplier));

            // Create new timeline with updated speed
            timeline = new Timeline(new KeyFrame(
                    Duration.millis(newSpeed),
                    e -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
            ));
            timeline.setCycleCount(Timeline.INDEFINITE);

            // Only play if game is not paused or over
            if (!isPause.get() && !isGameOver.get()) {
                timeline.play();
            }
        }
    }

    // --- REPLACED gameOver() METHOD ---
    public void gameOver() {
        timeline.stop();
        isGameOver.set(true);

        // Save score to leaderboard
        if (scoreTracker != null) {
            LeaderboardManager.saveScore(
                    playerName,
                    scoreTracker.scoreProperty().get(),
                    scoreTracker.getLevel(),
                    scoreTracker.getRowsCleared()
            );
        }

        // Clear active game when game is over
        MainMenu.clearActiveGame();

        // Prepare panel for animation
        gameOverPanel.setOpacity(0);
        gameOverPanel.setScaleX(0.9);
        gameOverPanel.setScaleY(0.9);
        gameOverPanel.setVisible(true);

        // Fade-in overlay
        if (overlay != null) {
            overlay.setVisible(true);
            FadeTransition overlayFade = new FadeTransition(Duration.millis(Constants.ANIM_DURATION_MS), overlay);
            overlayFade.setFromValue(0.0);
            overlayFade.setToValue(Constants.OVERLAY_OPACITY);
            overlayFade.play();
        }

        // Fade + scale the GameOver panel
        FadeTransition fade = new FadeTransition(Duration.millis(Constants.ANIM_DURATION_MS), gameOverPanel);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        ScaleTransition scale = new ScaleTransition(Duration.millis(Constants.ANIM_DURATION_MS), gameOverPanel);
        scale.setFromX(0.9);
        scale.setFromY(0.9);
        scale.setToX(1.0);
        scale.setToY(1.0);
        ParallelTransition pt = new ParallelTransition(fade, scale);
        pt.play();
    }
    // ---------------------------------

    public void newGame(ActionEvent evt) {
        timeline.stop();

        // Clear active game when starting new game
        MainMenu.clearActiveGame();

        // Hide overlay and panel with quick fade
        if (overlay != null && overlay.isVisible()) {
            FadeTransition overlayFadeOut = new FadeTransition(Duration.millis(200), overlay);
            overlayFadeOut.setFromValue(overlay.getOpacity());
            overlayFadeOut.setToValue(0.0);
            overlayFadeOut.setOnFinished(e -> overlay.setVisible(false));
            overlayFadeOut.play();
        }
        gameOverPanel.setVisible(false);
        gameOverPanel.setOpacity(1.0);
        gameOverPanel.setScaleX(1.0);
        gameOverPanel.setScaleY(1.0);

        eventListener.createNewGame();
        isPause.set(false);
        isGameOver.set(false);
        pauseButton.setText("Pause");

        // Speed will be reset via updateGameSpeed(1) call in GameController
        timeline.play();
        gamePanel.requestFocus();
    }

    public void goMainMenu(ActionEvent evt) throws IOException {
        timeline.stop();

        // Store current game scene before switching
        Stage primaryStage = (Stage) ((javafx.scene.Node) evt.getSource()).getScene().getWindow();
        MainMenu.setActiveGameScene(primaryStage.getScene());

        URL location = getClass().getClassLoader().getResource("menuLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 510);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void pauseGame(ActionEvent evt) {

        if (isGameOver.get()) return;

        if (isPause.get()) {
            timeline.play();
            pauseButton.setText("Pause");
            isPause.set(false);
        } else {
            timeline.pause();
            pauseButton.setText("Resume");
            isPause.set(true);
        }

        gamePanel.requestFocus();
    }

    // Toggle Shadow (ghost) projection via button or 'H' key
    // Delegates directly to the manager, but remains here for FXML binding.
    public void toggleShadow(ActionEvent evt) {
        brickViewManager.toggleShadow(evt);
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener listener) {
        this.eventListener = listener;
    }
}