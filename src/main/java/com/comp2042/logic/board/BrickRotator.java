package com.comp2042.logic.board;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.model.NextShapeInfo;

import java.util.List;

public class BrickRotator {

    private List<int[][]> rotations;
    private int currentRotationIndex = 0;

    public void setBrick(Brick brick) {
        this.rotations = brick.getShapeMatrix();
        this.currentRotationIndex = 0; // spawn always uses rotation 0
    }

    public int[][] getCurrentShape() {
        return rotations.get(currentRotationIndex);
    }

    public NextShapeInfo getNextShape() {

        int nextIndex = (currentRotationIndex + 1) % rotations.size();

        return new NextShapeInfo(
                rotations.get(nextIndex),
                nextIndex
        );
    }

    public void setCurrentShape(int newIndex) {
        if (newIndex < 0 || newIndex >= rotations.size()) {
            throw new IndexOutOfBoundsException("Rotation index " + newIndex + " is out of bounds for rotations size " + rotations.size());
        }
        this.currentRotationIndex = newIndex;
    }
}
