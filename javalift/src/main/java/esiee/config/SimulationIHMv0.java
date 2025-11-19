package esiee.config;
/* 
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import esiee.Etage;
import esiee.Event;
import esiee.Habitude;
import esiee.PersonneSimple;
import esiee.Personnes;

*/
public class SimulationIHMv0 {
    /* 
    
    public static void main(String[] args) {
        System.out.println("=== Simulation JavaLift ===\n");
        
        // Création des étages
        List<Etage> etages = creerEtages();
        System.out.println("Étages créés:");
        etages.forEach(e -> System.out.println("  " + e));
        
        // Création des personnes avec leurs habitudes
        List<Personnes> personnes = creerPersonnes();
        System.out.println("\nPersonnes créées:");
        personnes.forEach(p -> System.out.println("  " + p));
        
        // Simulation des événements
        simulerEvenements(personnes);
        
        System.out.println("\n=== Fin de simulation ===");
    }
    
    private static List<Etage> creerEtages() {
        List<Etage> etages = new ArrayList<>();
        etages.add(new Etage(0, 100));  // RDC - Hall
        etages.add(new Etage(1, 45));   // Étage 1
        etages.add(new Etage(2, 50));   // Étage 2
        etages.add(new Etage(3, 40));   // Étage 3
        return etages;
    }
    
    private static List<Personnes> creerPersonnes() {
        List<Personnes> personnes = new ArrayList<>();
        
        // Personne 1 : travailleur matin
        List<Habitude> habitudes1 = List.of(
            new Habitude(Event.ENTREE_BATIMENT, LocalTime.of(8, 0), LocalTime.of(8, 30), 2),
            new Habitude(Event.DEPLACEMENT_INTERNE, LocalTime.of(12, 0), LocalTime.of(12, 30), 0),
            new Habitude(Event.SORTIE_BATIMENT, LocalTime.of(17, 30), LocalTime.of(18, 0), 0)
        );
        personnes.add(new PersonneSimple("P001", 0, habitudes1));
        
        // Personne 2 : travailleur après-midi
        List<Habitude> habitudes2 = List.of(
            new Habitude(Event.ENTREE_BATIMENT, LocalTime.of(13, 0), LocalTime.of(13, 30), 3),
            new Habitude(Event.SORTIE_BATIMENT, LocalTime.of(22, 0), LocalTime.of(22, 30), 0)
        );
        personnes.add(new PersonneSimple("P002", 0, habitudes2));
        
        // Personne 3 : résident étage 1
        List<Habitude> habitudes3 = List.of(
            new Habitude(Event.DEPLACEMENT_INTERNE, LocalTime.of(7, 0), LocalTime.of(7, 30), 0),
            new Habitude(Event.DEPLACEMENT_INTERNE, LocalTime.of(18, 0), LocalTime.of(18, 30), 1)
        );
        personnes.add(new PersonneSimple("P003", 1, habitudes3));
        
        return personnes;
    }
    
    private static void simulerEvenements(List<Personnes> personnes) {
        System.out.println("\n--- Simulation des déplacements ---");
        
        for (Personnes personne : personnes) {
            System.out.println("\nPersonne: " + personne.id());
            System.out.println("  Étage actuel: " + personne.etage());
            
            for (Habitude habitude : personne.habitudes()) {
                System.out.println("  Habitude: " + habitude.type() + 
                                 " de " + habitude.debut() + 
                                 " à " + habitude.fin() + 
                                 " → Étage " + habitude.destination());
                
                // Simulation du déplacement
                personne.setEtage(habitude.destination());
                System.out.println("    → Étage actuel après déplacement: " + personne.etage());
            }
        }
    }
*/
}