package com.comp2042.logic.bricks;

import com.comp2042.logic.board.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 'S' shaped Tetris brick.
 */
public final class SBrick implements Brick {

    /** A list of 2D integer arrays, each representing a rotation of the 'S' brick. */
    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs an SBrick, initializing its rotation matrices.
     */
    public SBrick() {

        // 0°
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 5, 5, 0},
                {5, 5, 0, 0},
                {0, 0, 0, 0}
        });

        // 90°
        brickMatrix.add(new int[][]{
                {5, 0, 0, 0},
                {5, 5, 0, 0},
                {0, 5, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

    @Override
    public BrickType getBrickType() {
        return BrickType.S;
    }
}
