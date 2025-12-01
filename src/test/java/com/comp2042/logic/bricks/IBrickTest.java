package com.comp2042.logic.bricks; // Adjust package to match your structure

import com.comp2042.logic.bricks.IBrick;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class IBrickTest {

    private IBrick iBrick = new IBrick();

    @Test
    void testTwoDistinctRotationStates() {
        List<int[][]> shapes = iBrick.getShapeMatrix();
        assertEquals(2, shapes.size(),
                "IBrick must define exactly two distinct rotation states (0/180 and 90/270).");
    }

    @Test
    void testZeroDegreeShapeIsHorizontal() {
        List<int[][]> shapes = iBrick.getShapeMatrix();


        final int[][] EXPECTED_0_DEG = {
                {0, 0, 0, 0},
                {1, 1, 1, 1}, // Horizontal line in Row 1
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(EXPECTED_0_DEG, shapes.get(0),
                "The 0 degree matrix must be a horizontal line in Row 1.");
    }

    @Test
    void testNinetyDegreeShapeIsVertical() {
        List<int[][]> shapes = iBrick.getShapeMatrix();


        final int[][] EXPECTED_90_DEG = {
                {0, 1, 0, 0}, // Starts at Row 0
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        };
        assertArrayEquals(EXPECTED_90_DEG, shapes.get(1),
                "The 90 degree matrix must be a vertical line.");
    }
}