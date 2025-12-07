package com.comp2042.logic.movement;

import com.comp2042.model.ViewData;

/**
 * Represents the data returned after a brick moves down,
 * including information about any cleared rows and the updated view data.
 */
public final class DownData {
    /**
     * Information about cleared rows, or null if no rows were cleared.
     */
    private final ClearRow clearRow;
    /**
     * The updated ViewData object reflecting the game's current state.
     */
    private final ViewData viewData;

    /**
     * Constructs a new DownData object.
     * @param clearRow Information about cleared rows (can be null).
     * @param viewData The updated ViewData after the down movement.
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * Returns information about cleared rows.
     * @return A {@code ClearRow} object if rows were cleared, otherwise {@code null}.
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Returns the updated ViewData object.
     * @return The {@code ViewData} object reflecting the game's current state.
     */
    public ViewData getViewData() {
        return viewData;
    }
}
