package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import com.comp2042.logic.scoring.Score;

public class RandomBrickGenerationStrategy implements BrickGenerationStrategy {

    private static final double SLOW_BRICK_CHANCE = 0.15;

    private final Score score;

    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    private List<Brick> bag = new ArrayList<>();

    public RandomBrickGenerationStrategy(Score score) {
        this.score = score;
        refillBag();
        ensureQueueSize(3);
    }

    // Default constructor for compatibility, can be removed if all usage sites are updated
    public RandomBrickGenerationStrategy() {
        this(new Score());
    }

    private void refillBag() {
        List<Brick> fresh = new ArrayList<>(7);
        fresh.add(new IBrick());
        fresh.add(new JBrick());
        fresh.add(new LBrick());
        fresh.add(new OBrick());
        fresh.add(new SBrick());
        fresh.add(new TBrick());
        fresh.add(new ZBrick());

        Collections.shuffle(fresh, ThreadLocalRandom.current());
        bag = fresh;

        if (!nextBricks.isEmpty() && !bag.isEmpty()) {
            Brick tail = nextBricks.peekLast();
            if (tail != null && tail.getClass().equals(bag.get(0).getClass())) {
                Brick first = bag.remove(0);
                bag.add(first);
            }
        }
    }

    private void ensureQueueSize(int minSize) {
        while (nextBricks.size() < minSize) {
            if (bag.isEmpty()) refillBag();
            nextBricks.add(bag.remove(0));
        }
    }

    @Override
    public Brick generateNextBrick() {
        ensureQueueSize(1);

        Brick nextToDeliver = nextBricks.peek();

        if (score.getLevel() >= 3 &&
            nextToDeliver.getBrickType() != BrickType.SLOW &&
            ThreadLocalRandom.current().nextDouble() < SLOW_BRICK_CHANCE) {
            nextBricks.poll();
            nextBricks.addFirst(new SlowBrick());
        }

        Brick current = nextBricks.poll();
        ensureQueueSize(3);
        return current;
    }

    public Brick peekNextBrick() {
        ensureQueueSize(1);
        return nextBricks.peek();
    }

    public List<Brick> peekNextBricks(int count) {
        ensureQueueSize(Math.max(1, count));
        List<Brick> result = new ArrayList<>();
        int i = 0;
        for (Brick b : nextBricks) {
            if (i++ >= count) break;
            result.add(b);
        }
        return Collections.unmodifiableList(result);
    }
}
