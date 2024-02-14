package model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Stream;

public class Presets {
    private String name;
    private HashMap<Stages, Integer> presets;
    private static HashSet<Presets> defaultPresets;
    private static Path presetsFilesDirectory = Paths.get("src/main/resources/presets");

    public Presets(String name, HashMap<String, Integer> modifiersValues) {
        this.name = name;
        presets = new HashMap<>();
        setPresets(modifiersValues);
    }

    public static HashSet<Presets> loadPresetsFromDirectory() {
        final String[] fileName = new String[1];
        defaultPresets = new HashSet<>();

        try (Stream<Path> paths = Files.list(presetsFilesDirectory)) {
            paths.filter(file -> file.getFileName().toString().contains("presets") && file.toString().endsWith(".json"))
                    .forEach(file -> {
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            fileName[0] = file.getFileName().toString().replaceAll(".json", "");
                            HashMap<String, Integer> map = mapper.readValue(file.toFile(), HashMap.class);
                            defaultPresets.add(new Presets(fileName[0], map));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return defaultPresets;
    }

    public void setPresets(HashMap<String, Integer> modifiersValues){
        for (Map.Entry<String, Integer> entry : modifiersValues.entrySet()) {
            String modifierName = entry.getKey().toLowerCase();
            Integer modifierValue = entry.getValue();
            for (Stages value : Stages.values()) {
                String stageName = value.getStageName();
                if (modifierName.contains(stageName)){
                    presets.put(value, modifierValue);
                }
            }
        }
    }

    public HashMap<Stages, Integer> getPresets() {
        return presets;
    }

    @Override
    public String toString() {
        return "Presets{" +
                "name='" + name + '\'' +
                '}';
    }
}
