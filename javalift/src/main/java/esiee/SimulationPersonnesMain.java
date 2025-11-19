package esiee;

/**
 * Point d'entrée pour la simulation Personnes/Étages uniquement
 */
public class SimulationPersonnesMain {
    
    public static void main(String[] args) {
        try {
            System.out.println("SIMULATION PERSONNES & ÉTAGES");
            
            // Créer la simulation depuis les fichiers de configuration
            SimulationPersonnes simulation = SimulationPersonnes.fromConfiguration();
            
            // Démarrer la simulation
            simulation.demarrer();
            
            System.out.println("✅ Simulation terminée avec succès !");
            
        } catch (Exception e) {
            System.err.println("\n❌ Erreur lors de la simulation: " + e.getMessage());
        }
    }
}