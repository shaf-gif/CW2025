package com.comp2042.ui;

import com.comp2042.logic.Constants;
import com.comp2042.model.ViewData;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Manages the display and position of the active brick and its ghost.
 */
public class BrickViewManager {

    private final GridPane brickPanel;
    private final GridPane gamePanel; // Reference to the main grid for layout calculations
    private final Pane parentPane; // The common parent of brickPanel/ghostPanel
    private final javafx.scene.control.Button shadowButton;

    private Rectangle[][] rectangles;
    private Rectangle[][] ghostRectangles;
    private GridPane ghostPanel;

    private boolean isShadowEnabled = true;

    public BrickViewManager(GridPane brickPanel, GridPane gamePanel, javafx.scene.control.Button shadowButton, Pane brickPanelContainer) {
        this.brickPanel = brickPanel;
        this.gamePanel = gamePanel;
        this.shadowButton = shadowButton;
        this.parentPane = brickPanelContainer;

        if (brickPanel != null) {
            brickPanel.getStyleClass().add("piece-layer");
        }
        if (shadowButton != null) {
            shadowButton.setText("Shadow: " + (isShadowEnabled ? "ON" : "OFF"));
        }
    }

    /**
     * Initial drawing of the active piece and ghost piece layers.
     */
    public void initDrawBricks(ViewData brick) {
        drawActiveBrick(brick);
        drawGhostBrick(brick);
    }

    private void drawActiveBrick(ViewData brick) {
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        brickPanel.getChildren().clear();

        for (int r = 0; r < brick.getBrickData().length; r++) {
            for (int c = 0; c < brick.getBrickData()[r].length; c++) {
                Rectangle rect = createTile(brick.getBrickData()[r][c], Constants.TILE_SIZE);
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
                Rectangle rect = createTile(0, Constants.TILE_SIZE);
                rect.setFill(getGhostFillColor(brick.getBrickData()[r][c]));
                ghostRectangles[r][c] = rect;
                ghostPanel.add(rect, c, r);
            }
        }

        // Add ghost panel behind the active brick panel
        if (parentPane != null) {
            int idx = parentPane.getChildren().indexOf(brickPanel);
            parentPane.getChildren().add(idx, ghostPanel);
        }

        updateGhostPanelPosition(brick);
    }

    private Rectangle createTile(int val, double size) {
        Rectangle rect = new Rectangle(size, size);
        rect.setArcHeight(Constants.TILE_ROUNDING);
        rect.setArcWidth(Constants.TILE_ROUNDING);
        TileStyleUtility.applyTileStyle(rect, val);
        return rect;
    }

    private Paint getGhostFillColor(int val) {
        if (val == 0) return Color.TRANSPARENT;
        return new Color(1, 1, 1, Constants.GHOST_ALPHA);
    }

    /**
     * Updates the position and appearance of the active piece and its ghost.
     */
    public void refreshBrick(ViewData brick) {
        if (rectangles == null) return;

        updateBrickPanelPosition(brick);

        // Update active piece tiles
        for (int r = 0; r < brick.getBrickData().length; r++) {
            for (int c = 0; c < brick.getBrickData()[r].length; c++) {
                TileStyleUtility.applyTileStyle(rectangles[r][c], brick.getBrickData()[r][c]);
            }
        }

        // Update ghost piece
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
    }

    private void updateBrickPanelPosition(ViewData brick) {
        double tile = Constants.TILE_SIZE + Constants.GRID_GAP;
        // The original code uses gamePanel layout for offset
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * tile);
        brickPanel.setLayoutY(gamePanel.getLayoutY() +
                (brick.getyPosition() - Constants.HIDDEN_ROWS) * tile);
    }

    private void updateGhostPanelPosition(ViewData brick) {
        if (!isShadowEnabled || ghostPanel == null) return;
        double tile = Constants.TILE_SIZE + Constants.GRID_GAP;

        // The original code uses gamePanel layout for offset
        ghostPanel.setLayoutX(gamePanel.getLayoutX() + brick.getGhostXPosition() * tile);
        ghostPanel.setLayoutY(gamePanel.getLayoutY() +
                (brick.getGhostYPosition() - Constants.HIDDEN_ROWS) * tile);
    }

    /**
     * Toggles the visibility of the ghost piece.
     */
    public void toggleShadow(ActionEvent evt) {
        isShadowEnabled = !isShadowEnabled;
        if (shadowButton != null) {
            shadowButton.setText("Shadow: " + (isShadowEnabled ? "ON" : "OFF"));
        }
        if (ghostPanel != null) {
            ghostPanel.setVisible(isShadowEnabled);
        }
    }
}