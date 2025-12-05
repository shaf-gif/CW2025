package com.comp2042.ui;

import com.comp2042.JavaFxTestBase;
import com.comp2042.logic.board.Board;
import com.comp2042.logic.movement.ClearRow;
import com.comp2042.logic.movement.MoveEvent;
import com.comp2042.logic.scoring.Score;
import com.comp2042.logic.movement.EventSource;
import com.comp2042.logic.movement.EventType;
import com.comp2042.model.ViewData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameControllerTest extends JavaFxTestBase {

    @Mock
    private Board board;

    @Mock
    private GuiController guiController;

    @Mock
    private Score score;

    @Mock
    private AudioManager mockAudioManager;

    private GameController gameController;

    @BeforeEach
    public void setUp() {
        AudioManager.setInstanceForTest(mockAudioManager);
        // Stub the behavior of board.getScore() to return the mock Score object
        when(board.getScore()).thenReturn(score);
        when(board.getViewData()).thenReturn(new ViewData(new int[0][0], 0, 0, new int[0][0][0], 0, 0, new int[0][0]));

        // Now, create the GameController with the mocked dependencies
        gameController = new GameController(guiController, board);
        clearInvocations(board, guiController);
    }

    @AfterEach
    public void tearDown() {
        AudioManager.resetInstance();
    }


    @Test
    public void onDownEvent_canMove() {
        when(board.moveBrickDown()).thenReturn(true);

        gameController.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));

        verify(board).moveBrickDown();
        verify(board, never()).mergeBrickToBackground();
    }

    @Test
    public void onDownEvent_cannotMove_noRowsCleared() {
        when(board.moveBrickDown()).thenReturn(false);
        when(board.clearRows()).thenReturn(new ClearRow(0, new int[0][0], 0, new int[0]));

        gameController.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));

        verify(board).mergeBrickToBackground();
        verify(score, never()).add(anyInt());
        verify(board).createNewBrick();
        verify(guiController).refreshGameBackground(any());
    }

    @Test
    public void onDownEvent_cannotMove_rowsCleared() {
        when(board.moveBrickDown()).thenReturn(false);
        when(board.clearRows()).thenReturn(new ClearRow(1, new int[0][0], 10, new int[]{1})); // 1 line, 10 score
        when(board.getScore()).thenReturn(score);

        gameController.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));

        verify(board).mergeBrickToBackground();
        verify(score).add(10);
        verify(score).addClearedRows(1);
        verify(guiController).updateGameSpeed(anyInt());
        verify(board).createNewBrick();
        verify(guiController).refreshGameBackground(any());
    }

    @Test
    public void onHardDropEvent() {
        when(board.moveBrickDown()).thenReturn(true, true, false); // Moves down twice, then stops
        when(board.clearRows()).thenReturn(new ClearRow(0, new int[0][0], 0, new int[0]));

        gameController.onHardDropEvent(new MoveEvent(EventType.HARD_DROP, EventSource.USER));

        verify(board, times(3)).moveBrickDown();
        verify(board).mergeBrickToBackground();
        verify(board).createNewBrick();
        verify(guiController).refreshGameBackground(any());
    }

    @Test
    public void onLeftEvent() {
        gameController.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
        verify(board).moveBrickLeft();
    }

    @Test
    public void onRightEvent() {
        gameController.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
        verify(board).moveBrickRight();
    }

    @Test
    public void onRotateEvent() {
        gameController.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
        verify(board).rotateLeftBrick();
    }

    @Test
    public void onHoldEvent() {
        gameController.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER));
        verify(board).holdCurrentBrick();
    }


    @Test
    public void createNewGame() {
        gameController.createNewGame();
        verify(board).newGame();
        verify(guiController).refreshGameBackground(any());
        verify(guiController).updateGameSpeed(1);
    }
}
