package com.comp2042.logic.board;

import com.comp2042.logic.Constants;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
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

    private final Score score;

    public SimpleBoard() {
        this.width = Constants.BOARD_WIDTH;
        this.height = Constants.BOARD_HEIGHT;

        boardMatrix = new int[height][width];

        brickGenerator = new RandomBrickGenerator();
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
        brickRotator.setBrick(newBrick);

        int spawnX = width / 2 - 1;            // centered
        int spawnY = Constants.HIDDEN_ROWS;    // behind hidden rows

        currentOffset = new Point(spawnX, spawnY);

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

        return new ViewData(
                brickRotator.getCurrentShape(),
                currentOffset.x,
                currentOffset.y,
                brickGenerator.getNextBrick().getShapeMatrix().get(0),
                ghostX,
                ghostY
        );
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
    }

    @Override
    public ClearRow clearRows() {

        ClearRow row = MatrixOperations.checkRemoving(boardMatrix);
        boardMatrix = row.getNewMatrix();
        return row;
    }

    // ----------------------------------------------------
    // NEW GAME
    // ----------------------------------------------------

    @Override
    public void newGame() {
        boardMatrix = new int[height][width];
        score.reset();
        createNewBrick();
    }
}
