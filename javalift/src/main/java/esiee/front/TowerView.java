package esiee.front;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class TowerView {
    private final Pane root = new Pane();
    private final List<FloorView> floors = new ArrayList<>();
    private final List<ElevatorView> elevators = new ArrayList<>();
    private final List<PersonView> people = new ArrayList<>();
    private final List<Integer> floorYs = new ArrayList<>();
    private List<Integer> selectedPersonIds = new ArrayList<>();
    private TowerRenderer renderer;
    private final BooleanProperty replayRequested = new SimpleBooleanProperty(false);

    private static final int FLOOR_HEIGHT = 80;
    private static final double ELEVATOR_X = 450;

    public TowerView(int numFloors) {
        int totalHeight = (numFloors + 1) * FLOOR_HEIGHT;
        root.setStyle("-fx-background-color: beige;");

        for (int i = 0; i < numFloors; i++) {
            int y = totalHeight - (i + 1) * FLOOR_HEIGHT;
            floorYs.add(y);
            FloorView floor = new FloorView(500, y, 300, 10);
            floors.add(floor);
            root.getChildren().add(floor.getShape());
        }

        // Ascenseur
        ElevatorView elevator = new ElevatorView(ELEVATOR_X, totalHeight - FLOOR_HEIGHT - 40, 40, 60);
        elevators.add(elevator);
        root.getChildren().add(elevator.getShape());

        // Listener
        replayRequested.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                renderer.inLift();
                renderer.up();
                renderer.outLift();
                replayRequested.set(false); // reset automatique
            }
        });

        // Zone de bouton 
        HBox buttonBox = new HBox(20);
        buttonBox.setLayoutX(20);

        // Champ pour saisir les IDs des personnes
        TextField personInput = new TextField();
        personInput.setPromptText("IDs (ex: 0,1,2)");

        // Bouton pour valider les IDs
        Button setPersonsButton = new Button("Set Persons");

        // Champ pour saisir l’étage de départ
        TextField startFloorInput = new TextField();
        startFloorInput.setPromptText("Start floor (ex: 0)");

        // Bouton pour valider l’étage de départ
        Button setStartFloorButton = new Button("Set Start Floor");
        setStartFloorButton.setOnAction(ev -> {
            try {
                Integer.parseInt(startFloorInput.getText().trim());
                // Valeur validée, peut être utilisée par les boutons Up/Down
            } catch (NumberFormatException ex) {
                System.out.println("Entree invalide pour l etage de depart.");
            }
        });

        // Champ pour saisir l’étage de destination
        TextField destFloorInput = new TextField();
        destFloorInput.setPromptText("Destination floor (ex: 5)");

        // Bouton pour valider l’étage de destination
        Button setDestFloorButton = new Button("Set Destination Floor");
        setDestFloorButton.setOnAction(ev -> {
            try {
                Integer.parseInt(destFloorInput.getText().trim());
                // Valeur validée, peut être utilisée par les boutons Up/Down
            } catch (NumberFormatException ex) {
                System.out.println("Entree invalide pour l etage de destination.");
            }
        });

        Button replayButton = new Button("Up");
        replayButton.setOnAction(e -> {
            if (renderer != null) {
                int startFloor;
                int endFloor;

                try {
                    startFloor = getPerson(selectedPersonIds.get(0)).getFloor();  // Integer.parseInt(startFloorInput.getText().trim());
                    endFloor = Integer.parseInt(destFloorInput.getText().trim());
                } catch (NumberFormatException ex) {
                    System.out.println("Entree invalide pour les etages.");
                    return;
                }

                // Deplacer l’ascenseur d’abord à l’etage de depart si necessaire
                if (renderer.getCurrentElevatorFloor() != startFloor) {
                    TranslateTransition moveLift = renderer.moveElevatorToFloor(startFloor);
                    moveLift.setOnFinished(ev -> renderer.move(selectedPersonIds, startFloor, endFloor));
                } else {
                    renderer.move(selectedPersonIds, startFloor, endFloor);
                }
            }
        });

        Button anotherButton = new Button("Down");
        anotherButton.setOnAction(e -> {
            if (renderer != null) {

                int startFloor;
                int endFloor;
                try {
                    startFloor = getPerson(selectedPersonIds.get(0)).getFloor();
                    endFloor = Integer.parseInt(destFloorInput.getText().trim());
                } catch (NumberFormatException ex) {
                    System.out.println("Entree invalide pour les etages.");
                    return;
                }

                // Deplacer l’ascenseur d’abord à l’etage de depart si necessaire
                if (renderer.getCurrentElevatorFloor() != startFloor) {
                    TranslateTransition moveLift = renderer.moveElevatorToFloor(startFloor);
                    moveLift.setOnFinished(ev -> renderer.move(selectedPersonIds, startFloor, endFloor));
                } else {
                    renderer.move(selectedPersonIds, startFloor, endFloor);
                }
                for (int personId : selectedPersonIds) {
                    getPerson(personId).setFloor(endFloor);
                }
            }
        });

        Button varButton = new Button("Lift to random floor");
        varButton.setOnAction(e -> {
            if (renderer != null) {
                int randomFloor = (int)(Math.random() * 9); // etage aleatoire entre 0 et 9
                renderer.moveElevatorToFloor(randomFloor);
            }
        });


        setPersonsButton.setOnAction(e -> {
            if (renderer != null) {
                String text = personInput.getText();
                List<Integer> personIds = new ArrayList<>();
                try {
                    for (String s : text.split(",")) {
                        personIds.add(Integer.parseInt(s.trim()));
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Entree invalide. Format attendu : 0,1,2");
                    return;
                }
                // Stocker la liste quelque part ou l’utiliser directement
                selectedPersonIds = personIds; // selectedPersonIds est une variable List<Integer>
            }
        });

        buttonBox.getChildren().addAll(
            replayButton, anotherButton, varButton,
            personInput, setPersonsButton,
            startFloorInput, setStartFloorButton,
            destFloorInput, setDestFloorButton
        );        
        root.getChildren().add(buttonBox);

    }

    public void setRenderer(TowerRenderer renderer) {
        this.renderer = renderer;
    }

    public Pane getRoot() {
        return root;
    }

    public BooleanProperty replayRequestedProperty() {
        return replayRequested;
    }

    public double getElevator_X() {
        return ELEVATOR_X;
    }


    public ElevatorView getElevator(int id) { return elevators.get(id); }
    public PersonView getPerson(int id) { return people.get(id); }
    public int getFloorY(int floorIndex) {
        return floorYs.get(floorIndex);
    }


    public void addPerson(int id, double x, double y, Color color, int floor, int floor_dest) {
        PersonView p = new PersonView(x, y, color, floor, floor_dest);
        people.add(p);
        root.getChildren().add(p.getShape());
    }
}
