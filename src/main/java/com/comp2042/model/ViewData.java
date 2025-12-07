package com.comp2042.model;

import com.comp2042.logic.board.MatrixOperations;

/**
 * A data class that encapsulates all information required by the view layer to render the game state.
 * This includes the current brick's data, its position, next bricks, ghost brick position, and held brick.
 */
public final class ViewData {

    /** The 2D integer array representing the shape of the current active brick. */
    private final int[][] brickData;
    /** The x-coordinate (column) of the current active brick's top-left corner on the board. */
    private final int xPosition;
    /** The y-coordinate (row) of the current active brick's top-left corner on the board. */
    private final int yPosition;
    // Legacy single next preview (first element of nextBricksData)
    /**
     * The 2D integer array representing the shape of the next brick.
     * This is maintained for compatibility with older rendering logic.
     */
    private final int[][] nextBrickData;
    // New: support multiple next previews
    /** A 3D integer array where each 2D array represents the shape of an upcoming brick. */
    private final int[][][] nextBricksData;
    /** The x-coordinate (column) of the ghost brick's top-left corner on the board. */
    private final int ghostXPosition;
    /** The y-coordinate (row) of the ghost brick's top-left corner on the board. */
    private final int ghostYPosition;
    // Hold preview
    /** The 2D integer array representing the shape of the held brick. */
    private final int[][] heldBrickData;

    // Legacy constructor: single next preview (no hold)
    /**
     * Constructs a new ViewData object using a single next brick preview (legacy constructor).
     * @param brickData The 2D array of the current active brick's shape.
     * @param xPosition The x-coordinate of the current active brick.
     * @param yPosition The y-coordinate of the current active brick.
     * @param nextBrickData The 2D array of the next brick's shape.
     * @param ghostXPosition The x-coordinate of the ghost brick.
     * @param ghostYPosition The y-coordinate of the ghost brick.
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData,
                    int ghostXPosition, int ghostYPosition) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.nextBricksData = new int[][][] { MatrixOperations.copy(nextBrickData) };
        this.ghostXPosition = ghostXPosition;
        this.ghostYPosition = ghostYPosition;
        this.heldBrickData = new int[0][0];
    }

    // New constructor: multiple next previews + hold preview
    /**
     * Constructs a new ViewData object with support for multiple next brick previews and a held brick.
     * @param brickData The 2D array of the current active brick's shape.
     * @param xPosition The x-coordinate of the current active brick.
     * @param yPosition The y-coordinate of the current active brick.
     * @param nextBricksData A 3D array where each 2D array is the shape of an upcoming brick.
     * @param ghostXPosition The x-coordinate of the ghost brick.
     * @param ghostYPosition The y-coordinate of the ghost brick.
     * @param heldBrickData The 2D array of the held brick's shape, or null if no brick is held.
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][][] nextBricksData,
                    int ghostXPosition, int ghostYPosition, int[][] heldBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBricksData = copy3D(nextBricksData);
        // keep legacy first for compatibility
        this.nextBrickData = (nextBricksData != null && nextBricksData.length > 0)
                ? MatrixOperations.copy(nextBricksData[0]) : new int[0][0];
        this.ghostXPosition = ghostXPosition;
        this.ghostYPosition = ghostYPosition;
        this.heldBrickData = heldBrickData != null ? MatrixOperations.copy(heldBrickData) : new int[0][0];
    }

    /**
     * Creates a deep copy of a 3D integer array.
     * @param src The source 3D array to copy.
     * @return A deep copy of the source array.
     */
    private static int[][][] copy3D(int[][][] src) {
        if (src == null) return new int[0][][];
        int[][][] out = new int[src.length][][];
        for (int i = 0; i < src.length; i++) {
            out[i] = MatrixOperations.copy(src[i]);
        }
        return out;
    }

    /**
     * Returns a deep copy of the 2D integer array representing the current active brick's shape.
     * @return A deep copy of the brick data.
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Returns the x-coordinate (column) of the current active brick's top-left corner.
     * @return The x-coordinate.
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Returns the y-coordinate (row) of the current active brick's top-left corner.
     * @return The y-coordinate.
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Returns the x-coordinate (column) of the ghost brick's top-left corner.
     * @return The x-coordinate of the ghost brick.
     */
    public int getGhostXPosition() {
        return ghostXPosition;
    }

    /**
     * Returns the y-coordinate (row) of the ghost brick's top-left corner.
     * @return The y-coordinate of the ghost brick.
     */
    public int getGhostYPosition() {
        return ghostYPosition;
    }

    // Legacy single next
    /**
     * Returns a deep copy of the 2D integer array representing the next brick's shape (legacy).
     * @return A deep copy of the next brick data.
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    // New: multiple next
    /**
     * Returns a deep copy of the 3D integer array representing the upcoming bricks' shapes.
     * @return A deep copy of the next bricks data.
     */
    public int[][][] getNextBricksData() {
        return copy3D(nextBricksData);
    }

    // Hold preview
    /**
     * Returns a deep copy of the 2D integer array representing the held brick's shape.
     * @return A deep copy of the held brick data, or an empty 2D array if no brick is held.
     */
    public int[][] getHeldBrickData() {
        return MatrixOperations.copy(heldBrickData);
    }
}