package com.comp2042.logic.bricks;

import
        org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SlowBrickTest {

    @Test
    void testSlowBrickType() {
        SlowBrick slowBrick = new SlowBrick();
        assertEquals(BrickType.SLOW, slowBrick.getBrickType(), "SlowBrick should return BrickType.SLOW");
    }

    @Test
    void testSlowBrickShapeMatrixNotNull() {
        SlowBrick slowBrick = new SlowBrick();
        assertNotNull(slowBrick.getShapeMatrix(), "SlowBrick shape matrix should not be null");
        assertEquals(1, slowBrick.getShapeMatrix().size(), "SlowBrick should have one rotation state");
        assertNotNull(slowBrick.getShapeMatrix().get(0), "SlowBrick's first shape matrix should not be null");
    }
}
