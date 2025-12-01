package com.comp2042.logic.bricks;

import com.comp2042.logic.bricks.LBrick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LBrickTest {

    private LBrick lBrick;

    @BeforeEach
    void setUp() {
        lBrick = new LBrick();
    }

    @Test
    void testGetShapeMatrixReturnsFourRotations() {
        List<int[][]> shapes = lBrick.getShapeMatrix();
        assertEquals(4, shapes.size(),
                "LBrick must define exactly four rotation states (0, 90, 180, 270).");
    }

    @Test
    void testRotationMatricesAreDefinedCorrectly() {
        List<int[][]> shapes = lBrick.getShapeMatrix();

        // Verify 0° shape (T-shape layout)
        final int[][] EXPECTED_0_DEG = {
                {0, 0, 0, 0},
                {3, 3, 3, 0},
                {3, 0, 0, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(EXPECTED_0_DEG, shapes.get(0),
                "The 0 degree matrix must be defined correctly.");

        // Verify 90° shape (Vertical orientation)
        final int[][] EXPECTED_90_DEG = {
                {0, 3, 0, 0},
                {0, 3, 0, 0},
                {0, 3, 3, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(EXPECTED_90_DEG, shapes.get(1),
                "The 90 degree matrix must be defined correctly.");
    }

    @Test
    void testGetShapeMatrixReturnsDeepCopy() {
        // This is a CRITICAL test to ensure immutability via deep copy.
        List<int[][]> shapes1 = lBrick.getShapeMatrix();

        // Modify the first element of the first copy
        shapes1.get(0)[1][1] = 99;

        List<int[][]> shapes2 = lBrick.getShapeMatrix();

        // Verify that the second copy is unmodified
        assertEquals(3, shapes2.get(0)[1][1],
                "Modifying the returned list must not affect subsequent calls (deep copy failed).");
    }
}