package com.comp2042.model;

import com.comp2042.logic.bricks.IBrick;
import com.comp2042.logic.bricks.JBrick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewDataTest {

    private int[][] sampleBrickData;
    private int sampleXPosition;
    private int sampleYPosition;
    private int[][][] sampleNextBricksData;
    private int sampleGhostXPosition;
    private int sampleGhostYPosition;
    private int[][] sampleHeldBrickData;
    private ViewData viewData;

    @BeforeEach
    void setUp() {
        sampleBrickData = new IBrick().getShapeMatrix().get(0);
        sampleXPosition = 5;
        sampleYPosition = 10;
        sampleNextBricksData = new int[][][]{
                new JBrick().getShapeMatrix().get(0),
                new IBrick().getShapeMatrix().get(0)
        };
        sampleGhostXPosition = 5;
        sampleGhostYPosition = 18;
        sampleHeldBrickData = new JBrick().getShapeMatrix().get(0);

        viewData = new ViewData(
                sampleBrickData,
                sampleXPosition,
                sampleYPosition,
                sampleNextBricksData,
                sampleGhostXPosition,
                sampleGhostYPosition,
                sampleHeldBrickData
        );
    }

    @Test
    void testViewDataCreation() {
        assertNotNull(viewData);
        assertArrayEquals(sampleBrickData, viewData.getBrickData());
        assertEquals(sampleXPosition, viewData.getxPosition());
        assertEquals(sampleYPosition, viewData.getyPosition());
        assertArrayEquals(sampleNextBricksData[0], viewData.getNextBrickData()); // Legacy single next
        assertArrayEquals(sampleNextBricksData, viewData.getNextBricksData()); // New multiple next
        assertEquals(sampleGhostXPosition, viewData.getGhostXPosition());
        assertEquals(sampleGhostYPosition, viewData.getGhostYPosition());
        assertArrayEquals(sampleHeldBrickData, viewData.getHeldBrickData());
    }

    @Test
    void testGetBrickData() {
        assertArrayEquals(sampleBrickData, viewData.getBrickData());
    }

    @Test
    void testGetXPosition() {
        assertEquals(sampleXPosition, viewData.getxPosition());
    }

    @Test
    void testGetYPosition() {
        assertEquals(sampleYPosition, viewData.getyPosition());
    }

    @Test
    void testGetNextBrickData() {
        assertArrayEquals(sampleNextBricksData[0], viewData.getNextBrickData());
    }

    @Test
    void testGetNextBricksData() {
        assertArrayEquals(sampleNextBricksData, viewData.getNextBricksData());
    }

    @Test
    void testGetGhostXPosition() {
        assertEquals(sampleGhostXPosition, viewData.getGhostXPosition());
    }

    @Test
    void testGetGhostYPosition() {
        assertEquals(sampleGhostYPosition, viewData.getGhostYPosition());
    }

    @Test
    void testGetHeldBrickData() {
        assertArrayEquals(sampleHeldBrickData, viewData.getHeldBrickData());
    }
}
