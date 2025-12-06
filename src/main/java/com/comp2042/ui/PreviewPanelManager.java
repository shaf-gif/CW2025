package com.comp2042.ui;

import com.comp2042.logic.Constants;
import com.comp2042.model.ViewData;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class PreviewPanelManager {

    private final GridPane nextPanel1;
    private final GridPane nextPanel2;
    private final GridPane nextPanel3;
    private final GridPane holdPanel;

    public PreviewPanelManager(GridPane nextPanel1, GridPane nextPanel2, GridPane nextPanel3, GridPane holdPanel) {
        this.nextPanel1 = nextPanel1;
        this.nextPanel2 = nextPanel2;
        this.nextPanel3 = nextPanel3;
        this.holdPanel = holdPanel;
    }

    public void renderAllPreviews(ViewData data) {
        renderNextPreviews(data);
        renderHoldPreview(data);
    }

    void renderNextPreviews(ViewData data) {
        int[][][] previews = data.getNextBricksData();
        renderPreview(nextPanel1, previews != null && previews.length > 0 ? previews[0] : null);
        renderPreview(nextPanel2, previews != null && previews.length > 1 ? previews[1] : null);
        renderPreview(nextPanel3, previews != null && previews.length > 2 ? previews[2] : null);
    }

    void renderHoldPreview(ViewData data) {
        if (holdPanel == null) return;
        int[][] held = data.getHeldBrickData();
        holdPanel.getChildren().clear();
        if (held == null || held.length == 0) {
            return;
        }
        renderPreview(holdPanel, held);
    }

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