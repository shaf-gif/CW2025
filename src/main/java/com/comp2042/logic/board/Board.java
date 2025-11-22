package com.comp2042.logic.board;

import com.comp2042.logic.movement.ClearRow;
import com.comp2042.logic.scoring.Score;
import com.comp2042.model.ViewData;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    // Hold current piece (store/swap). Does nothing if already used this turn or if swap would collide.
    void holdCurrentBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();
}
