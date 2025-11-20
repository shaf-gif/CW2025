package com.comp2042.ui;

import com.comp2042.InputEventListener;
import com.comp2042.logic.Constants;
import com.comp2042.logic.movement.*;
import com.comp2042.model.ViewData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private javafx.scene.control.Label scoreLabel;
    @FXML private javafx.scene.control.Button pauseButton;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;
    private Rectangle[][] ghostRectangles;

    private GridPane ghostPanel;
    private InputEventListener eventListener;

    private Timeline timeline;

    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPress);

        gameOverPanel.setVisible(false);

        Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    private void handleKeyPress(KeyEvent keyEvent) {

        if (!isPause.get() && !isGameOver.get()) {

            switch (keyEvent.getCode()) {
                case LEFT, A -> refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                case RIGHT, D -> refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                case UP, W -> refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                case DOWN, S -> moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                case SPACE -> hardDrop(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
            }
        }

        if (keyEvent.getCode() == KeyCode.N) newGame(null);

        keyEvent.consume();
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {

        // Draw background grid (skip hidden rows)
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        for (int row = Constants.HIDDEN_ROWS; row < boardMatrix.length; row++) {
            for (int col = 0; col < boardMatrix[row].length; col++) {

                Rectangle rect = new Rectangle(Constants.TILE_SIZE, Constants.TILE_SIZE);
                rect.setFill(Color.TRANSPARENT);

                displayMatrix[row][col] = rect;
                gamePanel.add(rect, col, row - Constants.HIDDEN_ROWS);
            }
        }

        drawActiveBrick(brick);
        drawGhostBrick(brick);

        // Start game loop
        timeline = new Timeline(new KeyFrame(
                Duration.millis(Constants.FALL_DELAY_MS),
                e -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void drawActiveBrick(ViewData brick) {

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        brickPanel.getChildren().clear();

        for (int r = 0; r < brick.getBrickData().length; r++) {
            for (int c = 0; c < brick.getBrickData()[r].length; c++) {

                Rectangle rect = new Rectangle(Constants.TILE_SIZE, Constants.TILE_SIZE);
                rect.setFill(getFillColor(brick.getBrickData()[r][c]));
                rect.setArcHeight(Constants.TILE_ROUNDING);
                rect.setArcWidth(Constants.TILE_ROUNDING);

                rectangles[r][c] = rect;
                brickPanel.add(rect, c, r);
            }
        }

        updateBrickPanelPosition(brick);
    }

    private void drawGhostBrick(ViewData brick) {

        ghostPanel = new GridPane();
        ghostPanel.setHgap(Constants.GRID_GAP);
        ghostPanel.setVgap(Constants.GRID_GAP);

        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

        for (int r = 0; r < brick.getBrickData().length; r++) {
            for (int c = 0; c < brick.getBrickData()[r].length; c++) {

                Rectangle rect = new Rectangle(Constants.TILE_SIZE, Constants.TILE_SIZE);
                rect.setFill(getGhostFillColor(brick.getBrickData()[r][c]));
                rect.setArcHeight(Constants.TILE_ROUNDING);
                rect.setArcWidth(Constants.TILE_ROUNDING);

                ghostRectangles[r][c] = rect;
                ghostPanel.add(rect, c, r);
            }
        }

        if (brickPanel.getParent() instanceof Pane parent) {
            int idx = parent.getChildren().indexOf(brickPanel);
            parent.getChildren().add(idx, ghostPanel);
        }

        updateGhostPanelPosition(brick);
    }

    private Paint getFillColor(int i) {
        return switch (i) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.AQUA;
            case 2 -> Color.BLUEVIOLET;
            case 3 -> Color.DARKGREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.BEIGE;
            case 7 -> Color.BURLYWOOD;
            default -> Color.WHITE;
        };
    }

    private Paint getGhostFillColor(int val) {
        if (val == 0) return Color.TRANSPARENT;
        return new Color(1, 1, 1, Constants.GHOST_ALPHA);
    }

    private void refreshBrick(ViewData brick) {

        if (isPause.get()) return;

        updateBrickPanelPosition(brick);

        for (int r = 0; r < brick.getBrickData().length; r++) {
            for (int c = 0; c < brick.getBrickData()[r].length; c++) {
                rectangles[r][c].setFill(getFillColor(brick.getBrickData()[r][c]));
            }
        }

        updateGhostPanelPosition(brick);

        for (int r = 0; r < brick.getBrickData().length; r++) {
            for (int c = 0; c < brick.getBrickData()[r].length; c++) {
                ghostRectangles[r][c].setFill(getGhostFillColor(brick.getBrickData()[r][c]));
            }
        }
    }

    private void updateBrickPanelPosition(ViewData brick) {
        double tile = Constants.TILE_SIZE + Constants.GRID_GAP;

        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * tile);
        brickPanel.setLayoutY(gamePanel.getLayoutY() +
                (brick.getyPosition() - Constants.HIDDEN_ROWS) * tile);
    }

    private void updateGhostPanelPosition(ViewData brick) {
        double tile = Constants.TILE_SIZE + Constants.GRID_GAP;

        ghostPanel.setLayoutX(gamePanel.getLayoutX() + brick.getGhostXPosition() * tile);
        ghostPanel.setLayoutY(gamePanel.getLayoutY() +
                (brick.getGhostYPosition() - Constants.HIDDEN_ROWS) * tile);
    }

    public void refreshGameBackground(int[][] board) {
        for (int r = Constants.HIDDEN_ROWS; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                displayMatrix[r][c].setFill(getFillColor(board[r][c]));
            }
        }
    }

    private void moveDown(MoveEvent e) {

        if (isPause.get()) return;

        DownData data = eventListener.onDownEvent(e);

        if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel np = new NotificationPanel("+" + data.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(np);
            np.showScore(groupNotification.getChildren());
        }

        refreshBrick(data.getViewData());
        gamePanel.requestFocus();
    }

    private void hardDrop(MoveEvent e) {
        if (isPause.get()) return;

        DownData data = eventListener.onHardDropEvent(e);

        if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel np = new NotificationPanel("+" + data.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(np);
            np.showScore(groupNotification.getChildren());
        }

        refreshBrick(data.getViewData());
        gamePanel.requestFocus();
    }

    public void bindScore(IntegerProperty scoreProp) {
        scoreLabel.textProperty().bind(Bindings.format("Score: %d", scoreProp));
    }

    public void gameOver() {
        timeline.stop();
        gameOverPanel.setVisible(true);
        isGameOver.set(true);
    }

    public void newGame(ActionEvent evt) {
        timeline.stop();
        gameOverPanel.setVisible(false);

        eventListener.createNewGame();
        isPause.set(false);
        isGameOver.set(false);
        pauseButton.setText("Pause");

        timeline.play();
        gamePanel.requestFocus();
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

    public void setEventListener(InputEventListener listener) {
        this.eventListener = listener;
    }
}
