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
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
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

    @FXML private javafx.scene.control.Button shadowButton;

    // Shadow (ghost) projection toggle, enabled by default
    private boolean isShadowEnabled = true;

    @FXML private BorderPane gameBoard;
    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private javafx.scene.control.Label scoreLabel;
    @FXML private javafx.scene.control.Button pauseButton;
    // Next previews (1..3)
    @FXML private GridPane nextPanel1;
    @FXML private GridPane nextPanel2;
    @FXML private GridPane nextPanel3;
    // Hold preview
    @FXML private GridPane holdPanel;
    // Overlay for game over focus
    @FXML private Rectangle overlay;

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

        // Visual initial states
        gameOverPanel.setVisible(false);
        if (overlay != null) {
            overlay.setManaged(false);
            overlay.setVisible(false);
            overlay.setOpacity(Constants.OVERLAY_OPACITY);
            overlay.setMouseTransparent(true);
            // Bind overlay to the game board's bounds so it never spills outside
            if (gameBoard != null) {
                overlay.layoutXProperty().bind(gameBoard.layoutXProperty());
                overlay.layoutYProperty().bind(gameBoard.layoutYProperty());
                overlay.widthProperty().bind(gameBoard.widthProperty());
                overlay.heightProperty().bind(gameBoard.heightProperty());
            }
            // Match board corner rounding for a clean fit
            overlay.setArcWidth(Constants.OVERLAY_CORNER_RADIUS);
            overlay.setArcHeight(Constants.OVERLAY_CORNER_RADIUS);
        }

        // Apply subtle shadow to active piece layer
        if (brickPanel != null) {
            brickPanel.getStyleClass().add("piece-layer");
        }

        // Initialize Shadow toggle button label if present
        if (shadowButton != null) {
            shadowButton.setText("Shadow: " + (isShadowEnabled ? "ON" : "OFF"));
        }

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

    private void handleKeyPress(KeyEvent keyEvent) {

        if (!isPause.get() && !isGameOver.get()) {

            switch (keyEvent.getCode()) {
                case LEFT, A -> refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                case RIGHT, D -> refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                case UP, W -> refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                case DOWN, S -> moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                case SPACE -> hardDrop(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                case C -> refreshBrick(eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER)));
                case H -> toggleShadow(null);
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
                rect.setArcHeight(Constants.TILE_ROUNDING);
                rect.setArcWidth(Constants.TILE_ROUNDING);
                applyTileStyle(rect, 0);

                displayMatrix[row][col] = rect;
                gamePanel.add(rect, col, row - Constants.HIDDEN_ROWS);
            }
        }

        drawActiveBrick(brick);
        drawGhostBrick(brick);
        renderNextPreviews(brick);
        renderHoldPreview(brick);

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
                rect.setArcHeight(Constants.TILE_ROUNDING);
                rect.setArcWidth(Constants.TILE_ROUNDING);
                applyTileStyle(rect, brick.getBrickData()[r][c]);

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
        ghostPanel.setVisible(isShadowEnabled);
        ghostPanel.setMouseTransparent(true);
        ghostPanel.getStyleClass().add("ghost-layer");

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

    // Legacy color mapping kept for fallback; primary styling uses CSS classes via applyTileStyle
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

    private void applyTileStyle(Rectangle rect, int val) {
        rect.getStyleClass().removeIf(s -> s.startsWith("tile-"));
        if (!rect.getStyleClass().contains("tile")) {
            rect.getStyleClass().add("tile");
        }
        rect.getStyleClass().add("tile-" + Math.max(0, Math.min(7, val)));
        if (val == 0) {
            rect.setOpacity(1.0);
        }
    }

    private void renderNextPreviews(ViewData data) {
        int[][][] previews = data.getNextBricksData();
        renderPreview(nextPanel1, previews != null && previews.length > 0 ? previews[0] : null);
        renderPreview(nextPanel2, previews != null && previews.length > 1 ? previews[1] : null);
        renderPreview(nextPanel3, previews != null && previews.length > 2 ? previews[2] : null);
    }

    private void renderPreview(GridPane panel, int[][] shape) {
        if (panel == null) return;
        panel.getChildren().clear();
        if (shape == null) return;

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                Rectangle rect = new Rectangle(Constants.PREVIEW_TILE_SIZE, Constants.PREVIEW_TILE_SIZE);
                rect.setArcHeight(Constants.TILE_ROUNDING);
                rect.setArcWidth(Constants.TILE_ROUNDING);
                applyTileStyle(rect, shape[r][c]);
                panel.add(rect, c, r);
            }
        }
    }

    private void renderHoldPreview(ViewData data) {
        if (holdPanel == null) return;
        int[][] held = data.getHeldBrickData();
        holdPanel.getChildren().clear();
        if (held == null || held.length == 0) {
            return;
        }
        renderPreview(holdPanel, held);
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
                applyTileStyle(rectangles[r][c], brick.getBrickData()[r][c]);
            }
        }

        // Ghost: update only when enabled
        if (isShadowEnabled) {
            updateGhostPanelPosition(brick);
            if (ghostRectangles != null) {
                for (int r = 0; r < brick.getBrickData().length; r++) {
                    for (int c = 0; c < brick.getBrickData()[r].length; c++) {
                        ghostRectangles[r][c].setFill(getGhostFillColor(brick.getBrickData()[r][c]));
                    }
                }
            }
            if (ghostPanel != null) ghostPanel.setVisible(true);
        } else {
            if (ghostPanel != null) ghostPanel.setVisible(false);
        }

        // Update next previews
        renderNextPreviews(brick);
        // Update hold preview
        renderHoldPreview(brick);
    }

    private void updateBrickPanelPosition(ViewData brick) {
        double tile = Constants.TILE_SIZE + Constants.GRID_GAP;

        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * tile);
        brickPanel.setLayoutY(gamePanel.getLayoutY() +
                (brick.getyPosition() - Constants.HIDDEN_ROWS) * tile);
    }

    private void updateGhostPanelPosition(ViewData brick) {
        if (!isShadowEnabled || ghostPanel == null) return;

        double tile = Constants.TILE_SIZE + Constants.GRID_GAP;

        ghostPanel.setLayoutX(gamePanel.getLayoutX() + brick.getGhostXPosition() * tile);
        ghostPanel.setLayoutY(gamePanel.getLayoutY() +
                (brick.getGhostYPosition() - Constants.HIDDEN_ROWS) * tile);
    }

    public void refreshGameBackground(int[][] board) {
        for (int r = Constants.HIDDEN_ROWS; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                applyTileStyle(displayMatrix[r][c], board[r][c]);
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
        isGameOver.set(true);

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

    public void newGame(ActionEvent evt) {
        timeline.stop();

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

    // Toggle Shadow (ghost) projection via button or 'H' key
    public void toggleShadow(ActionEvent evt) {
        isShadowEnabled = !isShadowEnabled;
        if (shadowButton != null) {
            shadowButton.setText("Shadow: " + (isShadowEnabled ? "ON" : "OFF"));
        }
        if (ghostPanel != null) {
            ghostPanel.setVisible(isShadowEnabled);
        }
        // Keep focus on the board for continuous keyboard controls
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener listener) {
        this.eventListener = listener;
    }
}
