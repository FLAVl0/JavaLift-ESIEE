package esiee.config;

public record LiftConfig(int capacity, int speed, int doorTime, int acceleration, int deceleration, double energyCost, String manufacturer) implements ConfigurationData{

}
