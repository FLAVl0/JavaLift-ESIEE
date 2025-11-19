package esiee.front;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Hello world!
 *
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        TowerView view = new TowerView(10); // 10 étages

        TowerRenderer renderer = new TowerRenderer(view,5, 10);
        view.setRenderer(renderer);

        Scene scene = new Scene(view.getRoot(), 1900, 1100);
        stage.setScene(scene);
        stage.setTitle("Tower Simulation View");
        stage.show();
        // Activation du front end
        
        // Exemple d’appel depuis le backend (simulation)
        
        stage.sizeToScene();

    }

    public static void main(String[] args) {
        System.out.println("Starting Tower Simulation...");
        launch(args);
    }
}