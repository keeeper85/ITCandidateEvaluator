package model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Presets object store slider values for each stage.
 * Slider value describes the importance of the particular stage.
 * If a candidate gets 100 points at that stage, but slider is set to 5 it means only 50% of that score is added to the total result.
 * Sliders can be set to values 0-10, 0 value means the stage will not occur during the interview.
 * Presets can be kept as JSON files and parsed by loadPresetsFromDirectory() method call
 * Filed presets are checked before parsing - if the contents don't match file is omitted
 */

public class Presets implements Serializable {
    private String name;
    private Map<Stages, Integer> presetsValues;
    private static Set<Presets> defaultPresets;
    private static final Path PRESETS_FILES_DIRECTORY = Paths.get("src/main/resources/presets");
    private static final String[] STAGES_NAMES = {"Resume and social media evaluation","Own projects","English language assessment","Live coding","Salary expectations","Technical questions","Soft skills","Previous work experience"};

    public Presets(String name, HashMap<String, Integer> modifiersValues) {
        this.name = name;
        presetsValues = new HashMap<>();
        setPresetsValues(modifiersValues);
        Model.logger.info("A new Presets object has just been created.");
    }

    public static Set<Presets> loadPresetsFromDirectory() {
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
                            else Model.logger.error("The file " + fileName[0] + " is invalid and cannot be read.");
                        } catch (JsonParseException e) {
                            Model.logger.error("Error parsing JSON file: " + e.getMessage());
                        } catch (IOException e) {
                            Model.logger.error("Error reading JSON file: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            Model.logger.error("Error reading JSON file: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return defaultPresets;
    }

    private static boolean isMapValid(Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String stageName = entry.getKey();
            boolean isMatch = Arrays.asList(STAGES_NAMES).contains(stageName);
            if (!isMatch) return false;

            int value = entry.getValue();
            if (value < 0 || value > 10) return false;
        }
        return true;
    }

    static boolean saveNewPresetsToFile(String fileName, Map<String, Integer> newDefaultPresets){
        String filePath;
        if (fileName.contains("presets")) filePath = PRESETS_FILES_DIRECTORY.toString() + "\\" + fileName + ".json";
        else filePath = PRESETS_FILES_DIRECTORY.toString() + "\\" + fileName + "_presets.json";

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(filePath), newDefaultPresets);
        } catch (IOException e) {
            Model.logger.error("Error creating JSON file: " + e.getMessage());
            return false;
        }
        return true;
    }

    private void setPresetsValues(HashMap<String, Integer> modifiersValues){
        for (Map.Entry<String, Integer> entry : modifiersValues.entrySet()) {
            String modifierName = entry.getKey().toLowerCase();
            Integer modifierValue = entry.getValue();
            for (Stages value : Stages.values()) {
                String stageName = value.getStageName();
                if (modifierName.contains(stageName)){
                    presetsValues.put(value, modifierValue);
                }
            }
        }
    }

    public Map<Stages, Integer> getPresetsValues() {
        return presetsValues;
    }

    public String getName() {
        return name;
    }

    private String preparePresetsDescription(){
        StringBuilder descriptionBuilder = new StringBuilder();
        for (Map.Entry<Stages, Integer> entry : presetsValues.entrySet()) {
            String stageName = entry.getKey().getStageName();
            int stagePresetValue = entry.getValue();
            descriptionBuilder.append(stageName).append(":").append(stagePresetValue).append(" ");
        }
        return descriptionBuilder.toString().trim();
    }

    @Override
    public String toString() {
        return preparePresetsDescription();
    }
}
