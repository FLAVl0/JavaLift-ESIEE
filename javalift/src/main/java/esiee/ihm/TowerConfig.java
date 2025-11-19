package esiee.ihm;

import java.util.List;

public record TowerConfig(
    int nombreEtages,
    List<EtageData> etages
) {
    public record EtageData(
        int niveau,
        int capacite,
        String description  // ✅ AJOUTER ce champ
    ) {}
    
    public TowerConfig(int nombreEtages) {
        this(nombreEtages, generateDefaultEtages(nombreEtages));
    }
    
    private static List<EtageData> generateDefaultEtages(int nombreEtages) {
        return java.util.stream.IntStream.range(0, nombreEtages)
            .mapToObj(i -> new EtageData(
                i, 
                i == 0 ? 100 : 40,
                i == 0 ? "Hall" : "Appartements"  // ✅ AJOUTER ce champ
            ))
            .toList();
    }
}