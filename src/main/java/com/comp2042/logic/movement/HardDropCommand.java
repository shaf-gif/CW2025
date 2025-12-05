package com.comp2042.logic.movement;

import com.comp2042.ui.GameController;

public class HardDropCommand implements Command {
    private GameController gameController;

    public HardDropCommand(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void execute() {
        gameController.hardDrop();
    }
}
