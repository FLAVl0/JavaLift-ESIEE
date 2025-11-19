package esiee;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import esiee.ihm.Configuration;
import esiee.ihm.ConfigurationRecord;
import esiee.ihm.HabitudeDefinitionConfig;
import esiee.ihm.RecordList;
import esiee.ihm.SimulationConfig;
import esiee.ihm.Tower;
import esiee.lift.request.Call;

/**
 * Simulation simplifiée focalisée sur les étages et personnes
 * (sans gestion des ascenseurs)
 */
public class SimulationPersonnes {
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    private final Tower tower;
    private final SimulationConfig config;
    
    private LocalTime tempsActuel;
    private int tick;
    private boolean enCours;
    
    // Statistiques
    private int totalAppelsGeneres;
    private int totalDeplacements;
    private List<DeplacementInfo> historique;
    
    /**
     * Informations sur un déplacement
     */
    private record DeplacementInfo(
        String personneId,
        LocalTime heure,
        int etageDepart,
        int etageArrivee,
        Event typeHabitude
    ) {}
    
    /**
     * Constructeur
     */
    public SimulationPersonnes(Tower tower, SimulationConfig config) {
        this.tower = tower;
        this.config = config;
        
        this.tempsActuel = LocalTime.parse(config.heureDebut(), TIME_FORMATTER);
        this.tick = 0;
        this.enCours = false;
        
        this.totalAppelsGeneres = 0;
        this.totalDeplacements = 0;
        this.historique = new ArrayList<>();
    }
    
    /**
     * Crée une simulation depuis les fichiers de configuration
     */
    public static SimulationPersonnes fromConfiguration() {
        System.out.println("📂 Chargement de la configuration...\n");
        
        Configuration configuration = new Configuration();
        ConfigurationRecord configRecord = configuration.formatAll(RecordList.SIMULATION);
        
        // Charger les définitions d'habitudes
        HabitudeDefinitionConfig habitudeDefConfig = 
            (HabitudeDefinitionConfig) configuration.parse(
                "src/main/resources/" + RecordList.HABITUDE_DEF.getFileName(),
                RecordList.HABITUDE_DEF.getConfigClass()
            );
        
        // Créer la tour
        Tower tower = Tower.fromConfiguration(configRecord, habitudeDefConfig);
        
        System.out.println("✓ Configuration chargée avec succès");
        System.out.println("  - Nombre d'étages: " + tower.getNombreEtages());
        System.out.println("  - Nombre de personnes: " + tower.getNombrePersonnes());
        System.out.println();
        
        return new SimulationPersonnes(tower, configRecord.simulationConfig());
    }
    
    /**
     * Démarre la simulation
     */
    public void demarrer() {
        enCours = true;
        
        System.out.println("🚀 === DÉMARRAGE DE LA SIMULATION (Personnes/Étages) ===");
        System.out.println("⏰ Heure de début: " + tempsActuel.format(TIME_FORMATTER));
        System.out.println("⏱️  Durée: jusqu'à " + config.heureFin());
        System.out.println("=" .repeat(60) + "\n");
        
        afficherEtatInitial();
        
        LocalTime heureFin = LocalTime.parse(config.heureFin(), TIME_FORMATTER);
        
        while (enCours && tempsActuel.isBefore(heureFin)) {
            executerTick();
            tick++;
            
            // Avancer le temps (1 minute par tick)
            tempsActuel = tempsActuel.plusMinutes(1);
            
            // Affichage périodique
            if (tick % config.intervalAffichage() == 0) {
                afficherStatistiquesIntermediaires();
            }
        }
        
        arreter();
    }
    
    /**
     * Affiche l'état initial de la tour
     */
    private void afficherEtatInitial() {
        tower.afficherEtat();
        
        System.out.println("📋 Liste des personnes et leurs habitudes:");
        for (Personnes personne : tower.getPersonnes()) {
            System.out.println("\n  👤 " + personne.id() + " (Étage " + personne.etage() + "):");
            for (Habitude habitude : personne.habitudes()) {
                System.out.println("    • " + habitude.type() + 
                    " (" + habitude.debut() + " - " + habitude.fin() + 
                    ") → Étage " + habitude.destination());
            }
        }
        System.out.println();
    }
    
    /**
     * Exécute un tick de simulation
     */
    private void executerTick() {
        // Générer les appels pour cette heure
        List<Call> nouveauxAppels = tower.genererAppelsPourHeure(tempsActuel);
        
        if (!nouveauxAppels.isEmpty()) {
            totalAppelsGeneres += nouveauxAppels.size();
            
            if (config.loggingActif()) {
                System.out.println("\n[" + tempsActuel.format(TIME_FORMATTER) + "]");
            }
            
            // Simuler les déplacements
            for (Call call : nouveauxAppels) {
                simulerDeplacement(call);
            }
        }
    }
    
    /**
     * Simule un déplacement de personne
     */
    private void simulerDeplacement(Call call) {
        // Trouver la personne qui effectue ce déplacement
        for (Personnes personne : tower.getPersonnes()) {
            if (personne.etage() == call.fromFloor()) {
                
                // Trouver l'habitude correspondante pour avoir le type
                Event typeHabitude = Event.DEPLACEMENT_INTERNE;
                for (Habitude habitude : personne.habitudes()) {
                    if (habitude.destination() == call.toFloor() && 
                        estDansPlageHoraire(tempsActuel, habitude.debut(), habitude.fin())) {
                        typeHabitude = habitude.type();
                        break;
                    }
                }
                
                // Retirer la personne de l'étage actuel
                Etage etageDepart = tower.getEtage(call.fromFloor());
                if (etageDepart != null) {
                    etageDepart.retirerPersonne(personne);
                }
                
                // Mettre à jour l'étage de la personne
                int ancienEtage = personne.etage();
                personne.setEtage(call.toFloor());
                
                // Ajouter la personne au nouvel étage
                Etage etageArrivee = tower.getEtage(call.toFloor());
                if (etageArrivee != null) {
                    etageArrivee.ajouterPersonne(personne);
                }
                
                // Enregistrer le déplacement
                DeplacementInfo deplacement = new DeplacementInfo(
                    personne.id(),
                    tempsActuel,
                    ancienEtage,
                    call.toFloor(),
                    typeHabitude
                );
                historique.add(deplacement);
                totalDeplacements++;
                
                if (config.loggingActif()) {
                    String emoji = switch (typeHabitude) {
                        case SORTIE_BATIMENT -> "🚶";
                        case ENTREE_BATIMENT -> "🏠";
                        case INVITATION_ARRIVEE -> "🤝";
                        case INVITATION_DEPART -> "👋";
                        case DEPLACEMENT_INTERNE -> "🚪";
                    };
                    
                    System.out.println("  " + emoji + " " + personne.id() + 
                        " se déplace: Étage " + ancienEtage + " → Étage " + call.toFloor() +
                        " (" + typeHabitude + ")");
                }
                
                break; // Une seule personne par appel
            }
        }
    }
    
    /**
     * Vérifie si une heure est dans une plage horaire
     */
    private boolean estDansPlageHoraire(LocalTime heure, LocalTime debut, LocalTime fin) {
        if (debut.isBefore(fin)) {
            return !heure.isBefore(debut) && !heure.isAfter(fin);
        } else {
            return !heure.isBefore(debut) || !heure.isAfter(fin);
        }
    }
    
    /**
     * Affiche les statistiques intermédiaires
     */
    private void afficherStatistiquesIntermediaires() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 STATISTIQUES - " + tempsActuel.format(TIME_FORMATTER) + 
            " (Tick: " + tick + ")");
        System.out.println("=".repeat(60));
        
        System.out.println("\n📞 Appels et déplacements:");
        System.out.println("  • Total appels générés: " + totalAppelsGeneres);
        System.out.println("  • Total déplacements effectués: " + totalDeplacements);
        
        // Distribution actuelle des personnes
        System.out.println("\n🏢 Distribution actuelle par étage:");
        for (Etage etage : tower.getEtages()) {
            if (etage.getNombrePersonnesPresentes() > 0) {
                System.out.print("  • Étage " + etage.niveau() + ": " + 
                    etage.getNombrePersonnesPresentes() + " personne(s)");
                
                // Afficher les IDs des personnes
                List<String> ids = new ArrayList<>();
                for (Personnes p : etage.personnes()) {
                    ids.add(p.id());
                }
                System.out.println(" [" + String.join(", ", ids) + "]");
            }
        }
        
        // Statistiques par type d'événement
        System.out.println("\n📈 Déplacements par type:");
        var compteurTypes = new java.util.HashMap<Event, Integer>();
        for (DeplacementInfo dep : historique) {
            compteurTypes.merge(dep.typeHabitude(), 1, Integer::sum);
        }
        
        for (var entry : compteurTypes.entrySet()) {
            System.out.println("  • " + entry.getKey() + ": " + entry.getValue());
        }
        
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * Arrête la simulation
     */
    public void arreter() {
        enCours = false;
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🏁 === FIN DE LA SIMULATION ===");
        System.out.println("=".repeat(60));
        System.out.println("⏰ Heure de fin: " + tempsActuel.format(TIME_FORMATTER));
        System.out.println("⏱️  Durée totale: " + tick + " minutes simulées");
        
        afficherStatistiquesFinales();
        
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * Affiche les statistiques finales détaillées
     */
    private void afficherStatistiquesFinales() {
        System.out.println("\n📊 STATISTIQUES FINALES:");
        System.out.println("\n📞 Résumé des activités:");
        System.out.println("  • Total appels générés: " + totalAppelsGeneres);
        System.out.println("  • Total déplacements effectués: " + totalDeplacements);
        System.out.println("  • Moyenne: " + (tick > 0 ? String.format("%.2f", totalDeplacements / (double)tick) : "0") + " déplacements/minute");
        
        // Distribution finale
        System.out.println("\n🏢 Distribution FINALE par étage:");
        for (Etage etage : tower.getEtages()) {
            if (etage.getNombrePersonnesPresentes() > 0) {
                System.out.print("  • Étage " + etage.niveau() + ": " + 
                    etage.getNombrePersonnesPresentes() + " personne(s)");
                
                List<String> ids = new ArrayList<>();
                for (Personnes p : etage.personnes()) {
                    ids.add(p.id());
                }
                System.out.println(" [" + String.join(", ", ids) + "]");
            }
        }
        
        // Analyse par type d'événement
        System.out.println("\n📈 Analyse par type d'événement:");
        var compteurTypes = new java.util.HashMap<Event, Integer>();
        for (DeplacementInfo dep : historique) {
            compteurTypes.merge(dep.typeHabitude(), 1, Integer::sum);
        }
        
        for (var entry : compteurTypes.entrySet()) {
            double pourcentage = (entry.getValue() * 100.0) / totalDeplacements;
            System.out.printf("  • %-25s: %3d (%.1f%%)\n", 
                entry.getKey(), entry.getValue(), pourcentage);
        }
        
        // Analyse par personne
        System.out.println("\n👥 Activité par personne:");
        var compteurPersonnes = new java.util.HashMap<String, Integer>();
        for (DeplacementInfo dep : historique) {
            compteurPersonnes.merge(dep.personneId(), 1, Integer::sum);
        }
        
        for (Personnes p : tower.getPersonnes()) {
            int nbDeplacements = compteurPersonnes.getOrDefault(p.id(), 0);
            System.out.println("  • " + p.id() + " (Étage final: " + p.etage() + "): " + 
                nbDeplacements + " déplacement(s)");
        }
        
        // Périodes les plus actives
        System.out.println("\n⏰ Analyse temporelle:");
        analyserPeriodesActives();
        
        // Historique détaillé des 10 premiers déplacements
        if (!historique.isEmpty()) {
            System.out.println("\n📜 Historique (10 premiers déplacements):");
            int limit = Math.min(10, historique.size());
            for (int i = 0; i < limit; i++) {
                DeplacementInfo dep = historique.get(i);
                System.out.println("  " + (i+1) + ". [" + dep.heure().format(TIME_FORMATTER) + "] " +
                    dep.personneId() + ": Étage " + dep.etageDepart() + " → " + dep.etageArrivee() +
                    " (" + dep.typeHabitude() + ")");
            }
            if (historique.size() > 10) {
                System.out.println("  ... et " + (historique.size() - 10) + " autre(s) déplacement(s)");
            }
        }
    }
    
    /**
     * Analyse les périodes les plus actives
     */
    private void analyserPeriodesActives() {
        var compteurHeures = new java.util.HashMap<Integer, Integer>();
        
        for (DeplacementInfo dep : historique) {
            int heure = dep.heure().getHour();
            compteurHeures.merge(heure, 1, Integer::sum);
        }
        
        // Trouver les 3 heures les plus actives
        var heuresTriees = compteurHeures.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(3)
            .toList();
        
        System.out.println("  🔥 Top 3 des heures les plus actives:");
        int rang = 1;
        for (var entry : heuresTriees) {
            System.out.println("    " + rang + ". " + String.format("%02d:00", entry.getKey()) + 
                " - " + entry.getValue() + " déplacement(s)");
            rang++;
        }
    }
    
    // Getters
    public LocalTime getTempsActuel() {
        return tempsActuel;
    }
    
    public int getTick() {
        return tick;
    }
    
    public boolean estEnCours() {
        return enCours;
    }
    
    public Tower getTower() {
        return tower;
    }
    
    public List<DeplacementInfo> getHistorique() {
        return new ArrayList<>(historique);
    }
}