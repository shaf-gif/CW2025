package com.comp2042.logic;

import com.comp2042.logic.movement.ClearRow;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearRowTest {


    private final int[][] DUMMY_MATRIX = {
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {1, 1, 1, 1}
    };


    private final int[][] EMPTY_MATRIX = new int[20][10];


    // --- Test Cases ---

    @Test
    void testZeroLinesRemoved() {
        final int lines = 0;
        final int bonus = 0;

        ClearRow clearRow = new ClearRow(lines, EMPTY_MATRIX, bonus, new int[0]);

        assertEquals(lines, clearRow.getLinesRemoved(),
                "Lines removed should be 0 for no clear.");
        assertEquals(bonus, clearRow.getScoreBonus(),
                "Score bonus should be 0 for no clear.");
        assertArrayEquals(EMPTY_MATRIX, clearRow.getNewBoard(),
                "New board should match the input matrix.");
    }

    @Test
    void testSingleLineClear() {
        final int lines = 1;
        final int bonus = 100;

        ClearRow clearRow = new ClearRow(lines, DUMMY_MATRIX, bonus, new int[]{2});

        assertEquals(1, clearRow.getLinesRemoved(),
                "Lines removed should be 1.");
        assertEquals(100, clearRow.getScoreBonus(),
                "Score bonus should be 100.");
        assertArrayEquals(DUMMY_MATRIX, clearRow.getNewBoard(),
                "New board state should be correctly stored.");
    }

    @Test
    void testTetrisClear() {
        final int lines = 4;
        final int bonus = 800;

        ClearRow clearRow = new ClearRow(lines, DUMMY_MATRIX, bonus, new int[]{10, 11, 12, 13});

        assertEquals(4, clearRow.getLinesRemoved(),
                "Lines removed should be 4.");
        assertEquals(800, clearRow.getScoreBonus(),
                "Score bonus should be 800.");
    }

    @Test
    void testNewBoardReturnsCopy() {
        // Test that getNewBoard() returns a defensive copy, not the original reference.
        // This is crucial because your getNewBoard() method uses MatrixOperations.copy().

        final int lines = 1;
        final int bonus = 100;

        ClearRow clearRow = new ClearRow(lines, DUMMY_MATRIX, bonus, new int[]{2});
        int[][] board1 = clearRow.getNewBoard();


        board1[0][0] = 99;

        int[][] board2 = clearRow.getNewBoard();


        assertNotSame(board1, board2,
                "getNewBoard must return a new array instance (defensive copy).");

        // Also assert that the original data is unchanged
        assertNotEquals(99, board2[0][0],
                "The second copy should not contain the modification.");
    }
}