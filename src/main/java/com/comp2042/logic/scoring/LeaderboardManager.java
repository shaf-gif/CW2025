package com.comp2042.logic.scoring;

import java.io.*;
import java.util.*;

/**
 * Manages the game's leaderboard, including saving, loading, and sorting scores.
 * The leaderboard stores a limited number of top scores.
 */
public class LeaderboardManager {
    /** The file name for storing leaderboard data. */
    private static String currentLeaderboardFile = "leaderboard.dat";

    /**
     * Private constructor to prevent instantiation, as this is a utility class.
     */
    private LeaderboardManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Sets the leaderboard file name for testing purposes.
     * @param fileName The name of the file to use for the leaderboard.
     */
    public static void setLeaderboardFileForTesting(String fileName) {
        currentLeaderboardFile = fileName;
    }
    /** The maximum number of entries to keep in the leaderboard. */
    private static final int MAX_ENTRIES = 10;

    /**
     * Represents a single entry in the leaderboard, containing player name, score, level, rows cleared, and a timestamp.
     * Implements Serializable for persistence and Comparable for sorting.
     */
    public static class LeaderboardEntry implements Serializable, Comparable<LeaderboardEntry> {
        private static final long serialVersionUID = 1L;

        /** The name of the player. */
        private String playerName;
        /** The score achieved by the player. */
        private int score;
        /** The level reached by the player. */
        private int level;
        /** The number of rows cleared by the player. */
        private int rowsCleared;
        /** The timestamp when the score was recorded. */
        private long timestamp;

        /**
         * Constructs a new LeaderboardEntry.
         * @param playerName The name of the player.
         * @param score The score achieved.
         * @param level The level reached.
         * @param rowsCleared The number of rows cleared.
         */
        public LeaderboardEntry(String playerName, int score, int level, int rowsCleared) {
            this.playerName = playerName;
            this.score = score;
            this.level = level;
            this.rowsCleared = rowsCleared;
            this.timestamp = System.currentTimeMillis();
        }

        /**
         * Returns the player's name.
         * @return The player's name.
         */
        public String getPlayerName() { return playerName; }
        /**
         * Returns the player's score.
         * @return The player's score.
         */
        public int getScore() { return score; }
        /**
         * Returns the level reached by the player.
         * @return The level reached.
         */
        public int getLevel() { return level; }
        /**
         * Returns the number of rows cleared by the player.
         * @return The number of rows cleared.
         */
        public int getRowsCleared() { return rowsCleared; }
        /**
         * Returns the timestamp when the score was recorded.
         * @return The timestamp.
         */
        public long getTimestamp() { return timestamp; }

        @Override
        /**
         * Compares this LeaderboardEntry with another for ordering.
         * Entries are sorted primarily by score (descending), then by level (descending), and finally by rows cleared (descending).
         * @param other The other LeaderboardEntry to compare against.
         * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
         */
        public int compareTo(LeaderboardEntry other) {
            // Sort by score (descending), then by level, then by rows
            if (this.score != other.score) {
                return Integer.compare(other.score, this.score);
            }
            if (this.level != other.level) {
                return Integer.compare(other.level, this.level);
            }
            return Integer.compare(other.rowsCleared, this.rowsCleared);
        }
    }

    /**
     * Returns the maximum number of entries allowed in the leaderboard.
     * @return The maximum number of entries.
     */
    public static int getMaxEntries() {
        return MAX_ENTRIES;
    }

    /**
     * Saves a new score entry to the leaderboard. The new entry is added,
     * the leaderboard is re-sorted, and trimmed to {@code MAX_ENTRIES}.
     * If the provided {@code playerName} is empty or "Player", a unique name will be generated.
     * @param playerName The name of the player. If empty or "Player", a unique name will be generated.
     * @param score The score achieved by the player.
     * @param level The level reached by the player.
     * @param rowsCleared The number of rows cleared by the player.
     */
    public static void saveScore(String playerName, int score, int level, int rowsCleared) {
        List<LeaderboardEntry> entries = loadLeaderboard();

        // Generate unique player name if empty or "Player"
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = generatePlayerName(entries);
        } else if (playerName.trim().equalsIgnoreCase("Player")) {
            playerName = generatePlayerName(entries);
        }

        entries.add(new LeaderboardEntry(playerName.trim(), score, level, rowsCleared));

        // Sort and keep top entries
        Collections.sort(entries);
        if (entries.size() > MAX_ENTRIES) {
            entries = new ArrayList<>(entries.subList(0, MAX_ENTRIES));
        }

        saveLeaderboard(entries);
    }

    /**
     * Generates a unique player name. If "Player" is taken, it tries "Player 1", "Player 2", etc.
     * @param entries The current list of leaderboard entries to check for existing names.
     * @return A unique player name.
     */
    private static String generatePlayerName(List<LeaderboardEntry> entries) {
        Set<String> existingNames = new HashSet<>();
        for (LeaderboardEntry entry : entries) {
            existingNames.add(entry.getPlayerName().toLowerCase());
        }

        // Try "Player" first
        if (!existingNames.contains("player")) {
            return "Player";
        }

        // Try "Player 1", "Player 2", etc.
        int counter = 1;
        while (existingNames.contains("player " + counter)) {
            counter++;
        }

        return "Player " + counter;
    }

    /**
     * Loads all leaderboard entries from the file.
     * @return A list of {@code LeaderboardEntry} objects, sorted by score.
     */
    public static List<LeaderboardEntry> loadLeaderboard() {
        File file = new File(currentLeaderboardFile);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            List<LeaderboardEntry> entries = (List<LeaderboardEntry>) ois.readObject();
            return entries;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading leaderboard: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Saves the provided list of leaderboard entries to the file.
     * @param entries The list of {@code LeaderboardEntry} objects to save.
     */
    private static void saveLeaderboard(List<LeaderboardEntry> entries) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(currentLeaderboardFile))) {
            oos.writeObject(entries);
        } catch (IOException e) {
            System.err.println("Error saving leaderboard: " + e.getMessage());
        }
    }

    /**
     * Clears all leaderboard entries by deleting the leaderboard file.
     */
    public static void clearLeaderboard() {
        File file = new File(currentLeaderboardFile);
        if (file.exists()) {
            file.delete();
        }
    }
}