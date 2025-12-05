package com.comp2042.logic.bricks;

import java.util.List;

public final class RandomBrickGenerator implements BrickGenerator {

    private BrickGenerationStrategy strategy;
    private RandomBrickGenerationStrategy randomStrategy; // Keep a reference to the default for peek methods

    public RandomBrickGenerator() {
        this(new RandomBrickGenerationStrategy());
    }

    public RandomBrickGenerator(BrickGenerationStrategy strategy) {
        this.strategy = strategy;
        if (strategy instanceof RandomBrickGenerationStrategy) {
            this.randomStrategy = (RandomBrickGenerationStrategy) strategy;
        } else {
            // If a different strategy is used, peekNext and getNextBrick won't work as expected
            // without additional handling in the new strategy or an adapter.
            // For now, we assume RandomBrickGenerationStrategy is the default for these methods.
            this.randomStrategy = new RandomBrickGenerationStrategy(); // Fallback
        }
    }

    public void setStrategy(BrickGenerationStrategy strategy) {
        this.strategy = strategy;
        if (strategy instanceof RandomBrickGenerationStrategy) {
            this.randomStrategy = (RandomBrickGenerationStrategy) strategy;
        } else {
            this.randomStrategy = new RandomBrickGenerationStrategy(); // Fallback
        }
    }

    @Override
    public Brick getBrick() {
        return strategy.generateNextBrick();
    }

    @Override
    public Brick getNextBrick() {
        // This method is specific to the stateful RandomBrickGenerationStrategy.
        // If a different strategy is used, this might not make sense or need adaptation.
        return randomStrategy.peekNextBrick();
    }

    @Override
    public List<Brick> peekNext(int count) {
        // This method is specific to the stateful RandomBrickGenerationStrategy.
        // If a different strategy is used, this might not make sense or need adaptation.
        return randomStrategy.peekNextBricks(count);
    }
}