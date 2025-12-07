package com.comp2042.logic.movement;

import com.comp2042.ui.GameController;

/**
 * Command to move the current brick down one step.
 * This command is typically associated with a user input event.
 */
public class MoveDownCommand implements Command {
    /** The GameController instance to interact with. */
    private GameController gameController;

    /**
     * Constructs a new MoveDownCommand.
     * @param gameController The GameController instance that will execute the move down action.
     */
    public MoveDownCommand(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Executes the move down command, instructing the GameController to move the current brick down.
     */
    @Override
    public void execute() {
        gameController.moveDown();
    }
}
