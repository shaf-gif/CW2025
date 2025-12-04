package com.comp2042.logic.scoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ScoreTest {

    private Score scoreManager;

    @BeforeEach
    void setUp() {
        scoreManager = new Score();
    }

    @Test
    void testInitialState() {
        assertEquals(0, scoreManager.scoreProperty().get(), "Initial score should be 0");
        assertEquals(1, scoreManager.getLevel(), "Initial level should be 1");
        assertEquals(0, scoreManager.getRowsCleared(), "Initial rows cleared should be 0");
    }

    @Test
    void testAddScore() {
        scoreManager.add(100);
        assertEquals(100, scoreManager.scoreProperty().get(), "Score should increase by 100");
        scoreManager.add(50);
        assertEquals(150, scoreManager.scoreProperty().get(), "Score should increase by 50 more");
    }

    @Test
    void testAddClearedRowsNoLevelUp() {
        scoreManager.addClearedRows(3); // ROWS_PER_LEVEL is 5
        assertEquals(3, scoreManager.getRowsCleared(), "Rows cleared should be 3");
        assertEquals(1, scoreManager.getLevel(), "Level should remain 1");
    }

    @Test
    void testAddClearedRowsLevelUp() {
        scoreManager.addClearedRows(5); // First level up
        assertEquals(5, scoreManager.getRowsCleared(), "Rows cleared should be 5");
        assertEquals(2, scoreManager.getLevel(), "Level should increase to 2");

        scoreManager.addClearedRows(4); // 9 rows total, still level 2
        assertEquals(9, scoreManager.getRowsCleared(), "Rows cleared should be 9");
        assertEquals(2, scoreManager.getLevel(), "Level should remain 2");

        scoreManager.addClearedRows(1); // 10 rows total, second level up
        assertEquals(10, scoreManager.getRowsCleared(), "Rows cleared should be 10");
        assertEquals(3, scoreManager.getLevel(), "Level should increase to 3");
    }

    @Test
    void testReset() {
        scoreManager.add(1000);
        scoreManager.addClearedRows(7); // Should be level 2

        scoreManager.reset();

        assertEquals(0, scoreManager.scoreProperty().get(), "Score should reset to 0");
        assertEquals(1, scoreManager.getLevel(), "Level should reset to 1");
        assertEquals(0, scoreManager.getRowsCleared(), "Rows cleared should reset to 0");
    }

    @Test
    void testLevelGetter() {
        scoreManager.addClearedRows(6); // To change level to 2
        assertEquals(2, scoreManager.getLevel(), "getLevel() should reflect current level");
    }

    @Test
    void testRowsClearedGetter() {
        scoreManager.addClearedRows(4);
        assertEquals(4, scoreManager.getRowsCleared(), "getRowsCleared() should reflect current rows cleared");
    }
}
