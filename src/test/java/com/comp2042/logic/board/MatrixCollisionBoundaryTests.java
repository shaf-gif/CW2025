package com.comp2042.logic.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatrixCollisionBoundaryTests {

    @Test
    void testIntersectWallLeft() {
        // Piece 'I' at x=0, trying to move left to x=-1
        int[][] matrix = new int[20][10];
        int[][] brick = {{1}, {1}, {1}, {1}};

        // Position x=-1 is outside the left wall
        assertTrue(MatrixOperations.intersect(matrix, brick, -1, 0),
                "Should intersect when position is outside the left wall.");
    }

    @Test
    void testIntersectWallRight() {
        // Piece 'I' (4 wide) at x=6, trying to move right. Max index is 9 (10 columns).
        // x=7 means the piece occupies columns 7, 8, 9, 10 (index 10 is out of bounds).
        int[][] matrix = new int[20][10];
        int[][] brick = {{1, 1, 1, 1}};

        assertTrue(MatrixOperations.intersect(matrix, brick, 7, 0),
                "Should intersect when piece extends outside the right wall.");
    }

    @Test
    void testIntersectFloor() {
        // Matrix size is 20 rows (0-19). Piece 'I' (4 tall) at y=17. 17 + 4 = 21.
        int[][] matrix = new int[20][10];
        int[][] brick = {{1}, {1}, {1}, {1}};

        // Position y=17 is outside the floor boundary
        assertTrue(MatrixOperations.intersect(matrix, brick, 0, 17),
                "Should intersect when piece extends past the floor boundary (index 19).");
    }

    @Test
    void testIntersectInteriorCollision() {
        // Setup a piece locked near the middle
        int[][] matrix = new int[20][10];
        matrix[18][4] = 7; // Locked block at R18, C4

        // Brick 'O' (2x2) attempting to move right into the locked block
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        // Place the brick at R18, C3. The brick tile at (0, 1) relative to its matrix
        // overlaps matrix[18][4].
        assertTrue(MatrixOperations.intersect(matrix, brick, 3, 18),
                "Should intersect when a brick attempts to overlap an existing block.");
    }

    @Test
    void testIntersectTopOutGameOver() {
        // Simulates a game over scenario. Assuming Constants.HIDDEN_ROWS is defined.
        int[][] matrix = new int[20][10];
        // Fill a tile in the hidden area (e.g., R1, C5)
        matrix[1][5] = 9;

        // A piece 'O' (2x2) attempts to spawn at default position (R0, C4)
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        // Collision check is at matrix[0+1][4+1] = matrix[1][5]
        assertTrue(MatrixOperations.intersect(matrix, brick, 4, 0),
                "Should intersect when a piece attempts to spawn into a block in the hidden rows.");
    }
}