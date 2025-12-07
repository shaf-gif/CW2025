package com.comp2042.logic.movement;

import com.comp2042.ui.GameController;

/**
 * Command to move the current brick left one step.
 * This command is typically associated with a user input event.
 */
public class MoveLeftCommand implements Command {
    /** The GameController instance to interact with. */
    private GameController gameController;

    /**
     * Constructs a new MoveLeftCommand.
     * @param gameController The GameController instance that will execute the move left action.
     */
    public MoveLeftCommand(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Executes the move left command, instructing the GameController to move the current brick left.
     */
    @Override
    public void execute() {
        gameController.moveLeft();
    }
}
