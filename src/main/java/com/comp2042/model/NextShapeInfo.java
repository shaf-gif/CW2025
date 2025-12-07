package com.comp2042.model;

import com.comp2042.logic.board.MatrixOperations;

/**
 * A data class representing information about the next shape in a brick's rotation sequence.
 * It contains the 2D matrix of the shape and its corresponding rotation position index.
 */
public final class NextShapeInfo {

    /** The 2D integer array representing the shape of the brick. */
    private final int[][] shape;
    /** The index of the rotation for this shape. */
    private final int position;

    /**
     * Constructs a new NextShapeInfo object.
     * @param shape The 2D integer array representing the shape.
     * @param position The rotation index of the shape.
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Returns a deep copy of the 2D integer array representing the shape.
     * @return A deep copy of the shape matrix.
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Returns the rotation position index of the shape.
     * @return The integer representing the rotation position.
     */
    public int getPosition() {
        return position;
    }
}
