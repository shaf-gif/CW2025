package com.comp2042.ui;

import com.comp2042.logic.Constants;
import com.comp2042.model.ViewData;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;
import com.comp2042.JavaFxTestBase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // Suppress UnnecessaryStubbingException
public class BrickViewManagerTest extends JavaFxTestBase {

    @Mock
    private GridPane mockBrickPanel;
    @Mock
    private GridPane mockGamePanel;
    @Mock
    private javafx.scene.control.Button mockShadowButton;
    @Mock
    private Pane mockParentPane;
    @Mock
    private ObservableList<Node> mockBrickPanelChildren;
    @Mock
    private ObservableList<Node> mockParentPaneChildren;
    @Mock
    private ViewData mockViewData;

    private BrickViewManager brickViewManager;

    @BeforeEach
    void setUp() {
        // Common setup for GridPanes
        when(mockBrickPanel.getStyleClass()).thenReturn(mock(ObservableList.class));
        when(mockBrickPanel.getChildren()).thenReturn(mockBrickPanelChildren);
        when(mockParentPane.getChildren()).thenReturn(mockParentPaneChildren);
        when(mockParentPaneChildren.indexOf(mockBrickPanel)).thenReturn(0); 

        // Mock gamePanel layout properties for position calculations
        when(mockGamePanel.getLayoutX()).thenReturn(0.0);
        when(mockGamePanel.getLayoutY()).thenReturn(0.0); // Corrected this line

        brickViewManager = new BrickViewManager(mockBrickPanel, mockGamePanel, mockShadowButton, mockParentPane);
    }

    @AfterEach
    void tearDown() throws Exception {
        // No manual mock closing needed as MockitoExtension handles it
    }

    @Test
    void constructorInitializesCorrectly() {
        verify(mockBrickPanel.getStyleClass()).add("piece-layer");
        verify(mockShadowButton).setText("Shadow: ON");
        assertTrue(brickViewManager.isShadowEnabled());
    }

    @Test
    void initDrawBricksCallsDrawMethods() {
        // Mock BrickData for ViewData
        int[][] mockBrickData = {{1}}; // A simple 1x1 brick
        when(mockViewData.getBrickData()).thenReturn(mockBrickData);
        when(mockViewData.getxPosition()).thenReturn(0);
        when(mockViewData.getyPosition()).thenReturn(0);
        when(mockViewData.getGhostXPosition()).thenReturn(0);
        when(mockViewData.getGhostYPosition()).thenReturn(0);

        // Mock static method TileStyleUtility.applyTileStyle
        try (MockedStatic<TileStyleUtility> mockedStatic = mockStatic(TileStyleUtility.class);
             MockedConstruction<Rectangle> mockedConstructionRectangle = mockConstruction(Rectangle.class, (mock, context) -> {
                 // Configure the mock Rectangle to return a mock ObservableMap for getProperties()
                 when(mock.getProperties()).thenReturn(mock(ObservableMap.class));
             });
             MockedConstruction<GridPane> mockedGridPaneConstruction = mockConstruction(GridPane.class, (mock, context) -> {
                // Configure mock GridPane to return mock ObservableList for children
                 when(mock.getChildren()).thenReturn(mock(ObservableList.class));
                 when(mock.getStyleClass()).thenReturn(mock(ObservableList.class));
                 when(mock.getProperties()).thenReturn(mock(ObservableMap.class));
             })) {
            
            // Mock behavior for createTile's call to TileStyleUtility.applyTileStyle
            mockedStatic.when(() -> TileStyleUtility.applyTileStyle(any(Rectangle.class), anyInt()))
                    .thenAnswer(invocation -> null); // Does nothing

            brickViewManager.initDrawBricks(mockViewData);

            verify(mockBrickPanelChildren).clear();
            // Expect 1 Rectangle for the active brick, 1 for the ghost brick (for a 1x1 brick)
            // Constructor of Rectangle is called twice for a 1x1 brick (once for active, once for ghost)
            assertEquals(2, mockedConstructionRectangle.constructed().size()); 
            
            // Verifying the addition to brickPanel
            verify(mockBrickPanel, times(1)).add(any(Rectangle.class), anyInt(), anyInt()); 
            // The ghostPanel is added to parentPane, not brickPanelChildren
            verify(mockParentPaneChildren, times(1)).add(eq(0), any(GridPane.class));

            // Verify that createTile was called for both active and ghost bricks
            // Total calls to TileStyleUtility.applyTileStyle will be 2 (one for active, one for ghost) for a 1x1 brick
            mockedStatic.verify(() -> TileStyleUtility.applyTileStyle(any(Rectangle.class), anyInt()), times(2));
        }
    }
}