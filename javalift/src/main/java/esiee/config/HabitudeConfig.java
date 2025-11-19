package esiee.config;

import java.util.ArrayList;

public record HabitudeConfig(int heure_d_min, int heure_d_max, int heure_a_min, int heure_a_max, ArrayList<String> work, boolean rand_work, boolean friend)implements ConfigurationData {

}
