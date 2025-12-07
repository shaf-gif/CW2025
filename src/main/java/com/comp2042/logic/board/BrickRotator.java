package com.comp2042.logic.board;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.model.NextShapeInfo;

import java.util.List;

/**
 * Manages the rotation of a Tetris brick.
 */
public class BrickRotator {

    /**
     * Constructs a new BrickRotator.
     * This class manages the rotation state and logic for a Tetris brick.
     */
    public BrickRotator() {
        // Default constructor
    }

    /** A list of 2D integer arrays, each representing a possible rotation of the brick. */
    private List<int[][]> rotations;
    /** The index of the current rotation in the {@code rotations} list. */
    private int currentRotationIndex = 0;

    /**
     * Sets the brick to be rotated.
     *
     * @param brick the brick to set.
     */
    public void setBrick(Brick brick) {
        this.rotations = brick.getShapeMatrix();
        this.currentRotationIndex = 0; // spawn always uses rotation 0
    }

    /**
     * Gets the current shape of the brick based on its current rotation.
     *
     * @return a 2D integer array representing the current shape.
     */
    public int[][] getCurrentShape() {
        return rotations.get(currentRotationIndex);
    }

    /**
     * Gets the next shape of the brick (after rotating it once).
     *
     * @return a NextShapeInfo object containing the next shape and its index.
     */
    public NextShapeInfo getNextShape() {

        int nextIndex = (currentRotationIndex + 1) % rotations.size();

        return new NextShapeInfo(
                rotations.get(nextIndex),
                nextIndex
        );
    }

    /**
     * Sets the current rotation of the brick to a specific index.
     *
     * @param newIndex the index of the new rotation.
     * @throws IndexOutOfBoundsException if the newIndex is out of bounds.
     */
    public void setCurrentShape(int newIndex) {
        if (newIndex < 0 || newIndex >= rotations.size()) {
            throw new IndexOutOfBoundsException("Rotation index " + newIndex + " is out of bounds for rotations size " + rotations.size());
        }
        this.currentRotationIndex = newIndex;
    }
}
