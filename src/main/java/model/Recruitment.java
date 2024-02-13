package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recruitment {

    private final int STAGE_MAX_SCORE = 100;
    private final int PRESETS_DEFAULT_VALUE = 5;
    private final int SCORE_SLIDER_DEFAULT_VALUE = 50;
    private final int PRESETS_MAX_VALUE = 10;
    private final int PRESETS_SINGLE_POINT_MODIFIER = 10;
    private final int PRESETS_MAX_MODIFIER = (PRESETS_MAX_VALUE - PRESETS_DEFAULT_VALUE) * PRESETS_SINGLE_POINT_MODIFIER;
    private final int SOFT_SKILLS_MAX_MODIFIER = 30;
    private final int PERCENTAGE = 100;
    private Model model;
    private String name;
    private Presets presets;
    private List<Candidate> candidateList;
    private LocalDateTime dateOfCreation;
    private int maxPossibleScore;
    private boolean isFinished;
    private boolean areSoftSkillsIncluded = false;

    public Recruitment(Model model, String name, Presets presets) {
        this.model = model;
        this.name = name;
        this.presets = presets;
        candidateList = new ArrayList<>();
        dateOfCreation = LocalDateTime.now();
        maxPossibleScore = calculateMaxPossibleScore();
        isFinished = false;
    }

    public int calculateMaxPossibleScore(){
        int maxPossibleScore = 0;
        HashMap<Stages, Integer> modifiers = presets.getPresets();

        for (Map.Entry<Stages, Integer> entry : modifiers.entrySet()) {
            int stageOrdinal = entry.getKey().getStageOrdinal();
            if (stageOrdinal <= 5){
                maxPossibleScore += STAGE_MAX_SCORE;
            }
            if (stageOrdinal == 7) areSoftSkillsIncluded = true;
        }

        maxPossibleScore = maxPossibleScore + (maxPossibleScore * PRESETS_MAX_MODIFIER / PERCENTAGE);
        if (areSoftSkillsIncluded) maxPossibleScore = maxPossibleScore + (maxPossibleScore * SOFT_SKILLS_MAX_MODIFIER / PERCENTAGE);

        return maxPossibleScore;
    }

    public int calculatePresetStageModifiedScore(Stages stage, int thisStageScore){
        Integer presetSliderValue = presets.getPresets().entrySet().stream()
                .filter(stagesIntegerEntry -> stagesIntegerEntry.getKey() == stage)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0);

        int modifier = (presetSliderValue - PRESETS_DEFAULT_VALUE) * PRESETS_SINGLE_POINT_MODIFIER;
        int modifiedScore = thisStageScore + (thisStageScore * modifier / PERCENTAGE);

        return modifiedScore;
    }

    public int calculateFinalScore(Candidate candidate){
        HashMap<Stages, Integer> scores = candidate.getScores();
        int finalScore = 0;
        int softSkillsModifier = 0;

        for (Map.Entry<Stages, Integer> entry : scores.entrySet()) {
            Stages stage = entry.getKey();
            int stageOrdinal = stage.getStageOrdinal();
            int thisStageScore = entry.getValue();
            int stageModifierBonus = calculatePresetStageModifiedScore(stage, thisStageScore);
            if (stageOrdinal <= 5){
                finalScore += thisStageScore + stageModifierBonus;
            }
            if (stageOrdinal == 7) softSkillsModifier = (thisStageScore - SCORE_SLIDER_DEFAULT_VALUE);
        }
        if (areSoftSkillsIncluded) {
            finalScore = finalScore + (finalScore * softSkillsModifier / PERCENTAGE);
        }

        return finalScore;
    }

    public int calculateCostValueRatio(Candidate candidate){
        return 0;
    }

    public List<Candidate> getCandidateList() {
        return candidateList;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }
}
