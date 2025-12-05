package com.comp2042;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;

public abstract class JavaFxTestBase {

    private static boolean javafxInitialized = false;

    @BeforeAll
    static void initJavaFX() {
        if (!javafxInitialized) {
            try {
                Platform.startup(() -> {});
                javafxInitialized = true;
            } catch (IllegalStateException e) {
                // Platform already started, safe to ignore
            }
        }
    }
}
