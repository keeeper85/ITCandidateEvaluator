package controller;

import model.*;
import model.storage.FileStrategy;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CandidateDTOTest {

    private CandidateDTO candidateDTO;
    private Presets testPresets = new Presets("testPresets", new HashMap<>());
    private Recruitment recruitment = new Recruitment(new Model(new FileStrategy()), "test", testPresets);


    @Test
    public void testSaveData_WithNonNullCandidate() {
        Candidate candidate = new Candidate(recruitment, "test", "test");
        recruitment.addSingleCandidate(candidate);
        int candidateListSizeBefore = recruitment.getCandidateList().size();
        assertEquals(1,candidateListSizeBefore);
        candidateDTO = new CandidateDTO(candidate, recruitment);
        candidateDTO.saveData();
        int candidateListSizeAfter = recruitment.getCandidateList().size();
        assertEquals(1,candidateListSizeAfter);

        boolean isDataTransferOK = testTransferData(candidate, candidateDTO);

        assertTrue(isDataTransferOK);
    }

    private boolean testTransferData(Candidate candidate, CandidateDTO candidateDTO) {
        String testName = "TestName";
        candidateDTO.setFirstName(testName);
        candidateDTO.saveData();
        String firstName = candidate.getFirstName();

        return firstName.equals(testName);
    }

    @Test
    public void testSaveData_WithNullCandidate() {
        int candidateListSizeBefore = recruitment.getCandidateList().size();
        assertEquals(0,candidateListSizeBefore);
        candidateDTO = new CandidateDTO(null, recruitment);
        candidateDTO.saveData();
        int candidateListSizeAfter = recruitment.getCandidateList().size();
        assertEquals(1,candidateListSizeAfter);
    }

    @Test
    public void testCompleteEvaluation(){
        Candidate candidate = new Candidate(recruitment, "test", "test");
        candidateDTO = new CandidateDTO(candidate, recruitment);
        boolean isFinishedBefore = candidate.isFinished();
        assertFalse(isFinishedBefore);

        candidateDTO.setFinished(true);
        candidateDTO.saveDataAndCompleteEvaluation();
        boolean isFinishedAfter = candidate.isFinished();
        assertTrue(isFinishedAfter);
    }

    @Test
    public void testCalculateQuestionsAverageScore_NoEvaluatedQuestions() {
        candidateDTO = new CandidateDTO(null, recruitment);
        assertEquals(0, candidateDTO.calculateQuestionsAverageScore());
    }

    @Test
    public void testCalculateQuestionsAverageScore_WithEvaluatedQuestions() {
        candidateDTO = new CandidateDTO(null, recruitment);
        Map<Question, Integer> evaluatedQuestions = new HashMap<>();
        evaluatedQuestions.put(new Question("Question 1", "Body 1"), 5);
        evaluatedQuestions.put(new Question("Question 2", "Body 2"), 7);
        evaluatedQuestions.put(new Question("Question 3", "Body 3"), 8);
        candidateDTO.setEvaluatedQuestions(evaluatedQuestions);

        assertEquals(6, candidateDTO.calculateQuestionsAverageScore());
    }
}