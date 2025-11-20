package com.comp2042;

import com.comp2042.logic.board.Board;
import com.comp2042.logic.board.SimpleBoard;
import com.comp2042.logic.movement.ClearRow;
import com.comp2042.logic.movement.DownData;
import com.comp2042.logic.movement.MoveEvent;
import com.comp2042.model.ViewData;
import com.comp2042.ui.GuiController;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());

    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }
            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            //  DO NOT award +1 for user soft drops anymore.
            // This makes scoring dependent only on line clears (classic behavior).
        }

        return new DownData(clearRow, board.getViewData());
    }
    @Override
    public DownData onHardDropEvent(MoveEvent event) {
        int dropDistance = 0;
        boolean canMove;
        ClearRow clearRow = null;

        // Move the current brick down until it can't move anymore
        do {
            canMove = board.moveBrickDown();
            if (canMove) {
                dropDistance++;
            }
        } while (canMove);

        // Brick has locked – merge into background
        board.mergeBrickToBackground();

        // Clear any full rows
        clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
        }

        // Spawn a new brick or end game
        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        // Redraw background with the locked piece
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }


    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }
}