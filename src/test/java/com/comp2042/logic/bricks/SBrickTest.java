package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SBrickTest {

    private SBrick sBrick = new SBrick();

    @Test
    void testTwoDistinctRotationStates() {
        List<int[][]> shapes = sBrick.getShapeMatrix();
        assertEquals(2, shapes.size(),
                "SBrick must define exactly two distinct rotation states.");
    }

    @Test
    void testZeroDegreeShape() {
        List<int[][]> shapes = sBrick.getShapeMatrix();
        final int[][] EXPECTED_0_DEG = {
                {0, 0, 0, 0},
                {0, 5, 5, 0},
                {5, 5, 0, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(EXPECTED_0_DEG, shapes.get(0),
                "The 0 degree matrix for SBrick must be correct.");
    }

    @Test
    void testNinetyDegreeShape() {
        List<int[][]> shapes = sBrick.getShapeMatrix();
        final int[][] EXPECTED_90_DEG = {
                {5, 0, 0, 0},
                {5, 5, 0, 0},
                {0, 5, 0, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(EXPECTED_90_DEG, shapes.get(1),
                "The 90 degree matrix for SBrick must be correct.");
    }
}