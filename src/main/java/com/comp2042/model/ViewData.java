package com.comp2042.model;

import com.comp2042.logic.board.MatrixOperations;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    // Legacy single next preview (first element of nextBricksData)
    private final int[][] nextBrickData;
    // New: support multiple next previews
    private final int[][][] nextBricksData;
    private final int ghostXPosition;
    private final int ghostYPosition;
    // Hold preview
    private final int[][] heldBrickData;

    // Legacy constructor: single next preview (no hold)
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

    private static int[][][] copy3D(int[][][] src) {
        if (src == null) return new int[0][][];
        int[][][] out = new int[src.length][][];
        for (int i = 0; i < src.length; i++) {
            out[i] = MatrixOperations.copy(src[i]);
        }
        return out;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int getGhostXPosition() {
        return ghostXPosition;
    }

    public int getGhostYPosition() {
        return ghostYPosition;
    }

    // Legacy single next
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    // New: multiple next
    public int[][][] getNextBricksData() {
        return copy3D(nextBricksData);
    }

    // Hold preview
    public int[][] getHeldBrickData() {
        return MatrixOperations.copy(heldBrickData);
    }
}