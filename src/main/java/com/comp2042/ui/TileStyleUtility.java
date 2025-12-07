package com.comp2042.ui;

import com.comp2042.logic.Constants;
import javafx.scene.shape.Rectangle;

/**
 * Utility class for applying CSS styles to {@code Rectangle} objects representing game tiles.
 * This class cannot be instantiated.
 */
public class TileStyleUtility {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private TileStyleUtility() {
        // Private constructor to prevent instantiation
    }

    /**
     * Applies CSS styles to a given {@code Rectangle} based on an integer value.
     * The method first removes any existing "tile-" styles, adds a generic "tile" style,
     * and then adds a specific "tile-[value]" style. The value is clamped between 0 and 7.
     * If the value is 0 (representing an empty tile), its opacity is reset to 1.0.
     * @param rect The {@code Rectangle} to which styles will be applied.
     * @param val The integer value determining the specific tile style (e.g., brick type).
     */
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