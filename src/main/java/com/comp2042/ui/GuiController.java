package com.comp2042.ui;

import com.comp2042.logic.movement.InputEventListener;
import com.comp2042.logic.Constants;
import com.comp2042.logic.movement.*;
import com.comp2042.model.ViewData;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import com.comp2042.logic.scoring.Score;
import com.comp2042.logic.scoring.LeaderboardManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the main game GUI, handling user input, game display, and interactions.
 */
public class GuiController implements Initializable {

    /**
     * Constructs a new GuiController.
     * This class manages the graphical user interface and interactions for the game.
     */
    public GuiController() {
        // Default constructor
    }

    /** The name of the current player, used for leaderboard entries. */
    private String playerName = "Player";
    /** The Score object used to track the player's score, level, and rows cleared. */
    private Score scoreTracker;

    /**
     * The main container for the game, managed by FXML.
     */
    @FXML private Pane gameContainer;
    /** The button for toggling the shadow/ghost brick, managed by FXML. */
    @FXML private javafx.scene.control.Button shadowButton;
    /**
     * The BorderPane layout for the game board, managed by FXML.
     */
    @FXML private BorderPane gameBoard;
    /**
     * The GridPane representing the main game area where bricks fall, managed by FXML.
     */
    @FXML private GridPane gamePanel;
    /**
     * Group for displaying notifications, managed by FXML.
     */
    @FXML private Group groupNotification;
    /** The GridPane where individual brick tiles are dynamically displayed, managed by FXML. */
    @FXML private GridPane brickPanel;
    /** The panel displayed when the game is over, managed by FXML. */
    @FXML private GameOverPanel gameOverPanel;
    /**
     * Label to display the current score, managed by FXML.
     */
    @FXML private javafx.scene.control.Label scoreLabel;
    /**
     * Label to display the current game level, managed by FXML.
     */
    @FXML private javafx.scene.control.Label levelLabel;
    /**
     * Button to pause/resume the game, managed by FXML.
     */
    @FXML private javafx.scene.control.Button pauseButton;
    /** The GridPane for displaying the first next incoming brick preview, managed by FXML. */
    @FXML private GridPane nextPanel1;
    /** The GridPane for displaying the second next incoming brick preview, managed by FXML. */
    @FXML private GridPane nextPanel2;
    /** The GridPane for displaying the third next incoming brick preview, managed by FXML. */
    @FXML private GridPane nextPanel3;
    /** The GridPane for displaying the held brick preview, managed by FXML. */
    @FXML private GridPane holdPanel;
    /**
     * Rectangle used as an overlay for pause/game over screens, managed by FXML.
     */
    @FXML private Rectangle overlay;
    /**
     * Label to indicate if slow mode is active, managed by FXML.
     */
    @FXML private javafx.scene.control.Label slowModeIndicator; // Added for slow mode visual feedback

    @FXML private Pane tetrominoGameBackgroundContainer; // FXML for the tetromino animation container

    private FloatingTetrominos floatingTetrominos; // Instance of the FloatingTetrominos


    /**
     * A 2D array of Rectangles representing the individual tiles of the static game board background.
     */
    private Rectangle[][] displayMatrix;
    /** The listener for game input events, typically the {@code GameController}. */
    private InputEventListener eventListener;
    /** The JavaFX Timeline responsible for the automatic downward movement of bricks. */
    private Timeline timeline;

    /** A BooleanProperty to track if a UI animation (e.g., line clear) is currently running. */
    private final BooleanProperty isAnimating = new SimpleBooleanProperty(false);
    /** A BooleanProperty to track if the game is currently paused. */
    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    /** A BooleanProperty to track if the game is currently in a game-over state. */
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    /** Manages the visual display and updates of the active brick and its ghost. */
    private BrickViewManager brickViewManager;
    /** Manages the display of upcoming bricks and the held brick in their respective preview panels. */
    private PreviewPanelManager previewPanelManager;

    // Slow Mode fields
    /** Flag indicating whether "slow mode" is currently active, affecting game speed. */
    private boolean slowModeActive = false;
    /** The system time (in milliseconds) when the current slow mode period is scheduled to end. */
    private long slowModeEndTime = 0L;
    /** The duration of slow mode in milliseconds (8 seconds). */
    private static final long SLOW_MODE_DURATION_MS = 8000; // 8 seconds
    /**
     * The multiplier applied to the game's fall delay during slow mode.
     * A value of 3.0 means the delay is tripled, effectively making the game 3 times slower.
     */
    private static final double SLOW_MODE_SPEED_MULTIPLIER = 3.0; // Half speed (i.e., 2x delay)

    /**
     * Sets the player name.
     * @param name The name of the player.
     */
    public void setPlayerName(String name) {
        this.playerName = name;
    }

    /**
     * Sets the score tracker for the game.
     * @param score The Score object to track game score.
     */
    void setScoreTracker(Score score) {
        this.scoreTracker = score;
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     * This method sets up key event handlers, initializes managers for brick and preview views,
     * configures the game over panel, and sets up the overlay.
     * @param location The URL location of the FXML file, or null if not applicable.
     * @param resources The ResourceBundle for localization, or null if not applicable.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPress);

        Pane brickPanelContainer = (Pane) brickPanel.getParent();
        brickViewManager = new BrickViewManager(brickPanel, gamePanel, shadowButton, brickPanelContainer);
        previewPanelManager = new PreviewPanelManager(nextPanel1, nextPanel2, nextPanel3, holdPanel);

        gameOverPanel.setVisible(false);
        setupOverlay();
        if (slowModeIndicator != null) {
            slowModeIndicator.setText("SLOW MODE UNACTIVE");
            slowModeIndicator.getStyleClass().clear(); // Clear existing classes
            slowModeIndicator.getStyleClass().add("slow-mode-indicator"); // Add base class
        }

        if (Constants.SHOW_GRID && gamePanel != null) {
            gamePanel.setGridLinesVisible(true);
        }

        gameContainer.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() > 0) {
                gameContainer.setMaxWidth(newVal.doubleValue());
            }
        });
        gameContainer.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() > 0) {
                gameContainer.setMaxHeight(newVal.doubleValue());
            }
        });

        // Initialize and start the FloatingTetrominos animation
        if (tetrominoGameBackgroundContainer != null) {
            floatingTetrominos = new FloatingTetrominos(tetrominoGameBackgroundContainer);
            floatingTetrominos.startAnimations();
        }

        Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    /**
     * Sets up the overlay for pause/game over screens.
     */
    private void setupOverlay() {
        if (overlay != null) {
            overlay.setManaged(false);
            overlay.setVisible(false);
            overlay.setOpacity(Constants.OVERLAY_OPACITY);
            overlay.setMouseTransparent(true);
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

    /**
     * Handles keyboard key press events for game controls.
     * Delegates actions to the appropriate event listener methods based on the key pressed.
     * Ignores input if the game is paused, animating, or over, except for the 'N' key to start a new game
     * and 'BACK_SPACE' to pause/resume.
     * @param keyEvent The {@code KeyEvent} generated by a key press.
     */
    private void handleKeyPress(KeyEvent keyEvent) {

        if(isAnimating.get()) return;

        if (!isPause.get() && !isGameOver.get()) {

            switch (keyEvent.getCode()) {
                case LEFT, A -> handleMove(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                case RIGHT, D -> handleMove(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                case UP, W -> handleMove(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                case DOWN, S -> moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                case SPACE -> hardDrop(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                case C -> handleMove(eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER)));
                case H -> {
                    AudioManager.getInstance().playButtonClick();
                    brickViewManager.toggleShadow(null);
                }
            }
        }

        if (keyEvent.getCode() == KeyCode.N) newGame(null);

        keyEvent.consume();

        if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
            pauseGame(null);
        }
    }

    /**
     * Handles brick movement events, refreshing the brick display and previews.
     * @param brick The ViewData object containing information about the brick's new position.
     */
    private void handleMove(ViewData brick) {
        if (isPause.get()) return;
        brickViewManager.refreshBrick(brick);
        previewPanelManager.renderAllPreviews(brick);
    }

    /**
     * Initializes the game view by setting up the display matrix for the game board background,
     * drawing the initial active brick and ghost brick, and rendering the preview panels.
     * Also initializes and starts the game timeline for automatic brick movement.
     * @param boardMatrix The initial 2D integer array representing the game board's fixed background tiles.
     * @param brick The {@code ViewData} object for the initial active brick.
     */
    void initGameView(int[][] boardMatrix, ViewData brick) {

        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        for (int row = Constants.HIDDEN_ROWS; row < boardMatrix.length; row++) {
            for (int col = 0; col < boardMatrix[row].length; col++) {

                Rectangle rect = new Rectangle(Constants.TILE_SIZE, Constants.TILE_SIZE);
                rect.setArcHeight(Constants.TILE_ROUNDING);
                rect.setArcWidth(Constants.TILE_ROUNDING);
                TileStyleUtility.applyTileStyle(rect, 0);

                displayMatrix[row][col] = rect;
                gamePanel.add(rect, col, row - Constants.HIDDEN_ROWS);
            }
        }

        brickViewManager.initDrawBricks(brick);

        previewPanelManager.renderAllPreviews(brick);

        timeline = new Timeline(new KeyFrame(
                Duration.millis(Constants.FALL_DELAY_MS),
                e -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Refreshes the visual representation of the game board background based on the provided matrix.
     * This updates the colors of the static tiles on the board.
     * @param board The 2D integer array representing the updated game board background.
     */
    void refreshGameBackground(int[][] board) {
        for (int r = Constants.HIDDEN_ROWS; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                TileStyleUtility.applyTileStyle(displayMatrix[r][c], board[r][c]);
            }
        }
    }

    /**
     * Attempts to move the current brick down one step.
     * If the game is paused or animating, the movement is ignored.
     * After the movement, {@code handlePostDropUpdates} is called.
     * @param e The {@code MoveEvent} triggering the down movement (can be null for automatic moves).
     */
    private void moveDown(MoveEvent e) {
        if (isPause.get() || isAnimating.get()) return;
        DownData data = eventListener.onDownEvent(e);
        handlePostDropUpdates(data);
    }

    /**
     * Performs a hard drop of the current brick, moving it instantly to the lowest possible position.
     * If the game is paused or animating, the hard drop is ignored.
     * After the hard drop, {@code handlePostDropUpdates} is called.
     * @param e The {@code MoveEvent} triggering the hard drop (can be null for command-based drops).
     */
    private void hardDrop(MoveEvent e) {
        if (isPause.get() || isAnimating.get()) return;
        DownData data = eventListener.onHardDropEvent(e);
        handlePostDropUpdates(data);
    }

    /**
     * Handles updates that occur after a brick has dropped (either normally or via hard drop).
     * This includes checking for and animating cleared lines, displaying score notifications,
     * refreshing the active brick and preview displays, and ensuring the game panel maintains focus.
     * @param data The {@code DownData} object containing information about cleared rows and updated view data.
     */
    private void handlePostDropUpdates(DownData data) {
        if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
            ClearRow clearRow = data.getClearRow();
            animateLineClear(clearRow);
            NotificationPanel np = new NotificationPanel("+" + clearRow.getScoreBonus());
            np.setManaged(false);
            groupNotification.getChildren().add(np);
            np.showScore(groupNotification.getChildren());
        }

        ViewData brick = data.getViewData();
        brickViewManager.refreshBrick(brick);
        previewPanelManager.renderAllPreviews(brick);

        gamePanel.requestFocus();
    }

    /**
     * Animates the clearing of full lines from the game board by flashing white rectangles
     * over the cleared rows before removing them and refreshing the background.
     * Sets {@code isAnimating} to true during the animation.
     * @param clearRow The {@code ClearRow} object containing information about the cleared lines.
     */
    private void animateLineClear(ClearRow clearRow) {
        isAnimating.set(true);

        List<Rectangle> whiteRects = new ArrayList<>();
        for (int row : clearRow.getClearedRows()) {
            Rectangle whiteRect = new Rectangle(
                    gamePanel.getWidth(),
                    Constants.TILE_SIZE
            );
            whiteRect.setStyle("-fx-fill: white;");
            gamePanel.add(whiteRect, 0, row - Constants.HIDDEN_ROWS);
            GridPane.setColumnSpan(whiteRect, Constants.BOARD_WIDTH);
            whiteRects.add(whiteRect);
        }

        PauseTransition pause = new PauseTransition(Duration.millis(100));
        pause.setOnFinished(event -> {
            gamePanel.getChildren().removeAll(whiteRects);
            refreshGameBackground(clearRow.getNewBoard());
            isAnimating.set(false);
        });
        pause.play();
    }

    /**
     * Binds the game's score property to the {@code scoreLabel} for display in the UI.
     * The label text will be formatted to show "SCORE: [current_score]".
     * @param scoreProp The {@code ReadOnlyIntegerProperty} representing the game's current score.
     */
    void bindScore(ReadOnlyIntegerProperty scoreProp) {
        scoreLabel.textProperty().bind(Bindings.format("SCORE: %d", scoreProp));
    }

    /**
     * Binds the game's level property to the {@code levelLabel} for display in the UI.
     * The label text will be formatted to show "LEVEL: [current_level]".
     * @param levelProp The {@code ReadOnlyIntegerProperty} representing the game's current level.
     */
    void bindLevel(ReadOnlyIntegerProperty levelProp) {
        levelLabel.textProperty().bind(Bindings.format("LEVEL: %d", levelProp));
    }

    /**
     * Activates "slow mode", reducing game speed for a fixed duration.
     * Visual feedback is provided by updating the {@code slowModeIndicator} label.
     * The actual speed change is applied in {@code updateGameSpeed}.
     */
    void activateSlowMode() {
        slowModeActive = true;
        slowModeEndTime = System.currentTimeMillis() + SLOW_MODE_DURATION_MS;
        if (slowModeIndicator != null) {
            slowModeIndicator.setText("SLOW MODE ACTIVE");
            slowModeIndicator.getStyleClass().clear(); // Clear existing classes
            slowModeIndicator.getStyleClass().add("slow-mode-indicator"); // Add base class
            slowModeIndicator.getStyleClass().add("active"); // Add active class
        }
        // GameController will call updateGameSpeed after this, so no need to call here
    }

    /**
     * Updates the game speed based on the current level and slow mode status.
     * Stops the current game timeline and creates a new one with an adjusted fall delay.
     * Checks if slow mode has expired and deactivates it if necessary, updating the indicator.
     * @param level The current game level, which influences the base fall speed.
     */
    void updateGameSpeed(int level) {
        if (timeline != null) {
            timeline.stop();

            // Check if slow mode has expired
            if (slowModeActive && System.currentTimeMillis() > slowModeEndTime) {
                slowModeActive = false;
                if (slowModeIndicator != null) {
                    slowModeIndicator.setText("SLOW MODE UNACTIVE");
                    slowModeIndicator.getStyleClass().clear(); // Clear existing classes
                    slowModeIndicator.getStyleClass().add("slow-mode-indicator"); // Add base class
                }
            }

            int baseSpeed = Constants.FALL_DELAY_MS;
            double speedMultiplier = Math.pow(0.75, level - 1);
            int currentCalculatedSpeed = Math.max(50, (int)(baseSpeed * speedMultiplier));
            int newSpeed = currentCalculatedSpeed;

            if (slowModeActive) {
                newSpeed = (int)(currentCalculatedSpeed * SLOW_MODE_SPEED_MULTIPLIER); // Make it slower (increase delay)
            }

            timeline = new Timeline(new KeyFrame(
                    Duration.millis(newSpeed),
                    e -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
            ));
            timeline.setCycleCount(Timeline.INDEFINITE);

            if (!isPause.get() && !isGameOver.get()) {
                timeline.play();
            }
        }
    }

    /**
     * Handles the game over state: stops the game timeline, sets the game over flag,
     * saves the player's score to the leaderboard, clears the active game instance,
     * and animates the display of the {@code gameOverPanel} and a translucent overlay.
     */
    void gameOver() {
        timeline.stop();
        isGameOver.set(true);

        // Stop FloatingTetrominos animation when game is over
        if (floatingTetrominos != null) {
            floatingTetrominos.stopAnimations();
        }

        AudioManager.getInstance().stopAllSoundEffects(); // Stop all sound effects on game over

        if (scoreTracker != null) {
            LeaderboardManager.saveScore(
                    playerName,
                    scoreTracker.scoreProperty().get(),
                    scoreTracker.getLevel(),
                    scoreTracker.getRowsCleared()
            );
        }

        MainMenu.clearActiveGame();

        gameOverPanel.setOpacity(0);
        gameOverPanel.setScaleX(0.9);
        gameOverPanel.setScaleY(0.9);
        gameOverPanel.setVisible(true);

        if (overlay != null) {
            overlay.setVisible(true);
            FadeTransition overlayFade = new FadeTransition(Duration.millis(Constants.ANIM_DURATION_MS), overlay);
            overlayFade.setFromValue(0.0);
            overlayFade.setToValue(Constants.OVERLAY_OPACITY);
            overlayFade.play();
        }

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

    /**
     * Starts a new game, resetting the board, score, and UI elements.
     * Stops the current game timeline, clears any active game instance, hides the game over panel
     * and overlay, and resets game state flags (pause, game over, slow mode).
     * @param evt The {@code ActionEvent} triggering the new game (e.g., button click).
     */
    public void newGame(ActionEvent evt) {
            AudioManager.getInstance().playButtonClick();
            AudioManager.getInstance().stopAllSoundEffects(); // Stop all sound effects on new game start

            timeline.stop();

            // Stop and restart FloatingTetrominos animation for new game
            if (floatingTetrominos != null) {
                floatingTetrominos.stopAnimations();
                // Since the scene isn't reloaded, we need to regenerate tetrominos
                // to ensure they are positioned correctly within the current container size.
                // This will also restart animations.
                floatingTetrominos = new FloatingTetrominos(tetrominoGameBackgroundContainer);
                floatingTetrominos.startAnimations();
            }

            MainMenu.clearActiveGame();

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
            slowModeActive = false; // Reset slow mode on new game
            slowModeEndTime = 0L;

            if (slowModeIndicator != null) { // Ensure slowModeIndicator is hidden
                slowModeIndicator.setText("SLOW MODE UNACTIVE");
                slowModeIndicator.getStyleClass().clear(); // Clear existing classes
                slowModeIndicator.getStyleClass().add("slow-mode-indicator"); // Add base class
            }

            pauseButton.setText("Pause");

            timeline.play();
            gamePanel.requestFocus();
        }
    /**
     * Navigates back to the main menu.
     * Stops the current game timeline and disposes of the AudioManager instance.
     * @param evt The {@code ActionEvent} triggering the menu navigation.
     * @throws IOException If there is an error loading the main menu FXML.
     */
    public void goMainMenu(ActionEvent evt) throws IOException {
            // Automatically pause the game when navigating to the main menu
            pauseGame(null); 

            // Stop FloatingTetrominos animation before leaving game
            if (floatingTetrominos != null) {
                floatingTetrominos.stopAnimations();
            }

            AudioManager.getInstance().stopAllSoundEffects(); // Stop all sound effects before going to main menu
            AudioManager.getInstance().stopBackgroundMusic(); // Stop game background music
            Stage primaryStage = (Stage) ((javafx.scene.Node) evt.getSource()).getScene().getWindow();
            MainMenu.returnToMainMenu(primaryStage); // Correctly return to main menu
        }
    /**
     * Toggles the game's pause state. When paused, the game timeline stops, and the pause button text changes.
     * If the game is already over, this method does nothing.
     * @param evt The {@code ActionEvent} triggering the pause/resume action.
     */
    public void pauseGame(ActionEvent evt) {
            AudioManager.getInstance().playButtonClick();

            if (isGameOver.get()) return;

            if (isPause.get()) { // Resuming
                if (floatingTetrominos != null) floatingTetrominos.startAnimations();
                timeline.play();
                pauseButton.setText("Pause");
                isPause.set(false);
            } else { // Pausing
                if (floatingTetrominos != null) floatingTetrominos.stopAnimations();
                timeline.pause();
                pauseButton.setText("Resume");
                isPause.set(true);
            }
            gamePanel.requestFocus();
        }
    /**
     * Resumes the game from a paused state, typically when returning from a menu.
     * Starts the game timeline, updates the pause button text, and sets focus back to the game panel.
     * This method only acts if the game is currently paused and not in a game over state.
     */
    public void resumeFromMenu() {
            if (isPause.get() && !isGameOver.get()) {
                if (floatingTetrominos != null) floatingTetrominos.startAnimations(); // Restart animations
                timeline.play();
                pauseButton.setText("Pause");
                isPause.set(false);
            }
            gamePanel.requestFocus();
        }
    /**
     * Toggles the visibility of the shadow/ghost brick via the {@code BrickViewManager}.
     * Plays a button click sound and ensures the game panel retains focus.
     * @param evt The {@code ActionEvent} triggering the toggle.
     */
    public void toggleShadow(ActionEvent evt) {
            AudioManager.getInstance().playButtonClick();
            brickViewManager.toggleShadow(evt);
            gamePanel.requestFocus();
        }
    /**
     * Sets the event listener for game input events.
     * This listener (typically the {@code GameController}) will be notified of user actions.
     * @param listener The {@code InputEventListener} to be set.
     */
    void setEventListener(InputEventListener listener) {
            this.eventListener = listener;
        }

    }