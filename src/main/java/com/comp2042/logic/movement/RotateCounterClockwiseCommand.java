package com.comp2042.logic.movement;

import com.comp2042.ui.GameController;

/**
 * Command to rotate the current brick counter-clockwise.
 * This command is typically associated with a user input event.
 */
public class RotateCounterClockwiseCommand implements Command {
    /** The GameController instance to interact with. */
    private GameController gameController;

    /**
     * Constructs a new RotateCounterClockwiseCommand.
     * @param gameController The GameController instance that will execute the rotate counter-clockwise action.
     */
    public RotateCounterClockwiseCommand(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Executes the rotate counter-clockwise command, instructing the GameController to rotate the current brick counter-clockwise.
     */
    @Override
    public void execute() {
        gameController.rotateCounterClockwise();
    }
}
