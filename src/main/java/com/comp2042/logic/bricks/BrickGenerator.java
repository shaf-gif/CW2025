package com.comp2042.logic.bricks;

import java.util.List;

public interface BrickGenerator {
    Brick getBrick();          // returns the current brick (and advances queue)
    Brick getNextBrick();      // returns the next preview brick (legacy single peek)
    List<Brick> peekNext(int count); // returns up to 'count' next bricks without consuming
}
