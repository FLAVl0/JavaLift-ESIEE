package esiee;

import java.util.List;

public class PersonneSimple implements Personnes {
    private final String id;
    private int etage;
    private final List<Habitude> habitudes;

    public PersonneSimple(String id, int etage, List<Habitude> habitudes) {
        this.id = id;
        this.etage = etage;
        this.habitudes = List.copyOf(habitudes);
    }

    @Override
    public String id() {
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
    }

    @Override
    public String toString() {
        return "PersonneSimple{" +
                "id='" + id + '\'' +
                ", etage=" + etage +
                ", habitudes=" + habitudes +
                '}';
    }
}


