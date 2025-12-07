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

    /** The GridPane that displays the active brick. */
    private final GridPane brickPanel;
    /** Reference to the main GridPane of the game board for layout calculations. */
    private final GridPane gamePanel; // Reference to the main grid for layout calculations
    /** The common parent Pane for both the brickPanel and ghostPanel. */
    private final Pane parentPane; // The common parent of brickPanel/ghostPanel
    /** The button used to toggle the shadow (ghost piece) visibility. */
    private final javafx.scene.control.Button shadowButton;

    /** A 2D array of Rectangles representing the active brick's visual elements. */
    private Rectangle[][] rectangles;
    /** A 2D array of Rectangles representing the ghost piece's visual elements. */
    private Rectangle[][] ghostRectangles;
    /** The GridPane that displays the ghost piece. */
    private GridPane ghostPanel;

    /** Flag indicating whether the shadow (ghost piece) is currently enabled. */
    private boolean isShadowEnabled = true;

    /**
     * Constructs a new BrickViewManager.
     * @param brickPanel The GridPane where the active brick will be rendered.
     * @param gamePanel The main GridPane representing the game board, used for position calculations.
     * @param shadowButton The button that controls the visibility of the shadow (ghost) brick.
     * @param brickPanelContainer The parent Pane that contains both the brickPanel and ghostPanel.
     */
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
     * @param brick The ViewData object containing the active and ghost brick information.
     */
    public void initDrawBricks(ViewData brick) {
        drawActiveBrick(brick);
        drawGhostBrick(brick);
    }

    /**
     * Draws the active brick on the {@code brickPanel} based on the provided {@code ViewData}.
     * This method initializes the {@code rectangles} array and sets the initial position.
     * @param brick The {@code ViewData} containing the active brick's shape and position.
     */
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

    /**
     * Draws the ghost brick (shadow) on a new {@code GridPane} named {@code ghostPanel}.
     * The ghost piece indicates where the active brick will land if a hard drop occurs.
     * The {@code ghostPanel} is added behind the {@code brickPanel} in the {@code parentPane}.
     * @param brick The {@code ViewData} containing the ghost brick's shape and position.
     */
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

    /**
     * Creates a single rectangular tile (a piece of a brick) with specified value and size.
     * Applies styling based on the tile's value.
     * @param val The integer value representing the type of the tile (determines color/style).
     * @param size The side length of the square tile.
     * @return A styled {@code Rectangle} object.
     */
    private Rectangle createTile(int val, double size) {
        Rectangle rect = new Rectangle(size, size);
        rect.setArcHeight(Constants.TILE_ROUNDING);
        rect.setArcWidth(Constants.TILE_ROUNDING);
        TileStyleUtility.applyTileStyle(rect, val);
        return rect;
    }

    /**
     * Determines the fill color for a ghost tile. It's transparent if the tile value is 0,
     * otherwise it's a semi-transparent white.
     * @param val The integer value of the tile.
     * @return The {@code Paint} object for the ghost tile.
     */
    private Paint getGhostFillColor(int val) {
        if (val == 0) return Color.TRANSPARENT;
        return new Color(1, 1, 1, Constants.GHOST_ALPHA);
    }

    /**
     * Updates the position and appearance of the active piece and its ghost.
     * This method should be called when the brick moves or changes shape.
     * @param brick The {@code ViewData} containing the updated active brick and ghost brick information.
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

    /**
     * Updates the layout position of the {@code brickPanel} based on the active brick's coordinates.
     * @param brick The {@code ViewData} containing the active brick's position.
     */
    private void updateBrickPanelPosition(ViewData brick) {
        double tile = Constants.TILE_SIZE + Constants.GRID_GAP;
        // The original code uses gamePanel layout for offset
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * tile);
        brickPanel.setLayoutY(gamePanel.getLayoutY() +
                (brick.getyPosition() - Constants.HIDDEN_ROWS) * tile);
    }

    /**
     * Updates the layout position of the {@code ghostPanel} based on the ghost brick's coordinates.
     * This method only performs an update if shadow is enabled and {@code ghostPanel} exists.
     * @param brick The {@code ViewData} containing the ghost brick's position.
     */
    private void updateGhostPanelPosition(ViewData brick) {
        if (!isShadowEnabled || ghostPanel == null) return;
        double tile = Constants.TILE_SIZE + Constants.GRID_GAP;

        // The original code uses gamePanel layout for offset
        ghostPanel.setLayoutX(gamePanel.getLayoutX() + brick.getGhostXPosition() * tile);
        ghostPanel.setLayoutY(gamePanel.getLayoutY() +
                (brick.getGhostYPosition() - Constants.HIDDEN_ROWS) * tile);
    }

    /**
     * Toggles the visibility of the ghost piece (shadow).
     * Updates the text of the {@code shadowButton} to reflect the new state.
     * @param evt The {@code ActionEvent} that triggered this method (e.g., button click).
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

    /**
     * Checks if the shadow (ghost piece) is currently enabled.
     * @return True if the shadow is enabled, false otherwise.
     */
    public boolean isShadowEnabled() {
        return isShadowEnabled;
    }
}