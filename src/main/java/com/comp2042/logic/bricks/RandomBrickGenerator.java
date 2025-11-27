package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomBrickGenerator implements BrickGenerator {

    // Preview queue consumed by the game (head = current, rest = next previews)
    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    // Current 7-bag we are drawing from (empties, then refilled & shuffled)
    private List<Brick> bag = new ArrayList<>();

    public RandomBrickGenerator() {
        // Initialize with a shuffled 7-bag and prefill previews so UI can peek 3
        refillBag();
        ensureQueueSize(3);
    }

    // Create a fresh 7-bag (one of each piece) and shuffle it
    private void refillBag() {
        List<Brick> fresh = new ArrayList<>(7);
        fresh.add(new IBrick());
        fresh.add(new JBrick());
        fresh.add(new LBrick());
        fresh.add(new OBrick());
        fresh.add(new SBrick());
        fresh.add(new TBrick());
        fresh.add(new ZBrick());

        // Shuffle with ThreadLocalRandom
        Collections.shuffle(fresh, ThreadLocalRandom.current());
        bag = fresh;

        // Avoid immediate triplicate across bag boundary
        if (!nextBricks.isEmpty() && !bag.isEmpty()) {
            Brick tail = nextBricks.peekLast();
            if (tail != null && tail.getClass().equals(bag.get(0).getClass())) {
                // Move the first element to the end to reduce back-to-back repeats across bags
                Brick first = bag.remove(0);
                bag.add(first);
            }
        }
    }

    // Ensure the preview queue has at least minSize items by pulling from the bag
    private void ensureQueueSize(int minSize) {
        while (nextBricks.size() < minSize) {
            if (bag.isEmpty()) refillBag();
            nextBricks.add(bag.remove(0));
        }
    }

    @Override
    public Brick getBrick() {
        // Ensure there is at least one element to serve
        ensureQueueSize(1);
        // Return & remove the head of the queue
        Brick current = nextBricks.poll();
        // Top up previews so UI can always show 3
        ensureQueueSize(3);
        return current;
    }

    @Override
    public Brick getNextBrick() {
        // Legacy single peek
        ensureQueueSize(1);
        return nextBricks.peek();
    }

    @Override
    public List<Brick> peekNext(int count) {
        ensureQueueSize(Math.max(1, count));
        List<Brick> result = new ArrayList<>();
        int i = 0;
        for (Brick b : nextBricks) {
            // Only add up to 'count' pieces
            if (i++ >= count) break;
            result.add(b);
        }

        return Collections.unmodifiableList(result);
    }
}