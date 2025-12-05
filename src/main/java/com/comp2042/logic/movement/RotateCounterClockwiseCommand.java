package com.comp2042.logic.movement;

import com.comp2042.ui.GameController;

public class RotateCounterClockwiseCommand implements Command {
    private GameController gameController;

    public RotateCounterClockwiseCommand(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void execute() {
        gameController.rotateCounterClockwise();
    }
}
