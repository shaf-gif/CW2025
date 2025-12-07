package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The main entry point for the Tetris JFX application.
 * This class extends {@code javafx.application.Application} and is responsible for
 * initializing the primary stage and loading the initial UI (main menu).
 */
public class Main extends Application {

    /**
     * Constructs a new Main application instance.
     */
    public Main() {
        // Default constructor
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * @param primaryStage The primary stage for this application, onto which
     * the application scene can be set. The primary stage will be embedded in
     * the browser if the application was launched as an applet.
     * @throws Exception if something goes wrong during application startup.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        Font.loadFont(getClass().getResource("/digital.ttf").toExternalForm(), 12);

        // Load the FXML file
        URL location = getClass().getResource("/menuLayout.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);

        // Load the FXML content and create the scene
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 550, 550);

        // Set up the window
        primaryStage.setTitle("Tetris JFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main method is ignored in correctly deployed JavaFX applications.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
