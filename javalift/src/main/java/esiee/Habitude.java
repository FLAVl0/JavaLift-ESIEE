package esiee;

import java.time.LocalTime;

public record Habitude(
        Event type,         // Type d'événement (ex: SORTIE_BATIMENT)
        LocalTime debut,    // Heure minimale
        LocalTime fin,      // Heure maximale
        int destination     // Étape de destination (ex: 0 = rez-de-chaussée)
) {
    public Habitude {
        if (debut == null || fin == null || type == null) {
            throw new IllegalArgumentException("Les champs ne peuvent pas être nuls");
        }
        if (destination < 0) {
            throw new IllegalArgumentException("La destination ne peut pas être négative");
        }
    }

    @Override
    public String toString() {
        return "Habitude{" +
                "type=" + type +
                ", debut=" + debut +
                ", fin=" + fin +
                ", destination=" + destination +
                '}';
    }
}
