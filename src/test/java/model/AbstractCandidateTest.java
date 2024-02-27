package model;

import model.storage.FileStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AbstractCandidateTest {

    private Presets testPresets = new Presets("testPresets", new HashMap<>());
    private Recruitment recruitment = new Recruitment(new Model(new FileStrategy()), "test", testPresets);
    private Candidate candidate;

    @BeforeEach
    public void setUp(){
        candidate = new Candidate(recruitment, "John", "Smith");
    }

    @Test
    void calculateAge() {
        int yearOfBirth = 1999;
        candidate.setYearOfBirth(yearOfBirth);
        int currentYear = LocalDateTime.now().getYear();
        int ageExpected = currentYear - yearOfBirth;
        int ageActual = Integer.parseInt(candidate.calculateAge());

        assertEquals(ageExpected, ageActual);
    }

    @Test
    void translateAndAddRawScores() {
        Map<String, Integer> rawScores = new HashMap<>();
        rawScores.put("invalid", 77);
        rawScores.put("resume", 0);

        candidate.translateAndAddRawScores(rawScores);
        Map<Stages, Integer> translatedScores = candidate.getScores();

        boolean hasInvalid = false;
        boolean hasValid = false;

        if (translatedScores.containsValue(77)) hasInvalid = true;
        if (translatedScores.containsValue(0)) hasValid = true;

        assertFalse(hasInvalid);
        assertTrue(hasValid);
    }
}