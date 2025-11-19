package esiee.config;

public enum RecordList {
    SIMULATION("simulationConfig.json", SimulationConfig.class),
    TOWER("towerConfig.json", TowerConfig.class),
    LIFT("liftConfig.json", LiftConfig.class),
    HABITUDE("habitudeConfig.json", HabitudeConfig.class);

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
