package com.comp2042.logic.scoring;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final ReadOnlyIntegerWrapper score = new ReadOnlyIntegerWrapper(0);
    private final ReadOnlyIntegerWrapper level = new ReadOnlyIntegerWrapper(1);
    private final ReadOnlyIntegerWrapper rowsCleared = new ReadOnlyIntegerWrapper(0);

    private static final int ROWS_PER_LEVEL = 5;

    public ReadOnlyIntegerProperty scoreProperty() {
        return score.getReadOnlyProperty();
    }

    public ReadOnlyIntegerProperty levelProperty() {
        return level.getReadOnlyProperty();
    }

    public ReadOnlyIntegerProperty rowsClearedProperty() {
        return rowsCleared.getReadOnlyProperty();
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