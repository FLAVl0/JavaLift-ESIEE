package esiee;

import java.util.ArrayList;
import java.util.List;

public class Etage {
    private final int niveau;
    private final int nbHabitants;
    private final List<Personnes> personnes;

    /**
     * Constructeur complet
     */
    public Etage(int niveau, int nbHabitants, List<Personnes> personnes) {
        if (niveau < 0) {
            throw new IllegalArgumentException("Le niveau d'un étage ne peut être négatif.");
        }
        if (nbHabitants < 0) {
            throw new IllegalArgumentException("Le nombre d'habitants ne peut être négatif.");
        }
        this.niveau = niveau;
        this.nbHabitants = nbHabitants;
        this.personnes = personnes == null ? new ArrayList<>() : new ArrayList<>(personnes);
    }

    /**
     * Constructeur de convenance
     */
    public Etage(int niveau, int nbHabitants) {
        this(niveau, nbHabitants, new ArrayList<>());
    }

    // Getters
    public int niveau() {
        return niveau;
    }

    public int nbHabitants() {
        return nbHabitants;
    }

    public List<Personnes> personnes() {
        return new ArrayList<>(personnes);
    }

    public int getNombrePersonnesPresentes() {
        return personnes.size();
    }

    // Méthodes de gestion
    public boolean ajouterPersonne(Personnes personne) {
        if (personne != null && !personnes.contains(personne)) {
            return personnes.add(personne);
        }
        return false;
    }

    public boolean retirerPersonne(Personnes personne) {
        return personnes.remove(personne);
    }

    public boolean contientPersonne(Personnes personne) {
        return personnes.contains(personne);
    }

    public boolean estPlein() {
        return personnes.size() >= nbHabitants;
    }

    public boolean estRezDeChaussee() {
        return niveau == 0;
    }

    public void viderEtage() {
        personnes.clear();
    }

    @Override
    public String toString() {
        return "Etage{niveau=" + niveau + ", nbHabitants=" + nbHabitants + 
               ", personnesPresentes=" + personnes.size() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Etage etage = (Etage) o;
        return niveau == etage.niveau;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(niveau);
    }
}