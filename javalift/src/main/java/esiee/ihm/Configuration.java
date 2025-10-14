package esiee.ihm;

import java.io.File;
import java.util.Scanner;  
import com.google.gson.Gson;
import java.util.HashMap;

public class Configuration {

    public String load(String path) {
        String content = "";
        System.out.println("Loading configuration from: " + path);
        // Add code to read and parse the configuration file
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String c = scanner.next();
                System.out.println(c);
                content += c;
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public Object parse(String path, Class<?> record) {
        String json = load(path);
        Gson gson = new Gson();
        Object configData = gson.fromJson(json, record);
        return configData;
    }

    public ConfigurationRecord formatAll(Enum<?> ListConfig) {
        HashMap<String, Object> configs = new HashMap<>();
        for (Enum<?> e : ListConfig.getClass().getEnumConstants()) {
            if (e instanceof RecordList) {
                RecordList record = (RecordList) e;
                Object configData = parse("src/main/resources/" + record.getFileName(), record.getConfigClass());
                configs.put(record.name(), configData);
            }
        }
        // Instantiation of ConfigurationRecord with preparsed record with a for loop
        ConfigurationRecord config = new ConfigurationRecord(
            (SimulationConfig) configs.get(RecordList.SIMULATION.name()),
            (TowerConfig) configs.get(RecordList.TOWER.name()),
            (LiftConfig) configs.get(RecordList.LIFT.name()),
            (HabitudeConfig) configs.get(RecordList.HABITUDE.name())
        );
        return config;
    }

    
}


