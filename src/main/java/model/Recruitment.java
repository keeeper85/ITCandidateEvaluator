package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recruitment {

    private final int STAGE_MAX_SCORE = 100;
    private final double PERCENTAGE = 100.0;
    private final double DECIMAL = 10.0;
    private final double SOFT_SKILLS_FACTOR = 3.0; //at 0 soft skills can multiply total score by 0.1-1, at 5 it's 5.1-6 so the total impact is lowered to max 20%
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
        HashMap<Stages, Integer> stagesModifiers = presets.getPresets();
        int maxPossibleScore = 0;

        for (Stages stage : stagesModifiers.keySet()) {
            if (!stage.equals(Stages.SALARY) && !stage.equals(Stages.SOFT_SKILLS)){
                maxPossibleScore += STAGE_MAX_SCORE * getStageModifier(stage);
            }
            if (stage.equals(Stages.SOFT_SKILLS)) areSoftSkillsIncluded = true;
        }

        if (areSoftSkillsIncluded) {
            double multiplier = SOFT_SKILLS_FACTOR + getStageModifier(Stages.SOFT_SKILLS);
            maxPossibleScore = (int) (maxPossibleScore * multiplier);
        }
        return maxPossibleScore;
    }

    public double getStageModifier(Stages stage){
        HashMap<Stages, Integer> stagesModifiers = presets.getPresets();
        Integer presetsModifier = stagesModifiers.entrySet().stream()
                .filter(entry -> entry.getKey() == stage)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0);

        return presetsModifier / DECIMAL;
    }

    public int calculateCandidateScore(Candidate candidate){
        HashMap<Stages, Integer> stagesModifiers = presets.getPresets();
        HashMap<Stages, Integer> candidateResults = candidate.getScores();
        int finalScore = 0;

        HashMap<Stages, Double> modifiedScores = calculateModifiedScore(stagesModifiers, candidateResults);
        for (Map.Entry<Stages, Double> entry : modifiedScores.entrySet()) {
            Stages stage = entry.getKey();
            if (!stage.equals(Stages.SALARY) && !stage.equals(Stages.SOFT_SKILLS)){
                double stageScore = entry.getValue();
                finalScore += stageScore;
            }
        }

        if (areSoftSkillsIncluded) {
            double multiplier = SOFT_SKILLS_FACTOR + getSoftSkillsModifier(candidate);
            finalScore = (int) (finalScore * multiplier);
        }
        System.out.println(finalScore + " points");

        return finalScore;
    }

    public HashMap<Stages, Double> calculateModifiedScore(HashMap<Stages, Integer> stagesModifiers, HashMap<Stages, Integer> candidateResults){
        HashMap<Stages, Double> modifiedScores = new HashMap<>();

        for (Map.Entry<Stages, Integer> stageModifier : stagesModifiers.entrySet()) {
            Stages stage = stageModifier.getKey();
            double value = stageModifier.getValue();
            double modifier = value / DECIMAL;
            for (Map.Entry<Stages, Integer> candidateResult : candidateResults.entrySet()) {
                Stages candidateStage = candidateResult.getKey();
                if (stage == candidateStage){
                    double score = candidateResult.getValue();
                    double modifiedScore = score * modifier;
                    modifiedScores.put(candidateStage, modifiedScore);
                }
            }
        }

        return modifiedScores;
    }

    public double getSoftSkillsModifier(Candidate candidate){
        HashMap<Stages, Integer> stagesModifiers = presets.getPresets();
        Integer presetsModifier = stagesModifiers.entrySet().stream()
                .filter(entry -> entry.getKey() == Stages.SOFT_SKILLS)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0);

        HashMap<Stages, Integer> candidateResults = candidate.getScores();
        Integer softSkillsSliderValue = candidateResults.entrySet().stream()
                .filter(entry -> entry.getKey() == Stages.SOFT_SKILLS)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0);

        double softSkillsModifier = (presetsModifier / DECIMAL) * (softSkillsSliderValue / PERCENTAGE);

        return softSkillsModifier;
    }

    public int calculateFinalCandidateScorePercent(Candidate candidate){
        int percentScore = (int) (calculateCandidateScore(candidate) / (double) maxPossibleScore * PERCENTAGE);

        return percentScore;
    }
    public int calculateCostValueRatio(Candidate candidate){
        HashMap<Stages, Integer> candidateResults = candidate.getScores();
        Integer salarySliderValue = candidateResults.entrySet().stream()
                .filter(entry -> entry.getKey() == Stages.SALARY)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0);

        double scorePercent = calculateFinalCandidateScorePercent(candidate);
        double salaryModifier = salarySliderValue / PERCENTAGE;

        int costValueRatio = (int) (scorePercent / salaryModifier);


        return costValueRatio;
    }

    public List<Candidate> getCandidateList() {
        return candidateList;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }
}
