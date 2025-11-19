package esiee.config;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import esiee.Etage;
import esiee.Habitude;
import esiee.PersonneSimple;
import esiee.Personnes;
import esiee.lift.request.Call;

public class Tower {
    
    private final List<Etage> etages;
    private final List<Personnes> personnes;
    private final List<Call> appelEnAttente;
    private final Map<Integer, HabitudeDefinitionConfig.HabitudeDefinition> mappingHabitudes;
    
    public Tower(List<Etage> etages) {
        this.etages = new ArrayList<>(etages);
        this.personnes = new ArrayList<>();
        this.appelEnAttente = new ArrayList<>();
        this.mappingHabitudes = new HashMap<>();
    }
    
    /**
     * Construit une Tower depuis la configuration
     */
    public static Tower fromConfiguration(ConfigurationRecord config, 
                                          HabitudeDefinitionConfig habitudeDefConfig) {
        // Créer les étages
        List<Etage> etages = new ArrayList<>();
        for (TowerConfig.EtageData etageData : config.towerConfig().etages()) {
            etages.add(new Etage(etageData.niveau(), etageData.capacite()));
        }
        
        Tower tower = new Tower(etages);
        
        // Charger les définitions d'habitudes
        tower.loadHabitudeDefinitions(habitudeDefConfig);
        
        // Créer les personnes
        tower.loadPersonnesFromConfig(config.habitudeConfig());
        
        return tower;
    }
    
    private void loadHabitudeDefinitions(HabitudeDefinitionConfig config) {
        for (HabitudeDefinitionConfig.HabitudeDefinition def : config.habitudes()) {
            mappingHabitudes.put(def.id(), def);
        }
        System.out.println("✓ " + mappingHabitudes.size() + " habitude(s) chargée(s)");
    }
    
    private void loadPersonnesFromConfig(HabitudeConfig config) {
        for (HabitudeConfig.PersonneData personneData : config.personnes()) {
            List<Habitude> habitudes = new ArrayList<>();
            
            for (Integer habitudeId : personneData.habitudes()) {
                HabitudeDefinitionConfig.HabitudeDefinition habitludeDef = mappingHabitudes.get(habitudeId);
                
                if (habitludeDef != null) {
                    Habitude habitude = habitludeDef.toHabitude(personneData.etageInitial());
                    habitudes.add(habitude);
                } else {
                    System.err.println("⚠️ Habitude ID " + habitudeId + 
                        " introuvable pour " + personneData.id());
                }
            }
            
            Personnes personne = new PersonneSimple(
                personneData.id(),
                personneData.etageInitial(),
                habitudes
            );
            
            this.ajouterPersonne(personne);
            
            if (personneData.etageInitial() < etages.size()) {
                etages.get(personneData.etageInitial()).ajouterPersonne(personne);
            }
            
            System.out.println("✓ Personne chargée: " + personne.id() + 
                " à l'étage " + personne.etage() + 
                " avec " + habitudes.size() + " habitude(s)");
        }
    }
    
    public void ajouterPersonne(Personnes personne) {
        this.personnes.add(personne);
    }
    
    public List<Personnes> getPersonnes() {
        return new ArrayList<>(personnes);
    }
    
    public List<Call> genererAppelsPourHeure(LocalTime heureActuelle) {
        List<Call> appelsGeneres = new ArrayList<>();
        
        for (Personnes personne : personnes) {
            for (Habitude habitude : personne.habitudes()) {
                if (estDansPlageHoraire(heureActuelle, habitude.debut(), habitude.fin())) {
                    Call call = personne.creerCallDepuisHabitude(habitude);
                    if (call != null) {
                        appelsGeneres.add(call);
                    }
                }
            }
        }
        
        return appelsGeneres;
    }
    
    private boolean estDansPlageHoraire(LocalTime heure, LocalTime debut, LocalTime fin) {
        if (debut.isBefore(fin)) {
            return !heure.isBefore(debut) && !heure.isAfter(fin);
        } else {
            return !heure.isBefore(debut) || !heure.isAfter(fin);
        }
    }
    
    public void deplacerPersonne(Personnes personne, int etageDestination) {
        int etageActuel = personne.etage();
        Etage etageDepart = getEtage(etageActuel);
        if (etageDepart != null) {
            etageDepart.retirerPersonne(personne);
        }
        
        personne.setEtage(etageDestination);
        
        Etage etageArrivee = getEtage(etageDestination);
        if (etageArrivee != null) {
            etageArrivee.ajouterPersonne(personne);
        }
    }
    
    public List<Etage> getEtages() {
        return new ArrayList<>(etages);
    }
    
    public Etage getEtage(int niveau) {
        for (Etage etage : etages) {
            if (etage.niveau() == niveau) {
                return etage;
            }
        }
        return null;
    }
    
    public List<Call> getAppelsEnAttente() {
        return new ArrayList<>(appelEnAttente);
    }
    
    public int getNombrePersonnes() {
        return personnes.size();
    }
    
    public int getNombreEtages() {
        return etages.size();
    }
    
    public void afficherEtat() {
        System.out.println("\n🏢 === ÉTAT DE LA TOUR ===");
        System.out.println("Nombre d'étages: " + getNombreEtages());
        System.out.println("Nombre de personnes: " + getNombrePersonnes());
        System.out.println("\nDistribution par étage:");
        for (Etage etage : etages) {
            if (etage.getNombrePersonnesPresentes() > 0) {
                System.out.print("  Étage " + etage.niveau() + ": " + 
                    etage.getNombrePersonnesPresentes() + " personne(s)");
                
                List<String> ids = new ArrayList<>();
                for (Personnes p : etage.personnes()) {
                    ids.add(p.id());
                }
                System.out.println(" [" + String.join(", ", ids) + "]");
            }
        }
        System.out.println();
    }
}