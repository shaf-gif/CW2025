package com.comp2042.logic.bricks;

import com.comp2042.logic.board.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

public final class OBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

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
}