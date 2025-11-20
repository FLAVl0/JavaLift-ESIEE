package esiee.front;
import java.util.List;

import esiee.Demo;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;

public class TowerRenderer {
    private final TowerView view;
    private int currentElevatorFloor = 0; // étage actuel de l'ascenseur

    public TowerRenderer(TowerView view, int numPersons, int numFloors) {
        this.view = view;
        Demo demo = view.getDemo();
        int p = 0;
        // for (int i = 1; i < numFloors; i++) {
        //     for (int j = 0; j < numPersons; j++) {
        //         view.addPerson(p++, 20*j+700, i*80, Color.color(Math.random(), Math.random(), Math.random()), numFloors -i, 0);
        //     }
        // }
        for (esiee.Personnes person : demo.personnes) {
            int floor = person.etage();
            p = person.getId();
            if (floor >= 0){
                System.out.println("Adding person " + (person.getId()-1) + " at floor " + floor+ " at position " + ((10 * (person.getId()%5)) + 700) + ", " + ((numFloors - floor) * 80 -20));
                view.addPerson(person.getId(), 20 * (person.getId()%5) + 700, (numFloors - floor) * 80 -20, 
                    javafx.scene.paint.Color.color((floor*10)/255.0,floor*5 /255.0, floor*20/255.0), 
                    floor, 0);
                    
            }
            view.root.getChildren().add(view.createFloor0Mask());
            
        }
    }

    public TranslateTransition moveElevator(int elevatorId, int targetFloor) {
    double y = view.getFloorY(targetFloor);
        TranslateTransition tt = view.getElevator(elevatorId).moveToY(y - 40); // moveToY doit retourner la Transition
        return tt;
    }


    public TranslateTransition movePersonTo(int personId, int targetFloor, double targetX) {
        double y = view.getFloorY(targetFloor) - 20;
        TranslateTransition tt = view.getPersonById(personId).moveTo(targetX, y); // moveTo doit retourner la Transition
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
            view.getPersonById(0).moveTo(view.getElevator_X() + 10, view.getFloorY(5) - 20)
        );
    }

    public void inLift() {
        // Ascenseur descend à l'étage 0
        TranslateTransition elevatorMove = view.getElevator(0)
            .moveToY(view.getFloorY(0) - 40);
        elevatorMove.setOnFinished(e -> 
            // Personne 0 entre dans l'ascenseur
            view.getPersonById(0).moveTo(view.getElevator_X() + 10, view.getFloorY(0) - 20)
        );
    }

    public void outLift() {
        // Personne 0 sort de l'ascenseur en se décalant de +200 en X
        view.getPersonById(0).moveTo(view.getPersonById(0).getShape().getCenterX() + 200, view.getFloorY(0) - 20);
    }

    public void down() {
        // Ascenseur descend à l'étage 0
        TranslateTransition elevatorMove = view.getElevator(0)
            .moveToY(view.getFloorY(0) - 40);
        elevatorMove.setOnFinished(e -> 
            // Personne 0 descend avec l'ascenseur
            view.getPersonById(0).moveTo(view.getElevator_X() + 10, view.getFloorY(0) - 20)
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

    // 1️⃣ Positionner toutes les personnes dans l'ascenseur
    for (int i = 0; i < personIds.size(); i++) {
        double personX = baseX + 20 + i * 5;
        view.getPersonById(personIds.get(i)).moveTo(personX, view.getFloorY(startFloor) - 20);
    }

    int firstId = personIds.get(0);
    TranslateTransition firstPersonMove = view.getPersonById(firstId)
            .moveTo(baseX + 20, view.getFloorY(startFloor) - 20);

    firstPersonMove.setOnFinished(e -> {
        // Ascenseur et personnes montent en parallèle
        TranslateTransition elevatorMove = moveElevatorToFloor(endFloor);

        ParallelTransition liftUp = new ParallelTransition();
        liftUp.getChildren().add(elevatorMove);

        for (int i = 0; i < personIds.size(); i++) {
            double personX = baseX + 20 + i * 5;
            TranslateTransition personMove = movePersonTo(personIds.get(i), endFloor, personX);
            liftUp.getChildren().add(personMove);
        }

        liftUp.setOnFinished(e2 -> {
            // Sortie des personnes
            for (int i = 0; i < personIds.size(); i++) {
                double personX = baseX + 20 + i * 20 + 200;
                view.getPersonById(personIds.get(i)).moveTo(personX, view.getFloorY(endFloor) - 20);
            }
        });

        liftUp.play();
    });
}

public void fullDown(List<Integer> personIds, int startFloor, int endFloor) {
    double baseX = view.getElevator_X();

    // 1️⃣ Positionner toutes les personnes dans l'ascenseur
    for (int i = 0; i < personIds.size(); i++) {
        double personX = baseX + 20 + i * 5;
        view.getPersonById(personIds.get(i)).moveTo(personX, view.getFloorY(startFloor) - 20);
    }

    int firstId = personIds.get(0);
    TranslateTransition firstPersonMove = view.getPersonById(firstId)
            .moveTo(baseX + 20, view.getFloorY(startFloor) - 20);

    firstPersonMove.setOnFinished(e -> {
        // Ascenseur et personnes descendent en parallèle
        TranslateTransition elevatorMove = moveElevatorToFloor(endFloor);

        ParallelTransition liftDown = new ParallelTransition();
        liftDown.getChildren().add(elevatorMove);

        for (int i = 0; i < personIds.size(); i++) {
            double personX = baseX + 20 + i * 5;
            TranslateTransition personMove = movePersonTo(personIds.get(i), endFloor, personX);
            liftDown.getChildren().add(personMove);
        }

        liftDown.setOnFinished(e2 -> {
            // Sortie des personnes
            for (int i = 0; i < personIds.size(); i++) {
                double personX = baseX + 20 + i * 20 + 200;
                view.getPersonById(personIds.get(i)).moveTo(personX, view.getFloorY(endFloor) - 20);
            }
        });

        liftDown.play();
    });
}

public void move(List<Integer> personIds, int startFloor, int endFloor) {
    if (personIds.isEmpty()) return;

    double baseX = view.getElevator_X();

    // 1️⃣ Positionner toutes les personnes au départ
    for (int i = 0; i < personIds.size(); i++) {
        double personX = baseX + 20 + i * 5;
        view.getPersonById(personIds.get(i)).moveTo(personX, view.getFloorY(startFloor) - 20);
    }

    int firstId = personIds.get(0);
    TranslateTransition firstPersonMove = view.getPersonById(firstId)
            .moveTo(baseX + 20, view.getFloorY(startFloor) - 20);

    firstPersonMove.setOnFinished(e -> {
        // 2️⃣ Déplacement de l’ascenseur et des personnes
        TranslateTransition elevatorMove = moveElevatorToFloor(endFloor);

        ParallelTransition liftTransition = new ParallelTransition();
        liftTransition.getChildren().add(elevatorMove);

        for (int i = 0; i < personIds.size(); i++) {
            double personX = baseX + 20 + i * 5;
            TranslateTransition personMove = movePersonTo(personIds.get(i), endFloor, personX);
            liftTransition.getChildren().add(personMove);
        }

        // 3️⃣ Sortie des personnes à la fin
        liftTransition.setOnFinished(e2 -> exitLift(personIds, endFloor));

        liftTransition.play();
    });
}


public void waitLift(List<Integer> personIds, int floor) {
        double baseX = view.getElevator_X() + 30;   // position de file d'attente à droite
        double y = view.getFloorY(floor) - 20;      // Y correct de l'étage

        for (int i = 0; i < personIds.size(); i++) {
            int id = personIds.get(i);

            // Espacement entre personnes : 15 px
            double x = baseX - i * 10;

            view.getPersonById(id).moveTo(x, y);
        }
    }


    public void exitLift(List<Integer> personIds, int floor) {
        double baseX = view.getElevator_X() + 200; // point de départ à droite de l’ascenseur
        double y = view.getFloorY(floor) - 20;    // Y de l’étage

        for (int i = 0; i < personIds.size(); i++) {
            int id = personIds.get(i);
            baseX = 20 * (id %5) + 700;
            // Espacement horizontal entre personnes : 20 px
            double x = baseX + i * 20;

            // Déplacer la personne à la position finale
            view.getPersonById(id).moveTo(baseX, y);
        }
    }




    public void actionTwo() {
        moveElevator(0, 5);
        movePersonTo(0, 5, 150);
    }
}
