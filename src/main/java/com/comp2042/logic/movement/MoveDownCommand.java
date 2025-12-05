package com.comp2042.logic.movement;

import com.comp2042.ui.GameController;

public class MoveDownCommand implements Command {
    private GameController gameController;

    public MoveDownCommand(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void execute() {
        gameController.moveDown();
    }
}
