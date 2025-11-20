package com.comp2042;

import com.comp2042.logic.movement.DownData;
import com.comp2042.logic.movement.MoveEvent;
import com.comp2042.model.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    // NEW for hard drop
    DownData onHardDropEvent(MoveEvent event);

    void createNewGame();
}

