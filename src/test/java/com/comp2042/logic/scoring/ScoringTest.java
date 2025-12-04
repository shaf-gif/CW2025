package com.comp2042.logic.scoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ScoringTest {
    private Score scoring;

    @BeforeEach
    void setUp() {
        // Initialize a fresh Score object before each test
        scoring = new Score();
    }

    @Test
    void testInitialScoreIsZero() {
        // Verify the score property starts at 0
        assertEquals(0, scoring.scoreProperty().get(),
                "Initial score should be 0.");
    }

    @Test
    void testAddSingleScoreValue() {
        final int initialScore = scoring.scoreProperty().get();
        final int points = 500;

        scoring.add(points);

        assertEquals(initialScore + points, scoring.scoreProperty().get(),
                "Score should increase by the added amount.");
    }

    @Test
    void testAddScoreMultipleTimes() {
        final int points1 = 100;
        final int points2 = 300;

        scoring.add(points1);
        scoring.add(points2);

        assertEquals(400, scoring.scoreProperty().get(),
                "Score should correctly accumulate multiple additions.");
    }

    @Test
    void testAddZeroScoreDoesNotChangeTotal() {
        scoring.add(100);
        final int scoreBefore = scoring.scoreProperty().get();

        scoring.add(0);

        assertEquals(scoreBefore, scoring.scoreProperty().get(),
                "Adding zero points should not change the score.");
    }

    @Test
    void testResetScore() {
        scoring.add(1000);

        // FIX 3: Call the correct reset() method
        scoring.reset();

        assertEquals(0, scoring.scoreProperty().get(),
                "Score should reset to 0 after calling reset().");
    }
}