package esiee.ihm;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import esiee.Event;
import esiee.Habitude;

/**
 * Configuration des définitions d'habitudes chargée depuis HabitudeConfig.json
 */
public record HabitudeDefinitionConfig(
    List<HabitudeDefinition> habitudes
) {
    /**
     * Définition d'une habitude
     */
    public record HabitudeDefinition(
        int id,
        String type,
        String description,
        String debut,
        String fin,
        int destination
    ) {
        private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
        
        /**
         * Convertit cette définition en objet Habitude
         * @param etageActuel l'étage actuel de la personne (pour les destinations relatives)
         * @return l'objet Habitude
         */
        public Habitude toHabitude(int etageActuel) {
            Event eventType = Event.valueOf(type);
            LocalTime heureDebut = LocalTime.parse(debut, TIME_FORMATTER);
            LocalTime heureFin = LocalTime.parse(fin, TIME_FORMATTER);
            
            // Calculer la destination réelle
            int destinationReelle = switch (destination) {
                case -1 -> etageActuel; // Retour à l'étage initial
                case -2 -> Math.max(0, etageActuel - 1); // Un étage en dessous
                case -3 -> etageActuel + 1; // Un étage au-dessus
                default -> destination; // Destination absolue
            };
            
            return new Habitude(eventType, heureDebut, heureFin, destinationReelle);
        }
    }
}