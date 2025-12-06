package com.comp2042.logic.board;

import com.comp2042.logic.bricks.BrickType;
import com.comp2042.logic.movement.ClearRow;
import com.comp2042.logic.scoring.Score;
import com.comp2042.model.ViewData;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    void holdCurrentBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    BrickType mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();
}
