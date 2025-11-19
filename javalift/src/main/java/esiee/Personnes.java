package esiee;

import java.util.List;

import esiee.lift.request.Call;

public interface Personnes {

    String id();
    
    int etage();
    
    List<Habitude> habitudes();
    
    void setEtage(int nouvelEtage);

    /**
     * Convertit une habitude en Call pour l'ascenseur
     */
    default Call creerCallDepuisHabitude(Habitude habitude) {
        int fromFloor = this.etage();
        int toFloor = habitude.destination();
        
        if (fromFloor == toFloor) {
            return null; // Pas besoin de déplacement
        }
        
        return new Call(fromFloor, toFloor);
    }

    /**
     * Vérifie si la personne doit se déplacer selon son habitude
     */
    default boolean doitSeDeplacer(Habitude habitude) {
        return this.etage() != habitude.destination();
    }
}