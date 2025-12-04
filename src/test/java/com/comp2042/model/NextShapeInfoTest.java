package com.comp2042.model;

import com.comp2042.logic.bricks.IBrick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NextShapeInfoTest {

    private int[][] sampleShape;
    private int samplePosition;
    private NextShapeInfo nextShapeInfo;

    @BeforeEach
    void setUp() {
        sampleShape = new IBrick().getShapeMatrix().get(0); // Using IBrick's first shape for testing
        samplePosition = 0;
        nextShapeInfo = new NextShapeInfo(sampleShape, samplePosition);
    }

    @Test
    void testNextShapeInfoCreation() {
        assertNotNull(nextShapeInfo);
        assertArrayEquals(sampleShape, nextShapeInfo.getShape());
        assertEquals(samplePosition, nextShapeInfo.getPosition());
    }

    @Test
    void testGetShape() {
        assertArrayEquals(sampleShape, nextShapeInfo.getShape());
    }

    @Test
    void testGetPosition() {
        assertEquals(samplePosition, nextShapeInfo.getPosition());
    }
}
