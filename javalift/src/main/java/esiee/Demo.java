package esiee;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import esiee.front.TowerView;

public class Demo {
    public ArrayList<Etage> etages;
    public ArrayList<Personnes> personnes;


    public Demo() {
        // --- Création des étages de la tour ---
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
        //etages.add(new Etage(10, 25));
        this.etages = new ArrayList<>(etages);













        // // --- Création des habitudes de plusieurs profils d'habitants ---

        // // Personne 1 : part au travail le matin et rentre le soir
        // Habitude h1_matin = new Habitude(Event.SORTIE_BATIMENT,
        //         LocalTime.of(8, 0),
        //         LocalTime.of(8, 30),
        //         0);

        // Habitude h1_soir = new Habitude(Event.ENTREE_BATIMENT,
        //         LocalTime.of(17, 30),
        //         LocalTime.of(18, 30),
        //         10);

        // Personnes p1 = new PersonneSimple(1, 10, List.of(h1_matin, h1_soir));

        // // Personne 2 : reçoit un invité
        // Habitude h2_invite_arrivee = new Habitude(Event.INVITATION_ARRIVEE,
        //         LocalTime.of(15, 0),
        //         LocalTime.of(15, 15),
        //         5);
        // Habitude h2_invite_depart = new Habitude(Event.INVITATION_DEPART,
        //         LocalTime.of(17, 0),
        //         LocalTime.of(17, 30),
        //         0);
        // Personnes p2 = new PersonneSimple(2, 5, List.of(h2_invite_arrivee, h2_invite_depart));

        // // Personne 3 : visite un ami
        // Habitude h3_visite = new Habitude(Event.DEPLACEMENT_INTERNE,
        //         LocalTime.of(19, 0),
        //         LocalTime.of(19, 15),
        //         8);
        // Personnes p3 = new PersonneSimple(3, 3, List.of(h3_visite));

        // // --- Placement des personnes dans leurs étages ---
        // etages.get(10).ajouterPersonne(p1); // P1 à l'étage 10
        // etages.get(5).ajouterPersonne(p2);  // P2 à l'étage 5
        // etages.get(3).ajouterPersonne(p3);  // P3 à l'étage 3

            


        // // --- Rassemblement de toutes les personnes ---
        // this.personnes = new ArrayList<>(List.of(p1, p2, p3));

        // --- Création automatique de 5 personnes par étage ---
        this.personnes = new ArrayList<>();
        int personIdCounter = 1;
        for (Etage etage : etages) {
            if (etage.niveau() == 0) continue; // Pas de personnes au RDC
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

        System.out.println("\n===== Liste des personnes et de leurs habitudes =====");
        for (Personnes p : personnes) {
            System.out.println(p.getId() + " (étage " + p.etage() + ")");
            for (Habitude h : p.habitudes()) {
                System.out.println("  → " + h);
            }
        }

        // System.out.println("\n===== Simulation d'événements avec déplacements =====");

        // // --- Simulation 8h15 : P1 descend au RDC ---
        // LocalTime t1 = LocalTime.of(8, 15);
        // System.out.println("\n⏰ " + t1 + " - " + p1.getId() + " quitte son appartement (étage " +
        //         p1.etage() + ")");

        // var call1 = p1.creerCallDepuisHabitude(h1_matin);
        // if (call1 != null) {
        //     System.out.println("   📞 Call créé: " + call1.fromFloor() + " → " + call1.toFloor());

        //     // Retirer P1 de l'étage 10
        //     etages.get(10).retirerPersonne(p1);
        //     System.out.println("   ↓ " + p1.getId() + " quitte l'étage 10");

        //     // Simuler l'arrivée de l'ascenseur
        //     p1.setEtage(0);

        //     // Ajouter P1 au RDC
        //     etages.get(0).ajouterPersonne(p1);
        //     System.out.println("   ✓ " + p1.getId() + " est maintenant à l'étage " + p1.etage());
        // }

        // // --- Simulation 17h45 : P1 remonte chez lui ---
        // LocalTime t2 = LocalTime.of(17, 45);
        // System.out.println("\n⏰ " + t2 + " - " + p1.getId() + " rentre chez lui (depuis étage " +
        //         p1.etage() + ")");

        // var call2 = p1.creerCallDepuisHabitude(h1_soir);
        // if (call2 != null) {
        //     System.out.println("   📞 Call créé: " + call2.fromFloor() + " → " + call2.toFloor());

        //     // Retirer P1 du RDC
        //     etages.get(0).retirerPersonne(p1);
        //     System.out.println("   ↑ " + p1.getId() + " quitte l'étage 0");

        //     // Simuler l'arrivée de l'ascenseur
        //     p1.setEtage(10);

        //     // Ajouter P1 à l'étage 10
        //     etages.get(10).ajouterPersonne(p1);
        //     System.out.println("   ✓ " + p1.getId() + " est maintenant à l'étage " + p1.etage());
        // }

        // // --- Simulation 19h10 : P3 visite l'étage 8 ---
        // LocalTime t3 = LocalTime.of(19, 10);
        // System.out.println("\n⏰ " + t3 + " - " + p3.getId() + " se déplace pour une visite");

        // var call3 = p3.creerCallDepuisHabitude(h3_visite);
        // if (call3 != null) {
        //     System.out.println("   📞 Call créé: " + call3.fromFloor() + " → " + call3.toFloor());

        //     // Retirer P3 de l'étage 3
        //     etages.get(3).retirerPersonne(p3);
        //     System.out.println("   ↑ " + p3.getId() + " quitte l'étage 3");

        //     // Simuler l'arrivée de l'ascenseur
        //     p3.setEtage(8);

        //     // Ajouter P3 à l'étage 8
        //     etages.get(8).ajouterPersonne(p3);
        //     System.out.println("   ✓ " + p3.getId() + " est maintenant à l'étage " + p3.etage());
        // }

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
        while (true) {
            try { Thread.sleep(1500); } catch (Exception e) {}

            // --- Choisir un étage aléatoire
            int floor = rand.nextInt(etages.size());

            // --- Récupérer toutes les personnes présentes à cet étage
            List<Integer> idsAtFloor = new ArrayList<>();
            for (Personnes p : personnes) {
                if (p.etage() == floor) {
                    idsAtFloor.add(p.getId() - 1);
                }
            }
            if (idsAtFloor.isEmpty()) continue;

            // --- Choisir quelques personnes à déplacer (ex: 3)
            List<Integer> ids = new ArrayList<>();
            List<Integer> dests = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                int pid = rand.nextInt(personnes.size());
                int dest = rand.nextInt(etages.size());
                if (dest == 0) dest = 1; // éviter le RDC
                while (dest == personnes.get(pid).etage())
                    dest = rand.nextInt(etages.size());
                ids.add(personnes.get(pid).getId() - 1);
                dests.add(dest);
            }

            // --- Choisir aléatoirement un ascenseur
            int elevatorId = rand.nextInt(view.getElevators().size()); // index du ascenseur

            final List<Integer> finalIds = ids;
            final List<Integer> finalDests = dests;
            final int finalElevatorId = elevatorId;
            javafx.application.Platform.runLater(() -> {
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