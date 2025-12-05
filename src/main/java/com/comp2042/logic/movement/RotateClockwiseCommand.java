package com.comp2042.logic.movement;

import com.comp2042.ui.GameController;

public class RotateClockwiseCommand implements Command {
    private GameController gameController;

    public RotateClockwiseCommand(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void execute() {
        gameController.rotateClockwise();
    }
}
