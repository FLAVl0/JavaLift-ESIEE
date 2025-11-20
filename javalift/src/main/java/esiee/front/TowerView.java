package esiee.front;

import java.util.ArrayList;
import java.util.List;

import esiee.Demo;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
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
    public final Pane root = new Pane();
    private final List<FloorView> floors = new ArrayList<>();
    private final List<ElevatorView> elevators = new ArrayList<>();
    private final List<PersonView> people = new ArrayList<>();
    private final List<Integer> floorYs = new ArrayList<>();
    private List<Integer> selectedPersonIds = new ArrayList<>();
    private TowerRenderer renderer;
    private final BooleanProperty replayRequested = new SimpleBooleanProperty(false);
    private Demo demo;
    private Rectangle floor0Mask;
    // --- Synchronisation avec Demo ---
    private volatile boolean actionPending = false;
    private volatile boolean actionCompleted = false;
    private final List<Runnable> actionQueue = new ArrayList<>();
    private boolean actionRunning = false;

    // ID des personnes impliquées dans l’action
    private List<Integer> pendingPersonIds = new ArrayList<>();
    private List<Integer> pendingDestFloors;


    // étage destination
    private int pendingDestFloor = -1;



    private static final int FLOOR_HEIGHT = 80;
    private static final double ELEVATOR_X = 450;

    public TowerView(int numFloors, Demo demo) {
        this.demo = demo;
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
        double towerWidth = 500; // largeur de la tour
        double towerX = ELEVATOR_X - 50; // 50 px à gauche de l'ascenseur
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
        planeView.setX(1250); // position X
        planeView.setY(200); // position Y
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
            if (renderer == null)
                return;
            if (selectedPersonIds == null || selectedPersonIds.isEmpty())
                return;

            int destFloor;
            try {
                destFloor = Integer.parseInt(destFloorInput.getText().trim());
            } catch (Exception ex) {
                System.out.println("Destination invalide.");
                return;
            }

            // --- Construire une liste {personId, floor} ---
            class Entry {
                int pid;
                int floor;

                Entry(int pid, int floor) {
                    this.pid = pid;
                    this.floor = floor;
                }
            }

            List<Entry> entries = new ArrayList<>();

            for (int pid : selectedPersonIds) {
                System.out.println("Getting floor for person " + pid);
                entries.add(new Entry(pid, getPersonById(pid).getFloor()));
            }
            System.out.println("Entries before sort:");
            for (Entry en : entries) {
                System.out.println("Person " + en.pid + " at floor " + en.floor);
            }
            // --- TRI réel des personnes du plus haut au plus bas ---
            entries.sort((a, b) -> b.floor - a.floor);
            System.out.println("Entries after sort:");
            for (Entry en : entries) {
                System.out.println("Person " + en.pid + " at floor " + en.floor);
            }
            // --- On conclut : plus d’inversion possible ---
            List<Integer> boarded = new ArrayList<>();
            double baseX = getElevator_X();

            Runnable[] step = new Runnable[1];

            step[0] = () -> {

                if (entries.isEmpty()) {

                    ParallelTransition pt = new ParallelTransition();
                    pt.getChildren().add(renderer.moveElevatorToFloor(destFloor));

                    for (int i = 0; i < boarded.size(); i++) {
                        int pid = boarded.get(i);
                        double px = baseX + 20 + i * 10;
                        pt.getChildren().add(renderer.movePersonTo(pid, destFloor, px));
                    }

                    pt.setOnFinished(ev -> renderer.exitLift(boarded, destFloor));
                    pt.play();
                    return;
                }

                // --- Toujours correct maintenant : {pid, floor} ---
                Entry next = entries.remove(0);

                int pid = next.pid;
                int floor = next.floor;
                System.out.println("Picking person " + pid + " at floor " + floor);


                ParallelTransition goPick = new ParallelTransition();
                goPick.getChildren().add(renderer.moveElevatorToFloor(floor));

                for (int i = 0; i < boarded.size(); i++) {
                    int bid = boarded.get(i);
                    double px = baseX + 20 + i * 10;
                    goPick.getChildren().add(renderer.movePersonTo(bid, floor, px));
                }

                goPick.setOnFinished(ev -> {

                    double enterX = baseX + 20 + boarded.size() * 10;
                    System.out.println("Person " + pid + " entering elevator at position " + enterX);

                    TranslateTransition enter = renderer.movePersonTo(pid, floor, enterX);
                    enter.setOnFinished(ev2 -> {
                        boarded.add(pid);
                        step[0].run();
                    });

                    enter.play();
                });

                goPick.play();
            };

            step[0].run();

            // Mettre à jour l’étage des personnes
            for (int personId : selectedPersonIds) {
                getPersonById(personId).setFloor(destFloor);
            }

            // Demo backend update
            for (esiee.Personnes p : demo.personnes) {
                if (selectedPersonIds.contains(p.getId()-1)) {
                    p.setEtage(destFloor);
                }
            }
            System.out.println("Updated persons in demo:");
            demo.personnes.forEach(p -> System.out.println(p));

            demo.printDemo();

        });

        Button anotherButton = new Button("Down");
        anotherButton.setOnAction(e -> {
            if (renderer != null) {
                // Test d’affichage des personnes du demo
                System.out.println("Persons in selectedPerson :");
                System.out.println(selectedPersonIds);
                int startFloor;
                int endFloor;
                try {
                    startFloor =   getPersonById(selectedPersonIds.get(0)).getFloor();
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
                    getPersonById(personId).setFloor(endFloor);
                }
            }
        });

        Button varButton = new Button("Lift to random floor");
        varButton.setOnAction(e -> {
            if (renderer != null) {
                int randomFloor = (int) (Math.random() * 9); // etage aleatoire entre 0 et 9
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
                destFloorInput, setDestFloorButton);
        root.getChildren().add(buttonBox);

    }

    public Demo getDemo() {
        return demo;
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

    public ElevatorView getElevator(int id) {
        return elevators.get(id);
    }

    public PersonView  getPersonById(int id) {
        return people.get(id);
    }

    public int getFloorY(int floorIndex) {
        return floorYs.get(floorIndex);
    }


    public void scheduleExternalAction(List<Integer> personIds, List<Integer> destFloors) {
        this.pendingPersonIds = new ArrayList<>(personIds);
        this.pendingDestFloors = new ArrayList<>(destFloors);
        this.actionPending = true;
        this.actionCompleted = false;
    }


    public void enqueueAction(Runnable action) {
        actionQueue.add(action);
        if (!actionRunning) {
            runNextAction();
        }
    }

    private void runNextAction() {
        if (actionQueue.isEmpty()) {
            actionRunning = false;
            return;
        }
        actionRunning = true;
        Runnable action = actionQueue.remove(0);
        action.run();
    }



    private void notifyActionCompleted() {
        this.actionCompleted = true;
        this.actionPending = false;
    }

    public boolean isActionCompleted() {
        return actionCompleted;
    }

    public boolean hasPendingAction() {
        return actionPending;
    }

public void processExternalAction() {
    if (!actionPending || renderer == null) return;

    List<Integer> ids = new ArrayList<>(pendingPersonIds);
    List<Integer> dests = new ArrayList<>(pendingDestFloors); // Destination individuelle
    if (ids.isEmpty()) {
        notifyActionCompleted();
        return;
    }

    selectedPersonIds = ids;
    List<Integer> boarded = new ArrayList<>();
    double baseX = getElevator_X();

    // --- Création des entrées avec étage de départ et destination ---
    class Entry { int pid; int fromFloor; int toFloor; Entry(int p, int f, int t){ pid=p; fromFloor=f; toFloor=t; } }
    List<Entry> entries = new ArrayList<>();
    for (int i = 0; i < ids.size(); i++) {
        int pid = ids.get(i);
        int from = getPersonById(pid).getFloor(); // étage dynamique
        int to = dests.get(i);
        entries.add(new Entry(pid, from, to));
    }

    Runnable[] step = new Runnable[1];
    step[0] = new Runnable() {
        @Override
        public void run() {
            // --- Tout est terminé ---
            if (entries.isEmpty() && boarded.isEmpty()) {
                notifyActionCompleted();
                runNextAction();
                return;
            }

            // --- Récupérer toutes les personnes au même étage ---
            if (!entries.isEmpty()) {
                int currentFloor = getPersonById(entries.get(0).pid).getFloor(); // dynamique
                List<Entry> toBoard = new ArrayList<>();
                while (!entries.isEmpty() && getPersonById(entries.get(0).pid).getFloor() == currentFloor) {
                    toBoard.add(entries.remove(0));
                }

                // Monter l’ascenseur et déplacer les personnes déjà embarquées
                ParallelTransition goToFloor = new ParallelTransition();
                goToFloor.getChildren().add(renderer.moveElevatorToFloor(currentFloor));
                for (int i = 0; i < boarded.size(); i++) {
                    int pid = boarded.get(i);
                    double px = baseX + 20 + i * 1;
                    goToFloor.getChildren().add(renderer.movePersonTo(pid, currentFloor, px));
                }

                goToFloor.setOnFinished(ev -> {
                    // Faire entrer toutes les personnes du même étage
                    SequentialTransition enterAll = new SequentialTransition();
                    for (Entry e : toBoard) {
                        double enterX = baseX + 20 + boarded.size() * 1;
                        TranslateTransition enter = renderer.movePersonTo(e.pid, currentFloor, enterX);
                        enter.setOnFinished(ev2 -> {
                            boarded.add(e.pid);
                            // Mise à jour immédiate de l’étage pour synchronisation
                            getPersonById(e.pid).setFloor(currentFloor);
                            demo.personnes.get(e.pid).setEtage(currentFloor);
                        });
                        enterAll.getChildren().add(enter);
                    }
                    enterAll.setOnFinished(ev2 -> step[0].run());
                    enterAll.play();
                });

                goToFloor.play();
                return;
            }

            // --- Déplacer les personnes embarquées vers leurs destinations ---
            if (!boarded.isEmpty()) {
                // Identifier le prochain arrêt (le plus proche selon direction)
                int nextStop = boarded.stream()
                        .map(pid -> dests.get(ids.indexOf(pid)))
                        .min(Integer::compareTo)
                        .orElse(boarded.get(0));

                final int stopFloor = nextStop;

                ParallelTransition moveElevator = new ParallelTransition();
                moveElevator.getChildren().add(renderer.moveElevatorToFloor(stopFloor));

                // Déplacer toutes les personnes embarquées avec l’ascenseur
                for (int i = 0; i < boarded.size(); i++) {
                    int pid = boarded.get(i);
                    double px = baseX + 20 + i * 1;
                    moveElevator.getChildren().add(renderer.movePersonTo(pid, stopFloor, px));
                }

                moveElevator.setOnFinished(ev -> {
                    // Identifier les personnes qui doivent sortir à cet étage
                    List<Integer> toExit = new ArrayList<>();
                    for (int pid : boarded) {
                        if (dests.get(ids.indexOf(pid)) == stopFloor) toExit.add(pid);
                    }

                    if (!toExit.isEmpty()) {
                        ParallelTransition exitTransition = new ParallelTransition();
                        for (int pid : toExit) {
                            double px = baseX + 160 + stopFloor * 10; // décalage X selon étage
                            exitTransition.getChildren().add(renderer.movePersonTo(pid, stopFloor, px));
                        }

                        exitTransition.setOnFinished(ev2 -> {
                            for (int pid : toExit) {
                                boarded.remove(Integer.valueOf(pid));
                                getPersonById(pid).setFloor(stopFloor);
                                demo.personnes.get(pid).setEtage(stopFloor);
                            }
                            step[0].run();
                        });

                        exitTransition.play();
                    } else {
                        step[0].run();
                    }
                });

                moveElevator.play();
            }
        }
    };

    enqueueAction(step[0]);
}




    public Rectangle createFloor0Mask() {
        double x = 500; // position horizontale de la forme
        double y = 735; // méthode qui renvoie la coordonnée Y du floor 0
        double width = 250; // largeur du TowerView
        double height = 60; // hauteur du floor 0

        floor0Mask = new Rectangle(x, y, width, height);
        floor0Mask.setFill(Color.LIGHTGRAY); // couleur qui masque les personnes, tu peux mettre Color.LIGHTGRAY ou autre
        floor0Mask.setStroke(null);
        return floor0Mask;
        // Ajouter au "premier plan" → après toutes les personnes et l’ascenseur
    }

    public void addPerson(int id, double x, double y, Color color, int floor, int floor_dest) {
        PersonView p = new PersonView(id, x, y, color, floor, floor_dest);
        people.add(p);
        root.getChildren().add(p.getShape());
    }
}
