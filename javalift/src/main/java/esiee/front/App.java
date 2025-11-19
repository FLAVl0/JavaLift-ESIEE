package esiee.front;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main JavaFX application entry point for the Tower Simulation.
 *
 * This class initializes the graphical environment, creates the tower view,
 * assigns its renderer, and displays the main window.
 *
 * Responsibilities:
 * - Initialize JavaFX stage and scene
 * - Instantiate the TowerView containing floors, elevator, and persons
 * - Attach the TowerRenderer responsible for animations and logic
 * - Configure window dimensions and display properties
 *
 * @see TowerView
 * @see TowerRenderer
 */
public class App extends Application {

    /**
     * Starts the JavaFX application.
     *
     * @param stage the main application window created by JavaFX
     */
    @Override
    public void start(Stage stage) {
        // Create the main tower view with 10 floors
        TowerView view = new TowerView(10);

        // Create the renderer controlling animations
        TowerRenderer renderer = new TowerRenderer(view, 5, 10);
        view.setRenderer(renderer);

        // Create the scene and configure window size
        Scene scene = new Scene(view.getRoot(), 1900, 1100);
        stage.setScene(scene);
        stage.setTitle("Tower Simulation View");
        stage.show();

        // Ensures the stage fits the scene size
        stage.sizeToScene();
    }

    /**
     * Main application entry point (standard JVM launcher).
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        System.out.println("Starting Tower Simulation...");
        launch(args);
    }
}
