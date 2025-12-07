package com.comp2042.logic.board;

import com.comp2042.logic.bricks.BrickType;
import com.comp2042.logic.movement.ClearRow;
import com.comp2042.logic.scoring.Score;
import com.comp2042.model.ViewData;

/**
 * Represents the game board, defining the contract for all game board implementations.
 */
public interface Board {

    /**
     * Moves the current brick one step down.
     *
     * @return true if the brick was moved, false otherwise.
     */
    boolean moveBrickDown();

    /**
     * Moves the current brick one step to the left.
     *
     * @return true if the brick was moved, false otherwise.
     */
    boolean moveBrickLeft();

    /**
     * Moves the current brick one step to the right.
     *
     * @return true if the brick was moved, false otherwise.
     */
    boolean moveBrickRight();

    /**
     * Rotates the current brick to the left.
     *
     * @return true if the brick was rotated, false otherwise.
     */
    boolean rotateLeftBrick();

    /**
     * Creates a new brick and places it on the board.
     *
     * @return true if the new brick intersects with the existing board, indicating a game over.
     */
    boolean createNewBrick();

    /**
     * Holds the current brick, allowing the player to use it later.
     */
    void holdCurrentBrick();

    /**
     * Gets the current state of the board as a 2D integer matrix.
     *
     * @return the board matrix.
     */
    int[][] getBoardMatrix();

    /**
     * Gets the data required for the view to render the game state.
     *
     * @return the view data.
     */
    ViewData getViewData();

    /**
     * Merges the current brick into the board's background.
     *
     * @return the type of the merged brick.
     */
    BrickType mergeBrickToBackground();

    /**
     * Clears any completed rows from the board.
     *
     * @return a ClearRow object containing information about the cleared rows.
     */
    ClearRow clearRows();

    /**
     * Gets the current score.
     *
     * @return the score object.
     */
    Score getScore();

    /**
     * Starts a new game, resetting the board and score.
     */
    void newGame();
}
