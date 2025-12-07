package com.comp2042.logic.scoring;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages the player's score, level, and rows cleared in the game.
 * It uses JavaFX properties for observable score, level, and rows cleared values.
 */
public final class Score {

    /**
     * Constructs a new Score object.
     * Initializes the player's score, level, and rows cleared to their default starting values.
     */
    public Score() {
        // Default constructor
    }

    private final ReadOnlyIntegerWrapper score = new ReadOnlyIntegerWrapper(0);
    private final ReadOnlyIntegerWrapper level = new ReadOnlyIntegerWrapper(1);
    private final ReadOnlyIntegerWrapper rowsCleared = new ReadOnlyIntegerWrapper(0);

    /** The number of rows that must be cleared to advance to the next level. */
    private static final int ROWS_PER_LEVEL = 5;

    /**
     * Returns the read-only integer property for the score.
     * @return The {@code ReadOnlyIntegerProperty} for the score.
     */
    public ReadOnlyIntegerProperty scoreProperty() {
        return score.getReadOnlyProperty();
    }

    /**
     * Returns the read-only integer property for the level.
     * @return The {@code ReadOnlyIntegerProperty} for the level.
     */
    public ReadOnlyIntegerProperty levelProperty() {
        return level.getReadOnlyProperty();
    }

    /**
     * Returns the read-only integer property for the number of rows cleared.
     * @return The {@code ReadOnlyIntegerProperty} for the rows cleared.
     */
    public ReadOnlyIntegerProperty rowsClearedProperty() {
        return rowsCleared.getReadOnlyProperty();
    }

    /**
     * Returns the current level.
     * @return The current level.
     */
    public int getLevel() {
        return level.get();
    }

    /**
     * Returns the total number of rows cleared.
     * @return The total number of rows cleared.
     */
    public int getRowsCleared() {
        return rowsCleared.get();
    }

    /**
     * Adds the specified value to the current score.
     * @param i The value to add to the score.
     */
    public void add(int i) {
        score.setValue(score.getValue() + i);
    }

    /**
     * Adds the specified number of cleared rows and updates the level accordingly.
     * @param rows The number of rows that were cleared.
     */
    public void addClearedRows(int rows) {
        int oldRows = rowsCleared.get();
        int newRows = oldRows + rows;
        rowsCleared.setValue(newRows);

        // Calculate new level (starts at 1)
        int newLevel = (newRows / ROWS_PER_LEVEL) + 1;

        // Update level if it changed
        if (newLevel != level.get()) {
            level.setValue(newLevel);
        }
    }

    /**
     * Resets the score, level, and rows cleared to their initial values.
     */
    public void reset() {
        score.setValue(0);
        level.setValue(1);
        rowsCleared.setValue(0);
    }
}