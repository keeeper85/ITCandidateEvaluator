package model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CandidateFactoryTest {

    @Test
    public void testGetCandidatesFromResumes() {
        String directoryWithResumes = "src/main/resources/resumeTest";
        Recruitment recruitment = new Recruitment(mock(Model.class), "Mock Recruitment", new Presets("Test", new HashMap<>()));
        List<Candidate> candidates = CandidateFactory.getCandidatesFromResumes(directoryWithResumes, recruitment);

        assertEquals(12, candidates.size());

        boolean hasBrad = false;
        boolean hasRoger = false;
        boolean hasAshely = false;

        for (Candidate candidate : candidates) {
            if (candidate.getFirstName().equals("Brad")) hasBrad = true;
            if (candidate.getFirstName().equals("Roger")) hasRoger = true;
            if (candidate.getFirstName().equals("Ashley")) hasAshely = true;
        }

        assertTrue(hasBrad);
        assertTrue(hasRoger);
        assertTrue(hasAshely);
    }

    @Test
    void getFileNames() {
        String directoryWithResumes = "src/main/resources/resumeTest";
        Recruitment recruitment = new Recruitment(mock(Model.class), "Mock Recruitment", new Presets("Test", new HashMap<>()));
        Map<String,String> filesAndNames = CandidateFactory.getFileNames(directoryWithResumes);

        assertEquals(12, filesAndNames.size());

        boolean hasBrad = false;
        boolean hasRoger = false;
        boolean hasAshely = false;

        for (String value : filesAndNames.values()) {
            if (value.equals("BradPitt25.pdf")) hasBrad = true;
            if (value.equals("RogerPodactercv.pdf")) hasRoger = true;
            if (value.equals("AshleyTyCV.pdf")) hasAshely = true;
        }

        assertTrue(hasBrad);
        assertTrue(hasRoger);
        assertTrue(hasAshely);
    }
}