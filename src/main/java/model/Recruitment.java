package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recruitment {

    private final int STAGE_MAX_SCORE = 100;
    private final double PERCENTAGE = 100.0;
    private final double DECIMAL = 10.0;
    private final double SOFT_SKILLS_FACTOR = 3.0; //higher value = less impact soft skills have on the other scores
    private Model model;
    private String name;
    private Presets presets;
    private List<Candidate> candidateList;
    private LocalDateTime dateOfCreation;
    private int maxPossibleScore;
    private boolean isFinished;
    private boolean areSoftSkillsIncluded = false;
    private int nextCandidateID;

    public Recruitment(Model model, String name, Presets presets) {
        this.model = model;
        this.name = name;
        this.presets = presets;
        candidateList = new ArrayList<>();
        nextCandidateID = candidateList.size() + 1;
        dateOfCreation = LocalDateTime.now();
        maxPossibleScore = calculateMaxPossibleScore();
        isFinished = false;
    }

    public int calculateMaxPossibleScore(){
        HashMap<Stages, Integer> stagesModifiers = presets.getPresetsValues();
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
        HashMap<Stages, Integer> stagesModifiers = presets.getPresetsValues();
        Integer presetsModifier = stagesModifiers.entrySet().stream()
                .filter(entry -> entry.getKey() == stage)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0);

        return presetsModifier / DECIMAL;
    }

    public int calculateCandidateScore(Candidate candidate){
        HashMap<Stages, Integer> stagesModifiers = presets.getPresetsValues();
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
        HashMap<Stages, Integer> stagesModifiers = presets.getPresetsValues();
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
        candidate.setEvaluationScore(percentScore);

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

    public void addCandidates(List<Candidate> newCandidates){
        for (Candidate newCandidate : newCandidates) {
            newCandidate.setId(nextCandidateID);
            candidateList.add(newCandidate);
            nextCandidateID++;
        }
    }

    public void addSingleCandidate(Candidate candidate){
        candidate.setId(nextCandidateID);
        candidateList.add(candidate);
        nextCandidateID++;
    }

    public Presets getPresets() {
        return presets;
    }

    public String getName() {
        return name;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public List<Candidate> getCandidateList() {
        return candidateList;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }


    @Override
    public String toString() {
        String startDate = dateOfCreation.format(DateTimeFormatter.ISO_DATE);
        String status = isFinished() ? "finished" : "unfinished";

        return String.format("%s   (%d candidates)   STARTED: %s   STATUS: %s   PRESETS: %s", name, candidateList.size(), startDate, status, presets.toString());
    }
}
