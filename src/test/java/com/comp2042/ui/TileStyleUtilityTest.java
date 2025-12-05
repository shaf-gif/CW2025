package com.comp2042.ui;

import com.comp2042.JavaFxTestBase;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TileStyleUtilityTest extends JavaFxTestBase {

    private Rectangle rect;

    @BeforeEach
    public void setUp() {
        rect = new Rectangle();
    }

    @Test
    public void testApplyTileStyle_WithValue3() {
        TileStyleUtility.applyTileStyle(rect, 3);
        assertTrue(rect.getStyleClass().contains("tile"));
        assertTrue(rect.getStyleClass().contains("tile-3"));
    }

    @Test
    public void testApplyTileStyle_WithValue0() {
        TileStyleUtility.applyTileStyle(rect, 0);
        assertTrue(rect.getStyleClass().contains("tile"));
        assertTrue(rect.getStyleClass().contains("tile-0"));
        assertEquals(1.0, rect.getOpacity());
    }

    @Test
    public void testApplyTileStyle_WithInvalidHighValue() {
        TileStyleUtility.applyTileStyle(rect, 10);
        assertTrue(rect.getStyleClass().contains("tile"));
        assertTrue(rect.getStyleClass().contains("tile-7"));
    }

    @Test
    public void testApplyTileStyle_WithInvalidLowValue() {
        TileStyleUtility.applyTileStyle(rect, -5);
        assertTrue(rect.getStyleClass().contains("tile"));
        assertTrue(rect.getStyleClass().contains("tile-0"));
    }

    @Test
    public void testApplyTileStyle_StyleRemainsAfterUpdate() {
        TileStyleUtility.applyTileStyle(rect, 4);
        assertTrue(rect.getStyleClass().contains("tile-4"));

        TileStyleUtility.applyTileStyle(rect, 2);
        assertFalse(rect.getStyleClass().contains("tile-4"));
        assertTrue(rect.getStyleClass().contains("tile-2"));
    }
}
