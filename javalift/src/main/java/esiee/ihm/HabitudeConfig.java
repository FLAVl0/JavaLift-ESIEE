package esiee.ihm;

import java.util.List;

/**
 * Configuration des personnes et de leurs habitudes chargée depuis poi.json
 */
public record HabitudeConfig(
    String journee,
    int nombrePersonnes,
    List<PersonneData> personnes
) {
    /**
     * Données d'une personne depuis le JSON
     */
    public record PersonneData(
        String id,
        int etageInitial,
        List<Integer> habitudes
    ) {}
}