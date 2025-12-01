package com.comp2042.logic.board;

import com.comp2042.logic.movement.ClearRow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public final class MatrixOperations {

    private MatrixOperations() {}

    // ----------------------------------------------------------
    // COLLISION CHECK
    // ----------------------------------------------------------
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

    // ----------------------------------------------------------
    // COPY MATRIX
    // ----------------------------------------------------------
    public static int[][] copy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int r = 0; r < original.length; r++) {
            copy[r] = original[r].clone();
        }
        return copy;
    }

    // ----------------------------------------------------------
    // MERGE BRICK INTO BOARD
    // ----------------------------------------------------------
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

    // ----------------------------------------------------------
    // REMOVE FULL ROWS
    // ----------------------------------------------------------
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

    // ----------------------------------------------------------
    // DEEP COPY LIST OF MATRICES (USED BY BRICK ROTATIONS)
    // ----------------------------------------------------------
    public static List<int[][]> deepCopyList(List<int[][]> list) {
        return list.stream()
                .map(MatrixOperations::copy)
                .collect(Collectors.toList());
    }
}