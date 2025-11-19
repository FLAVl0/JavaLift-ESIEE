/**
 * Auto‑documented class TowerRenderer.
 */
package esiee.front;
import java.util.List;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;

public class TowerRenderer {
    private final TowerView view;
    private int currentElevatorFloor = 0; // étage actuel de l'ascenseur

    public TowerRenderer(TowerView view, int numPersons, int numFloors) {
        this.view = view;
        int p = 0;
        for (int i = 1; i < numFloors; i++) {
            for (int j = 0; j < numPersons; j++) {
                view.addPerson(p++, 20*j+620, i*80 - 10, Color.color(Math.random(), Math.random(), Math.random()), numFloors -i, 0);
            }
        }
        
    }

    public TranslateTransition moveElevator(int elevatorId, int targetFloor) {
    double y = view.getFloorY(targetFloor);
        TranslateTransition tt = view.getElevator(elevatorId).moveToY(y - 40); // moveToY doit retourner la Transition
        return tt;
    }


    public TranslateTransition movePersonTo(int personId, int targetFloor, double targetX) {
        double y = view.getFloorY(targetFloor) - 20;
        TranslateTransition tt = view.getPerson(personId).moveTo(targetX, y); // moveTo doit retourner la Transition
        return tt;
    }


    public void initialize() {
        moveElevator(0, 0);
        movePersonTo(0, 0, 80);
        movePersonTo(1, 0, 120);
        // Initialisation si nécessaire
    }

    public void up() {
        // Ascenseur monte à l'étage 5
        TranslateTransition elevatorMove = view.getElevator(0)
            .moveToY(view.getFloorY(5) - 40);
        elevatorMove.setOnFinished(e -> 
            // Personne 0 suit l'ascenseur
            view.getPerson(0).moveTo(view.getElevator_X() + 10, view.getFloorY(5) - 20)
        );
    }

    public void inLift() {
        // Ascenseur descend à l'étage 0
        TranslateTransition elevatorMove = view.getElevator(0)
            .moveToY(view.getFloorY(0) - 40);
        elevatorMove.setOnFinished(e -> 
            // Personne 0 entre dans l'ascenseur
            view.getPerson(0).moveTo(view.getElevator_X() + 10, view.getFloorY(0) - 20)
        );
    }

    public void outLift() {
        // Personne 0 sort de l'ascenseur en se décalant de +200 en X
        view.getPerson(0).moveTo(view.getPerson(0).getShape().getCenterX() + 200, view.getFloorY(0) - 20);
    }

    public void down() {
        // Ascenseur descend à l'étage 0
        TranslateTransition elevatorMove = view.getElevator(0)
            .moveToY(view.getFloorY(0) - 40);
        elevatorMove.setOnFinished(e -> 
            // Personne 0 descend avec l'ascenseur
            view.getPerson(0).moveTo(view.getElevator_X() + 10, view.getFloorY(0) - 20)
        );
    }

public int getCurrentElevatorFloor() {
    return currentElevatorFloor;
}

public void setCurrentElevatorFloor(int floor) {
    currentElevatorFloor = floor;
}

public TranslateTransition moveElevatorToFloor(int targetFloor) {
    TranslateTransition tt = moveElevator(0, targetFloor);
    setCurrentElevatorFloor(targetFloor);
    return tt;
}

public void fullUp(List<Integer> personIds, int startFloor, int endFloor) {
    double baseX = view.getElevator_X();

    for (int i = 0; i < personIds.size(); i++) {
        double personX = baseX + 20 + i * 2;
        view.getPerson(personIds.get(i)).moveTo(personX, view.getFloorY(startFloor) - 10);
    }

    int firstId = personIds.get(0);
    TranslateTransition firstPersonMove = view.getPerson(firstId)
            .moveTo(baseX + 20, view.getFloorY(startFloor) - 10);

    firstPersonMove.setOnFinished(e -> {
        TranslateTransition elevatorMove = moveElevatorToFloor(endFloor);

        ParallelTransition liftUp = new ParallelTransition();
        liftUp.getChildren().add(elevatorMove);

        for (int i = 0; i < personIds.size(); i++) {
            double personX = baseX + 20 + i * 2;
            TranslateTransition personMove = movePersonTo(personIds.get(i), endFloor, personX);
            liftUp.getChildren().add(personMove);
        }

        liftUp.setOnFinished(e2 -> {
            for (int i = 0; i < personIds.size(); i++) {
                double personX = baseX + 20 + i * 20 + 200;
                view.getPerson(personIds.get(i)).moveTo(personX, view.getFloorY(endFloor) - 10);
            }
        });

        liftUp.play();
    });
}

public void fullDown(List<Integer> personIds, int startFloor, int endFloor) {
    double baseX = view.getElevator_X();

    for (int i = 0; i < personIds.size(); i++) {
        double personX = baseX + 20 + i * 2;
        view.getPerson(personIds.get(i)).moveTo(personX, view.getFloorY(startFloor) - 10);
    }

    int firstId = personIds.get(0);
    TranslateTransition firstPersonMove = view.getPerson(firstId)
            .moveTo(baseX + 20, view.getFloorY(startFloor) - 20);

    firstPersonMove.setOnFinished(e -> {
        // Ascenseur et personnes descendent en parallèle
        TranslateTransition elevatorMove = moveElevatorToFloor(endFloor);

        ParallelTransition liftDown = new ParallelTransition();
        liftDown.getChildren().add(elevatorMove);

        for (int i = 0; i < personIds.size(); i++) {
            double personX = baseX + 20 + i * 2;
            TranslateTransition personMove = movePersonTo(personIds.get(i), endFloor, personX);
            liftDown.getChildren().add(personMove);
        }

        liftDown.setOnFinished(e2 -> {
            // Sortie des personnes
            for (int i = 0; i < personIds.size(); i++) {
                double personX = baseX + 20 + i * 20 + 200;
                view.getPerson(personIds.get(i)).moveTo(personX, view.getFloorY(endFloor) - 10);
            }
        });

        liftDown.play();
    });
}

public void move(List<Integer> personIds, int startFloor, int endFloor) {
    if (personIds.isEmpty()) return;

    double baseX = view.getElevator_X();

    for (int i = 0; i < personIds.size(); i++) {
        double personX = baseX + 20 + i * 5;
        view.getPerson(personIds.get(i)).moveTo(personX, view.getFloorY(startFloor) - 20);
    }

    int firstId = personIds.get(0);
    TranslateTransition firstPersonMove = view.getPerson(firstId)
            .moveTo(baseX + 20, view.getFloorY(startFloor) - 20);

    firstPersonMove.setOnFinished(e -> {
        TranslateTransition elevatorMove = moveElevatorToFloor(endFloor);

        ParallelTransition liftTransition = new ParallelTransition();
        liftTransition.getChildren().add(elevatorMove);

        for (int i = 0; i < personIds.size(); i++) {
            double personX = baseX + 20 + i * 5;
            TranslateTransition personMove = movePersonTo(personIds.get(i), endFloor, personX);
            liftTransition.getChildren().add(personMove);
        }

        liftTransition.setOnFinished(e2 -> exitLift(personIds, endFloor));

        liftTransition.play();
    });
}


public void waitLift(List<Integer> personIds, int floor) {
        double baseX = view.getElevator_X() + 30;   // position de file d'attente à droite
        double y = view.getFloorY(floor) - 20;      // Y correct de l'étage

        for (int i = 0; i < personIds.size(); i++) {
            int id = personIds.get(i);

            double x = baseX - i * 10;

            view.getPerson(id).moveTo(x, y);
        }
    }


    public void exitLift(List<Integer> personIds, int floor) {
        double baseX = view.getElevator_X() + 200; // point de départ à droite de l’ascenseur
        double y = view.getFloorY(floor) - 20;    // Y de l’étage

        for (int i = 0; i < personIds.size(); i++) {
            int id = personIds.get(i);

            double x = baseX + i * 20;

            // Déplacer la personne à la position finale
            view.getPerson(id).moveTo(x, y);
        }
    }

    public void actionTwo() {
        moveElevator(0, 5);
        movePersonTo(0, 5, 150);
    }
}
