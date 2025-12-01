package com.comp2042.ui;

import com.comp2042.logic.Constants;
import javafx.scene.shape.Rectangle;

public class TileStyleUtility {

    public static void applyTileStyle(Rectangle rect, int val) {
        rect.getStyleClass().removeIf(s -> s.startsWith("tile-"));
        if (!rect.getStyleClass().contains("tile")) {
            rect.getStyleClass().add("tile");
        }
        // Ensure val is clamped between 0 and 7 for valid tile styling
        rect.getStyleClass().add("tile-" + Math.max(0, Math.min(7, val)));
        if (val == 0) {
            rect.setOpacity(1.0); // Reset opacity for empty tiles
        }
    }
}