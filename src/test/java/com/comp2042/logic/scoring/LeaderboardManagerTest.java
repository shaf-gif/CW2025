package com.comp2042.logic.scoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderboardManagerTest {

    @TempDir
    Path tempDir; // JUnit 5 provides a temporary directory for tests
    private String testLeaderboardFile;

    @BeforeEach
    void setUp() {
        testLeaderboardFile = tempDir.resolve("test_leaderboard.dat").toString();
        LeaderboardManager.setLeaderboardFileForTesting(testLeaderboardFile);

        // Clear any potential existing leaderboard from previous test runs in this temp dir
        LeaderboardManager.clearLeaderboard();
    }

    @AfterEach
    void tearDown() {
        // Ensure the test file is deleted after each test
        LeaderboardManager.clearLeaderboard();
    }

    @Test
    void testLeaderboardEntryComparable() {
        LeaderboardManager.LeaderboardEntry entry1 = new LeaderboardManager.LeaderboardEntry("A", 100, 1, 10);
        LeaderboardManager.LeaderboardEntry entry2 = new LeaderboardManager.LeaderboardEntry("B", 200, 1, 10);
        LeaderboardManager.LeaderboardEntry entry3 = new LeaderboardManager.LeaderboardEntry("C", 100, 2, 5);
        LeaderboardManager.LeaderboardEntry entry4 = new LeaderboardManager.LeaderboardEntry("D", 100, 1, 15);
        LeaderboardManager.LeaderboardEntry entry5 = new LeaderboardManager.LeaderboardEntry("E", 100, 1, 10); // Same as entry1

        assertTrue(entry1.compareTo(entry2) > 0, "Entry 1 (100) should be less than Entry 2 (200) - descending score sort");
        assertTrue(entry2.compareTo(entry1) < 0, "Entry 2 (200) should be greater than Entry 1 (100) - descending score sort");

        assertTrue(entry1.compareTo(entry3) > 0, "Entry 1 (L1) should be less than Entry 3 (L2) for same score - descending level sort");
        assertTrue(entry3.compareTo(entry1) < 0, "Entry 3 (L2) should be greater than Entry 1 (L1) for same score - descending level sort");

        assertTrue(entry1.compareTo(entry4) > 0, "Entry 1 (R10) should be less than Entry 4 (R15) for same score/level - descending rows sort");
        assertTrue(entry4.compareTo(entry1) < 0, "Entry 4 (R15) should be greater than Entry 1 (R10) for same score/level - descending rows sort");

        assertEquals(0, entry1.compareTo(entry5), "Entry 1 and Entry 5 should be equal (same score, level, rows)");
    }

    @Test
    void testSaveAndLoadEmptyLeaderboard() {
        List<LeaderboardManager.LeaderboardEntry> loadedEntries = LeaderboardManager.loadLeaderboard();
        assertTrue(loadedEntries.isEmpty(), "Initially, the leaderboard should be empty.");
    }

    @Test
    void testSaveSingleScore() {
        LeaderboardManager.saveScore("Player1", 100, 1, 10);
        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.loadLeaderboard();
        assertEquals(1, entries.size());
        assertEquals("Player1", entries.get(0).getPlayerName());
        assertEquals(100, entries.get(0).getScore());
    }

    @Test
    void testSaveMultipleScoresAndSorting() {
        LeaderboardManager.saveScore("P1", 100, 1, 10);
        LeaderboardManager.saveScore("P2", 300, 2, 20);
        LeaderboardManager.saveScore("P3", 200, 1, 15);

        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.loadLeaderboard();
        assertEquals(3, entries.size());
        assertEquals("P2", entries.get(0).getPlayerName(), "Should be sorted by score descending");
        assertEquals("P3", entries.get(1).getPlayerName());
        assertEquals("P1", entries.get(2).getPlayerName());
    }

    @Test
    void testLeaderboardMaxEntries() {
        // Add more than MAX_ENTRIES
        for (int i = 0; i < LeaderboardManager.getMaxEntries() + 5; i++) {
            LeaderboardManager.saveScore("Test" + i, 100 + i, 1, 10);
        }

        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.loadLeaderboard();
        assertEquals(LeaderboardManager.getMaxEntries(), entries.size(), "Leaderboard should trim to MAX_ENTRIES");
        assertEquals(100 + LeaderboardManager.getMaxEntries() + 4, entries.get(0).getScore(),
                "Highest score should be at the top after trimming.");
    }

    @Test
    void testGeneratePlayerNameUnique() throws NoSuchMethodException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
        // Access private method using reflection
        java.lang.reflect.Method generateMethod = LeaderboardManager.class.getDeclaredMethod("generatePlayerName", List.class);
        generateMethod.setAccessible(true);

        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.loadLeaderboard(); // Should be empty

        // Test initial "Player" name
        assertEquals("Player", generateMethod.invoke(null, entries));

        // Add "Player"
        LeaderboardManager.saveScore("Player", 50, 1, 1);
        entries = LeaderboardManager.loadLeaderboard();
        assertEquals("Player 1", generateMethod.invoke(null, entries));

        // Add "Player 1"
        LeaderboardManager.saveScore("Player 1", 50, 1, 1);
        entries = LeaderboardManager.loadLeaderboard();
        assertEquals("Player 2", generateMethod.invoke(null, entries));
    }

    @Test
    void testSaveScoreWithEmptyOrDefaultName() {
        LeaderboardManager.saveScore("", 10, 1, 1);
        LeaderboardManager.saveScore("   ", 20, 1, 1);
        LeaderboardManager.saveScore("Player", 30, 1, 1);

        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.loadLeaderboard();
        Set<String> playerNames = entries.stream().map(LeaderboardManager.LeaderboardEntry::getPlayerName).collect(Collectors.toSet());

        assertTrue(playerNames.contains("Player"), "Should contain generated 'Player'");
        assertTrue(playerNames.contains("Player 1"), "Should contain generated 'Player 1'");
        assertTrue(playerNames.contains("Player 2"), "Should contain generated 'Player 2'");
        assertEquals(3, playerNames.size(), "Should have three unique generated names");
    }

    @Test
    void testClearLeaderboard() {
        LeaderboardManager.saveScore("P1", 100, 1, 10);
        File file = new File(testLeaderboardFile);
        assertTrue(file.exists(), "Leaderboard file should exist after saving a score.");

        LeaderboardManager.clearLeaderboard();
        assertFalse(file.exists(), "Leaderboard file should not exist after clearing.");
        assertTrue(LeaderboardManager.loadLeaderboard().isEmpty(), "Leaderboard should be empty after clearing.");
    }

    @Test
    void testLoadCorruptedLeaderboard() throws IOException {
        File file = new File(testLeaderboardFile);
        // Write some invalid data to simulate a corrupted file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("This is not a serialized object.");
        }

        List<LeaderboardManager.LeaderboardEntry> entries = LeaderboardManager.loadLeaderboard();
        assertTrue(entries.isEmpty(), "Loading a corrupted file should return an empty list.");
        // The error message for loading will be printed to System.err, which is fine for this test.
    }
}
