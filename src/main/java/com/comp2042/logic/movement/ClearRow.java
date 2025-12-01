package com.comp2042.logic.movement;

import com.comp2042.logic.board.MatrixOperations;


import java.util.Arrays;

public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;
    private final int[] clearedRows;

    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, int[] clearedRows) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.clearedRows = clearedRows;
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public int[][] getNewBoard() {
        return MatrixOperations.copy(newMatrix);
    }

    public int getScoreBonus() {
        return scoreBonus;
    }

    public int[] getClearedRows() {
        return Arrays.copyOf(clearedRows, clearedRows.length);
    }
}
