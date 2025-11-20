package com.comp2042.logic.bricks;

public interface BrickGenerator {
    Brick getBrick();          // returns the current brick (and advances queue)
    Brick getNextBrick();      // returns the next preview brick
}
