package com.comp2042.logic.scoring;

import java.io.*;
import java.util.*;

public class LeaderboardManager {
    private static final String LEADERBOARD_FILE = "leaderboard.dat";
    private static final int MAX_ENTRIES = 10;

    public static class LeaderboardEntry implements Serializable, Comparable<LeaderboardEntry> {
        private static final long serialVersionUID = 1L;

        private String playerName;
        private int score;
        private int level;
        private int rowsCleared;
        private long timestamp;

        public LeaderboardEntry(String playerName, int score, int level, int rowsCleared) {
            this.playerName = playerName;
            this.score = score;
            this.level = level;
            this.rowsCleared = rowsCleared;
            this.timestamp = System.currentTimeMillis();
        }

        public String getPlayerName() { return playerName; }
        public int getScore() { return score; }
        public int getLevel() { return level; }
        public int getRowsCleared() { return rowsCleared; }
        public long getTimestamp() { return timestamp; }

        @Override
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
     * Save a new score to the leaderboard with auto-generated player name if needed
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
     * Generate a unique player name: Player, Player 1, Player 2, etc.
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
     * Load all leaderboard entries
     */
    public static List<LeaderboardEntry> loadLeaderboard() {
        File file = new File(LEADERBOARD_FILE);
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
     * Save leaderboard entries to file
     */
    private static void saveLeaderboard(List<LeaderboardEntry> entries) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LEADERBOARD_FILE))) {
            oos.writeObject(entries);
        } catch (IOException e) {
            System.err.println("Error saving leaderboard: " + e.getMessage());
        }
    }

    /**
     * Clear all leaderboard entries
     */
    public static void clearLeaderboard() {
        File file = new File(LEADERBOARD_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}