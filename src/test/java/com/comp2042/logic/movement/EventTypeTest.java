package com.comp2042.logic.movement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EventTypeTest {

    @Test
    void testEnumValues() {
        assertNotNull(EventType.DOWN);
        assertNotNull(EventType.LEFT);
        assertNotNull(EventType.RIGHT);
        assertNotNull(EventType.ROTATE);
        assertNotNull(EventType.HARD_DROP);
        assertNotNull(EventType.HOLD);

        assertEquals("DOWN", EventType.DOWN.name());
        assertEquals("LEFT", EventType.LEFT.name());
        assertEquals("RIGHT", EventType.RIGHT.name());
        assertEquals("ROTATE", EventType.ROTATE.name());
        assertEquals("HARD_DROP", EventType.HARD_DROP.name());
        assertEquals("HOLD", EventType.HOLD.name());
    }
}
