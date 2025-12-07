package com.comp2042.ui;

import com.comp2042.logic.movement.InputEventListener;
import com.comp2042.logic.board.Board;
import com.comp2042.logic.bricks.BrickType;
import com.comp2042.logic.movement.ClearRow;
import com.comp2042.logic.movement.DownData;
import com.comp2042.logic.movement.MoveEvent;
import com.comp2042.model.ViewData;

/**
 * Handles game logic and acts as an intermediary between the GUI and the game board.
 */
public class GameController implements InputEventListener {

    /** The game board instance that manages the game state. */
    private final Board board;
    /** The GUI controller responsible for updating the view. */
    private final GuiController viewGuiController;

    /**
     * Constructs a new GameController.
     * @param c The GuiController instance for UI interactions.
     * @param board The game board instance.
     */
    public GameController(GuiController c, Board board) {
        this.viewGuiController = c;
        this.board = board;

        board.createNewBrick();

        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindLevel(board.getScore().levelProperty());

        viewGuiController.setScoreTracker(board.getScore());
    }

    /**
     * Handles the event when a brick moves down.
     * @param event The MoveEvent triggering the down movement.
     * @return {@code DownData} containing information about cleared rows and the updated view data.
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        BrickType mergedBrickType = null;

        if (!canMove) {
            mergedBrickType = board.mergeBrickToBackground();
            AudioManager.getInstance().playDrop();

            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                AudioManager.getInstance().playLineClear();
                board.getScore().add(clearRow.getScoreBonus());
                board.getScore().addClearedRows(clearRow.getLinesRemoved());
            }

            if (mergedBrickType == BrickType.SLOW) {
                viewGuiController.activateSlowMode();
            }

            viewGuiController.updateGameSpeed(board.getScore().getLevel());

            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }

        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Handles the event when a brick is hard-dropped.
     * @param event The MoveEvent triggering the hard drop.
     * @return {@code DownData} containing information about cleared rows and the updated view data.
     */
    @Override
    public DownData onHardDropEvent(MoveEvent event) {
        int dropDistance = 0;
        boolean canMove;
        ClearRow clearRow = null;
        BrickType mergedBrickType = null;

        do {
            canMove = board.moveBrickDown();
            if (canMove) dropDistance++;
        } while (canMove);

        mergedBrickType = board.mergeBrickToBackground();
        AudioManager.getInstance().playDrop();

        clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            AudioManager.getInstance().playLineClear();
            board.getScore().add(clearRow.getScoreBonus());
            board.getScore().addClearedRows(clearRow.getLinesRemoved());
        }

        if (mergedBrickType == BrickType.SLOW) {
            viewGuiController.activateSlowMode();
        }

        viewGuiController.updateGameSpeed(board.getScore().getLevel());

        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Handles the event when a brick moves left.
     * @param event The MoveEvent triggering the left movement.
     * @return {@code ViewData} representing the updated game state.
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    /**
     * Handles the event when a brick moves right.
     * @param event The MoveEvent triggering the right movement.
     * @return {@code ViewData} representing the updated game state.
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    /**
     * Handles the event when a brick rotates.
     * @param event The MoveEvent triggering the rotation.
     * @return {@code ViewData} representing the updated game state.
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        AudioManager.getInstance().playRotate();
        return board.getViewData();
    }

    /**
     * Handles the event when a brick is held.
     * @param event The MoveEvent triggering the hold action.
     * @return {@code ViewData} representing the updated game state.
     */
    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        board.holdCurrentBrick();
        return board.getViewData();
    }

    /**
     * Creates a new game, resetting the board and refreshing the UI.
     */
    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.updateGameSpeed(1);
    }

    /**
     * Moves the current brick one step to the left.
     */
    public void moveLeft() {
        onLeftEvent(null);
    }

    /**
     * Moves the current brick one step to the right.
     */
    public void moveRight() {
        onRightEvent(null);
    }

    /**
     * Moves the current brick one step down.
     */
    public void moveDown() {
        onDownEvent(null);
    }

    /**
     * Rotates the current brick clockwise.
     */
    public void rotateClockwise() {
        onRotateEvent(null);
    }

    /**
     * Rotates the current brick counter-clockwise.
     */
    public void rotateCounterClockwise() {
        onRotateEvent(null);
    }

    /**
     * Performs a hard drop of the current brick.
     */
    public void hardDrop() {
        onHardDropEvent(null);
    }
}