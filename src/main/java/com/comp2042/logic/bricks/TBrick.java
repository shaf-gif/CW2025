package com.comp2042.logic.bricks;

import com.comp2042.logic.board.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 'T' shaped Tetris brick.
 */
public final class TBrick implements Brick {

    /** A list of 2D integer arrays, each representing a rotation of the 'T' brick. */
    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a TBrick, initializing its rotation matrices.
     */
    public TBrick() {

        // 0°
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {6, 6, 6, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        });

        // 90°
        brickMatrix.add(new int[][]{
                {0, 6, 0, 0},
                {6, 6, 0, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        });

        // 180°
        brickMatrix.add(new int[][]{
                {0, 6, 0, 0},
                {6, 6, 6, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });

        // 270°
        brickMatrix.add(new int[][]{
                {0, 6, 0, 0},
                {0, 6, 6, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

    @Override
    public BrickType getBrickType() {
        return BrickType.T;
    }
}
