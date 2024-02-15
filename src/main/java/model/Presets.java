package model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class Presets {
    private String name;
    private HashMap<Stages, Integer> presets;
    private static HashSet<Presets> defaultPresets;
    private static final Path PRESETS_FILES_DIRECTORY = Paths.get("src/main/resources/presets");
    private static final String[] stagesNames = {"Resume and social media evaluation","Own projects","English language assessment","Live coding","Salary expectations","Technical questions","Soft skills","Previous work experience"};

    public Presets(String name, HashMap<String, Integer> modifiersValues) {
        this.name = name;
        presets = new HashMap<>();
        setPresets(modifiersValues);
    }

    public static HashSet<Presets> loadPresetsFromDirectory() {
        final String[] fileName = new String[1];
        defaultPresets = new HashSet<>();

        try (Stream<Path> paths = Files.list(PRESETS_FILES_DIRECTORY)) {
            paths.filter(file -> file.getFileName().toString().contains("presets") && file.toString().endsWith(".json"))
                    .forEach(file -> {
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            fileName[0] = file.getFileName().toString().replaceAll(".json", "");
                            HashMap<String, Integer> map = mapper.readValue(file.toFile(), HashMap.class);
                            if (isMapValid(map)) defaultPresets.add(new Presets(fileName[0], map));
                            else System.out.println("invalid file"); //todo LOGGER
                        } catch (JsonParseException e) {
                            System.out.println("invalid file"); //todo LOGGER
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return defaultPresets;
    }

    private static boolean isMapValid(HashMap<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String stageName = entry.getKey();
            boolean isMatch = Arrays.asList(stagesNames).contains(stageName);
            if (!isMatch) return false;

            int value = entry.getValue();
            if (value < 0 || value > 10) return false;
        }
        return true;
    }

    public static void saveNewPresetsToFile(String fileName, HashMap<String, Integer> newDefaultPresets){
        String filePath = PRESETS_FILES_DIRECTORY.toString() + "\\" + fileName + "_presets.json";

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(filePath), newDefaultPresets);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
