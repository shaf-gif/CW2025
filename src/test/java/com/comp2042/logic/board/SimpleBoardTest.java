package com.comp2042.logic.board;

import com.comp2042.logic.Constants;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.BrickType;
import com.comp2042.logic.bricks.IBrick;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.logic.movement.ClearRow;
import com.comp2042.model.NextShapeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SimpleBoardTest {

    private SimpleBoard simpleBoard;
    private BrickGenerator mockBrickGenerator;
    private BrickRotator mockBrickRotator;

    @BeforeEach
    void setUp() {
        mockBrickGenerator = mock(BrickGenerator.class);
        mockBrickRotator = mock(BrickRotator.class);

        try {
            simpleBoard = new SimpleBoard();
            
            java.lang.reflect.Field brickGeneratorField = SimpleBoard.class.getDeclaredField("brickGenerator");
            brickGeneratorField.setAccessible(true);
            brickGeneratorField.set(simpleBoard, mockBrickGenerator);

            java.lang.reflect.Field brickRotatorField = SimpleBoard.class.getDeclaredField("brickRotator");
            brickRotatorField.setAccessible(true);
            brickRotatorField.set(simpleBoard, mockBrickRotator);

            when(mockBrickRotator.getCurrentShape()).thenReturn(new int[][]{{0}});

        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to inject mocks: " + e.getMessage());
        }
    }

    @Test
    void testInitialBoardState() {
        int[][] boardMatrix = simpleBoard.getBoardMatrix();
        assertNotNull(boardMatrix);
        assertEquals(Constants.BOARD_HEIGHT, boardMatrix.length);
        assertEquals(Constants.BOARD_WIDTH, boardMatrix[0].length);
        for (int i = 0; i < Constants.BOARD_HEIGHT; i++) {
            for (int j = 0; j < Constants.BOARD_WIDTH; j++) {
                assertEquals(0, boardMatrix[i][j], "Board should be empty initially");
            }
        }
    }

    @Test
    void testCreateNewBrick() {
        Brick mockBrick = new IBrick();
        when(mockBrickGenerator.getBrick()).thenReturn(mockBrick);
        when(mockBrickRotator.getCurrentShape()).thenReturn(mockBrick.getShapeMatrix().get(0));

        simpleBoard.createNewBrick();

        verify(mockBrickGenerator, times(1)).getBrick();
        verify(mockBrickRotator, times(1)).setBrick(mockBrick);

        try {
            java.lang.reflect.Field currentOffsetField = SimpleBoard.class.getDeclaredField("currentOffset");
            currentOffsetField.setAccessible(true);
            Point currentOffset = (Point) currentOffsetField.get(simpleBoard);

            int expectedSpawnX = Constants.BOARD_WIDTH / 2 - 1;
            int expectedSpawnY = Constants.HIDDEN_ROWS;

            assertEquals(expectedSpawnX, currentOffset.x);
            assertEquals(expectedSpawnY, currentOffset.y);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access currentOffset field: " + e.getMessage());
        }
    }

    @Test
    void testMoveBrickDown() {
        Brick mockBrick = new IBrick();
        int[][] initialShape = mockBrick.getShapeMatrix().get(0);
        when(mockBrickGenerator.getBrick()).thenReturn(mockBrick);
        when(mockBrickRotator.getCurrentShape()).thenReturn(initialShape);

        simpleBoard.createNewBrick();

        try {
            java.lang.reflect.Field currentOffsetField = SimpleBoard.class.getDeclaredField("currentOffset");
            currentOffsetField.setAccessible(true);
            Point initialOffset = (Point) currentOffsetField.get(simpleBoard);
            assertEquals(Constants.HIDDEN_ROWS, initialOffset.y);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access currentOffset field: " + e.getMessage());
        }

        when(mockBrickRotator.getCurrentShape()).thenReturn(initialShape);
        boolean moved = simpleBoard.moveBrickDown();
        assertTrue(moved);

        try {
            java.lang.reflect.Field currentOffsetField = SimpleBoard.class.getDeclaredField("currentOffset");
            currentOffsetField.setAccessible(true);
            Point afterMoveOffset = (Point) currentOffsetField.get(simpleBoard);
            assertEquals(Constants.HIDDEN_ROWS + 1, afterMoveOffset.y);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access currentOffset field: " + e.getMessage());
        }
    }

    @Test
    void testHoldCurrentBrick_initialHold() {
        Brick initialBrick = new IBrick();
        when(mockBrickGenerator.getBrick()).thenReturn(initialBrick);
        simpleBoard.createNewBrick();

        Brick nextBrick = new Brick() {
            @Override
            public List<int[][]> getShapeMatrix() {
                List<int[][]> shapes = new ArrayList<>();
                shapes.add(new int[][]{{1}});
                return shapes;
            }

            @Override
            public BrickType getBrickType() {
                return BrickType.I;
            }
        };
        when(mockBrickGenerator.getBrick()).thenReturn(nextBrick);

        simpleBoard.holdCurrentBrick();

        try {
            java.lang.reflect.Field heldBrickField = SimpleBoard.class.getDeclaredField("heldBrick");
            heldBrickField.setAccessible(true);
            Brick held = (Brick) heldBrickField.get(simpleBoard);
            assertSame(initialBrick, held);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access heldBrick field: " + e.getMessage());
        }

        try {
            java.lang.reflect.Field currentBrickField = SimpleBoard.class.getDeclaredField("currentBrick");
            currentBrickField.setAccessible(true);
            Brick current = (Brick) currentBrickField.get(simpleBoard);
            assertSame(nextBrick, current);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access currentBrick field: " + e.getMessage());
        }

        try {
            java.lang.reflect.Field holdUsedField = SimpleBoard.class.getDeclaredField("holdUsedThisTurn");
            holdUsedField.setAccessible(true);
            boolean holdUsed = (boolean) holdUsedField.get(simpleBoard);
            assertTrue(holdUsed);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access holdUsedThisTurn field: " + e.getMessage());
        }
    }

    @Test
    void testHoldCurrentBrick_swapHold() {
        Brick initialBrick = new IBrick();
        when(mockBrickGenerator.getBrick()).thenReturn(initialBrick);
        simpleBoard.createNewBrick();

        Brick heldBrickBeforeSwap = new Brick() {
            @Override
            public List<int[][]> getShapeMatrix() {
                List<int[][]> shapes = new ArrayList<>();
                shapes.add(new int[][]{{2}});
                return shapes;
            }

            @Override
            public BrickType getBrickType() {
                return BrickType.J;
            }
        };

        try {
            java.lang.reflect.Field heldBrickField = SimpleBoard.class.getDeclaredField("heldBrick");
            heldBrickField.setAccessible(true);
            heldBrickField.set(simpleBoard, heldBrickBeforeSwap);

            java.lang.reflect.Field holdUsedField = SimpleBoard.class.getDeclaredField("holdUsedThisTurn");
            holdUsedField.setAccessible(true);
            holdUsedField.set(simpleBoard, false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set up held brick for swap test: " + e.getMessage());
        }

        simpleBoard.holdCurrentBrick();

        try {
            java.lang.reflect.Field heldBrickField = SimpleBoard.class.getDeclaredField("heldBrick");
            heldBrickField.setAccessible(true);
            Brick held = (Brick) heldBrickField.get(simpleBoard);
            assertSame(initialBrick, held);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access heldBrick field: " + e.getMessage());
        }

        try {
            java.lang.reflect.Field currentBrickField = SimpleBoard.class.getDeclaredField("currentBrick");
            currentBrickField.setAccessible(true);
            Brick current = (Brick) currentBrickField.get(simpleBoard);
            assertSame(heldBrickBeforeSwap, current);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access currentBrick field: " + e.getMessage());
        }

        try {
            java.lang.reflect.Field holdUsedField = SimpleBoard.class.getDeclaredField("holdUsedThisTurn");
            holdUsedField.setAccessible(true);
            boolean holdUsed = (boolean) holdUsedField.get(simpleBoard);
            assertTrue(holdUsed);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access holdUsedThisTurn field: " + e.getMessage());
        }
    }

    @Test
    void testHoldCurrentBrick_cannotHoldTwiceInTurn() {
        Brick initialBrick = new IBrick();
        when(mockBrickGenerator.getBrick()).thenReturn(initialBrick);
        simpleBoard.createNewBrick();

        Brick nextBrick = new IBrick();
        when(mockBrickGenerator.getBrick()).thenReturn(nextBrick);
        simpleBoard.holdCurrentBrick();

        Brick currentBrickAfterFirstHold = null;
        Brick heldBrickAfterFirstHold = null;
        try {
            java.lang.reflect.Field currentBrickField = SimpleBoard.class.getDeclaredField("currentBrick");
            currentBrickField.setAccessible(true);
            currentBrickAfterFirstHold = (Brick) currentBrickField.get(simpleBoard);

            java.lang.reflect.Field heldBrickField = SimpleBoard.class.getDeclaredField("heldBrick");
            heldBrickField.setAccessible(true);
            heldBrickAfterFirstHold = (Brick) heldBrickField.get(simpleBoard);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access fields: " + e.getMessage());
        }

        simpleBoard.holdCurrentBrick();

        try {
            java.lang.reflect.Field currentBrickField = SimpleBoard.class.getDeclaredField("currentBrick");
            currentBrickField.setAccessible(true);
            assertSame(currentBrickAfterFirstHold, currentBrickField.get(simpleBoard));

            java.lang.reflect.Field heldBrickField = SimpleBoard.class.getDeclaredField("heldBrick");
            heldBrickField.setAccessible(true);
            assertSame(heldBrickAfterFirstHold, heldBrickField.get(simpleBoard));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access fields: " + e.getMessage());
        }
    }
}