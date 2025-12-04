package com.comp2042.logic.movement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EventSourceTest {

    @Test
    void testEnumValues() {
        assertNotNull(EventSource.USER);
        assertNotNull(EventSource.THREAD);

        assertEquals("USER", EventSource.USER.name());
        assertEquals("THREAD", EventSource.THREAD.name());
    }
}
