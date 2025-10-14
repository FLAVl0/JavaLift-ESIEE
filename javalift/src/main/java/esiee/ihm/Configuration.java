package esiee.ihm;

import java.io.File;
import java.util.Scanner;  
import java.util.ArrayList;

public class Configuration {
    private ArrayList<String> configValues;

    public Configuration() {
        configValues = new ArrayList<>();
    }

    public void load(String path) {
        System.out.println("Loading configuration from: " + path);
        // Add code to read and parse the configuration file
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
                configValues.add(line);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parse() {
        System.out.println("Parsing configuration...");
        // Add code to parse the configuration values
        for (String value : configValues) {
            System.out.println("Parsed value: " + value);
        }
    }
}
