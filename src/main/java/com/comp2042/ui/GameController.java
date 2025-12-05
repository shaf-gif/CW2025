package com.comp2042.ui;

import com.comp2042.logic.movement.InputEventListener;
import com.comp2042.logic.board.Board;
import com.comp2042.logic.board.SimpleBoard;
import com.comp2042.logic.movement.ClearRow;
import com.comp2042.logic.movement.DownData;
import com.comp2042.logic.movement.MoveEvent;
import com.comp2042.model.ViewData;

public class GameController implements InputEventListener {

    private final Board board;
    private final GuiController viewGuiController;

    public GameController(GuiController c, Board board) {
        this.viewGuiController = c;
        this.board = board;

        board.createNewBrick();

        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindLevel(board.getScore().levelProperty());

        // Pass score reference for leaderboard saving
        viewGuiController.setScoreTracker(board.getScore());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!canMove) {
            board.mergeBrickToBackground();
            AudioManager.getInstance().playDrop();

            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                AudioManager.getInstance().playLineClear();
                board.getScore().add(clearRow.getScoreBonus());
                board.getScore().addClearedRows(clearRow.getLinesRemoved());

                // Notify GUI of level change for speed adjustment
                viewGuiController.updateGameSpeed(board.getScore().getLevel());
            }

            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }

        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public DownData onHardDropEvent(MoveEvent event) {
        int dropDistance = 0;
        boolean canMove;
        ClearRow clearRow = null;

        // instantly drop until collision
        do {
            canMove = board.moveBrickDown();
            if (canMove) dropDistance++;
        } while (canMove);

        board.mergeBrickToBackground();
        AudioManager.getInstance().playDrop();

        clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            AudioManager.getInstance().playLineClear();
            board.getScore().add(clearRow.getScoreBonus());
            board.getScore().addClearedRows(clearRow.getLinesRemoved());

            // Notify GUI of level change for speed adjustment
            viewGuiController.updateGameSpeed(board.getScore().getLevel());
        }

        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

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
        AudioManager.getInstance().playRotate();
        return board.getViewData();
    }

    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        board.holdCurrentBrick();
        return board.getViewData();
    }

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.updateGameSpeed(1); // Reset to level 1 speed
    }

    // New methods for Command pattern integration
    public void moveLeft() {
        onLeftEvent(null);
    }

    public void moveRight() {
        onRightEvent(null);
    }

    public void moveDown() {
        onDownEvent(null);
    }

    public void rotateClockwise() {
        onRotateEvent(null);
    }

    public void rotateCounterClockwise() {
        // As per current GameController, onRotateEvent is the only rotation.
        // If distinct counter-clockwise is needed, board logic must change first.
        onRotateEvent(null);
    }

    public void hardDrop() {
        onHardDropEvent(null);
    }
}