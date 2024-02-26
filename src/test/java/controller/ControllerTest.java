package controller;

import model.Candidate;
import model.Model;
import model.Presets;
import model.Recruitment;
import model.storage.FileStrategy;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private Presets testPresets = new Presets("testPresets", new HashMap<>());
    private Recruitment recruitment = new Recruitment(new Model(new FileStrategy()), "test", testPresets);
    private Candidate candidate = new Candidate(recruitment, "test", "test");
    @Test
    void createTemporaryCandidate() {
        CandidateDTO candidateDTO = new Controller(new Model(new FileStrategy())).createTemporaryCandidate(candidate,recruitment);

        String firstName = candidateDTO.getFirstName();

        assertNotNull(candidateDTO);
        assertEquals("test", firstName);
    }
}