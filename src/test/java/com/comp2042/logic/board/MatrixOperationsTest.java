package com.comp2042.logic;

import com.comp2042.logic.board.MatrixOperations;
import com.comp2042.logic.movement.ClearRow;
import java.util.List;
import java.util.ArrayList;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MatrixOperationsTest {

    // Test for 'intersect' method
    @Test
    void testIntersect_noCollision() {
        int[][] matrix = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        // No collision when the brick is within bounds and on an empty matrix
        assertFalse(MatrixOperations.intersect(matrix, brick, 0, 0));
    }

    @Test
    void testIntersect_collision() {
        int[][] matrix = {
                {0, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        // Collision because the brick overlaps with an already filled position
        assertTrue(MatrixOperations.intersect(matrix, brick, 0, 0));
    }

    // Test for 'copy' method
    @Test
    void testCopy() {
        int[][] matrix = {
                {1, 1, 0},
                {0, 1, 1},
                {1, 0, 0}
        };

        int[][] copiedMatrix = MatrixOperations.copy(matrix);

        // Verify that the copied matrix is a deep copy
        assertNotSame(matrix, copiedMatrix); // Ensure they are different objects
        assertArrayEquals(matrix, copiedMatrix); // Ensure the content is the same
    }

    // Test for 'merge' method
    @Test
    void testMerge() {
        int[][] board = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        int[][] mergedBoard = MatrixOperations.merge(board, brick, 1, 1);

        int[][] expectedBoard = {
                {0, 0, 0, 0},
                {0, 1, 1, 0},
                {0, 1, 1, 0},
                {0, 0, 0, 0}
        };

        // Check if the brick is correctly merged into the board
        assertArrayEquals(expectedBoard, mergedBoard);
    }

    // Test for 'checkRemoving' method
    @Test
    void testCheckRemoving_noFullRows() {
        int[][] matrix = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        // No full rows, score bonus should be 0 and matrix should remain the same
        assertEquals(0, result.getLinesRemoved());
        assertArrayEquals(matrix, result.getNewBoard());
        assertEquals(0, result.getScoreBonus());
    }

    @Test
    void testCheckRemoving_fullRow() {
        int[][] matrix = {
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        // 1 full row removed, score bonus should be 50
        assertEquals(1, result.getLinesRemoved());
        assertArrayEquals(new int[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        }, result.getNewBoard());
        assertEquals(50, result.getScoreBonus());
    }

    // Test for 'deepCopyList' method
    @Test
    void testDeepCopyList() {
        List<int[][]> matrices = new ArrayList<>();
        matrices.add(new int[][]{{1, 1}, {1, 0}});
        matrices.add(new int[][]{{0, 1}, {1, 1}});

        List<int[][]> copiedList = MatrixOperations.deepCopyList(matrices);

        // Ensure the copied list is a deep copy
        assertNotSame(matrices, copiedList);
        assertArrayEquals(matrices.get(0), copiedList.get(0));
        assertArrayEquals(matrices.get(1), copiedList.get(1));

        // Ensure modifying the copied list does not affect the original
        copiedList.get(0)[0][0] = 99;
        assertNotEquals(matrices.get(0)[0][0], copiedList.get(0)[0][0]);
    }
}

