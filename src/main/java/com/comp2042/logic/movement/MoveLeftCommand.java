package com.comp2042.logic.movement;

import com.comp2042.ui.GameController;

public class MoveLeftCommand implements Command {
    private GameController gameController;

    public MoveLeftCommand(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void execute() {
        gameController.moveLeft();
    }
}
