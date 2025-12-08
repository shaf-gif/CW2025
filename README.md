# CW2025 - Tetris-JavaFX

## GitHub Repository
[https://github.com/shaf-gif/CW2025]

## Compilation Instructions

To compile and run this project, follow these steps:

1.  **Prerequisites:**
    *   Java Development Kit (JDK) 17 or newer.

2.  **Clone the repository:**
    ```bash
    git clone https://github.com/shaf-gif/CW2025
    cd CW2025
    ```okay just

3.  **Compile the project:**
    Use the Maven Wrapper to compile the project. This will automatically download the correct Maven version and all necessary dependencies.

    *   On **macOS/Linux**:
        ```bash
        ./mvnw clean install
        ```
    *   On **Windows**:
        ```bash
        mvnw.cmd clean install
        ```

4.  **Run the application:**
    After successful compilation, you can run the application using the Maven Wrapper and the JavaFX plugin.
    *   On **macOS/Linux**:
        ```bash
        ./mvnw javafx:run
        ```
    *   On **Windows**:
        ```bash
        mvnw.cmd javafx:run
        ```

## Implemented and Working Properly

The following features were found to be **already implemented** in the provided codebase and are functioning as expected:

1.  **Main Menu:**
    - **Function & Use:** The main menu serves as the initial screen of the application, providing players with options to start a new game, view high scores on the leaderboard, adjust game settings, or exit the application. It also allows players to input their name and resume an active game.

2.  **Leaderboard System:**
    - **Function & Use:** This system tracks and displays the top game scores, along with player names, the level reached, and the number of rows cleared in each game. Scores are saved persistently between application runs, and players can view or clear the leaderboard from the main menu.

3.  **Music and Sound Effects (SFX):**
    - **Function & Use:** The game features immersive audio feedback, including background music that changes between the main menu and in-game, and sound effects for crucial player actions like button clicks, dropping bricks, clearing lines, and rotating pieces. Players can control the volume and toggle music/SFX on or off.

4.  **Next Three Preview:**
    - **Function & Use:** This feature provides players with a strategic advantage by displaying the next three bricks that will enter the game board. This allows for better planning and more complex setups. The brick generation employs a "bag" system, ensuring that all seven standard Tetris pieces (I, J, L, O, S, T, Z) appear in a random but fair distribution before the bag is refilled and reshuffled.
    - **Core Logic Snippet:**
        ```java
        // RandomBrickGenerationStrategy.java: Method to get next N bricks
        public List<Brick> peekNextBricks(int count) { /* ... returns immutable list of next bricks ... */ }
        // PreviewPanelManager.java: Rendering in UI
        renderPreview(nextPanel1, previews[0]); renderPreview(nextPanel2, previews[1]); renderPreview(nextPanel3, previews[2]);
        ```

5.  **Hold Piece:**
    - **Function & Use:** The "Hold" feature allows players to store the currently falling brick in a separate "hold" area. This brick can then be swapped back into play at a later time. A player can only use the hold feature once per turn (i.e., after a brick has landed and a new one generated, the hold can be used again).
    - **Core Logic Snippet:**
        ```java
        // SimpleBoard.java: Hold logic
        if (heldBrick == null) { heldBrick = currentBrick; /* ... get new brick ... */ }
        else { Brick temp = heldBrick; heldBrick = currentBrick; currentBrick = temp; }
        holdUsedThisTurn = true;
        ```

6.  **Ghost Shadow with Toggle Button:**
    - **Function & Use:** The ghost shadow provides a visual cue, showing where the current falling brick will land if it were to drop immediately. This helps players accurately position pieces. The visibility of this ghost can be toggled on or off via a button (mapped to 'H' key) or a settings option.
    - **Core Logic Snippet:**
        ```java
        // SimpleBoard.java: Calculate ghost position
        private int computeGhostY() { /* ... simulates dropping current brick to find Y ... */ }
        // BrickViewManager.java: Toggle visibility
        public void toggleShadow(ActionEvent evt) { isShadowEnabled = !isShadowEnabled; ghostPanel.setVisible(isShadowEnabled); }
        ```

7.  **Level System:**
    - **Function & Use:** The game incorporates a dynamic level system where difficulty increases as the player progresses. Clearing rows advances the player to the next level, which in turn speeds up the automatic falling rate of bricks. The current level is prominently displayed on the game screen. Specifically, players advance to a new level for every **5 rows cleared**. As the level increases, the base fall delay of bricks is reduced, making them fall faster.
    - **Formula for Fall Delay:**
        ```
        fall_delay_ms = base_fall_delay_ms * (0.75 ^ (level - 1))
        ```
        (minimum 50ms).

8.  **Slow Block:**
    - **Function & Use:** The Slow Block is a special type of brick that introduces a unique gameplay modifier. When a Slow Block is generated (which occurs randomly with a **15% probability** once the player reaches **Level 3 or higher**), it is temporarily inserted into the normal brick generation sequence (which uses the bag system). It is not shown in the "Next Three Preview," making its appearance a surprise. Once successfully placed by the player, it temporarily slows down the entire game's pace for **8 seconds**. This offers a brief period of reduced difficulty, providing players with more time to plan and react. The Slow Block can also be held, allowing players to strategically save its slowdown effect for a critical moment, potentially "preserving" their game from a difficult situation. Visually, the Slow Block appears as a **2x2 green square**, and a "SLOW MODE ACTIVE" indicator is displayed on the UI while the effect is in progress.
    - **Formula for Slow Mode Fall Delay:**
        ```
        slow_fall_delay_ms = current_fall_delay_ms * 3.0
        ```

9.  **Hard Drop:**
    - **Function & Use:** Hard drop is a fundamental Tetris mechanic that allows players to instantly move the current falling brick to the lowest possible position on the board. This provides quick and precise placement, crucial for advanced strategies. The 'SPACE' bar triggers this action.

10. **Floating Tetromino Constellations:**
    - **Function & Use:** A visually appealing background animation present on both the main menu and game screens. This feature displays translucent tetromino outlines that slowly drift, rotate, and fade across the background, creating a serene "constellation" effect. The animation dynamically adjusts to window resizing, ensuring a consistent visual experience across different window sizes.

## UI Enhancements

The game features a modern and intuitive user interface designed for an engaging player experience.

*   **Overall Aesthetic:** The application employs a consistent dark theme, often leveraging `window_style.css` and `style.css` for a sleek, contemporary look. The background now features a soft gradient and subtle animations.
*   **Floating Tetromino Constellations:** A new visually appealing background animation featuring translucent tetromino outlines that slowly drift, rotate, and fade has been added to both the main menu and the game screen, creating a serene "constellation" effect. This animation dynamically adjusts to window resizing, ensuring a consistent and enhanced visual experience across various window dimensions.
*   **Main Menu & Navigation:** A clear main menu (`menuLayout.fxml`) provides straightforward navigation with distinct buttons for "Start Game," "Leaderboard," "Settings," and "Exit." The player name input field is integrated seamlessly.
*   **Game Board Presentation:** The primary game area is a grid (`gamePanel` in `gameLayout.fxml`) where individual brick tiles are rendered. The UI ensures that hidden rows are accounted for and provides a clean backdrop for gameplay.
*   **Dynamic Information Displays:**
    *   **Score & Level:** The current score and level are prominently displayed using a retro digital-style font (`digital.ttf`), enhancing the classic arcade feel.
    *   **Next Three Preview:** Dedicated panels (`nextPanel1`, `nextPanel2`, `nextPanel3`) visually showcase the upcoming three bricks, aiding player strategy.
    *   **Hold Piece Display:** A specific panel (`holdPanel`) is reserved for displaying the currently held brick, clearly indicating the available saved piece.
    *   **Slow Mode Indicator:** A visible label (`slowModeIndicator`) dynamically updates to inform the player when "SLOW MODE ACTIVE" is engaged, providing immediate feedback.
*   **Interactive Elements & Feedback:**
    *   **Game Controls Access:** The game's controls (hotkeys) are accessible and viewable through a dedicated "CONTROLS" button within the settings menu, allowing players to review and understand all available actions.
    *   **Ghost Shadow:** A translucent ghost piece helps players visualize the exact landing spot of the current brick, improving precision. This visual aid can be toggled on/off.
    *   **Pause/Game Over Overlay:** During pause or game over states, a translucent overlay appears over the game board, dimming the background and drawing focus to relevant panels (`gameOverPanel`, pause menu). The game over panel itself features smooth fade and scale animations for a polished presentation.
    *   **Notification Panel:** Brief, dynamic notifications appear on screen to indicate score bonuses (e.g., "+100") when lines are cleared.
*   **Button Styling:** Buttons across the application maintain a consistent and responsive design, likely utilizing custom CSS classes for a unified look and feel.

## Implemented but Not Working Properly

During development, I encountered issues with implementing certain features, such as multiplayer functionality. Rather than including partially working or buggy code, all changes related to these problematic features were completely removed from the codebase. Therefore, currently, there are no features in the game that were implemented but are not working properly.

## Features Not Implemented

*   **Game Modes**: While the core game offers a dynamic experience, additional game modes (e.g., time attack, puzzle mode) were not implemented. This decision was influenced by the recent addition of the SlowBlock mechanic, which already introduced a unique gameplay modifier. The aim was to avoid overcomplicating the current scope.
*   **Multiplayer**: The game does not currently support multiplayer functionality.

## New Java Classes

The following classes represent new additions or significant refactorings in the codebase:

*   **FloatingTetrominos**: Manages the background animation of floating, rotating tetromino outlines on both the main menu and game screens.
*   **AudioManager**: Manages background music and sound effects for the game.
*   **ControlsController**: Handles user input and controls for the game.
*   **BrickViewManager**: Manages the visual representation and rendering of bricks on the game board, including the ghost shadow.
*   **LeaderboardController**: Manages the display and interaction with the game's leaderboard.
*   **SettingsController**: Manages game settings such as volume and other game preferences.
*   **TileStyleUtility**: A utility class responsible for applying visual styles to individual game tiles (e.g., brick segments).
*   **LeaderboardManager**: Manages the persistent storage, retrieval, and sorting of high scores for the leaderboard.
*   **SlowBrick**: A special brick type that, when placed, temporarily slows down the game's pace.
*   **Constants**: Stores various constant values and configuration parameters used throughout the game (e.g., tile sizes, game speeds, probabilities).

**Note: Test classes were created for all new Java classes, as well as some op ther less critical classes not explicitly mentioned here, ensuring comprehensive test coverage.**

## New FXML Classes

The following FXML files define the structure and layout for various user interface components in the application:

*   **menuLayout.fxml**: Defines the user interface for the main menu of the game, including options to start a new game, view the leaderboard, adjust settings, and exit.
*   **settingsLayout.fxml**: Defines the user interface for the game's settings screen, allowing players to adjust preferences suchs audio volume and toggle music/SFX.
*   **controlsLayout.fxml**: Defines the user interface for displaying game controls and key bindings, helping players understand how to interact with the game.
*   **leaderboardLayout.fxml**: Defines the user interface for displaying the game's high scores, player names, and other relevant ranking information.

## Modified Java Classes

Existing classes were modified to integrate new features and accommodate refactoring efforts, particularly for implementing design patterns and new game mechanics. These modifications include:

*   **MainMenu**: Modified to integrate and control the Floating Tetrominos background animation.
*   **GuiController**: Manages the overall graphical user interface, coordinating between different UI elements and game logic, now including the Floating Tetrominos background animation.
*   **GameController**: Orchestrates the main game loop, handles game state transitions, and integrates various game mechanics.
*   **GameOverPanel**: Manages the display and behavior of the game over screen, including animations and user interaction.
*   **Score**: Handles the calculation, storage, and updates of the player's score based on game events like line clears.
*   **Brick Classes (IBrick, JBrick, LBrick, OBrick, SBrick, TBrick, ZBrick, SlowBrick)**: Individual brick classes that define the shape, rotation, and behavior of each specific brick type. Modifications integrated new features like the SlowBrick effects and interactions with other game mechanics.
*   **RandomBrickGenerator**: Manages the generation of new bricks for the game, incorporating a "bag" system to ensure fair distribution and integrating special brick types like the SlowBrick.
*   **MatrixOperations**: Provides utility methods for manipulating the game board's underlying matrix, crucial for brick movement, rotation, and collision detection.
*   **SimpleBoard**: Represents the core game board logic, handling brick placement, row clearing, collision detection, and integrating features like hold, ghost shadow, and slow block effects.
*   **BrickType**: Modified to include an `id` field and getter for numerical representation of brick types.


## Design Pattern Implementations

The codebase leverages several design patterns to enhance modularity and maintainability, particularly for game logic and controls.

### Command Design Pattern

The Command design pattern encapsulates requests as objects, allowing for parameterization of clients with different requests, queuing or logging of requests, and support for undoable operations. Classes implementing this pattern include:

*   **Command**: An interface defining the execute method for all commands.
*   **HardDropCommand**: A concrete command to execute a hard drop operation for the current brick.
*   **MoveDownCommand**: A concrete command to move the current brick one unit down.
*   **MoveLeftCommand**: A concrete command to move the current brick one unit left.
*   **MoveRightCommand**: A concrete command to move the current brick one unit right.
*   **RotateClockwiseCommand**: A concrete command to rotate the current brick clockwise.
*   **RotateCounterClockwiseCommand**: A concrete command to rotate the current brick counter-clockwise.

### Other Related Classes in `movement`

These classes support the overall game mechanics, some of which may be part of a Strategy pattern or event handling:

*   **ClearRow**: Manages the logic and effects associated with clearing complete rows on the game board.
*   **DownData**: A data class used to encapsulate information relevant to downward movement actions.
*   **EventSource**: Provides a mechanism for game events to be generated and observed.
*   **EventType**: An enumeration defining various types of events that can occur in the game.
*   **InputEventListener**: An interface for classes that need to react to user input events.
*   **MoveEvent**: An event class specifically for capturing details about a movement action.
*   **NotificationPanel**: Responsible for displaying transient in-game notifications or messages to the player.

## Unexpected Problems

### Slow Mode Indicator Placement

During the initial development, an attempt was made to display the "SLOW MODE ACTIVE" indicator as a dynamic pop-up directly over the main game board (`gamePanel`). This proved challenging primarily due to the architectural design of JavaFX layouts.

The `gamePanel` is a `GridPane`, which is optimized for arranging elements in a strict grid. Overlaying elements that "float" above this grid, without being confined to a cell, requires a parent container specifically designed for layering, such as a `StackPane`. While other dynamic UI elements like score notifications leverage a `Group` (`groupNotification`) for transient displays, the `slowModeIndicator` was implemented as a simple `Label`.

Directly adding a `Label` to a `GridPane` would attempt to place it within a grid cell, not over it. Achieving a floating pop-up would necessitate either:
1.  Wrapping the `gamePanel` within a `StackPane` in the FXML, and then adding the `slowModeIndicator` as a child of the `StackPane`.
2.  Repurposing or extending the existing `groupNotification` mechanism for the slow mode indicator, which is already set up for dynamic, transient overlays.
3.  Manually managing the `layoutX` and `layoutY` properties of the `Label` within a flexible parent `Pane` to position it.

Given these considerations, the decision was made to place the `slowModeIndicator` on a static side panel, which was a more pragmatic solution for immediate visual feedback within the existing FXML structure. This approach ensured functionality without requiring significant refactoring of the main game layout.

### Game Window Centering

Initially, when the application window was resized or expanded, the main game content did not automatically re-center itself within the window, remaining fixed to one side or corner. This resulted in a suboptimal user experience where expanding the window would create empty, uneven space around the game.

The problem was resolved by ensuring the main `Scene`'s root container (`BorderPane` or `StackPane` in the `gameLayout.fxml`) was properly configured to handle resizing and centering. Specifically, the root pane was set to `fit` the scene, and its content (the game board and side panels) was aligned to the center using layout constraints (e.g., `BorderPane.setAlignment(node, Pos.CENTER)`) or by using a `StackPane` as the top-level container, which inherently centers its children by default. This allowed the game elements to dynamically adjust their positions to remain visually centered regardless of the window's dimensions.