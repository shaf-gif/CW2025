package com.comp2042.logic.bricks;

import com.comp2042.logic.board.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 'O' shaped Tetris brick.
 */
public final class OBrick implements Brick {

    /** A list of 2D integer arrays, each representing a rotation of the 'O' brick. */
    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs an OBrick, initializing its rotation matrices.
     */
    public OBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0}, // NEW: Empty Row 0
                {0, 4, 4, 0}, // Row 1 (The piece itself)
                {0, 4, 4, 0}, // Row 2 (The piece itself)
                {0, 0, 0, 0}  // Row 3
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

    @Override
    public BrickType getBrickType() {
        return BrickType.O;
    }
}