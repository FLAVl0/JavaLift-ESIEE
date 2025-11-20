package esiee;

import java.util.List;

public class PersonneSimple implements Personnes {
    private final int id;
    private int etage;
    private final List<Habitude> habitudes;
    private boolean enDeplacement;
    private Integer etageDestination;

    public PersonneSimple(int id, int etage, List<Habitude> habitudes) {
        this.id = id;
        this.etage = etage;
        this.habitudes = List.copyOf(habitudes);
        this.enDeplacement = false;
        this.etageDestination = null;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int etage() {
        return etage;
    }

    @Override
    public List<Habitude> habitudes() {
        return habitudes;
    }

    @Override
    public void setEtage(int nouvelEtage) {
        this.etage = nouvelEtage;
        if (etageDestination != null && etageDestination == nouvelEtage) {
            this.enDeplacement = false;
            this.etageDestination = null;
        }
    }

    public boolean estEnDeplacement() {
        return enDeplacement;
    }

    public void deplacerVers(int destination) {
        this.enDeplacement = true;
        this.etageDestination = destination;
    }

    public Integer getDestination() {
        return etageDestination;
    }

    @Override
    public String toString() {
        String statut = enDeplacement ? " (→ étage " + etageDestination + ")" : "";
        return "PersonneSimple{id='" + id + "', etage=" + etage + statut + 
               ", habitudes=" + habitudes.size() + '}';
    }
}