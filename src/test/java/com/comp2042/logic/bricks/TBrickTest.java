package com.comp2042.logic.bricks; // Adjust package to match your structure

import com.comp2042.logic.bricks.TBrick;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TBrickTest {

    private TBrick tBrick = new TBrick();

    @Test
    void testFourDistinctRotationStates() {
        List<int[][]> shapes = tBrick.getShapeMatrix();
        assertEquals(4, shapes.size(),
                "TBrick must define exactly four distinct rotation states.");
    }

    @Test
    void testZeroDegreeShapeIsCorrect() {
        List<int[][]> shapes = tBrick.getShapeMatrix();

        // FIX: Match the production code exactly. The shape starts at Row 1 (index 1)
        // and is left-aligned on the grid.
        final int[][] EXPECTED_0_DEG = {
                {0, 0, 0, 0},
                {6, 6, 6, 0}, // Row 1: Horizontal bar
                {0, 6, 0, 0}, // Row 2: Stem
                {0, 0, 0, 0}
        };

        assertArrayEquals(EXPECTED_0_DEG, shapes.get(0),
                "The 0 degree matrix must match the TBrick's definition.");
    }

    @Test
    void testTwoSeventyDegreeShapeIsCorrect() { // Renamed test for clarity
        List<int[][]> shapes = tBrick.getShapeMatrix();

        // The test is currently checking index 3 (270 degrees)

        final int[][] EXPECTED_270_DEG = {
                {0, 6, 0, 0},
                {0, 6, 6, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        };

        // Check index 3
        assertArrayEquals(EXPECTED_270_DEG, shapes.get(3),
                "The 270 degree matrix must match the TBrick's definition.");
    }
}