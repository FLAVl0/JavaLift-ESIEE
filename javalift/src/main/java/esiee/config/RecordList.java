package esiee.config;

public enum RecordList {
    SIMULATION("simulation.json", SimulationConfig.class),
    TOWER("TowerConfig.json", TowerConfig.class),
    LIFT("LiftConfig.json", LiftConfig.class),
    HABITUDE("poi.json", HabitudeConfig.class),
    HABITUDE_DEF("HabitudeConfig.json", HabitudeDefinitionConfig.class); // ✅ DÉCOMMENTER

    private final String fileName;
    private final Class<?> configClass;

    RecordList(String fileName, Class<?> configClass) {
        this.fileName = fileName;
        this.configClass = configClass;
    }

    public String getFileName() {
        return fileName;
    }

    public Class<?> getConfigClass() {
        return configClass;
    }
}