package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import com.comp2042.logic.scoring.Score;

/**
 * A brick generation strategy that uses a "bag" of bricks to ensure a random but fair distribution.
 */
public class RandomBrickGenerationStrategy implements BrickGenerationStrategy {

    /** The probability (0.0-1.0) of generating a Slow Brick when the level is 3 or higher. */
    private static final double SLOW_BRICK_CHANCE = 0.15;

    /** The game's score object, used to determine the current level. */
    private final Score score;

    /** A queue of bricks ready to be delivered to the game. */
    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    /** A list of bricks representing the current "bag" for random selection. */
    private List<Brick> bag = new ArrayList<>();

    /**
     * Constructs a new RandomBrickGenerationStrategy.
     *
     * @param score the score object to be used for level-based logic.
     */
    public RandomBrickGenerationStrategy(Score score) {
        this.score = score;
        refillBag();
        ensureQueueSize(3);
    }

    /**
     * Default constructor for compatibility.
     * Can be removed if all usage sites are updated.
     */
    public RandomBrickGenerationStrategy() {
        this(new Score());
    }

    /**
     * Refills the internal bag with a new set of all seven standard Tetris bricks (I, J, L, O, S, T, Z)
     * and shuffles them. Ensures that the first brick in the new bag is not the same type as the last
     * brick delivered, if possible.
     */
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

    /**
     * Ensures that the {@code nextBricks} queue has at least {@code minSize} bricks.
     * If the queue falls below {@code minSize}, new bricks are added from the bag,
     * refilling the bag if necessary.
     * @param minSize The minimum desired size of the {@code nextBricks} queue.
     */
    private void ensureQueueSize(int minSize) {
        while (nextBricks.size() < minSize) {
            if (bag.isEmpty()) refillBag();
            nextBricks.add(bag.remove(0));
        }
    }

    /**
     * Generates the next brick to be used in the game.
     * This method also incorporates logic to potentially introduce a {@code SlowBrick}
     * if the game level is 3 or higher, based on {@code SLOW_BRICK_CHANCE}.
     *
     * @return The next {@code Brick} in the sequence.
     */
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

    /**
     * Peeks at the next brick in the sequence without consuming it.
     *
     * @return the next brick.
     */
    /**
     * Peeks at the next brick in the sequence without consuming it.
     * Ensures the queue has at least one brick before peeking.
     *
     * @return The next {@code Brick} that would be generated.
     */
    public Brick peekNextBrick() {
        ensureQueueSize(1);
        return nextBricks.peek();
    }

    /**
     * Peeks at the next 'count' bricks in the sequence without consuming them.
     *
     * @param count the number of bricks to peek.
     * @return a list of the next bricks.
     */
    /**
     * Peeks at the next 'count' bricks in the sequence without consuming them.
     * Ensures the queue has enough bricks, refilling the bag if necessary.
     * The returned list is unmodifiable.
     *
     * @param count The number of bricks to peek.
     * @return A list of the next {@code Brick} objects.
     */
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
