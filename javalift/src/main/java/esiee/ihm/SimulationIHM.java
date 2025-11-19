package esiee.ihm;
/* 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import esiee.Etage;
import esiee.Event;
import esiee.Habitude;
import esiee.PersonneSimple;
import esiee.Personnes;
*/
public class SimulationIHM {
     
    // Déterminer le chemin dynamiquement
    // private static final String RESOURCES_PATH = determinerCheminRessources();
    // private static final Gson gson = new Gson();
    
    /**
     * Détermine le chemin correct des ressources en testant plusieurs emplacements
     */
    /* 
    private static String determinerCheminRessources() {
        // Cherche dans plusieurs emplacements possibles
        String[] cheminsPossibles = {
            "src/main/resources/",
            "javalift/src/main/resources/",
            "target/classes/",
        };
        
        for (String chemin : cheminsPossibles) {
            Path path = Paths.get(chemin);
            if (Files.exists(path)) {
                System.out.println("✓ Ressources trouvées: " + path.toAbsolutePath());
                return chemin;
            }
        }
        
        System.err.println("⚠️  Aucun dossier resources trouvé!");
        System.err.println("Chemins testés:");
        for (String chemin : cheminsPossibles) {
            System.err.println("  - " + Paths.get(chemin).toAbsolutePath());
        }
        return "src/main/resources/"; // Défaut
    }
    
    public static void main(String[] args) {
        System.out.println("=== Simulation JavaLift (Configuration) ===\n");
        
        try {
            // 1. Charger la configuration de la tour
            List<Etage> etages = chargerEtages();
            System.out.println("✓ Étages chargés depuis TowerConfig.json:");
            etages.forEach(e -> System.out.println("  " + e));
            
            // 2. Charger les habitudes
            List<Habitude> habituesDisponibles = chargerHabitudes();
            System.out.println("\n✓ Habitudes chargées depuis HabitudeConfig.json:");
            habituesDisponibles.forEach(h -> System.out.println("  " + h));
            
            // 3. Charger la configuration de simulation
            JsonObject simConfig = chargerSimulationConfig();
            System.out.println("\n✓ Configuration simulation chargée:");
            System.out.println("  Journée: " + simConfig.get("journee").getAsString());
            System.out.println("  Nombre de personnes: " + simConfig.get("nombrePersonnes").getAsInt());
            
            // 4. Créer les personnes avec les habitudes
            List<Personnes> personnes = creerPersonnesDepuisConfig(simConfig, habituesDisponibles);
            System.out.println("\n✓ Personnes créées:");
            personnes.forEach(p -> System.out.println("  " + p));
            
            // 5. Lancer la simulation
            System.out.println("\n--- Simulation des déplacements ---");
            simulerEvenements(personnes, etages);
            
            System.out.println("\n=== Fin de simulation ===");
            
        } catch (IOException e) {
            System.err.println("\n❌ Erreur lors du chargement des fichiers: " + e.getMessage());
            System.err.println("\nChemin recherché: " + RESOURCES_PATH);
            System.err.println("Répertoire courant: " + Paths.get("").toAbsolutePath());
           
        } catch (Exception e) {
            System.err.println("\n❌ Erreur lors de la simulation: " + e.getMessage());
        }
    } */
    
    /**
     * Charge les étages depuis TowerConfig.json
     */
    /* 
    private static List<Etage> chargerEtages() throws IOException {
        String filePath = RESOURCES_PATH + "TowerConfig.json";
        System.out.println("  📂 Chargement depuis: " + Paths.get(filePath).toAbsolutePath());
        
        String content = Files.readString(Paths.get(filePath));
        JsonObject config = gson.fromJson(content, JsonObject.class);
        JsonArray etagesArray = config.getAsJsonArray("etages");
        
        List<Etage> etages = new ArrayList<>();
        for (int i = 0; i < etagesArray.size(); i++) {
            JsonObject etageObj = etagesArray.get(i).getAsJsonObject();
            int niveau = etageObj.get("niveau").getAsInt();
            int nbHabitants = etageObj.get("nbHabitants").getAsInt();
            etages.add(new Etage(niveau, nbHabitants));
        }
        
        return etages;
    } */
    
    /**
     * Charge les habitudes depuis HabitudeConfig.json
     */
    /* 
    private static List<Habitude> chargerHabitudes() throws IOException {
        String filePath = RESOURCES_PATH + "HabitudeConfig.json";
        System.out.println("  📂 Chargement depuis: " + Paths.get(filePath).toAbsolutePath());
        
        String content = Files.readString(Paths.get(filePath));
        JsonObject config = gson.fromJson(content, JsonObject.class);
        JsonArray habitudesArray = config.getAsJsonArray("habitudes");
        
        List<Habitude> habitudes = new ArrayList<>();
        for (int i = 0; i < habitudesArray.size(); i++) {
            JsonObject habObj = habitudesArray.get(i).getAsJsonObject();
            Event type = Event.valueOf(habObj.get("type").getAsString());
            LocalTime debut = LocalTime.parse(habObj.get("debut").getAsString());
            LocalTime fin = LocalTime.parse(habObj.get("fin").getAsString());
            int destination = habObj.get("destination").getAsInt();
            
            habitudes.add(new Habitude(type, debut, fin, destination));
        }
        
        return habitudes;
    } */
    
    /**
     * Charge la configuration de simulation depuis SimulationConfig.json
     */
    /* 
    private static JsonObject chargerSimulationConfig() throws IOException {
        String filePath = RESOURCES_PATH + "SimulationConfig.json";
        System.out.println("  📂 Chargement depuis: " + Paths.get(filePath).toAbsolutePath());
        
        String content = Files.readString(Paths.get(filePath));
        return gson.fromJson(content, JsonObject.class);
    } */
    
    /**
     * Crée les personnes à partir de la configuration
     */
    /*
    private static List<Personnes> creerPersonnesDepuisConfig(
            JsonObject simConfig, List<Habitude> habituesDisponibles) {
        
        List<Personnes> personnes = new ArrayList<>();
        JsonArray personnesArray = simConfig.getAsJsonArray("personnes");
        
        for (int i = 0; i < personnesArray.size(); i++) {
            JsonObject persObj = personnesArray.get(i).getAsJsonObject();
            String id = persObj.get("id").getAsString();
            int etageInitial = persObj.get("etageInitial").getAsInt();
            JsonArray habitudesIndices = persObj.getAsJsonArray("habitudes");
            
            // Récupérer les habitudes de cette personne
            List<Habitude> habitudesPersonne = new ArrayList<>();
            for (int j = 0; j < habitudesIndices.size(); j++) {
                int indice = habitudesIndices.get(j).getAsInt();
                if (indice >= 0 && indice < habituesDisponibles.size()) {
                    habitudesPersonne.add(habituesDisponibles.get(indice));
                }
            }
            
            personnes.add(new PersonneSimple(id, etageInitial, habitudesPersonne));
        }
        
        return personnes;
    }
    */
    
    /**
     * Simule les événements des personnes
     */
    /*
    private static void simulerEvenements(List<Personnes> personnes, List<Etage> etages) {
        for (Personnes personne : personnes) {
            System.out.println("\n👤 Personne: " + personne.id());
            System.out.println("   Étage initial: " + personne.etage());
            
            for (Habitude habitude : personne.habitudes()) {
                System.out.println("\n   📍 " + habitude.type() + 
                                 " de " + habitude.debut() + 
                                 " à " + habitude.fin() + 
                                 " → Étage " + habitude.destination());
                
                // Vérifier que l'étage de destination existe
                boolean etageExiste = etages.stream()
                    .anyMatch(e -> e.niveau() == habitude.destination());
                
                if (!etageExiste) {
                    System.out.println("      ⚠️  Étage " + habitude.destination() + " n'existe pas!");
                    continue;
                }
                
                personne.setEtage(habitude.destination());
                System.out.println("      ✓ Position actuelle: Étage " + personne.etage());
            }
        }
    } 
    */
}