package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Recruitment is the main class just after Model. A Recruitment object MUST be created to start the evaluation.
 * Recruitment class holds the methods needed for calculating candidates' scores.
 * SOFT_SKILLS_FACTOR is set to 3.0; The higher value = less impact soft skills have on the other scores. At 3.0 it's up to 25% (100% / (3.0 + 1.0))
 * At SOFT_SKILLS_FACTOR = 0.0 the total score is multiplied by soft skills score (from 0% to 100%)
 * In general calculating score looks like this:
 * - we take score slider value from that stage, e.g. is set to 80 points
 * - we multiply it by the stage importance set in presets, e.g. 5 = 50% so 80 points * 50% equals 40 points
 * - we calculate the soft skills modifier as above: score slider (e.g. 50) * presets modifier (e.g. 7) -> 50 * 70 / 100% = 35% (getSoftSkillsModifier(Candidate candidate) method)
 * - we take modified score (40 points) and add soft skills bonus (40 * 35%,  rounded by integer primitive = 14), 40 + 14 = 54
 * - we calculate the max score for this recruitment (if all score sliders were set to 100) with method calculateMaxPossibleScore()
 * - max score for the above example would be (100 * 50%) = 50 points + 70% bonus from soft skills -> 85 points
 * - we divide candidate score by max score: 54 / 85 * 100% = 63% (calculateFinalCandidateScorePercent(Candidate candidate) method)
 * The above calculations are correct for SOFT_SKILLS_FACTOR = 0.0
 *
 * Candidates should be added to the List<Candidate> candidateList only through the methids in this class: addCandidates(List<Candidate> newCandidates) or addSingleCandidate(Candidate candidate)
 * Only then each Candidate will have a proper id number!
 */

public class Recruitment implements Serializable {
    private final int SINGLE_STAGE_MAX_SCORE = 100;
    private final double PERCENTAGE = 100.0;
    private final double DECIMAL = 10.0;
    private final double SOFT_SKILLS_FACTOR = 3.0;
    private transient Model model;
    private String name;
    private Presets presets;
    private List<Candidate> candidateList = new ArrayList<>();
    private LocalDateTime dateOfCreation;
    private int maxPossibleScore;
    private int nextCandidateID;
    private boolean isModified = false;
    private boolean isFinished = false;
    private boolean areSoftSkillsIncluded = false;

    public Recruitment(Model model, String name, Presets presets) {
        this.model = model;
        this.name = name;
        this.presets = presets;
        nextCandidateID = candidateList.size() + 1;
        dateOfCreation = LocalDateTime.now();
        maxPossibleScore = calculateMaxPossibleScore();
        Model.logger.info("A new recruitment object has just been created. Max possible score is " + maxPossibleScore);
    }

    private int calculateMaxPossibleScore(){
        Map<Stages, Integer> stagesModifiers = presets.getPresetsValues();
        int maxPossibleScore = 0;

        for (Stages stage : stagesModifiers.keySet()) {
            if (!stage.equals(Stages.SALARY) && !stage.equals(Stages.SOFT_SKILLS)){
                maxPossibleScore += SINGLE_STAGE_MAX_SCORE * getStageModifier(stage);
            }
            if (stage.equals(Stages.SOFT_SKILLS)) areSoftSkillsIncluded = true;
        }

        if (areSoftSkillsIncluded) {
            double multiplier = SOFT_SKILLS_FACTOR + getStageModifier(Stages.SOFT_SKILLS);
            maxPossibleScore = (int) (maxPossibleScore * multiplier);
        }
        Model.logger.info("Max possible score for this recruitment: " + maxPossibleScore + " points.");
        return maxPossibleScore;
    }

    private double getStageModifier(Stages stage){
        Map<Stages, Integer> stagesModifiers = presets.getPresetsValues();
        Integer presetsModifier = stagesModifiers.entrySet().stream()
                .filter(entry -> entry.getKey() == stage)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0);

        return presetsModifier / DECIMAL;
    }

    private int calculateCandidateScore(Candidate candidate){
        Map<Stages, Integer> stagesModifiers = presets.getPresetsValues();
        Map<Stages, Integer> candidateResults = candidate.getScores();
        int finalScore = 0;

        Map<Stages, Double> modifiedScores = calculateModifiedScore(stagesModifiers, candidateResults);
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
        Model.logger.info(candidate.getFirstName() + " " + candidate.getLastName() + "'s score for this recruitment: " + finalScore + " points.");

        return finalScore;
    }

    private Map<Stages, Double> calculateModifiedScore(Map<Stages, Integer> stagesModifiers, Map<Stages, Integer> candidateResults){
        Map<Stages, Double> modifiedScores = new HashMap<>();

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

    private double getSoftSkillsModifier(Candidate candidate){
        Map<Stages, Integer> stagesModifiers = presets.getPresetsValues();
        Integer presetsModifier = stagesModifiers.entrySet().stream()
                .filter(entry -> entry.getKey() == Stages.SOFT_SKILLS)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0);

        Map<Stages, Integer> candidateResults = candidate.getScores();
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
        Model.logger.info(candidate.getFirstName() + " " + candidate.getLastName() + "'s score for this recruitment: " + percentScore + " percent.");

        return percentScore;
    }
    public int calculateCostValueRatio(Candidate candidate){
        Map<Stages, Integer> candidateResults = candidate.getScores();
        Integer salarySliderValue = candidateResults.entrySet().stream()
                .filter(entry -> entry.getKey() == Stages.SALARY)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0);

        double scorePercent = calculateFinalCandidateScorePercent(candidate);
        double salaryModifier = salarySliderValue / PERCENTAGE;

        int costValueRatio = (int) (scorePercent / salaryModifier);
        Model.logger.info(candidate.getFirstName() + " " + candidate.getLastName() + "'s cost/value ratio for this recruitment: " + costValueRatio + " percent.");

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

    public List<String> getStagesForEvaluation(){
        List<String> stagesForEvaluation = new ArrayList<>();
        Map<Stages,Integer> presetsValues = presets.getPresetsValues();

        for (Map.Entry<Stages, Integer> entry : presetsValues.entrySet()) {
            if (entry.getValue() > 0) stagesForEvaluation.add(entry.getKey().getStageName());
        }

        return stagesForEvaluation;
    }

    public String getName() { return name; }
    public Presets getPresets() { return presets; }
    public Model getModel() { return model; }
    public boolean isFinished() { return isFinished; }
    public void setFinished(boolean finished) { isFinished = finished; }
    public List<Candidate> getCandidateList() { return candidateList; }
    public LocalDateTime getDateOfCreation() { return dateOfCreation; }
    public boolean isModified() { return isModified; }
    public void setModified(boolean modified) { isModified = modified; }
    public void setModel(Model model) { this.model = model; }

    @Override
    public String toString() {
        String startDate = dateOfCreation.format(DateTimeFormatter.ISO_DATE);
        String status = isFinished() ? "finished" : "unfinished";

        return String.format("%s   (%d candidates)   STARTED: %s   STATUS: %s   PRESETS: %s", name, candidateList.size(), startDate, status, presets.toString());
    }
}
