package com.comp2042.logic;

public final class Constants {

    private Constants() {} // Prevent instantiation

    // --- Board Dimensions ---
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 25;
    public static final int HIDDEN_ROWS = 2;

    // --- Rendering / UI ---
    public static final int TILE_SIZE = 20;      // Tile size for bricks (main board)
    public static final int PREVIEW_TILE_SIZE = 14; // Tile size for next-piece previews (smaller boxes)
    public static final int GRID_GAP = 1;        // Gap between tiles in grid
    public static final int TILE_ROUNDING = 9;   // Corner radius for tiles

    // Y-offset for brickPanel & ghostPanel so falling brick aligns perfectly
    public static final int PANEL_OFFSET_Y = -42;

    // --- Ghost Piece ---
    public static final double GHOST_ALPHA = 0.25;   // Transparency level

    // --- Gameplay ---
    public static final int FALL_DELAY_MS = 400; // milliseconds for falling speed
}
