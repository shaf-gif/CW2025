package com.comp2042.logic.bricks;

import com.comp2042.logic.board.MatrixOperations;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList = new ArrayList<>();
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    public RandomBrickGenerator() {

        // Add bricks in the standard Tetris color order
        brickList.add(new IBrick());  // 1
        brickList.add(new JBrick());  // 2
        brickList.add(new LBrick());  // 3
        brickList.add(new OBrick());  // 4
        brickList.add(new SBrick());  // 5
        brickList.add(new TBrick());  // 6
        brickList.add(new ZBrick());  // 7

        // Fill queue with two random pieces to start
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
    }

    @Override
    public Brick getBrick() {

        // If we are about to run low → add new random brick
        if (nextBricks.size() <= 1) {
            nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        }

        // Return & remove the head of the queue
        return nextBricks.poll();
    }

    @Override
    public Brick getNextBrick() {
        // Peek = show without removing
        return nextBricks.peek();
    }
}
