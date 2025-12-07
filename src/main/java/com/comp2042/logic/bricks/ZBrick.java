package com.comp2042.logic.bricks;

import com.comp2042.logic.board.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 'Z' shaped Tetris brick.
 */
public final class ZBrick implements Brick {

    /** A list of 2D integer arrays, each representing a rotation of the 'Z' brick. */
    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a ZBrick, initializing its rotation matrices.
     */
    public ZBrick() {

        // 0°
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {7, 7, 0, 0},
                {0, 7, 7, 0},
                {0, 0, 0, 0}
        });

        // 90°
        brickMatrix.add(new int[][]{
                {0, 7, 0, 0},
                {7, 7, 0, 0},
                {7, 0, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

    @Override
    public BrickType getBrickType() {
        return BrickType.Z;
    }
}
