package esiee;

import java.time.LocalTime;
import java.util.List;


public class Demo {
    public static void main(String[] args) {
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

        Personnes p1 = new PersonneSimple("P1",10, List.of(h1_matin, h1_soir));
        
        Habitude h2_invite_arrivee = new Habitude(Event.INVITATION_ARRIVEE, LocalTime.of(15, 0), LocalTime.of(15, 15), 5);
        Habitude h2_invite_depart = new Habitude(Event.INVITATION_DEPART, LocalTime.of(17, 0), LocalTime.of(17, 30), 0);
        Personnes p2 = new PersonneSimple("P2", 5, List.of(h2_invite_arrivee, h2_invite_depart));

        Habitude h3_visite = new Habitude(Event.DEPLACEMENT_INTERNE, LocalTime.of(19, 0), LocalTime.of(19, 15), 8);
        Personnes p3 = new PersonneSimple("P3", 3, List.of(h3_visite));

        // --- Rassemblement de toutes les personnes ---
        List<Personnes> personnes = List.of(p1, p2, p3);

        // --- Affichage global ---
        System.out.println("===== Liste des personnes et de leurs habitudes =====");
        for (Personnes p : personnes) {
            System.out.println(p.id() + " (étage " + p.etage() + ")");
            for (Habitude h : p.habitudes()) {
                System.out.println("  → " + h);
            }
        }

        System.out.println("\n===== Simulation d'événements =====");

        // --- Simulation de base : on affiche selon le type d’événement ---
        for (Personnes p : personnes) {
            for (Habitude h : p.habitudes()) {
                switch (h.type()) {
                    case SORTIE_BATIMENT -> System.out.println(p.id() + " quitte la tour entre " +
                            h.debut() + " et " + h.fin() + " (vers étage " + h.destination() + ")");
                    case ENTREE_BATIMENT -> System.out.println(p.id() + " rentre chez lui entre " +
                            h.debut() + " et " + h.fin() + " (vers étage " + h.destination() + ")");
                    case INVITATION_ARRIVEE -> System.out.println(p.id() +
                            " reçoit un ami qui arrive à l’étage " + p.etage() + " vers " + h.debut());
                    case INVITATION_DEPART -> System.out.println("L’ami de " + p.id() +
                            " repart vers le rez-de-chaussée entre " + h.debut() + " et " + h.fin());
                    case DEPLACEMENT_INTERNE -> System.out.println(p.id() +
                            " se déplace dans la tour entre " + h.debut() + " et " + h.fin() +
                            " pour aller à l’étage " + h.destination());
                    default -> System.out.println(p.id() + " a un événement inconnu : " + h.type());
                }
            }
        }
    }
}
