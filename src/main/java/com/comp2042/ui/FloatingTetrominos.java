package com.comp2042.ui;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickType;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.logic.scoring.Score;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manages the background animation of floating, rotating tetromino outlines.
 * These animations are designed to be chill, minimal, and run on both the main menu and game screens.
 * The tetrominos drift diagonally, rotate slowly, and subtly fade in and out.
 * The animation dynamically adjusts to the container's size changes.
 */
public class FloatingTetrominos {

    /** The JavaFX Pane that contains the floating tetrominos. */
    private Pane container;
    /** A list to hold all active tetromino animations. */
    private List<TetrominoAnimation> tetrominoAnimations = new ArrayList<>();
    /** Random number generator for various animation properties. */
    private Random random = new Random();
    /** Generates random brick types for the tetrominos. */
    private RandomBrickGenerator brickGenerator = new RandomBrickGenerator(new Score());

    /** Scaling factor for the size of individual tetromino blocks. */
    private final double TETROMINO_SCALE = 0.5;
    /** The number of tetrominos to display simultaneously. */
    private final int TETROMINO_COUNT = 10;

    /**
     * Constructs a new FloatingTetrominos animation manager.
     * Initializes the container and sets up listeners for dynamic resizing.
     *
     * @param container The Pane where the tetrominos will be displayed and animated.
     */
    public FloatingTetrominos(Pane container) {
        this.container = container;
        
        // Add listeners to resize tetrominos when container dimensions change
        this.container.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                resizeTetrominos();
            }
        });
        this.container.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                resizeTetrominos();
            }
        });

        generateTetrominos(TETROMINO_COUNT); // Generate initial tetrominos
    }

    /**
     * Generates a specified number of tetrominos, positions them randomly,
     * starts their animations, and adds them to the container.
     *
     * @param count The number of tetrominos to generate.
     */
    private void generateTetrominos(int count) {
        for (int i = 0; i < count; i++) {
            Group tetrominoGroup = createTetromino();
            if (tetrominoGroup != null) {
                positionTetromino(tetrominoGroup);
                TetrominoAnimation anim = animateTetromino(tetrominoGroup);
                tetrominoAnimations.add(anim);
                container.getChildren().add(tetrominoGroup);
            }
        }
    }

    /**
     * Resizes the existing tetrominos. This stops all current animations,
     * clears the container, and regenerates new tetrominos based on the new dimensions.
     */
    private void resizeTetrominos() {
        stopAnimations();
        container.getChildren().clear();
        tetrominoAnimations.clear();
        generateTetrominos(TETROMINO_COUNT);
        startAnimations();
    }

    /**
     * Creates a single tetromino visual representation (Group of Rectangles) based on a random brick type.
     * The tetromino is rendered as an outline with a semi-transparent fill.
     *
     * @return A Group object representing the tetromino.
     */
    private Group createTetromino() {
        Brick brick = brickGenerator.getNextBrick();
        int[][] shape = brick.getShapeMatrix().get(0); // Get the first rotation shape
        BrickType type = brick.getBrickType();

        Group tetrominoGroup = new Group();
        Color color = getColorForBrickType(type);

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    Rectangle rect = new Rectangle(col * 20 * TETROMINO_SCALE, row * 20 * TETROMINO_SCALE, 20 * TETROMINO_SCALE, 20 * TETROMINO_SCALE);
                    rect.setFill(color.deriveColor(1, 1, 1, 0.4)); // More vibrant fill
                    rect.setStroke(color.deriveColor(1, 1, 1, 0.6)); // More visible outline
                    rect.setStrokeWidth(2);
                    tetrominoGroup.getChildren().add(rect);
                }
            }
        }
        tetrominoGroup.setOpacity(0.5); // Increased overall group opacity
        return tetrominoGroup;
    }

    /**
     * Randomly positions a given tetromino Group within the container bounds
     * and sets its initial rotation.
     *
     * @param tetromino The Group representing the tetromino to position.
     */
    private void positionTetromino(Group tetromino) {
        tetromino.setLayoutX(random.nextDouble() * container.getWidth());
        tetromino.setLayoutY(random.nextDouble() * container.getHeight());
        tetromino.setRotate(random.nextDouble() * 360);
    }

    /**
     * Applies and starts the translation, rotation, and fade animations for a tetromino.
     *
     * @param tetromino The Group representing the tetromino to animate.
     * @return A TetrominoAnimation object containing the tetromino and its associated transitions.
     */
    private TetrominoAnimation animateTetromino(Group tetromino) {
        // Translate animation (drift diagonally)
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(random.nextInt(30) + 60), tetromino); // 60-90 seconds
        translateTransition.setByX((random.nextBoolean() ? 1 : -1) * (container.getWidth() / 2 + random.nextDouble() * container.getWidth() / 2));
        translateTransition.setByY((random.nextBoolean() ? 1 : -1) * (container.getHeight() / 2 + random.nextDouble() * container.getHeight() / 2));
        translateTransition.setCycleCount(Animation.INDEFINITE); 
        translateTransition.setAutoReverse(true);
        translateTransition.play();

        // Rotate animation
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(random.nextInt(40) + 80), tetromino); // 80-120 seconds
        rotateTransition.setByAngle(random.nextBoolean() ? 360 : -360);
        rotateTransition.setCycleCount(Animation.INDEFINITE); 
        rotateTransition.play();

        // Fade in/out slightly
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(random.nextInt(10) + 20), tetromino);
        fadeTransition.setFromValue(tetromino.getOpacity());
        fadeTransition.setToValue(random.nextDouble() * 0.1 + 0.15); 
        fadeTransition.setCycleCount(Animation.INDEFINITE); 
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();

        return new TetrominoAnimation(tetromino, translateTransition, rotateTransition, fadeTransition);
    }

    /**
     * Returns the JavaFX Color associated with a given BrickType.
     *
     * @param type The BrickType to get the color for.
     * @return The corresponding JavaFX Color.
     */
    private Color getColorForBrickType(BrickType type) {
        switch (type) {
            case I: return Color.CYAN;
            case J: return Color.BLUE;
            case L: return Color.ORANGE;
            case O: return Color.YELLOW;
            case S: return Color.LIMEGREEN;
            case T: return Color.PURPLE;
            case Z: return Color.RED;
            case SLOW: return Color.GRAY;
            default: return Color.WHITE;
        }
    }

    /**
     * Starts all animations for all floating tetrominos.
     */
    public void startAnimations() {
        for (TetrominoAnimation anim : tetrominoAnimations) {
            anim.play();
        }
    }

    /**
     * Stops all animations for all floating tetrominos.
     */
    public void stopAnimations() {
        for (TetrominoAnimation anim : tetrominoAnimations) {
            anim.stop();
        }
    }

    /**
     * Helper class to store a tetromino Group and its associated JavaFX Animations.
     */
    private static class TetrominoAnimation {
        /** The visual Group of the tetromino. */
        final Group group;
        /** The TranslateTransition for the tetromino's drifting movement. */
        final TranslateTransition translate;
        /** The RotateTransition for the tetromino's rotation. */
        final RotateTransition rotate;
        /** The FadeTransition for the tetromino's fading in/out effect. */
        final FadeTransition fade;

        /**
         * Constructs a TetrominoAnimation instance.
         *
         * @param group The visual Group of the tetromino.
         * @param translate The TranslateTransition for drifting.
         * @param rotate The RotateTransition for rotation.
         * @param fade The FadeTransition for fading.
         */
        public TetrominoAnimation(Group group, TranslateTransition translate, RotateTransition rotate, FadeTransition fade) {
            this.group = group;
            this.translate = translate;
            this.rotate = rotate;
            this.fade = fade;
        }

        /**
         * Plays all associated animations.
         */
        public void play() {
            translate.play();
            rotate.play();
            fade.play();
        }

        /**
         * Stops all associated animations.
         */
        public void stop() {
            translate.stop();
            rotate.stop();
            fade.stop();
        }
    }
}