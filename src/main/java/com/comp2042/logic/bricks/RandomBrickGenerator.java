package com.comp2042.logic.bricks;

import com.comp2042.logic.scoring.Score;
import java.util.List;

/**
 * A brick generator that uses a strategy to create new bricks.
 */
public final class RandomBrickGenerator implements BrickGenerator {

    /** The current brick generation strategy being used. */
    private BrickGenerationStrategy strategy;
    /**
     * A reference to the {@code RandomBrickGenerationStrategy} instance,
     * used for peeking at upcoming bricks. This is {@code null} if a non-random strategy is set.
     */
    private RandomBrickGenerationStrategy randomStrategy;

    /**
     * Constructs a new RandomBrickGenerator with a default random strategy.
     *
     * @param score the score object to be used by the generation strategy.
     */
    public RandomBrickGenerator(Score score) {
        this.strategy = new RandomBrickGenerationStrategy(score);
        this.randomStrategy = (RandomBrickGenerationStrategy) this.strategy;
    }

    /**
     * Sets the brick generation strategy.
     *
     * @param strategy the strategy to use.
     */
    /**
     * Sets the brick generation strategy to be used.
     * If the provided strategy is not an instance of {@code RandomBrickGenerationStrategy},
     * an {@code IllegalArgumentException} is thrown as this generator specifically relies on it.
     *
     * @param strategy The new brick generation strategy to use.
     * @throws IllegalArgumentException if the provided strategy is not a {@code RandomBrickGenerationStrategy}.
     */
    public void setStrategy(BrickGenerationStrategy strategy) {
        this.strategy = strategy;
        if (strategy instanceof RandomBrickGenerationStrategy) {
            this.randomStrategy = (RandomBrickGenerationStrategy) strategy;
        } else {
            throw new IllegalArgumentException("RandomBrickGenerator expects a RandomBrickGenerationStrategy initialized with a Score.");
        }
    }

    @Override
    public Brick getBrick() {
        return strategy.generateNextBrick();
    }

    @Override
    public Brick getNextBrick() {
        return randomStrategy.peekNextBrick();
    }

    @Override
    public List<Brick> peekNext(int count) {
        return randomStrategy.peekNextBricks(count);
    }
}