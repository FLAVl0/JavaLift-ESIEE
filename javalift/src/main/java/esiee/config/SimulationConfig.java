package esiee.config;

public record SimulationConfig(
    int dureeSimulation,
    int heureDebut,
    int heureFin,
    double vitesseSimulation,
    boolean loggingActif,
    boolean affichageGraphique,
    int intervalAffichage,
    long seed
) {
    public SimulationConfig() {
        this(43200, 21600, 79200, 1.0, true, false, 60, 12345L);
    }
    public int getDuration() {
        return dureeSimulation;
    }
    public int getStartTime() {
        return heureDebut;
    }
    public int getEndTime() {
        return heureFin;
    }
    public double getSimulationSpeed() {
        return vitesseSimulation;
    }
    
}