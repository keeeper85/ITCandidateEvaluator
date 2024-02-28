package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RecruitmentTest {

    private Presets presets;
    private Recruitment recruitment;
    private Model model = mock(Model.class);
    private Candidate candidate;

    @BeforeEach
    public void setUp(){
        recruitment = null;
        candidate = null;
        HashMap<String, Integer> modifiersValues = new HashMap<>();
        modifiersValues.put("resume", 10);
        modifiersValues.put("language", 10);
        modifiersValues.put("experience", 10);
        modifiersValues.put("projects", 10);
        modifiersValues.put("coding", 10);
        modifiersValues.put("questions", 10);
        modifiersValues.put("salary", 10);
        modifiersValues.put("soft", 10);
        presets = new Presets("test", modifiersValues);
        recruitment = new Recruitment(model, "Test Recruitment", presets);
        candidate = new Candidate(recruitment, "John", "Smith");

        Map<Stages, Integer> scores = new HashMap<>();
        scores.put(Stages.RESUME, 100);
        scores.put(Stages.LANGUAGE, 100);
        scores.put(Stages.EXPERIENCE, 100);
        scores.put(Stages.PROJECTS, 100);
        scores.put(Stages.LIVE_CODING, 100);
        scores.put(Stages.QUESTIONS, 100);
        scores.put(Stages.SALARY, 100);
        scores.put(Stages.SOFT_SKILLS, 100);
        candidate.getScores().putAll(scores);

        candidate.setFinished(true);
    }

    @Test
    void calculateFinalCandidateScorePercent() {
        int maxScorePercent = 100;
        int candidateScorePercent = recruitment.calculateFinalCandidateScorePercent(candidate);

        assertEquals(maxScorePercent, candidateScorePercent);
    }

    @Test
    void calculateCostValueRatio() {
        int ratioAtAverageSalaryPercent = 100;
        int candidateRatioPercent = recruitment.calculateCostValueRatio(candidate);

        assertEquals(ratioAtAverageSalaryPercent, candidateRatioPercent);
    }

    @Test
    void addCandidates() {
        int initialNumberOfCandidates = 0;
        List<Candidate> candidateList = new ArrayList<>();
        candidateList.add(mock(Candidate.class));
        candidateList.add(mock(Candidate.class));
        candidateList.add(mock(Candidate.class));
        recruitment.addCandidates(candidateList);
        int numberOfCandidatesAfter = recruitment.getCandidateList().size();

        assertEquals(initialNumberOfCandidates + 3, numberOfCandidatesAfter);
    }

    @Test
    void addSingleCandidate() {
        int initialNumberOfCandidates = 0;
        recruitment.addSingleCandidate(candidate);
        int numberOfCandidatesAfter = recruitment.getCandidateList().size();

        assertEquals(initialNumberOfCandidates + 1, numberOfCandidatesAfter);
    }

    @Test
    void getStagesForEvaluation() {
        int stagesSet = presets.getPresetsValues().size();
        List<String> stagesList = recruitment.getStagesForEvaluation();
        int stagesPresent = stagesList.size();

        assertEquals(stagesSet, stagesPresent);

        boolean hasResumeStage = false;
        boolean hasExperienceStage = false;
        boolean hasQuestionsStage = false;
        boolean hasSoftSkillsStage = false;


        if (stagesList.contains("resume")) hasResumeStage = true;
        if (stagesList.contains("experience")) hasExperienceStage = true;
        if (stagesList.contains("questions")) hasQuestionsStage = true;
        if (stagesList.contains("soft")) hasSoftSkillsStage = true;

        assertTrue(hasResumeStage);
        assertTrue(hasExperienceStage);
        assertTrue(hasQuestionsStage);
        assertTrue(hasSoftSkillsStage);
    }
}