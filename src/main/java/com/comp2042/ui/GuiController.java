package com.comp2042.ui;

import com.comp2042.InputEventListener;
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
import com.comp2042.logic.scoring.Score;
import com.comp2042.logic.scoring.LeaderboardManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private String playerName = "Player";
    private Score scoreTracker;

    @FXML private Pane gameContainer;
    @FXML private javafx.scene.control.Button shadowButton;
    @FXML private BorderPane gameBoard;
    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private javafx.scene.control.Label scoreLabel;
    @FXML private javafx.scene.control.Label levelLabel;
    @FXML private javafx.scene.control.Button pauseButton;
    @FXML private GridPane nextPanel1;
    @FXML private GridPane nextPanel2;
    @FXML private GridPane nextPanel3;
    @FXML private GridPane holdPanel;
    @FXML private Rectangle overlay;



    private Rectangle[][] displayMatrix;
    private InputEventListener eventListener;
    private Timeline timeline;

    private final BooleanProperty isAnimating = new SimpleBooleanProperty(false);
    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    private BrickViewManager brickViewManager;
    private PreviewPanelManager previewPanelManager;

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    void setScoreTracker(Score score) {
        this.scoreTracker = score;
    }

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

    private void handleMove(ViewData brick) {
        if (isPause.get()) return;
        brickViewManager.refreshBrick(brick);
        previewPanelManager.renderAllPreviews(brick);
    }

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

    void refreshGameBackground(int[][] board) {
        for (int r = Constants.HIDDEN_ROWS; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                TileStyleUtility.applyTileStyle(displayMatrix[r][c], board[r][c]);
            }
        }
    }

    private void moveDown(MoveEvent e) {
        if (isPause.get() || isAnimating.get()) return;
        DownData data = eventListener.onDownEvent(e);
        handlePostDropUpdates(data);
    }

    private void hardDrop(MoveEvent e) {
        if (isPause.get() || isAnimating.get()) return;
        DownData data = eventListener.onHardDropEvent(e);
        handlePostDropUpdates(data);
    }

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

    void bindScore(ReadOnlyIntegerProperty scoreProp) {
        scoreLabel.textProperty().bind(Bindings.format("SCORE: %d", scoreProp));
    }

    void bindLevel(ReadOnlyIntegerProperty levelProp) {
        levelLabel.textProperty().bind(Bindings.format("LEVEL: %d", levelProp));
    }

    void updateGameSpeed(int level) {
        if (timeline != null) {
            timeline.stop();

            int baseSpeed = Constants.FALL_DELAY_MS;
            double speedMultiplier = Math.pow(0.75, level - 1);
            int newSpeed = Math.max(50, (int)(baseSpeed * speedMultiplier));

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

    void gameOver() {
        timeline.stop();
        isGameOver.set(true);

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

    public void newGame(ActionEvent evt) {
        AudioManager.getInstance().playButtonClick();
        timeline.stop();

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
        pauseButton.setText("Pause");

        timeline.play();
        gamePanel.requestFocus();
    }

    public void goMainMenu(ActionEvent evt) throws IOException {
        AudioManager.getInstance().playButtonClick();
        if (!isGameOver.get()) {
            timeline.pause();
            isPause.set(true);
            pauseButton.setText("Resume");
        }

        Stage primaryStage = (Stage) ((javafx.scene.Node) evt.getSource()).getScene().getWindow();

        primaryStage.getScene().getRoot().setUserData(this);

        MainMenu.setActiveGameScene(primaryStage.getScene());

        URL location = getClass().getClassLoader().getResource("menuLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 510);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void pauseGame(ActionEvent evt) {
        AudioManager.getInstance().playButtonClick();

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

    public void resumeFromMenu() {
        if (isPause.get() && !isGameOver.get()) {
            timeline.play();
            pauseButton.setText("Pause");
            isPause.set(false);
        }
        gamePanel.requestFocus();
    }

    public void toggleShadow(ActionEvent evt) {
        AudioManager.getInstance().playButtonClick();
        brickViewManager.toggleShadow(evt);
        gamePanel.requestFocus();
    }

    void setEventListener(InputEventListener listener) {
        this.eventListener = listener;
    }
}