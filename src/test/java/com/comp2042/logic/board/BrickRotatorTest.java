package com.comp2042.logic.board;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.model.NextShapeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class BrickRotatorTest {

    private BrickRotator brickRotator;

    @Mock
    private Brick mockBrick;

    private List<int[][]> mockRotations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        brickRotator = new BrickRotator();

        // Define some mock rotation shapes
        int[][] shape0 = {{1}};
        int[][] shape1 = {{2}};
        int[][] shape2 = {{3}};
        mockRotations = Arrays.asList(shape0, shape1, shape2);

        // Configure the mock brick to return these shapes
        when(mockBrick.getShapeMatrix()).thenReturn(mockRotations);

        // Set the brick on the rotator
        brickRotator.setBrick(mockBrick);
    }

    @Test
    void testSetBrickInitializesCorrectly() {
        // currentRotationIndex should be 0 after setBrick
        assertArrayEquals(mockRotations.get(0), brickRotator.getCurrentShape(),
                "Initial shape after setBrick should be the first rotation.");
    }

    @Test
    void testGetCurrentShapeReturnsCorrectShape() {
        // After setup, current should be shape0
        assertArrayEquals(mockRotations.get(0), brickRotator.getCurrentShape(),
                "getCurrentShape should return shape0 initially.");

        // Simulate a rotation by setting current index
        brickRotator.setCurrentShape(1);
        assertArrayEquals(mockRotations.get(1), brickRotator.getCurrentShape(),
                "getCurrentShape should return shape1 after setting index to 1.");
    }

    @Test
    void testGetNextShapePreviewsCorrectly() {
        // Initial state: current is 0, next should be 1
        NextShapeInfo nextShapeInfo = brickRotator.getNextShape();
        assertArrayEquals(mockRotations.get(1), nextShapeInfo.getShape(),
                "getNextShape should preview shape1.");
        assertEquals(1, nextShapeInfo.getPosition(),
                "getNextShape should indicate next rotation index is 1.");

        // Change current to 1, next should be 2
        brickRotator.setCurrentShape(1);
        nextShapeInfo = brickRotator.getNextShape();
        assertArrayEquals(mockRotations.get(2), nextShapeInfo.getShape(),
                "getNextShape should preview shape2.");
        assertEquals(2, nextShapeInfo.getPosition(),
                "getNextShape should indicate next rotation index is 2.");

        // Change current to 2 (last), next should wrap around to 0
        brickRotator.setCurrentShape(2);
        nextShapeInfo = brickRotator.getNextShape();
        assertArrayEquals(mockRotations.get(0), nextShapeInfo.getShape(),
                "getNextShape should preview shape0 (wrapped around).");
        assertEquals(0, nextShapeInfo.getPosition(),
                "getNextShape should indicate next rotation index is 0 (wrapped around).");
    }

    @Test
    void testSetCurrentShapeUpdatesIndex() {
        brickRotator.setCurrentShape(1);
        assertArrayEquals(mockRotations.get(1), brickRotator.getCurrentShape(),
                "setCurrentShape should update the current shape to index 1.");

        brickRotator.setCurrentShape(mockRotations.size() - 1); // Last index
        assertArrayEquals(mockRotations.get(mockRotations.size() - 1), brickRotator.getCurrentShape(),
                "setCurrentShape should update to the last index.");

        // Test an invalid index (though class doesn't prevent this, behavior should be predictable)
        // Note: For a robust system, input validation might be added in BrickRotator
        // For this test, we assume valid input as per current implementation
        assertThrows(IndexOutOfBoundsException.class, () -> brickRotator.setCurrentShape(mockRotations.size()),
                "Setting an out-of-bounds index should throw IndexOutOfBoundsException.");
    }
}
