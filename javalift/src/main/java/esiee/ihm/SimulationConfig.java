package esiee.ihm;

public record SimulationConfig(byte day, int duration, int startHour, int endHour, int seed) implements ConfigurationData{

}
