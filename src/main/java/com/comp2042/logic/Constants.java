package com.comp2042.logic;

public final class Constants {

    private Constants() {} // Prevent instantiation

    // --- Board Dimensions ---
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 25;
    public static final int HIDDEN_ROWS = 2;

    // --- Rendering / UI ---
    public static final int TILE_SIZE = 20;         // Tile size for bricks (main board)
    public static final int PREVIEW_TILE_SIZE = 12; // Tile size for next-piece previews (smaller boxes)
    public static final int GRID_GAP = 1;           // Gap between tiles in grid
    public static final int TILE_ROUNDING = 9;      // Corner radius for tiles

    // Layout helpers
    public static final int UI_SPACING = 12;        // General spacing between UI elements
    public static final int UI_CORNER_RADIUS = 10;  // Generic UI corner radius

    // Y-offset for brickPanel & ghostPanel so falling brick aligns perfectly
    public static final int PANEL_OFFSET_Y = -42;

    // --- Ghost Piece ---
    public static final double GHOST_ALPHA = 0.25;  // Transparency level

    // --- Grid (board helper) ---
    public static final boolean SHOW_GRID = true;   // Turn on/off visible grid lines on the board

    // --- Visual Theme (strings keep JavaFX out of logic layer) ---
    public static final String NEON_COLOR = "#00E5FF";         // Cyan glow
    public static final int NEON_BORDER_WIDTH = 2;              // px
    public static final int NEON_GLOW_RADIUS = 16;              // px
    public static final double OVERLAY_OPACITY = 0.6;           // overlay darkness
    public static final int ANIM_DURATION_MS = 350;             // fade/scale animation duration
    public static final int OVERLAY_CORNER_RADIUS = 12;         // match board rounding if desired
    public static final String BG_GRADIENT_DARK =
            "linear-gradient(to bottom, #0f2027, #203a43, #2c5364)";
    public static final String BG_GRADIENT_LIGHT =
            "linear-gradient(to bottom, #e6f2ff, #cce0ff, #b3ccff)";

    // --- Gameplay ---
    public static final int FALL_DELAY_MS = 400; // milliseconds for falling speed
}
