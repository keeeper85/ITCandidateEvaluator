package controller;

import model.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CandidateDTO {
    private HashMap<String, Integer> rawScores = new HashMap<>();
    private HashMap<Question, Integer> evaluatedQuestions = new HashMap<>();
    private Candidate candidate;
    private Recruitment recruitment;
    private String firstName = "";
    private String lastName = "";
    private int yearOfBirth = 0;
    private String pathToResumeFile = "";
    private String notes = "";
    private int minOfferedSalary;
    private int maxOfferedSalary;
    private int expectedSalary;
    private int evaluationTimeSeconds;

    public CandidateDTO(Candidate candidate, Recruitment recruitment) {
        this.candidate = candidate;
        this.recruitment = recruitment;
        copyDataFromRealCandidate(candidate);
    }

    private void copyDataFromRealCandidate(Candidate candidate) {
        if (candidate != null){
            firstName = candidate.getFirstName();
            lastName = candidate.getLastName();
            yearOfBirth = candidate.getYearOfBirth();
            pathToResumeFile = candidate.getPathToResumeFile();
            notes = candidate.getAdditionalNotes();
        }
    }

    public void saveData(){
        if (candidate != null) transferDataToRealCandidate(candidate);
        else createNewCandidate(false);

    }

    public void saveDataAndCompleteEvaluation(){
        if (candidate != null) transferDataToRealCandidate(candidate);
        else createNewCandidate(true);
    }

    private void createNewCandidate(boolean isFinished) {
        candidate = new Candidate(recruitment,firstName,lastName);
        recruitment.addSingleCandidate(candidate);
        transferDataToRealCandidate(candidate);
        if (isFinished) candidate.setFinished(true);
    }

    private void transferDataToRealCandidate(Candidate candidate) {
        candidate.setYearOfBirth(yearOfBirth);
        candidate.setPathToResumeFile(pathToResumeFile);
        candidate.setAdditionalNotes(notes);
        candidate.setEvaluationTimeSeconds(evaluationTimeSeconds);
        candidate.setDateOfFinishingEvaluation(LocalDateTime.now());
        candidate.getEvaluatedQuestions().putAll(evaluatedQuestions);
        candidate.setMaxOfferedSalary(maxOfferedSalary);
        candidate.setMinOfferedSalary(minOfferedSalary);
        candidate.setExpectedSalary(expectedSalary);
        candidate.translateAndAddRawScores(rawScores);
    }

    public int calculateQuestionsAverageScore(){
        int numberOfQuestions = evaluatedQuestions.size();
        int totalScore = 0;

        for (Map.Entry<Question, Integer> questions : evaluatedQuestions.entrySet()) {
            int currentQuestionScore = questions.getValue();
            totalScore += currentQuestionScore;
        }
        return totalScore / numberOfQuestions;
    }

    public HashMap<String, Integer> getRawScores() {
        return rawScores;
    }

    public HashMap<Question, Integer> getEvaluatedQuestions() {
        return evaluatedQuestions;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public String getPathToResumeFile() {
        return pathToResumeFile;
    }

    public String getNotes() {
        return notes;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public void setPathToResumeFile(String pathToResumeFile) {
        this.pathToResumeFile = pathToResumeFile;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setMinOfferedSalary(int minOfferedSalary) {
        this.minOfferedSalary = minOfferedSalary;
    }

    public void setMaxOfferedSalary(int maxOfferedSalary) {
        this.maxOfferedSalary = maxOfferedSalary;
    }

    public void setExpectedSalary(int expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public void setEvaluationTimeSeconds(int evaluationTimeSeconds) {
        this.evaluationTimeSeconds = evaluationTimeSeconds;
    }
}
