package com.comp2042.logic.bricks;

import com.comp2042.logic.board.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 'Slow' special brick.
 */
public final class SlowBrick implements Brick {

    /** A list of 2D integer arrays, each representing a rotation of the 'SLOW' brick. */
    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a SlowBrick, initializing its rotation matrices.
     */
    public SlowBrick() {
        // A simple 2x2 shape for the SlowBrick
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 8, 8, 0},
                {0, 8, 8, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

    @Override
    public BrickType getBrickType() {
        return BrickType.SLOW;
    }
}
