package com.comp2042.logic.bricks; // Adjust package to match your structure

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class OBrickTest {

    private OBrick oBrick;

    @BeforeEach
    void setUp() {
        oBrick = new OBrick();
    }

    @Test
    void testSingleRotationState() {
        List<int[][]> shapes = oBrick.getShapeMatrix();
        assertEquals(1, shapes.size(),
                "OBrick must define exactly one rotation state.");
    }

    @Test
    void testZeroDegreeShapeIsCorrect() {
        List<int[][]> shapes = oBrick.getShapeMatrix();


        final int[][] EXPECTED_SHAPE = {
                {0, 0, 0, 0},
                {0, 4, 4, 0}, // Row 1: Contains the piece
                {0, 4, 4, 0}, // Row 2: Contains the piece
                {0, 0, 0, 0}
        };

        assertArrayEquals(EXPECTED_SHAPE, shapes.get(0),
                "The OBrick matrix must be a 2x2 square using the correct Brick ID (4).");
    }
}