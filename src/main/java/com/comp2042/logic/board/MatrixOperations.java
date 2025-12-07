package com.comp2042.logic.board;

import com.comp2042.logic.movement.ClearRow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides static utility methods for performing operations on 2D integer matrices,
 * primarily used for game board and brick manipulations in Tetris.
 */
public final class MatrixOperations {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private MatrixOperations() {}

    /**
     * Checks if a brick intersects with the given matrix (game board) at a specific position.
     * Intersection can be due to hitting the boundaries of the matrix or colliding with existing blocks.
     *
     * @param matrix the 2D integer array representing the game board.
     * @param brick the 2D integer array representing the brick's shape.
     * @param x the x-coordinate (column) of the brick's top-left corner on the matrix.
     * @param y the y-coordinate (row) of the brick's top-left corner on the matrix.
     * @return true if an intersection occurs, false otherwise.
     */
    public static boolean intersect(final int[][] matrix,
                                    final int[][] brick,
                                    int x, int y) {

        for (int row = 0; row < brick.length; row++) {
            for (int col = 0; col < brick[row].length; col++) {

                if (brick[row][col] == 0) continue;

                int targetRow = y + row;
                int targetCol = x + col;

                // Out of bounds → collision
                if (targetRow < 0 ||
                        targetRow >= matrix.length ||
                        targetCol < 0 ||
                        targetCol >= matrix[0].length) {
                    return true;
                }

                // Collision with background
                if (matrix[targetRow][targetCol] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Creates a deep copy of a 2D integer matrix.
     *
     * @param original the original 2D integer array to copy.
     * @return a new 2D integer array that is a deep copy of the original.
     */
    public static int[][] copy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int r = 0; r < original.length; r++) {
            copy[r] = original[r].clone();
        }
        return copy;
    }

    /**
     * Merges a brick into the game board at a specified position.
     * The brick's non-zero cells overwrite the corresponding cells in the board.
     *
     * @param board the 2D integer array representing the game board.
     * @param brick the 2D integer array representing the brick's shape.
     * @param x the x-coordinate (column) of the brick's top-left corner on the board.
     * @param y the y-coordinate (row) of the brick's top-left corner on the board.
     * @return a new 2D integer array representing the board after the merge.
     */
    public static int[][] merge(int[][] board,
                                int[][] brick,
                                int x, int y) {

        int[][] result = copy(board);

        for (int row = 0; row < brick.length; row++) {
            for (int col = 0; col < brick[row].length; col++) {

                if (brick[row][col] == 0) continue;

                int targetRow = y + row;
                int targetCol = x + col;

                result[targetRow][targetCol] = brick[row][col];
            }
        }

        return result;
    }

    /**
     * Checks the given matrix for full rows and removes them.
     * It also calculates the score bonus for cleared lines.
     *
     * @param matrix the 2D integer array representing the game board.
     * @return a ClearRow object containing information about removed lines, the new board, and score bonus.
     */
    public static ClearRow checkRemoving(final int[][] matrix) {

        List<Integer> cleared = new ArrayList<>();
        Deque<int[]> remaining = new ArrayDeque<>();

        for (int r = 0; r < matrix.length; r++) {

            boolean full = true;
            for (int c = 0; c < matrix[r].length; c++) {
                if (matrix[r][c] == 0) {
                    full = false;
                    break;
                }
            }

            if (full) {
                cleared.add(r);
            } else {
                remaining.add(matrix[r]);
            }
        }

        // Build new board (bottom to top)
        int[][] newBoard = new int[matrix.length][matrix[0].length];
        int writeRow = matrix.length - 1;

        while (!remaining.isEmpty()) {
            newBoard[writeRow--] = remaining.pollLast();
        }

        // Score bonus: 1 line = 50, 2 lines = 200, 3 lines = 450, 4 lines = 800
        int scoreBonus = 50 * cleared.size() * cleared.size();

        int[] clearedRowsArray = cleared.stream().mapToInt(i -> i).toArray();

        return new ClearRow(cleared.size(), newBoard, scoreBonus, clearedRowsArray);
    }

    /**
     * Creates a deep copy of a list of 2D integer matrices.
     * This is typically used for copying brick rotation matrices.
     *
     * @param list the list of 2D integer arrays to deep copy.
     * @return a new list containing deep copies of the original matrices.
     */
    /**
     * Creates a deep copy of a list of 2D integer matrices.
     * This is typically used for copying brick rotation matrices.
     *
     * @param list the list of 2D integer arrays to deep copy.
     * @return a new list containing deep copies of the original matrices.
     */
    public static List<int[][]> deepCopyList(List<int[][]> list) {
        return list.stream()
                .map(MatrixOperations::copy)
                .collect(Collectors.toList());
    }
}