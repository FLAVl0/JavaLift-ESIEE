package esiee;

import java.util.List;


public interface Personnes {

    /** Identifiant unique de la personne. */
    String id();

    /** Étape actuel de la personne. */
    int etage();

    /** Liste des habitudes de la personne. */
    List<Habitude> habitudes();

    /** Met à jour l’étage actuel. */
    void setEtage(int nouvelEtage);
}


