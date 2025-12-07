package com.comp2042.logic.board;

import com.comp2042.ui.AudioManager;
import com.comp2042.logic.Constants;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.BrickType;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerationStrategy;
import com.comp2042.logic.movement.ClearRow;
import com.comp2042.logic.scoring.Score;
import com.comp2042.model.NextShapeInfo;
import com.comp2042.model.ViewData;

import java.awt.Point;

/**
 * A simple implementation of the game board.
 */
public class SimpleBoard implements Board {

    /** The width of the game board in number of cells. */
    private final int width;
    /** The height of the game board in number of cells. */
    private final int height;

    /** Generates new bricks for the game. */
    private final BrickGenerator brickGenerator;
    /** Manages the rotation of the current brick. */
    private final BrickRotator brickRotator;

    /** The 2D array representing the game board's current state. */
    private int[][] boardMatrix;
    /** The current position (top-left corner) of the active brick on the board. */
    private Point currentOffset;

    /** The brick currently being held by the player. */
    private Brick heldBrick;
    /** The current active brick in play. */
    private Brick currentBrick;
    /** Flag indicating if the hold feature has been used in the current turn. */
    private boolean holdUsedThisTurn;

    /** The game's score manager. */
    private final Score score;

    /**
     * Constructs a new SimpleBoard.
     */
    /**
     * Constructs a new SimpleBoard, initializing its dimensions, score,
     * brick generator, and brick rotator.
     */
    public SimpleBoard() {
        this.width = Constants.BOARD_WIDTH;
        this.height = Constants.BOARD_HEIGHT;

        boardMatrix = new int[height][width];

        score = new Score();
        brickGenerator = new RandomBrickGenerator(score);
        brickRotator = new BrickRotator();
    }

    /**
     * Moves the current brick one step down.
     *
     * @return true if the brick was moved, false otherwise.
     */
    @Override
    public boolean moveBrickDown() {
        int[][] temp = MatrixOperations.copy(boardMatrix);

        Point p = new Point(currentOffset);
        p.translate(0, 1);

        if (MatrixOperations.intersect(temp, brickRotator.getCurrentShape(), p.x, p.y)) {
            return false;
        }

        currentOffset = p;
        return true;
    }

    /**
     * Moves the current brick one step to the left.
     *
     * @return true if the brick was moved, false otherwise.
     */
    @Override
    public boolean moveBrickLeft() {
        int[][] temp = MatrixOperations.copy(boardMatrix);

        Point p = new Point(currentOffset);
        p.translate(-1, 0);

        if (MatrixOperations.intersect(temp, brickRotator.getCurrentShape(), p.x, p.y)) {
            return false;
        }

        currentOffset = p;
        return true;
    }

    /**
     * Moves the current brick one step to the right.
     *
     * @return true if the brick was moved, false otherwise.
     */
    @Override
    public boolean moveBrickRight() {
        int[][] temp = MatrixOperations.copy(boardMatrix);

        Point p = new Point(currentOffset);
        p.translate(1, 0);

        if (MatrixOperations.intersect(temp, brickRotator.getCurrentShape(), p.x, p.y)) {
            return false;
        }

        currentOffset = p;
        return true;
    }

    /**
     * Rotates the current brick to the left.
     *
     * @return true if the brick was rotated, false otherwise.
     */
    @Override
    public boolean rotateLeftBrick() {

        int[][] temp = MatrixOperations.copy(boardMatrix);

        NextShapeInfo nextShape = brickRotator.getNextShape();

        if (MatrixOperations.intersect(temp,
                nextShape.getShape(),
                currentOffset.x,
                currentOffset.y)) {

            return false;
        }

        brickRotator.setCurrentShape(nextShape.getPosition());
        return true;
    }

    /**
     * Creates a new brick and places it on the board.
     *
     * @return true if the new brick intersects with the existing board, indicating a game over.
     */
    /**
     * Creates a new brick and places it on the board at the top center.
     * Resets the hold usage flag for the new brick.
     *
     * @return true if the new brick immediately intersects with existing blocks (game over condition), false otherwise.
     */
    @Override
    public boolean createNewBrick() {

        Brick newBrick = brickGenerator.getBrick();
        currentBrick = newBrick;
        brickRotator.setBrick(newBrick);

        int spawnX = width / 2 - 1;
        int spawnY = Constants.HIDDEN_ROWS;

        currentOffset = new Point(spawnX, spawnY);
        holdUsedThisTurn = false;

        return MatrixOperations.intersect(
                boardMatrix,
                brickRotator.getCurrentShape(),
                currentOffset.x,
                currentOffset.y
        );
    }

    /**
     * Gets the current state of the board as a 2D integer matrix.
     *
     * @return the board matrix.
     */
    @Override
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    /**
     * Gets the current score object.
     *
     * @return the score object.
     */
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * Gets the data required for the view to render the game state.
     *
     * @return the view data.
     */
    /**
     * Gets the data required for the view to render the game state,
     * including the current brick's shape, position, next bricks, ghost brick position, and held brick.
     *
     * @return a {@code ViewData} object containing all necessary information for rendering.
     */
    @Override
    public ViewData getViewData() {

        int ghostX = currentOffset.x;
        int ghostY = computeGhostY();

        var nextBricks = brickGenerator.peekNext(3);
        int[][][] nextShapes = new int[nextBricks.size()][][];
        for (int i = 0; i < nextBricks.size(); i++) {
            nextShapes[i] = nextBricks.get(i).getShapeMatrix().get(0);
        }

        int[][] heldShape = null;
        if (heldBrick != null) {
            heldShape = heldBrick.getShapeMatrix().get(0);
        }
        return new ViewData(
                brickRotator.getCurrentShape(),
                currentOffset.x,
                currentOffset.y,
                nextShapes,
                ghostX,
                ghostY,
                heldShape
        );
    }

    /**
     * Holds the current brick, allowing the player to use it later.
     */
    /**
     * Swaps the current brick with the held brick. If no brick is currently held,
     * the current brick is moved to hold and a new brick is generated.
     * This action can only be performed once per turn.
     */
    @Override
    public void holdCurrentBrick() {
        if (holdUsedThisTurn) return;

        int spawnX = width / 2 - 1;
        int spawnY = Constants.HIDDEN_ROWS;

        if (heldBrick == null) {
            heldBrick = currentBrick;
            Brick newBrick = brickGenerator.getBrick();
            currentBrick = newBrick;
            brickRotator.setBrick(newBrick);
            currentOffset = new Point(spawnX, spawnY);
        } else {
            Brick temp = heldBrick;
            heldBrick = currentBrick;
            currentBrick = temp;
            brickRotator.setBrick(currentBrick);
            currentOffset = new Point(spawnX, spawnY);
        }

        holdUsedThisTurn = true;
    }

    /**
     * Computes the Y-coordinate where the ghost brick should be displayed.
     * The ghost brick shows where the current brick would land if a hard drop occurred.
     *
     * @return the Y-coordinate for the ghost brick.
     */
    /**
     * Computes the Y-coordinate where the ghost brick should be displayed.
     * The ghost brick shows where the current brick would land if a hard drop occurred.
     *
     * @return the Y-coordinate for the ghost brick.
     */
    private int computeGhostY() {

        int[][] temp = MatrixOperations.copy(boardMatrix);
        int[][] shape = brickRotator.getCurrentShape();

        int x = currentOffset.x;
        int y = currentOffset.y;

        int testY = y;

        while (true) {
            int nextY = testY + 1;

            if (MatrixOperations.intersect(temp, shape, x, nextY)) {
                return testY;
            }

            testY = nextY;
        }
    }

    /**
     * Merges the current brick into the board's background.
     * This typically happens when a brick lands.
     *
     * @return the type of the merged brick.
     */
    @Override
    public BrickType mergeBrickToBackground() {

        boardMatrix = MatrixOperations.merge(
                boardMatrix,
                brickRotator.getCurrentShape(),
                currentOffset.x,
                currentOffset.y
        );
        holdUsedThisTurn = false;
        return currentBrick.getBrickType();
    }

    /**
     * Clears any completed rows from the board and plays a sound effect if rows were cleared.
     *
     * @return a ClearRow object containing information about the cleared rows.
     */
    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(boardMatrix);
        if (clearRow.getLinesRemoved() > 0) {
            AudioManager.getInstance().playLineClear();
        }
        boardMatrix = clearRow.getNewBoard();
        return clearRow;
    }

    /**
     * Starts a new game, resetting the board, score, and brick states.
     */
    @Override
    public void newGame() {
        boardMatrix = new int[height][width];
        heldBrick = null;
        currentBrick = null;
        holdUsedThisTurn = false;
        score.reset();
        createNewBrick();
    }
}