package com.comp2042.logic.scoring;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty rowsCleared = new SimpleIntegerProperty(0);

    // Rows needed to advance to next level
    private static final int ROWS_PER_LEVEL = 5;

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public IntegerProperty rowsClearedProperty() {
        return rowsCleared;
    }

    public int getLevel() {
        return level.get();
    }

    public int getRowsCleared() {
        return rowsCleared.get();
    }

    public void add(int i) {
        score.setValue(score.getValue() + i);
    }

    /**
     * Add cleared rows and update level accordingly
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

    public void reset() {
        score.setValue(0);
        level.setValue(1);
        rowsCleared.setValue(0);
    }
}