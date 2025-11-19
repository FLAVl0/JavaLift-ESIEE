package esiee.config;

public record ConfigurationRecord(SimulationConfig simulationConfig, TowerConfig towerConfig, LiftConfig liftConfig, HabitudeConfig habitudeConfig) implements ConfigurationData {

}
