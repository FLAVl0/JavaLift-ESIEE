package esiee.front;

import esiee.Demo;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Hello world!
 *
 */
public class App extends Application {
    //private Demo demo;
    @Override
    public void start(Stage stage) {
        // new Thread(() -> {
        //     try {
        //         Thread.sleep(2000); // Attendre 2 secondes avant de lancer la démo
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        //     demo = new Demo();
        // }).start();

        // while (demo == null) {
        //     try {
        //         Thread.sleep(100); // Attendre que la démo soit prête
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }
        // TowerView view = new TowerView(10, demo); // 10 étages

        Demo demo = new Demo();
        TowerView tv = new TowerView(10, demo);

        demo.startAutomaticSimulation(tv);



        TowerRenderer renderer = new TowerRenderer(tv,5, 10);
        tv.setRenderer(renderer);

        Scene scene = new Scene(tv.getRoot(), 1900, 1100);
        stage.setScene(scene);
        stage.setTitle("Tower Simulation View");
        stage.show();
        // Activation du front end
        
        // Exemple d’appel depuis le backend (simulation)
        System.out.println("Launching Demo...");
        stage.sizeToScene();
        
        System.out.println("Front-end started.");

    }

    public static void main(String[] args) {
        System.out.println("Starting Tower Simulation...");
        launch(args);
    }
}