package esiee;


public record Etage(int niveau, int nbHabitants) {
    public Etage {
        if (niveau < 0) {
            throw new IllegalArgumentException("Le niveau d'un étage ne peut être négatif.");
        }
        if (nbHabitants < 0) {
            throw new IllegalArgumentException("Le nombre d'habitants ne peut être négatif.");
        }
    }

    /** Indique si l'étage est le rez-de-chaussée (hall). */
    public boolean estRezDeChaussee() {
        return niveau == 0;
    }

    @Override
    public String toString() {
        return "Etage{niveau=" + niveau + ", nbHabitants=" + nbHabitants + "}";
    }
}
