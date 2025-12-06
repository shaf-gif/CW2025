package com.comp2042.logic.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.comp2042.logic.bricks.BrickType;
import com.comp2042.logic.scoring.Score;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RandomBrickGeneratorTest {

    private RandomBrickGenerator generator;
    private Score mockScore;

    @BeforeEach
    void setUp() {
        mockScore = mock(Score.class);
        generator = new RandomBrickGenerator(mockScore);
    }



    @Test
    void testInitialQueueSizeIsPrefilled() {
        when(mockScore.getLevel()).thenReturn(3);
        List<Brick> initialPeek = generator.peekNext(10);
        assertTrue(initialPeek.size() >= 3, "Initial queue must be prefilled to at least size 3.");
    }

    @Test
    void testGetBrickAdvancesQueueAndRefills() {
        when(mockScore.getLevel()).thenReturn(3);
        Brick firstPiece = generator.getBrick();
        Brick secondPiece = generator.getBrick();

        assertEquals(3, generator.peekNext(3).size(), "Queue size should be maintained at 3 or more after generation.");
        assertNotSame(firstPiece, secondPiece, "Generated pieces must be unique objects.");
    }

    @Test
    void testBagContainsAllSevenTypes() {
        when(mockScore.getLevel()).thenReturn(3);
        Set<BrickType> typesGenerated = new HashSet<>();
        boolean slowBrickFound = false;

        for (int i = 0; i < 50; i++) {
            Brick brick = generator.getBrick();
            typesGenerated.add(brick.getBrickType());
            if (brick.getBrickType() == BrickType.SLOW) {
                slowBrickFound = true;
            }
        }

        assertTrue(typesGenerated.size() >= 7,
                "Should generate at least 7 unique brick types (standard Tetris pieces).");
        assertTrue(typesGenerated.contains(BrickType.I));
        assertTrue(typesGenerated.contains(BrickType.J));
        assertTrue(typesGenerated.contains(BrickType.L));
        assertTrue(typesGenerated.contains(BrickType.O));
        assertTrue(typesGenerated.contains(BrickType.S));
        assertTrue(typesGenerated.contains(BrickType.T));
        assertTrue(typesGenerated.contains(BrickType.Z));
        assertTrue(slowBrickFound, "SlowBrick should be generated at least once over many iterations.");
    }

    @Test
    void testPeekReturnsCorrectCount() {
        when(mockScore.getLevel()).thenReturn(3);
        List<Brick> peeked = generator.peekNext(5);
        assertEquals(5, peeked.size(), "Peek must return the requested count of 5.");
    }

    @Test
    void testPeekDoesNotAdvanceQueue() {
        when(mockScore.getLevel()).thenReturn(3);
        Class<? extends Brick> firstPeekedType = generator.peekNext(1).get(0).getClass();
        Class<? extends Brick> generatedType = generator.getBrick().getClass();

        assertEquals(firstPeekedType, generatedType,
                "getBrick must return the piece that was at the head of the queue.");

        Class<? extends Brick> secondPeekedType = generator.peekNext(1).get(0).getClass();

        assertNotEquals(generatedType, secondPeekedType,
                "The piece returned by peek after generation must be different from the piece just generated.");
    }

    @Test
    void testPeekReturnsImmutableList() {
        when(mockScore.getLevel()).thenReturn(3);
        List<Brick> peeked = generator.peekNext(3);
        assertThrows(UnsupportedOperationException.class, () -> {
            peeked.clear();
        }, "The list returned by peekNext should be immutable.");
    }
    
    @Test
    void testSlowBrickGenerationProbability() {
        when(mockScore.getLevel()).thenReturn(3);

        int totalGenerations = 1000;
        int slowBrickCount = 0;
        double expectedChance = 0.1;
        double tolerance = 0.05;

        for (int i = 0; i < totalGenerations; i++) {
            if (generator.getBrick().getBrickType() == BrickType.SLOW) {
                slowBrickCount++;
            }
        }

        double actualChance = (double) slowBrickCount / totalGenerations;

        assertTrue(actualChance >= expectedChance - tolerance && actualChance <= expectedChance + tolerance,
                String.format("SlowBrick generation probability (%.2f) should be around %.2f (tolerance %.2f)",
                        actualChance, expectedChance, tolerance));
    }

    @Test
    void testSlowBrickGenerationNoPriorToLevel3() {
        when(mockScore.getLevel()).thenReturn(1);

        int totalGenerations = 100;
        int slowBrickCount = 0;

        for (int i = 0; i < totalGenerations; i++) {
            if (generator.getBrick().getBrickType() == BrickType.SLOW) {
                slowBrickCount++;
            }
        }

        assertEquals(0, slowBrickCount, "No SlowBricks should be generated when level is below 3.");
    }
}