package com.comp2042.logic.movement;

import com.comp2042.ui.GameController;

public class MoveRightCommand implements Command {
    private GameController gameController;

    public MoveRightCommand(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void execute() {
        gameController.moveRight();
    }
}
