package com.comp2042.logic.movement;

import com.comp2042.model.ViewData;

/**
 * Defines the contract for an event listener that handles various game input events.
 */
public interface InputEventListener {

    /**
     * Handles a down movement event for the active brick.
     * @param event The MoveEvent containing details about the down movement.
     * @return DownData containing information about cleared rows and the updated view data.
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Handles a left movement event for the active brick.
     * @param event The MoveEvent containing details about the left movement.
     * @return ViewData representing the updated game state.
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Handles a right movement event for the active brick.
     * @param event The MoveEvent containing details about the right movement.
     * @return ViewData representing the updated game state.
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Handles a rotation event for the active brick.
     * @param event The MoveEvent containing details about the rotation.
     * @return ViewData representing the updated game state.
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Handles a hard drop event for the active brick.
     * @param event The MoveEvent containing details about the hard drop.
     * @return DownData containing information about cleared rows and the updated view data.
     */
    DownData onHardDropEvent(MoveEvent event);

    /**
     * Handles a hold event for the active brick, swapping it with the held brick.
     * @param event The MoveEvent containing details about the hold action.
     * @return ViewData representing the updated game state.
     */
    ViewData onHoldEvent(MoveEvent event);


    /**
     * Initiates a new game.
     */
    void createNewGame();
}

