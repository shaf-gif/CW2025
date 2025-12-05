package com.comp2042.logic.board;

import com.comp2042.ui.AudioManager;
import com.comp2042.logic.Constants;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerationStrategy;
import com.comp2042.logic.movement.ClearRow;
import com.comp2042.logic.scoring.Score;
import com.comp2042.model.NextShapeInfo;
import com.comp2042.model.ViewData;

import java.awt.Point;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;

    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;

    private int[][] boardMatrix;
    private Point currentOffset;

    // Hold feature state
    private Brick heldBrick;
    private Brick currentBrick;
    private boolean holdUsedThisTurn;

    private final Score score;

    public SimpleBoard() {
        this.width = Constants.BOARD_WIDTH;
        this.height = Constants.BOARD_HEIGHT;

        boardMatrix = new int[height][width];

        brickGenerator = new RandomBrickGenerator(new RandomBrickGenerationStrategy());
        brickRotator = new BrickRotator();

        score = new Score();
    }

    // ----------------------------------------------------
    // MOVEMENT
    // ----------------------------------------------------

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

    // ----------------------------------------------------
    // SPAWN NEW BRICK
    // ----------------------------------------------------

    @Override
    public boolean createNewBrick() {

        Brick newBrick = brickGenerator.getBrick();
        currentBrick = newBrick;
        brickRotator.setBrick(newBrick);

        int spawnX = width / 2 - 1;            // centered
        int spawnY = Constants.HIDDEN_ROWS;    // behind hidden rows

        currentOffset = new Point(spawnX, spawnY);
        holdUsedThisTurn = false; // allow hold on new piece

        // If collision on spawn → game over
        return MatrixOperations.intersect(
                boardMatrix,
                brickRotator.getCurrentShape(),
                currentOffset.x,
                currentOffset.y
        );
    }

    // ----------------------------------------------------
    // BOARD GETTERS
    // ----------------------------------------------------

    @Override
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    @Override
    public Score getScore() {
        return score;
    }

    // ----------------------------------------------------
    // GHOST + VIEW DATA
    // ----------------------------------------------------

    @Override
    public ViewData getViewData() {

        int ghostX = currentOffset.x;
        int ghostY = computeGhostY();

        // Collect up to three next preview shapes
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

    @Override
    public void holdCurrentBrick() {
        if (holdUsedThisTurn) return;

        int spawnX = width / 2 - 1;
        int spawnY = Constants.HIDDEN_ROWS;

        if (heldBrick == null) {
            // Store current and spawn a new one
            heldBrick = currentBrick;
            Brick newBrick = brickGenerator.getBrick();
            currentBrick = newBrick;
            brickRotator.setBrick(newBrick);
            currentOffset = new Point(spawnX, spawnY);
        } else {
            // Swap held with current
            Brick temp = heldBrick;
            heldBrick = currentBrick;
            currentBrick = temp;
            brickRotator.setBrick(currentBrick);
            currentOffset = new Point(spawnX, spawnY);
        }

        holdUsedThisTurn = true;
    }

    private int computeGhostY() {

        int[][] temp = MatrixOperations.copy(boardMatrix);
        int[][] shape = brickRotator.getCurrentShape();

        int x = currentOffset.x;
        int y = currentOffset.y;

        int testY = y;

        while (true) {
            int nextY = testY + 1;

            if (MatrixOperations.intersect(temp, shape, x, nextY)) {
                return testY; // ghost stops here
            }

            testY = nextY;
        }
    }

    // ----------------------------------------------------
    // MERGE + CLEAR
    // ----------------------------------------------------

    @Override
    public void mergeBrickToBackground() {

        boardMatrix = MatrixOperations.merge(
                boardMatrix,
                brickRotator.getCurrentShape(),
                currentOffset.x,
                currentOffset.y
        );
        // After locking a piece, allow hold again on the next piece
        holdUsedThisTurn = false;
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(boardMatrix);
        if (clearRow.getLinesRemoved() > 0) {
            AudioManager.getInstance().playLineClear();
        }
        boardMatrix = clearRow.getNewBoard();
        return clearRow;
    }

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