package com.comp2042.logic.movement;

import com.comp2042.logic.movement.ClearRow;
import com.comp2042.model.ViewData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class DownDataTest {

    @Test
    void testConstructorAndGetters() {
        ClearRow mockClearRow = mock(ClearRow.class);
        ViewData mockViewData = mock(ViewData.class);

        DownData downData = new DownData(mockClearRow, mockViewData);

        assertEquals(mockClearRow, downData.getClearRow());
        assertEquals(mockViewData, downData.getViewData());
    }
}
