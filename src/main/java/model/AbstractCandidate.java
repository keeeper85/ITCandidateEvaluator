package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractCandidate {
    protected final Recruitment recruitment;
    protected final String firstName;
    protected final String lastName;
    protected int id;
    protected HashMap<Stages, Integer> scores = new HashMap<>();
    protected HashMap<Question, Integer> evaluatedQuestions = new HashMap<>();
    protected List<String> questionsEvaluatedForDisplay = new ArrayList<>();
    protected String pathToResumeFile;
    protected String additionalNotes = "";
    protected int evaluationTimeSeconds;
    protected int evaluationScore;
    protected int valueCostRatio;
    protected int yearOfBirth;
    protected int expectedSalary = 7500;
    protected int minOfferedSalary = 5000;
    protected int maxOfferedSalary = 10000;
    protected boolean isFinished;
    protected LocalDateTime dateOfJoiningEvaluation;
    protected LocalDateTime dateOfFinishingEvaluation;

    public AbstractCandidate(Recruitment recruitment, String firstName, String lastName) {
        this.recruitment = recruitment;
        this.firstName = firstName;
        this.lastName = lastName;
        isFinished = false;
        dateOfJoiningEvaluation = LocalDateTime.now();
    }

    public HashMap<Stages, Integer> getScores() {
        return scores;
    }

    public void translateAndAddRawScores(HashMap<String, Integer> rawScores){
        for (Map.Entry<String, Integer> rawScore : rawScores.entrySet()) {
            String stageRawName = rawScore.getKey();
            int sliderValue = rawScore.getValue();
            for (Stages stage : Stages.values()) {
                String stageName = stage.getStageName().toLowerCase();
                if (stageName.contains(stageRawName)){
                    scores.put(stage, sliderValue);
                }
            }
        }
    }

    public void setPathToResumeFile(String pathToResumeFile) {
        this.pathToResumeFile = pathToResumeFile;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public void setEvaluationTimeSeconds(int evaluationTimeSeconds) {
        this.evaluationTimeSeconds = evaluationTimeSeconds;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public void setDateOfJoiningEvaluation(LocalDateTime dateOfJoiningEvaluation) {
        this.dateOfJoiningEvaluation = dateOfJoiningEvaluation;
    }

    public void setDateOfFinishingEvaluation(LocalDateTime dateOfFinishingEvaluation) {
        this.dateOfFinishingEvaluation = dateOfFinishingEvaluation;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPathToResumeFile() {
        return pathToResumeFile;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setExpectedSalary(int expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public void setMinOfferedSalary(int minOfferedSalary) {
        this.minOfferedSalary = minOfferedSalary;
    }

    public void setMaxOfferedSalary(int maxOfferedSalary) {
        this.maxOfferedSalary = maxOfferedSalary;
    }

    public int getEvaluationTimeSeconds() {
        return evaluationTimeSeconds;
    }

    public int getMinOfferedSalary() {
        return minOfferedSalary;
    }

    public int getMaxOfferedSalary() {
        return maxOfferedSalary;
    }

    public LocalDateTime getDateOfJoiningEvaluation() {
        return dateOfJoiningEvaluation;
    }

    public LocalDateTime getDateOfFinishingEvaluation() {
        return dateOfFinishingEvaluation;
    }

    public List<String> getQuestionsEvaluatedForDisplay() {
        return questionsEvaluatedForDisplay;
    }
    public void setQuestionsEvaluatedForDisplay(List<String> questionsEvaluatedForDisplay) {
        this.questionsEvaluatedForDisplay = questionsEvaluatedForDisplay;
    }
}
