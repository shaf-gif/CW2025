package com.comp2042.logic.bricks;

/**
 * Defines the contract for brick generation strategies.
 */
public interface BrickGenerationStrategy {
    /**
     * Generates the next brick in the sequence.
     *
     * @return the next brick.
     */
    Brick generateNextBrick();
}
