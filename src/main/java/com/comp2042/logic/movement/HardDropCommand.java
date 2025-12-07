package com.comp2042.logic.movement;

import com.comp2042.ui.GameController;

/**
 * Command to perform a hard drop action in the game.
 * This command is typically associated with a user input event.
 */
public class HardDropCommand implements Command {
    /** The GameController instance to interact with. */
    private GameController gameController;

    /**
     * Constructs a new HardDropCommand.
     * @param gameController The GameController instance that will execute the hard drop.
     */
    public HardDropCommand(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Executes the hard drop command, instructing the GameController to hard drop the current brick.
     */
    @Override
    public void execute() {
        gameController.hardDrop();
    }
}
