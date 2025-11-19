package esiee;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
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
        etages.add(new Etage(10, 25));

        // --- Création des habitudes de plusieurs profils d'habitants ---

        // Personne 1 : part au travail le matin et rentre le soir
        Habitude h1_matin = new Habitude(Event.SORTIE_BATIMENT,
                LocalTime.of(8, 0),
                LocalTime.of(8, 30),
                0);

        Habitude h1_soir = new Habitude(Event.ENTREE_BATIMENT,
                LocalTime.of(17, 30),
                LocalTime.of(18, 30),
                10);

        Personnes p1 = new PersonneSimple("P1", 10, List.of(h1_matin, h1_soir));

        // Personne 2 : reçoit un invité
        Habitude h2_invite_arrivee = new Habitude(Event.INVITATION_ARRIVEE,
                LocalTime.of(15, 0),
                LocalTime.of(15, 15),
                5);
        Habitude h2_invite_depart = new Habitude(Event.INVITATION_DEPART,
                LocalTime.of(17, 0),
                LocalTime.of(17, 30),
                0);
        Personnes p2 = new PersonneSimple("P2", 5, List.of(h2_invite_arrivee, h2_invite_depart));

        // Personne 3 : visite un ami
        Habitude h3_visite = new Habitude(Event.DEPLACEMENT_INTERNE,
                LocalTime.of(19, 0),
                LocalTime.of(19, 15),
                8);
        Personnes p3 = new PersonneSimple("P3", 3, List.of(h3_visite));

        // --- Placement des personnes dans leurs étages ---
        etages.get(10).ajouterPersonne(p1); // P1 à l'étage 10
        etages.get(5).ajouterPersonne(p2);  // P2 à l'étage 5
        etages.get(3).ajouterPersonne(p3);  // P3 à l'étage 3

        // --- Rassemblement de toutes les personnes ---
        List<Personnes> personnes = List.of(p1, p2, p3);

        // --- Affichage global ---
        System.out.println("===== État de la tour =====");
        for (Etage etage : etages) {
            if (etage.getNombrePersonnesPresentes() > 0) {
                System.out.println(etage + " - Personnes: " + etage.personnes());
            }
        }

        System.out.println("\n===== Liste des personnes et de leurs habitudes =====");
        for (Personnes p : personnes) {
            System.out.println(p.id() + " (étage " + p.etage() + ")");
            for (Habitude h : p.habitudes()) {
                System.out.println("  → " + h);
            }
        }

        System.out.println("\n===== Simulation d'événements avec déplacements =====");

        // --- Simulation 8h15 : P1 descend au RDC ---
        LocalTime t1 = LocalTime.of(8, 15);
        System.out.println("\n⏰ " + t1 + " - " + p1.id() + " quitte son appartement (étage " +
                p1.etage() + ")");

        var call1 = p1.creerCallDepuisHabitude(h1_matin);
        if (call1 != null) {
            System.out.println("   📞 Call créé: " + call1.fromFloor() + " → " + call1.toFloor());

            // Retirer P1 de l'étage 10
            etages.get(10).retirerPersonne(p1);
            System.out.println("   ↓ " + p1.id() + " quitte l'étage 10");

            // Simuler l'arrivée de l'ascenseur
            p1.setEtage(0);

            // Ajouter P1 au RDC
            etages.get(0).ajouterPersonne(p1);
            System.out.println("   ✓ " + p1.id() + " est maintenant à l'étage " + p1.etage());
        }

        // --- Simulation 17h45 : P1 remonte chez lui ---
        LocalTime t2 = LocalTime.of(17, 45);
        System.out.println("\n⏰ " + t2 + " - " + p1.id() + " rentre chez lui (depuis étage " +
                p1.etage() + ")");

        var call2 = p1.creerCallDepuisHabitude(h1_soir);
        if (call2 != null) {
            System.out.println("   📞 Call créé: " + call2.fromFloor() + " → " + call2.toFloor());

            // Retirer P1 du RDC
            etages.get(0).retirerPersonne(p1);
            System.out.println("   ↑ " + p1.id() + " quitte l'étage 0");

            // Simuler l'arrivée de l'ascenseur
            p1.setEtage(10);

            // Ajouter P1 à l'étage 10
            etages.get(10).ajouterPersonne(p1);
            System.out.println("   ✓ " + p1.id() + " est maintenant à l'étage " + p1.etage());
        }

        // --- Simulation 19h10 : P3 visite l'étage 8 ---
        LocalTime t3 = LocalTime.of(19, 10);
        System.out.println("\n⏰ " + t3 + " - " + p3.id() + " se déplace pour une visite");

        var call3 = p3.creerCallDepuisHabitude(h3_visite);
        if (call3 != null) {
            System.out.println("   📞 Call créé: " + call3.fromFloor() + " → " + call3.toFloor());

            // Retirer P3 de l'étage 3
            etages.get(3).retirerPersonne(p3);
            System.out.println("   ↑ " + p3.id() + " quitte l'étage 3");

            // Simuler l'arrivée de l'ascenseur
            p3.setEtage(8);

            // Ajouter P3 à l'étage 8
            etages.get(8).ajouterPersonne(p3);
            System.out.println("   ✓ " + p3.id() + " est maintenant à l'étage " + p3.etage());
        }

        // --- Affichage final de l'état de la tour ---
        System.out.println("\n===== État final de la tour =====");
        for (Etage etage : etages) {
            if (etage.getNombrePersonnesPresentes() > 0) {
                System.out.println(etage);
                for (Personnes p : etage.personnes()) {
                    System.out.println("  - " + p.id());
                }
            }
        }
    }
}