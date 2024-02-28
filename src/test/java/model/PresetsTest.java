package model;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PresetsTest {

    @Test
    void loadPresetsFromDirectory() {
            Set<Presets> loadedPresets = Presets.loadPresetsFromDirectory();

            boolean hasJunior = false;
            boolean hasHalf = false;
            boolean hasInvalid = false;

            int presetsAmount = loadedPresets.size();
            assertEquals(5,presetsAmount);

        for (Presets loadedPreset : loadedPresets) {
            if (loadedPreset.getName().contains("junior")) hasJunior = true;
            if (loadedPreset.getName().contains("half")) hasHalf = true;
            if (loadedPreset.getName().contains("invalid")) hasInvalid = true;
        }

        assertTrue(hasJunior);
        assertTrue(hasHalf);
        assertFalse(hasInvalid);
    }

    @Test
    void saveNewPresetsToFile() throws IOException {
        Path directoryToFile = Paths.get("src/main/resources/presets/test_presets.json");

        boolean hasFileBefore = false;
        if (Files.exists(directoryToFile)) hasFileBefore = true;
        assertFalse(hasFileBefore);

        Map<String, Integer> newDefaultPresets = new HashMap<>();
        newDefaultPresets.put("t1", 8);
        newDefaultPresets.put("t2", 3);

        Presets.saveNewPresetsToFile("test_presets", newDefaultPresets);
        boolean hasFileAfter = false;
        if (Files.exists(directoryToFile)) hasFileAfter = true;
        assertTrue(hasFileAfter);

        Files.deleteIfExists(directoryToFile);
    }
}