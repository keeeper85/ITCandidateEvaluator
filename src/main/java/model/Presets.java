package model;

import java.util.HashMap;
import java.util.Map;

public class Presets {
    private String name;
    private HashMap<Stages, Integer> presets;

    public Presets(String name, HashMap<String, Integer> modifiersValues) {
        this.name = name;
        presets = new HashMap<>();
        setPresets(modifiersValues);
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
}
