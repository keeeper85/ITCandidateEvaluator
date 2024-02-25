package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AbstractCandidate holds all the values every candidate should possess.
 * Id is set in the Recruitment class. Candidates belonging to different recruitments can have same id numbers.
 * Method translateAndAddRawScores(Map<String, Integer> rawScores) takes raw Strings with stages names and changes them to appropriate Stages objects.
 * Salary variables are set do default values - if you change them, modify CandidateDTO as well.
 */

public class AbstractCandidate implements Serializable {

    protected final Recruitment recruitment;
    protected Map<Stages, Integer> scores = new HashMap<>();
    protected Map<Question, Integer> evaluatedQuestions = new HashMap<>();
    protected List<String> questionsEvaluatedForDisplay = new ArrayList<>();
    protected String firstName;
    protected String lastName;
    protected String pathToResumeFile = "";
    protected String additionalNotes = "";
    protected String age;
    protected int id;
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
        Model.logger.info("A new Candidate object has just been created.");
    }

    public String calculateAge(){
        if (yearOfBirth <= 0) return "N/A";
        int age = LocalDateTime.now().getYear() - yearOfBirth;

        return String.valueOf(age);
    }

    public void translateAndAddRawScores(Map<String, Integer> rawScores){
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
    public Recruitment getRecruitment() { return recruitment; }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Map<Stages, Integer> getScores() {
        return scores;
    }
    public List<String> getQuestionsEvaluatedForDisplay() {
        return questionsEvaluatedForDisplay;
    }
    public void setQuestionsEvaluatedForDisplay(List<String> questionsEvaluatedForDisplay) {
        this.questionsEvaluatedForDisplay = questionsEvaluatedForDisplay;
    }
    public int getYearOfBirth() {
        return yearOfBirth;
    }
    public void setYearOfBirth(int yearOfBirth) { this.yearOfBirth = yearOfBirth; }
    public String getPathToResumeFile() {
        return pathToResumeFile;
    }
    public void setPathToResumeFile(String pathToResumeFile) {
        this.pathToResumeFile = pathToResumeFile;
    }
    public String getAdditionalNotes() {
        return additionalNotes;
    }
    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }
    public Integer getID() { return id; }
    public void setId(int id) {
        this.id = id;
    }
    public int getEvaluationTimeSeconds() {
        return evaluationTimeSeconds;
    }
    public void setEvaluationTimeSeconds(int evaluationTimeSeconds) { this.evaluationTimeSeconds = evaluationTimeSeconds; }
    public Integer getEvaluationScore() { return evaluationScore; }
    public void setEvaluationScore(int evaluationScore) { this.evaluationScore = evaluationScore; }
    public int getExpectedSalary() { return expectedSalary; }
    public void setExpectedSalary(int expectedSalary) {
        this.expectedSalary = expectedSalary;
    }
    public int getMinOfferedSalary() {
        return minOfferedSalary;
    }
    public void setMinOfferedSalary(int minOfferedSalary) {
        this.minOfferedSalary = minOfferedSalary;
    }
    public int getMaxOfferedSalary() {
        return maxOfferedSalary;
    }
    public void setMaxOfferedSalary(int maxOfferedSalary) {
        this.maxOfferedSalary = maxOfferedSalary;
    }
    public boolean isFinished() { return isFinished; }
    public void setFinished(boolean finished) {
        isFinished = finished;
        if (isFinished) {
            Model.logger.info("Candidate has finished the evaluation.");
            dateOfFinishingEvaluation = LocalDateTime.now();
        }
    }
    public LocalDateTime getDateOfJoiningEvaluation() {
        return dateOfJoiningEvaluation;
    }
    public Map<Question, Integer> getEvaluatedQuestions() { return evaluatedQuestions; }
    public Integer getValueCostRatio() { return valueCostRatio; }
}
