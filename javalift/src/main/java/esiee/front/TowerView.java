package esiee.front;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

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
        int sceneWidth = 1900;
        int sceneHeight = 1100;
        root.setStyle("-fx-background-color: lightblue;"); // ciel

        double groundHeight = 280; // sol
        Rectangle ground = new Rectangle(0, sceneHeight - groundHeight, sceneWidth, groundHeight);
        ground.setFill(Color.LIGHTGREEN);
        root.getChildren().add(ground);

        // Soleil
        Circle sun = new Circle(sceneWidth - 150, 150, 60);
        sun.setFill(Color.GOLD);
        root.getChildren().add(sun);

        // Structure extérieure de la tour
        // Alignement de la tour avec les étages
        double towerWidth = 500;                  // largeur de la tour
        double towerX = ELEVATOR_X - 50;          // 50 px à gauche de l'ascenseur
        double towerHeight = numFloors * FLOOR_HEIGHT;
        double towerY = sceneHeight - groundHeight - towerHeight; // en haut du sol

        Rectangle towerStructure = new Rectangle(towerX, towerY, towerWidth, towerHeight);
        towerStructure.setFill(Color.LIGHTGRAY);
        towerStructure.setStroke(Color.DARKGRAY);
        towerStructure.setStrokeWidth(5);
        root.getChildren().add(towerStructure);
        
        // 🌳 Ajout d'arbres décoratifs sur le sol
        int numTrees = 6; // nombre d'arbres
        double groundY = sceneHeight - groundHeight;

        for (int i = 0; i < numTrees; i++) {
            double treeX = 1000 + i * 150; // espacement horizontal
            double trunkHeight = 40 + Math.random() * 20;
            double trunkWidth = 15;
            Rectangle trunk = new Rectangle(treeX, groundY - trunkHeight, trunkWidth, trunkHeight);
            trunk.setFill(Color.SADDLEBROWN);

            double foliageRadius = 30 + Math.random() * 15;
            Circle foliage = new Circle(treeX + trunkWidth / 2, groundY - trunkHeight - foliageRadius, foliageRadius);
            foliage.setFill(Color.FORESTGREEN);

            root.getChildren().addAll(trunk, foliage);
        }
        numTrees = 3; // nombre d'arbres

        for (int i = 0; i < numTrees; i++) {
            double treeX = 50 + i * 130; // espacement horizontal
            double trunkHeight = 40 + Math.random() * 20;
            double trunkWidth = 15;
            Rectangle trunk = new Rectangle(treeX, groundY - trunkHeight, trunkWidth, trunkHeight);
            trunk.setFill(Color.SADDLEBROWN);

            double foliageRadius = 30 + Math.random() * 15;
            Circle foliage = new Circle(treeX + trunkWidth / 2, groundY - trunkHeight - foliageRadius, foliageRadius);
            foliage.setFill(Color.FORESTGREEN);

            root.getChildren().addAll(trunk, foliage);
        }
        // Charger l’image
        Image planeImage = new Image(getClass().getResource("/airplane.png").toExternalForm());
        ImageView planeView = new ImageView(planeImage);
        planeView.setX(1250);   // position X
        planeView.setY(200);    // position Y
        planeView.setFitWidth(200); // largeur
        planeView.setFitHeight(150); // hauteur

        root.getChildren().add(planeView);
        
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
            if (renderer == null) return;
            if (selectedPersonIds == null || selectedPersonIds.isEmpty()) return;

            // Etage de destination
            int destFloor;
            try {
                destFloor = Integer.parseInt(destFloorInput.getText().trim());
            } catch (Exception ex) {
                System.out.println("Destination invalide.");
                return;
            }

            // Récupérer les étages de départ
            List<Integer> startFloors = selectedPersonIds.stream()
                    .map(id -> getPerson(id).getFloor())
                    .toList();

            // Tri des personnes du plus haut étage au plus bas
            List<Integer> order = new ArrayList<>();
            for (int i = 0; i < selectedPersonIds.size(); i++) order.add(i);
            order.sort((a, b) -> startFloors.get(b) - startFloors.get(a));

            List<Integer> boarded = new ArrayList<>();
            double baseX = getElevator_X();

            Runnable[] step = new Runnable[1];
            step[0] = () -> {

                // Si plus personne à prendre → aller au destination
                if (order.isEmpty()) {

                    ParallelTransition pt = new ParallelTransition();
                    pt.getChildren().add(renderer.moveElevatorToFloor(destFloor));

                    // Déplacer les personnes embarquées avec l'ascenseur
                    for (int i = 0; i < boarded.size(); i++) {
                        int pid = boarded.get(i);
                        double px = baseX + 20 + i * 10;
                        pt.getChildren().add(renderer.movePersonTo(pid, destFloor, px));
                    }

                    // Sortie lorsque arrivée
                    pt.setOnFinished(ev -> renderer.exitLift(boarded, destFloor));
                    pt.play();
                    return;
                }

                // ➤ Prochaine personne à ramasser
                int idx = order.remove(0);
                int pid = selectedPersonIds.get(idx);
                int floor = startFloors.get(idx);

                // 1) Monter à l'étage de cette personne (avec personnes embarquées)
                ParallelTransition goPick = new ParallelTransition();
                goPick.getChildren().add(renderer.moveElevatorToFloor(floor));

                for (int i = 0; i < boarded.size(); i++) {
                    int bid = boarded.get(i);
                    double px = baseX + 20 + i * 10;
                    goPick.getChildren().add(renderer.movePersonTo(bid, floor, px));
                }

                goPick.setOnFinished(ev -> {

                    // 2) Faire entrer la personne dans l'ascenseur
                    double enterX = baseX + 20 + boarded.size() * 10;

                    TranslateTransition enter = renderer.movePersonTo(pid, floor, enterX);
                    enter.setOnFinished(ev2 -> {

                        boarded.add(pid); // maintenant embarqué

                        // continuer avec la suivante
                        step[0].run();
                    });

                    enter.play();
                });

                goPick.play();
            };

            step[0].run();
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


    private void runUpSequence(List<Integer> sortedIndexes,
                            List<Integer> startFloors,
                            int destFloor,
                            int step) {

        // Si on a récupéré toutes les personnes → aller à la destination
        if (step >= sortedIndexes.size()) {

            TranslateTransition finalMove =
                    renderer.moveElevatorToFloor(destFloor);

            finalMove.setOnFinished(e -> {
                // Déplacer toutes les personnes vers la destination finale
                renderer.move(selectedPersonIds,
                        renderer.getCurrentElevatorFloor(),
                        destFloor);

                for (int personId : selectedPersonIds)
                    getPerson(personId).setFloor(destFloor);
            });

            return;
        }

        // Id de la personne à récupérer dans cette étape
        int index = sortedIndexes.get(step);
        int personId = selectedPersonIds.get(index);
        int floor = startFloors.get(index);

        // 1️⃣ Monter (ou descendre) à l’étage de la personne
        TranslateTransition travel =
                renderer.moveElevatorToFloor(floor);

        travel.setOnFinished(e -> {

            // 2️⃣ Faire entrer la personne dans l’ascenseur
            renderer.move(
                    List.of(personId),
                    floor,
                    floor   // même étage : juste entrée dans lift
            );

            // 3️⃣ Étape suivante : ramasser la personne suivante
            runUpSequence(sortedIndexes, startFloors, destFloor, step + 1);
        });
    }


    public void addPerson(int id, double x, double y, Color color, int floor, int floor_dest) {
        PersonView p = new PersonView(x, y, color, floor, floor_dest);
        people.add(p);
        root.getChildren().add(p.getShape());
    }
}
