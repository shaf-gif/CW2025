package com.comp2042.logic.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// Note: Because Collections.shuffle uses Random internally, and Mockito cannot easily mock
// ThreadLocalRandom, we will test the structural integrity and boundary conditions,
// assuming the built-in Java shuffle works correctly.

@ExtendWith(MockitoExtension.class)
public class RandomBrickGeneratorTest {

    private RandomBrickGenerator generator;

    @BeforeEach
    void setUp() {
        // Initialize the generator. This automatically fills the first bag and queue.
        generator = new RandomBrickGenerator();
    }

    // ----------------------------------------------------
    // INITIALIZATION & QUEUE MANAGEMENT TESTS
    // ----------------------------------------------------

    @Test
    void testInitialQueueSizeIsPrefilled() {
        // The constructor calls ensureQueueSize(3)
        // Since getBrick() advances the queue, we use peekNext to check size.
        List<Brick> initialPeek = generator.peekNext(10); // Peek well past 3

        // Initial queue size must be at least 3, and the first bag is 7 pieces total.
        // It's possible the entire first bag (7 pieces) is immediately pulled into the queue.
        // We verify that the first 3 pieces are present.
        assertTrue(initialPeek.size() >= 3, "Initial queue must be prefilled to at least size 3.");
    }

    @Test
    void testGetBrickAdvancesQueueAndRefills() {
        // Get the first piece and note its type
        Brick firstPiece = generator.getBrick();

        // Get the second piece
        Brick secondPiece = generator.getBrick();

        // After two generations, the queue should still have at least 3 pieces ready (3 + 2 generated - 2 consumed = 3)
        assertEquals(3, generator.peekNext(3).size(), "Queue size should be maintained at 3 or more after generation.");

        // Ensure the two generated pieces are different objects
        assertNotSame(firstPiece, secondPiece, "Generated pieces must be unique objects.");
    }

    // ----------------------------------------------------
    // BAG INTEGRITY TESTS
    // ----------------------------------------------------

    @Test
    void testBagContainsAllSevenTypes() {
        Set<Class<? extends Brick>> typesGenerated = new HashSet<>();

        // FIX: Consume enough pieces to guarantee at least one full bag cycle (7 pieces)
        // The queue starts with a few, so we pull 10 to ensure we hit a refill.
        for (int i = 0; i < 10; i++) {
            typesGenerated.add(generator.getBrick().getClass());
        }

        // Still check for all 7 types, as the system MUST produce them
        assertEquals(7, typesGenerated.size(),
                "Generating pieces must yield all 7 unique types (confirms 7-bag works).");
    }

    // ----------------------------------------------------
    // PEEKING TESTS
    // ----------------------------------------------------

    @Test
    void testPeekReturnsCorrectCount() {
        // Request 5 pieces
        List<Brick> peeked = generator.peekNext(5);
        assertEquals(5, peeked.size(), "Peek must return the requested count of 5.");
    }

    @Test
    void testPeekDoesNotAdvanceQueue() {
        // Get the first peeked piece's class
        Class<? extends Brick> firstPeekedType = generator.peekNext(1).get(0).getClass();

        // Generate the piece (advance the queue)
        Class<? extends Brick> generatedType = generator.getBrick().getClass();

        // Verify the piece returned by getBrick was indeed the first one peeked
        assertEquals(firstPeekedType, generatedType,
                "getBrick must return the piece that was at the head of the queue.");

        // Now, peek the next piece. It should be the second piece from the initial sequence.
        Class<? extends Brick> secondPeekedType = generator.peekNext(1).get(0).getClass();

        assertNotEquals(generatedType, secondPeekedType,
                "The piece returned by peek after generation must be different from the piece just generated.");
    }

    @Test
    void testPeekReturnsImmutableList() {
        List<Brick> peeked = generator.peekNext(3);

        // Verify that the list is unmodifiable (or at least throws an exception if we try to clear it)
        assertThrows(UnsupportedOperationException.class, () -> {
            peeked.clear();
        }, "The list returned by peekNext should be immutable.");
    }
}