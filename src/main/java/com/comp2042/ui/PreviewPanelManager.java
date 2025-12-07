package com.comp2042.ui;

import com.comp2042.logic.Constants;
import com.comp2042.model.ViewData;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

/**
 * Manages the display of next brick previews and the held brick in the UI.
 */
public class PreviewPanelManager {

    /** The GridPane for displaying the first upcoming brick preview. */
    private final GridPane nextPanel1;
    /** The GridPane for displaying the second upcoming brick preview. */
    private final GridPane nextPanel2;
    /** The GridPane for displaying the third upcoming brick preview. */
    private final GridPane nextPanel3;
    /** The GridPane for displaying the currently held brick preview. */
    private final GridPane holdPanel;

    /**
     * Constructs a new PreviewPanelManager.
     *
     * @param nextPanel1 The GridPane for the first next brick preview.
     * @param nextPanel2 The GridPane for the second next brick preview.
     * @param nextPanel3 The GridPane for the third next brick preview.
     * @param holdPanel The GridPane for the held brick preview.
     */
    public PreviewPanelManager(GridPane nextPanel1, GridPane nextPanel2, GridPane nextPanel3, GridPane holdPanel) {
        this.nextPanel1 = nextPanel1;
        this.nextPanel2 = nextPanel2;
        this.nextPanel3 = nextPanel3;
        this.holdPanel = holdPanel;
    }

    /**
     * Renders all preview panels (next bricks and held brick) based on the provided game data.
     *
     * @param data The ViewData object containing the current game state information.
     */

    public void renderAllPreviews(ViewData data) {
        renderNextPreviews(data);
        renderHoldPreview(data);
    }

    /**
     * Renders the next brick preview panels.
     *
     * @param data The ViewData object containing the current game state information.
     */

    void renderNextPreviews(ViewData data) {
        int[][][] previews = data.getNextBricksData();
        renderPreview(nextPanel1, previews != null && previews.length > 0 ? previews[0] : null);
        renderPreview(nextPanel2, previews != null && previews.length > 1 ? previews[1] : null);
        renderPreview(nextPanel3, previews != null && previews.length > 2 ? previews[2] : null);
    }

    /**
     * Renders the held brick preview panel.
     *
     * @param data The ViewData object containing the current game state information.
     */
    /**
     * Renders the held brick preview panel.
     * Clears existing content and draws the held brick's shape from the {@code ViewData}.
     * If no brick is held, the panel will remain empty.
     * @param data The {@code ViewData} object containing the current game state information, specifically held brick data.
     */
    void renderHoldPreview(ViewData data) {
        if (holdPanel == null) return;
        int[][] held = data.getHeldBrickData();
        holdPanel.getChildren().clear();
        if (held == null || held.length == 0) {
            return;
        }
        renderPreview(holdPanel, held);
    }

    /**
     * Renders a single brick shape onto a given GridPane.
     *
     * @param panel The GridPane to render the shape onto.
     * @param shape The 2D integer array representing the brick's shape.
     */
    /**
     * Renders a single brick shape onto a given {@code GridPane}.
     * Clears the panel and then adds {@code Rectangle} nodes for each non-zero cell in the shape matrix.
     * @param panel The {@code GridPane} to render the shape onto.
     * @param shape The 2D integer array representing the brick's shape to be rendered.
     */
    void renderPreview(GridPane panel, int[][] shape) {
        if (panel == null) return;
        panel.getChildren().clear();
        if (shape == null) return;

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] != 0) { // Only add if it's not an empty tile
                    Rectangle rect = new Rectangle(Constants.PREVIEW_TILE_SIZE, Constants.PREVIEW_TILE_SIZE);
                    rect.setArcHeight(Constants.TILE_ROUNDING);
                    rect.setArcWidth(Constants.TILE_ROUNDING);
                    TileStyleUtility.applyTileStyle(rect, shape[r][c]);
                    panel.add(rect, c, r);
                }
            }
        }
    }
}