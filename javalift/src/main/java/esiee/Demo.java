package esiee;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import esiee.config.Configuration;
import esiee.config.ConfigurationRecord;
import esiee.config.RecordList;
import esiee.config.SimulationConfig;
import esiee.front.TowerView;
import esiee.lift.AGA;
import esiee.lift.global.dispatcher.DispatcherStrategy;
import esiee.lift.global.request.Call;
import esiee.lift.local.Heuristics;
import esiee.lift.local.builder.LiftManagerBuilder;
import javafx.application.Platform;
public class Demo {
    public ArrayList<Etage> etages;
    public ArrayList<Personnes> personnes;
    public SimulationConfig simConfig;
    public ConfigurationRecord config;
    public AGA aga = new AGA(DispatcherStrategy.CLOSEST_LIFT);

    public Demo() {
        // --- Création des étages de la tour --- //
        List<Etage> etages = new ArrayList<>();
        etages.add(new Etage(0, 100)); // Rez-de-chaussée (hall)
        etages.add(new Etage(1, 45));
        etages.add(new Etage(2, 50));
        etages.add(new Etage(3, 40));
        etages.add(new Etage(4, 40));
        etages.add(new Etage(5, 35));
        etages.add(new Etage(6, 35));
        etages.add(new Etage(7, 30));
        etages.add(new Etage(8, 30));
        etages.add(new Etage(9, 25));

        // --- Création des ascenseurs de la tour --- //
        aga.addLift(new LiftManagerBuilder().setHeuristics(Heuristics.SHORTEST_PATH));
        aga.addLift(new LiftManagerBuilder().setHeuristics(Heuristics.SHORTEST_PATH));

        // etages.add(new Etage(10, 25));
        this.etages = new ArrayList<>(etages);
        // --- Chargement de la configuration depuis les fichiers JSON ---
        Configuration Configuration = new Configuration();
        Enum<?>[] recordList = new RecordList[]{
            RecordList.SIMULATION,
            RecordList.TOWER,
            RecordList.LIFT,
            RecordList.HABITUDE,
            RecordList.HABITUDE_DEF
        };


        config = Configuration.formatAll(recordList);
        System.out.println("Configuration loaded: " + config);
        simConfig = config.simulationConfig();
        
        this.personnes = new ArrayList<>();
        int personIdCounter = 1;
        for (Etage etage : etages) {
            if (etage.niveau() == 0)
                continue; // Pas de personnes au RDC
            for (int i = 0; i < 5; i++) {
                // Création d'une personne simple avec habits vides
                Personnes p = new PersonneSimple(personIdCounter, 5, new ArrayList<>());
                p.setEtage(etage.niveau());
                etage.ajouterPersonne(p);
                this.personnes.add(p);
                personIdCounter++;
            }
        }

        // --- Affichage pour vérification ---
        System.out.println("==== Initialisation automatique des personnes ====");
        for (Personnes p : personnes) {
            System.out.println("Person " + p.getId() + " at floor " + p.etage());
        }

        // --- Affichage global ---
        System.out.println("===== État de la tour =====");
        for (Etage etage : etages) {
            if (etage.getNombrePersonnesPresentes() > 0) {
                System.out.println(etage + " - Personnes: " + etage.personnes());
            }
        }
        // --- Attribution des habitudes --- //
        // for (Personnes p : personnes) {
        //     List<Habitude> habitudes = new ArrayList<>();
        //     // Exemple d'habitude: aller au 5ème étage à 8h00
        //     //habitudes.add(new Habitude(8 * 3600, 5)); // 8h00 en secondes
        //     // Ajouter d'autres habitudes si nécessaire
        //     // ...
        //     // Ici on remplace les habitudes vides par les nouvelles
        //     if (p instanceof PersonneSimple) {
        //         ((PersonneSimple) p).habitudes().clear();
        //         ((PersonneSimple) p).habitudes().addAll(habitudes);
        //     }
        // }
        // --- Affichage des habitudes ---
        System.out.println("\n===== Liste des personnes et de leurs habitudes =====");
        for (Personnes p : personnes) {
            System.out.println(p.getId() + " (étage " + p.etage() + ")");
            for (Habitude h : p.habitudes()) {
                System.out.println("  → " + h);
            }
        }

        

        // --- Affichage final de l'état de la tour ---
        System.out.println("\n===== État final de la tour =====");
        for (Etage etage : etages) {
            if (etage.getNombrePersonnesPresentes() > 0) {
                System.out.println(etage);
                for (Personnes p : etage.personnes()) {
                    System.out.println("  - " + p.getId());
                }
            }
        }
    }

    public void startAutomaticSimulation(TowerView view) {
        new Thread(() -> {
            Random rand = new Random();
            int timer = config.simulationConfig().getStartTime(); // compteur de secondes

            while (true) {
                if (timer >= config.simulationConfig().getEndTime()) { // 12 heures en secondes
                    view.stopSimulation();
                    break; // arret de simu
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new AssertionError(e);
                }

                timer++;
                int timerFinal = timer;
                Platform.runLater(() -> view.updateClock(timerFinal));
                System.out.println("Timer = " + timer + " sec");

                // --- Choisir un étage aléatoire
                int floor = rand.nextInt(etages.size());
                System.out.println("");

                // --- Récupérer toutes les personnes présentes à cet étage
                List<Integer> idsAtFloor = new ArrayList<>();
                for (Personnes p : personnes) {
                    if (p.etage() == floor) {
                        idsAtFloor.add(p.getId() - 1);
                    }
                }
                if (idsAtFloor.isEmpty())
                    continue;

                // --- Choisir quelques personnes à déplacer (ex: 3)
                List<Integer> ids = new ArrayList<>();
                List<Integer> dests = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    int pid = rand.nextInt(personnes.size());
                    int dest = rand.nextInt(etages.size());
                    if (dest == 0)
                        dest = 1; // éviter le RDC

                    while (dest == personnes.get(pid).etage())
                        dest = rand.nextInt(etages.size());

                    ids.add(personnes.get(pid).getId() - 1);
                    dests.add(dest);
                }

                // --- Choisir aléatoirement un ascenseur
                // int elevatorId = rand.nextInt(view.getElevators().size());
                Call call = new Call(floor, dests.get(0));
                int elevatorId = aga.respondToCall(call);
                aga.getLiftManagers().forEach(lm -> {
                    lm.move();
                });
                System.out.println("Habitude de la personne " + ids.get(0) + ": " + personnes.get(ids.get(0)).habitudes());
                System.out.println("Appel de l'ascenseur " + elevatorId + " pour les personnes " + ids + " de l'étage " + floor + " vers " + dests);
                final List<Integer> finalIds = ids;
                final List<Integer> finalDests = dests;
                final int finalElevatorId = elevatorId;

                Platform.runLater(() -> {
                    view.scheduleExternalAction(finalIds, finalDests, finalElevatorId);
                    view.processExternalAction();
                });
            }
        }).start();
    }

    public void printDemo() {
        System.out.println("Persons:");
        for (Personnes p : personnes) {
            System.out.println(" - Person " + p.getId() + " at floor " + p.etage());
        }
    }

}