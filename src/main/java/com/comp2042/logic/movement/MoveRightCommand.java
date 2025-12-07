package com.comp2042.logic.movement;

import com.comp2042.ui.GameController;

/**
 * Command to move the current brick right one step.
 * This command is typically associated with a user input event.
 */
public class MoveRightCommand implements Command {
    /** The GameController instance to interact with. */
    private GameController gameController;

    /**
     * Constructs a new MoveRightCommand.
     * @param gameController The GameController instance that will execute the move right action.
     */
    public MoveRightCommand(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Executes the move right command, instructing the GameController to move the current brick right.
     */
    @Override
    public void execute() {
        gameController.moveRight();
    }
}
