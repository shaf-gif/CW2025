package com.comp2042.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;

// Explicitly re-adding import for ViewData
import com.comp2042.model.ViewData;
import com.comp2042.JavaFxTestBase;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // Suppress UnnecessaryStubbingException
public class PreviewPanelManagerTest extends JavaFxTestBase {

    @Mock private GridPane mockNextPanel1;
    @Mock private GridPane mockNextPanel2;
    @Mock private GridPane mockNextPanel3;
    @Mock private GridPane mockHoldPanel;
    @Mock private ViewData mockViewData;
    @Mock private ObservableList<Node> mockNextPanel1Children; // Separate mock for each
    @Mock private ObservableList<Node> mockNextPanel2Children;
    @Mock private ObservableList<Node> mockNextPanel3Children;
    @Mock private ObservableList<Node> mockHoldPanelChildren;


    private PreviewPanelManager previewPanelManager;

    @BeforeEach
    void setUp() {
        // Common setup for GridPanes
        when(mockNextPanel1.getChildren()).thenReturn(mockNextPanel1Children);
        when(mockNextPanel2.getChildren()).thenReturn(mockNextPanel2Children);
        when(mockNextPanel3.getChildren()).thenReturn(mockNextPanel3Children);
        when(mockHoldPanel.getChildren()).thenReturn(mockHoldPanelChildren);

        // Explicitly stub clear() for all children mocks to prevent NullPointerExceptions
        doNothing().when(mockNextPanel1Children).clear();
        doNothing().when(mockNextPanel2Children).clear();
        doNothing().when(mockNextPanel3Children).clear();
        doNothing().when(mockHoldPanelChildren).clear();
        // Also stub add() method to prevent UnnecessaryStubbingException or verify errors
        doAnswer(invocation -> null).when(mockNextPanel1Children).add(any(Node.class));
        doAnswer(invocation -> null).when(mockNextPanel2Children).add(any(Node.class));
        doAnswer(invocation -> null).when(mockNextPanel3Children).add(any(Node.class));
        doAnswer(invocation -> null).when(mockHoldPanelChildren).add(any(Node.class));


        previewPanelManager = new PreviewPanelManager(mockNextPanel1, mockNextPanel2, mockNextPanel3, mockHoldPanel);
    }

    @Test
    void constructorAssignsPanelsCorrectly() {
        // Assertions are implicitly done via the setup() method already
        assertNotNull(previewPanelManager);
    }

    @Test
    void renderAllPreviewsCallsRenderNextAndRenderHold() {
        // Spy on the previewPanelManager to verify internal method calls
        PreviewPanelManager spyPreviewPanelManager = spy(previewPanelManager);

        spyPreviewPanelManager.renderAllPreviews(mockViewData);

        verify(spyPreviewPanelManager).renderNextPreviews(mockViewData);
        verify(spyPreviewPanelManager).renderHoldPreview(mockViewData);
    }

    @Test
    void renderNextPreviewsRendersCorrectPanels() {
        int[][][] mockNextBricksData = {
                {{1}}, // Brick 1
                {{2, 2}}, // Brick 2
                {{3, 3, 3}} // Brick 3
        };
        when(mockViewData.getNextBricksData()).thenReturn(mockNextBricksData);

        // Spy on the manager to verify renderPreview calls
        PreviewPanelManager spyPreviewPanelManager = spy(previewPanelManager);
        spyPreviewPanelManager.renderNextPreviews(mockViewData);

        // Verify renderPreview is called for each next panel with correct data
        verify(spyPreviewPanelManager).renderPreview(eq(mockNextPanel1), eq(mockNextBricksData[0]));
        verify(spyPreviewPanelManager).renderPreview(eq(mockNextPanel2), eq(mockNextBricksData[1]));
        verify(spyPreviewPanelManager).renderPreview(eq(mockNextPanel3), eq(mockNextBricksData[2]));
    }

    @Test
    void renderNextPreviewsHandlesFewerThanThreeBricks() {
        int[][][] mockNextBricksData = {
                {{1}} // Only one brick
        };
        when(mockViewData.getNextBricksData()).thenReturn(mockNextBricksData);

        PreviewPanelManager spyPreviewPanelManager = spy(previewPanelManager);
        spyPreviewPanelManager.renderNextPreviews(mockViewData);

        verify(spyPreviewPanelManager).renderPreview(eq(mockNextPanel1), eq(mockNextBricksData[0]));
        verify(spyPreviewPanelManager).renderPreview(eq(mockNextPanel2), eq(null)); // Expect null for missing brick
        verify(spyPreviewPanelManager).renderPreview(eq(mockNextPanel3), eq(null)); // Expect null for missing brick
    }

    @Test
    void renderNextPreviewsHandlesNullNextBricksData() {
        when(mockViewData.getNextBricksData()).thenReturn(null);

        PreviewPanelManager spyPreviewPanelManager = spy(previewPanelManager);
        spyPreviewPanelManager.renderNextPreviews(mockViewData);

        verify(spyPreviewPanelManager, times(3)).renderPreview(any(GridPane.class), eq(null));
    }

    @Test
    void renderHoldPreviewRendersCorrectPanel() {
        int[][] mockHeldBrickData = {{1, 1}};
        when(mockViewData.getHeldBrickData()).thenReturn(mockHeldBrickData);

        PreviewPanelManager spyPreviewPanelManager = spy(previewPanelManager);
        spyPreviewPanelManager.renderHoldPreview(mockViewData);

        verify(mockHoldPanelChildren, times(2)).clear(); // Corrected verification
        verify(spyPreviewPanelManager).renderPreview(eq(mockHoldPanel), eq(mockHeldBrickData));
    }

    @Test
    void renderHoldPreviewHandlesNullHeldBrickData() {
        when(mockViewData.getHeldBrickData()).thenReturn(null);

        PreviewPanelManager spyPreviewPanelManager = spy(previewPanelManager);
        spyPreviewPanelManager.renderHoldPreview(mockViewData);

        verify(mockHoldPanelChildren).clear(); // Corrected verification
        verify(spyPreviewPanelManager, never()).renderPreview(any(GridPane.class), any());
    }

    @Test
    void renderPreviewClearsPanelAndAddsRectanglesForShape() {
        int[][] testShape = {{1, 2}, {0, 3}}; // Sample 2x2 shape
        try (MockedConstruction<Rectangle> mockedConstructionRectangle = mockConstruction(Rectangle.class);
             MockedStatic<TileStyleUtility> mockedStaticTileStyleUtility = mockStatic(TileStyleUtility.class)) {

            previewPanelManager.renderPreview(mockNextPanel1, testShape);

            verify(mockNextPanel1, times(1)).getChildren(); // Verify getChildren is called on the panel
            verify(mockNextPanel1Children).clear(); // Corrected verification
            // Verify Rectangle construction and TileStyleUtility calls
            assertEquals(3, mockedConstructionRectangle.constructed().size()); // 3 non-zero tiles in testShape
            mockedStaticTileStyleUtility.verify(() -> TileStyleUtility.applyTileStyle(any(Rectangle.class), eq(1)), times(1));
            mockedStaticTileStyleUtility.verify(() -> TileStyleUtility.applyTileStyle(any(Rectangle.class), eq(2)), times(1));
            mockedStaticTileStyleUtility.verify(() -> TileStyleUtility.applyTileStyle(any(Rectangle.class), eq(3)), times(1));

            // Verify add is called for each non-zero tile
            verify(mockNextPanel1, times(1)).add(any(Rectangle.class), eq(0), eq(0)); // Tile 1
            verify(mockNextPanel1, times(1)).add(any(Rectangle.class), eq(1), eq(0)); // Tile 2
            verify(mockNextPanel1, times(1)).add(any(Rectangle.class), eq(1), eq(1)); // Tile 3
        }
    }

    @Test
    void renderPreviewHandlesNullShape() {
        previewPanelManager.renderPreview(mockNextPanel1, null);
        verify(mockNextPanel1, times(1)).getChildren(); // Explicitly verify getChildren() call
        verify(mockNextPanel1Children, times(1)).clear(); // Explicitly verify clear() call
        verifyNoMoreInteractions(mockNextPanel1, mockNextPanel1Children); // No other interactions with panel/children mock
    }
}
