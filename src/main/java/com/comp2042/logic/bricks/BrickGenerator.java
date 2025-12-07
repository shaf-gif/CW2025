package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Defines the contract for brick generation logic.
 */
public interface BrickGenerator {
    /**
     * Gets the current brick and advances the generation queue.
     *
     * @return the current brick.
     */
    Brick getBrick();

    /**
     * Gets the next brick in the sequence for preview purposes.
     *
     * @return the next brick.
     */
    Brick getNextBrick();

    /**
     * Peeks at the next 'count' bricks in the sequence without consuming them.
     *
     * @param count the number of bricks to peek.
     * @return a list of the next bricks.
     */
    List<Brick> peekNext(int count);
}
