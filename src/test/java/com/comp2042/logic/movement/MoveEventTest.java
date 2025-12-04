package com.comp2042.logic.movement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveEventTest {

    @Test
    void testConstructorAndGetters() {
        EventType eventType = EventType.DOWN;
        EventSource eventSource = EventSource.USER;

        MoveEvent moveEvent = new MoveEvent(eventType, eventSource);

        assertEquals(eventType, moveEvent.getEventType());
        assertEquals(eventSource, moveEvent.getEventSource());
    }
}
