package com.comp2042.logic.movement;

import com.comp2042.model.ViewData;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class InputEventListenerTest {

    @Test
    void testInterfaceMethods() {
        // Create a mock implementation of the interface
        InputEventListener mockListener = mock(InputEventListener.class);

        // Define behavior for the mock methods (optional, but shows methods are callable)
        when(mockListener.onDownEvent(any(MoveEvent.class))).thenReturn(mock(DownData.class));
        when(mockListener.onLeftEvent(any(MoveEvent.class))).thenReturn(mock(ViewData.class));
        when(mockListener.onRightEvent(any(MoveEvent.class))).thenReturn(mock(ViewData.class));
        when(mockListener.onRotateEvent(any(MoveEvent.class))).thenReturn(mock(ViewData.class));
        when(mockListener.onHardDropEvent(any(MoveEvent.class))).thenReturn(mock(DownData.class));
        when(mockListener.onHoldEvent(any(MoveEvent.class))).thenReturn(mock(ViewData.class));

        // Call the methods on the mock object
        mockListener.onDownEvent(mock(MoveEvent.class));
        mockListener.onLeftEvent(mock(MoveEvent.class));
        mockListener.onRightEvent(mock(MoveEvent.class));
        mockListener.onRotateEvent(mock(MoveEvent.class));
        mockListener.onHardDropEvent(mock(MoveEvent.class));
        mockListener.onHoldEvent(mock(MoveEvent.class));
        mockListener.createNewGame();

        // Verify that the methods were called (optional, but good practice for mocks)
        verify(mockListener, times(1)).onDownEvent(any(MoveEvent.class));
        verify(mockListener, times(1)).onLeftEvent(any(MoveEvent.class));
        verify(mockListener, times(1)).onRightEvent(any(MoveEvent.class));
        verify(mockListener, times(1)).onRotateEvent(any(MoveEvent.class));
        verify(mockListener, times(1)).onHardDropEvent(any(MoveEvent.class));
        verify(mockListener, times(1)).onHoldEvent(any(MoveEvent.class));
        verify(mockListener, times(1)).createNewGame();
    }
}
