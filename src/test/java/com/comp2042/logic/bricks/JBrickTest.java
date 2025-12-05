package com.comp2042.logic.bricks;

import com.comp2042.logic.board.MatrixOperations;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class JBrickTest {

    private JBrick jBrick = new JBrick();

    @Test
    void testFourDistinctRotationStates() {
        List<int[][]> shapes = jBrick.getShapeMatrix();
        assertEquals(4, shapes.size(),
                "JBrick must define exactly four distinct rotation states.");
    }

    @Test
    void testZeroDegreeShape() {
        List<int[][]> shapes = jBrick.getShapeMatrix();
        final int[][] EXPECTED_0_DEG = {
                {0, 0, 0, 0},
                {2, 2, 2, 0},
                {0, 0, 2, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(EXPECTED_0_DEG, shapes.get(0),
                "The 0 degree matrix for JBrick must be correct.");
    }

    @Test
    void testNinetyDegreeShape() {
        List<int[][]> shapes = jBrick.getShapeMatrix();
        final int[][] EXPECTED_90_DEG = {
                {0, 2, 0, 0},
                {0, 2, 0, 0},
                {2, 2, 0, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(EXPECTED_90_DEG, shapes.get(1),
                "The 90 degree matrix for JBrick must be correct.");
    }

    @Test
    void testOneEightyDegreeShape() {
        List<int[][]> shapes = jBrick.getShapeMatrix();
        final int[][] EXPECTED_180_DEG = {
                {2, 0, 0, 0},
                {2, 2, 2, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(EXPECTED_180_DEG, shapes.get(2),
                "The 180 degree matrix for JBrick must be correct.");
    }

    @Test
    void testTwoSeventyDegreeShape() {
        List<int[][]> shapes = jBrick.getShapeMatrix();
        final int[][] EXPECTED_270_DEG = {
                {0, 2, 2, 0},
                {0, 2, 0, 0},
                {0, 2, 0, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(EXPECTED_270_DEG, shapes.get(3),
                "The 270 degree matrix for JBrick must be correct.");
    }
}