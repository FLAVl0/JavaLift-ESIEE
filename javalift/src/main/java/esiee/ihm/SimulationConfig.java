package esiee.ihm;

public record SimulationConfig(
    int dureeSimulation,
    String heureDebut,
    String heureFin,
    double vitesseSimulation,
    boolean loggingActif,
    boolean affichageGraphique,
    int intervalAffichage,
    long seed
) {
    public SimulationConfig() {
        this(43200, "06:00:00", "22:00:00", 1.0, true, false, 60, 12345L);
    }
}