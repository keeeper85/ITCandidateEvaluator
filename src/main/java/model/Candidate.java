package model;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Candidate {

    private Recruitment recruitment;
    private String firstName;
    private String lastName;
    private int id;
    private HashMap<Stages, Integer> scores;
    private HashMap<Question, Integer> evaluatedQuestions = new HashMap<>();
    private String pathToResumeFile;
    private String additionalNotes;
    private int evaluationTimeSeconds;
    private int evaluationScore;
    private int yearOfBirth;
    private boolean isFinished;
    private LocalDateTime dateOfJoining;
    private String age;

    public Candidate(Recruitment recruitment, String firstName, String lastName) {
        this.recruitment = recruitment;
        this.firstName = firstName;
        this.lastName = lastName;
        id = recruitment.getCandidateList().size();
        isFinished = false;
        dateOfJoining = LocalDateTime.now();
        age = calculateAge();

        recruitment.getCandidateList().add(this);
    }

    public void setScores(HashMap<Stages, Integer> scores) {
        this.scores = scores;
    }

    public HashMap<Stages, Integer> getScores() {
        return scores;
    }

    public String calculateAge(){
        if (yearOfBirth <= 0) return "N/A";
        int age = LocalDateTime.now().getYear() - yearOfBirth;

        return String.valueOf(age);
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public HashMap<Question, Integer> getEvaluatedQuestions() {
        return evaluatedQuestions;
    }

    public void setEvaluatedQuestions(HashMap<Question, Integer> evaluatedQuestions) {
        this.evaluatedQuestions = evaluatedQuestions;
    }

    public Recruitment getRecruitment() {
        return recruitment;
    }

    public int getEvaluationScore() {
        return evaluationScore;
    }

    public void setEvaluationScore(int evaluationScore) {
        this.evaluationScore = evaluationScore;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public String generateFeedback(){
        isFinished = true;
        evaluationScore = recruitment.calculateFinalCandidateScorePercent(this);
        return Feedback.generateFeedback(this);
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id=" + id +
                '}';
    }
}
