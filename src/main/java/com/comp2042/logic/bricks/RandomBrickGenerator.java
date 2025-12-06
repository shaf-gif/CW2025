package com.comp2042.logic.bricks;

import com.comp2042.logic.scoring.Score;
import java.util.List;

public final class RandomBrickGenerator implements BrickGenerator {

    private BrickGenerationStrategy strategy;
    private RandomBrickGenerationStrategy randomStrategy;

    public RandomBrickGenerator(Score score) {
        this.strategy = new RandomBrickGenerationStrategy(score);
        this.randomStrategy = (RandomBrickGenerationStrategy) this.strategy;
    }

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