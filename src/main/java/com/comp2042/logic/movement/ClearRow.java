package com.comp2042.logic.movement;

import com.comp2042.logic.board.MatrixOperations;


import java.util.Arrays;

/**
 * Represents the result of clearing rows on the game board,
 * including the number of lines removed, the new board matrix,
 * any score bonus, and the indices of the cleared rows.
 */
public final class ClearRow {

    /** The number of lines removed in this clear operation. */
    private final int linesRemoved;
    /** The updated game board matrix after the rows have been cleared. */
    private final int[][] newMatrix;
    /** The score bonus awarded for clearing the rows. */
    private final int scoreBonus;
    /** An array of indices of the rows that were cleared. */
    private final int[] clearedRows;

    /**
     * Constructs a new ClearRow object.
     * @param linesRemoved The number of lines removed.
     * @param newMatrix The new game board matrix after clearing.
     * @param scoreBonus The score bonus gained from clearing lines.
     * @param clearedRows An array of the indices of the rows that were cleared.
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, int[] clearedRows) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.clearedRows = clearedRows;
    }

    /**
     * Returns the number of lines removed in this clear operation.
     * @return The number of lines removed.
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Returns a copy of the updated game board matrix after the rows have been cleared.
     * @return A 2D integer array representing the new game board matrix.
     */
    public int[][] getNewBoard() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Returns the score bonus awarded for clearing the rows.
     * @return The score bonus.
     */
    public int getScoreBonus() {
        return scoreBonus;
    }

    /**
     * Returns an array of the indices of the rows that were cleared.
     * @return An integer array containing the indices of the cleared rows.
     */
    public int[] getClearedRows() {
        return Arrays.copyOf(clearedRows, clearedRows.length);
    }
}
