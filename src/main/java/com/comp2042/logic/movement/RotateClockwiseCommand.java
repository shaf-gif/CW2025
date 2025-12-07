package com.comp2042.logic.movement;

import com.comp2042.ui.GameController;

/**
 * Command to rotate the current brick clockwise.
 * This command is typically associated with a user input event.
 */
public class RotateClockwiseCommand implements Command {
    /** The GameController instance to interact with. */
    private GameController gameController;

    /**
     * Constructs a new RotateClockwiseCommand.
     * @param gameController The GameController instance that will execute the rotate clockwise action.
     */
    public RotateClockwiseCommand(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Executes the rotate clockwise command, instructing the GameController to rotate the current brick clockwise.
     */
    @Override
    public void execute() {
        gameController.rotateClockwise();
    }
}
