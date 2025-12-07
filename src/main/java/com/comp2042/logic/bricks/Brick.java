package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Represents a Tetris brick, defining the common behavior for all brick types.
 */
public interface Brick {

    /**
     * Gets the shape matrix of the brick. Each `int[][]` in the list represents a rotation of the brick.
     *
     * @return a list of 2D integer arrays representing the brick's shapes.
     */
    List<int[][]> getShapeMatrix();

    /**
     * Gets the type of the brick.
     *
     * @return the brick type.
     */
    BrickType getBrickType();
}
