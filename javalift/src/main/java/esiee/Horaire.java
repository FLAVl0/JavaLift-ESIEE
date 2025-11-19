package esiee;

import java.time.LocalTime;

public enum Horaire {
    MATIN(LocalTime.of(6, 0), LocalTime.of(10, 0)),
    FIN_MATINEE(LocalTime.of(10, 0), LocalTime.of(12, 30)),
    APRES_MIDI(LocalTime.of(12, 30), LocalTime.of(17, 30)),
    SOIREE(LocalTime.of(17, 30), LocalTime.of(22, 0)),
    NUIT(LocalTime.of(22, 0), LocalTime.of(6, 0)); 

    private final LocalTime debut;
    private final LocalTime fin;

    Horaire(LocalTime debut, LocalTime fin) {
        this.debut = debut;
        this.fin = fin;
    }

    public LocalTime getDebut() {
        return debut;
    }

    public LocalTime getFin() {
        return fin;
    }

    /**
     * Vérifie si une heure est dans cette plage horaire
     * Gère correctement le cas de la NUIT qui traverse minuit
     */
    public boolean contient(LocalTime heure) {
        // Cas normal : début < fin
        if (debut.isBefore(fin)) {
            return !heure.isBefore(debut) && heure.isBefore(fin);
        }
        // Cas spécial NUIT : 22h -> 6h (traverse minuit)
        else {
            return !heure.isBefore(debut) || heure.isBefore(fin);
        }
    }

    @Override
    public String toString() {
        return name() + "[" + debut + "-" + fin + "]";
    }
}