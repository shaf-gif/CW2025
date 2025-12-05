package com.comp2042.logic.bricks;

/**
 * Strategy interface for different brick generation algorithms.
 */
public interface BrickGenerationStrategy {
    Brick generateNextBrick();
}
