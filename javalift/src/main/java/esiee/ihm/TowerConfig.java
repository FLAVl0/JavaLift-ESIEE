package esiee.ihm;

public record TowerConfig(int nbFloors, int nbLifts, int floorHeight, int maxPeoplePerFloor, boolean randHabitant) implements ConfigurationData{

}
