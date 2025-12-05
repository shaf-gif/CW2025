package com.comp2042.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConstantsTest {

    @Test
    void testBoardDimensions() {
        assertEquals(10, Constants.BOARD_WIDTH, "BOARD_WIDTH should be 10");
        assertEquals(25, Constants.BOARD_HEIGHT, "BOARD_HEIGHT should be 25");
        assertEquals(2, Constants.HIDDEN_ROWS, "HIDDEN_ROWS should be 2");
    }

    @Test
    void testTileSizes() {
        assertEquals(20, Constants.TILE_SIZE, "TILE_SIZE should be 20");
        assertEquals(12, Constants.PREVIEW_TILE_SIZE, "PREVIEW_TILE_SIZE should be 12");
        assertEquals(1, Constants.GRID_GAP, "GRID_GAP should be 1");
        assertEquals(9, Constants.TILE_ROUNDING, "TILE_ROUNDING should be 9");
    }

    @Test
    void testUISpacingAndRounding() {
        assertEquals(12, Constants.UI_SPACING, "UI_SPACING should be 12");
        assertEquals(10, Constants.UI_CORNER_RADIUS, "UI_CORNER_RADIUS should be 10");
    }

    @Test
    void testPanelOffsetY() {
        assertEquals(-42, Constants.PANEL_OFFSET_Y, "PANEL_OFFSET_Y should be -42");
    }

    @Test
    void testGhostPieceAlpha() {
        assertEquals(0.25, Constants.GHOST_ALPHA, 0.001, "GHOST_ALPHA should be 0.25");
    }

    @Test
    void testShowGrid() {
        assertTrue(Constants.SHOW_GRID, "SHOW_GRID should be true");
    }

    @Test
    void testVisualThemeColors() {
        assertEquals("#00E5FF", Constants.NEON_COLOR, "NEON_COLOR should be #00E5FF");
        assertEquals(2, Constants.NEON_BORDER_WIDTH, "NEON_BORDER_WIDTH should be 2");
        assertEquals(16, Constants.NEON_GLOW_RADIUS, "NEON_GLOW_RADIUS should be 16");
        assertEquals(0.6, Constants.OVERLAY_OPACITY, 0.001, "OVERLAY_OPACITY should be 0.6");
        assertEquals(350, Constants.ANIM_DURATION_MS, "ANIM_DURATION_MS should be 350");
        assertEquals(12, Constants.OVERLAY_CORNER_RADIUS, "OVERLAY_CORNER_RADIUS should be 12");
        assertEquals("linear-gradient(to bottom, #0f2027, #203a43, #2c5364)", Constants.BG_GRADIENT_DARK, "BG_GRADIENT_DARK should be correct");
        assertEquals("linear-gradient(to bottom, #e6f2ff, #cce0ff, #b3ccff)", Constants.BG_GRADIENT_LIGHT, "BG_GRADIENT_LIGHT should be correct");
    }

    @Test
    void testGameplayConstants() {
        assertEquals(400, Constants.FALL_DELAY_MS, "FALL_DELAY_MS should be 400");
    }

    @Test
    void testPrivateConstructor() {
        // This test simply ensures the private constructor is not accessible, preventing instantiation.
        // It's mainly for code coverage and demonstrating non-instantiability.
        try {
            Constants.class.getDeclaredConstructor().newInstance();
            fail("Should have thrown an exception due to private constructor");
        } catch (Exception e) {
            // Expected exception, e.g., IllegalAccessException or InvocationTargetException
            assertTrue(e instanceof java.lang.reflect.InvocationTargetException || e instanceof IllegalAccessException);
        }
    }
}
