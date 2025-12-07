package com.comp2042.logic;

/**
 * Utility class containing various constants used throughout the game,
 * including board dimensions, UI rendering parameters, and gameplay mechanics.
 * This class cannot be instantiated.
 */
public final class Constants {

    private Constants() {} // Prevent instantiation

    // --- Board Dimensions ---
    /** The width of the game board in tiles. */
    public static final int BOARD_WIDTH = 10;
    /** The height of the game board in tiles. */
    public static final int BOARD_HEIGHT = 25;
    /** The number of rows hidden at the top of the board. */
    public static final int HIDDEN_ROWS = 2;

    // --- Rendering / UI ---
    /** Tile size for bricks (main board). */
    public static final int TILE_SIZE = 20;         // Tile size for bricks (main board)
    /** Tile size for next-piece previews (smaller boxes). */
    public static final int PREVIEW_TILE_SIZE = 12; // Tile size for next-piece previews (smaller boxes)
    /** Gap between tiles in grid. */
    public static final int GRID_GAP = 1;           // Gap between tiles in grid
    /** Corner radius for tiles. */
    public static final int TILE_ROUNDING = 9;      // Corner radius for tiles

    // Layout helpers
    /** General spacing between UI elements. */
    public static final int UI_SPACING = 12;        // General spacing between UI elements
    /** Generic UI corner radius. */
    public static final int UI_CORNER_RADIUS = 10;  // Generic UI corner radius

    // Y-offset for brickPanel & ghostPanel so falling brick aligns perfectly
    /** Y-offset for brickPanel &amp; ghostPanel so falling brick aligns perfectly. */
    public static final int PANEL_OFFSET_Y = -42;

    // --- Ghost Piece ---
    /** Transparency level for the ghost piece. */
    public static final double GHOST_ALPHA = 0.25;  // Transparency level

    // --- Grid (board helper) ---
    /** Flag to turn on/off visible grid lines on the board. */
    public static final boolean SHOW_GRID = true;   // Turn on/off visible grid lines on the board

    // --- Visual Theme (strings keep JavaFX out of logic layer) ---
    /** Hex color code for neon glow effects. */
    public static final String NEON_COLOR = "#00E5FF";         // Cyan glow
    /** Border width for neon effects in pixels. */
    public static final int NEON_BORDER_WIDTH = 2;              // px
    /** Glow radius for neon effects in pixels. */
    public static final int NEON_GLOW_RADIUS = 16;              // px
    /** Opacity level for UI overlays. */
    public static final double OVERLAY_OPACITY = 0.6;           // overlay darkness
    /** Duration for animations in milliseconds. */
    public static final int ANIM_DURATION_MS = 350;             // fade/scale animation duration
    /** Corner radius for UI overlays. */
    public static final int OVERLAY_CORNER_RADIUS = 12;         // match board rounding if desired
    /** CSS string for a dark background gradient. */
    public static final String BG_GRADIENT_DARK =
            "linear-gradient(to bottom, #0f2027, #203a43, #2c5364)";
    /** CSS string for a light background gradient. */
    public static final String BG_GRADIENT_LIGHT =
            "linear-gradient(to bottom, #e6f2ff, #cce0ff, #b3ccff)";

    // --- Gameplay ---
    /** Delay in milliseconds for the automatic falling of bricks. */
    public static final int FALL_DELAY_MS = 400; // milliseconds for falling speed
}
