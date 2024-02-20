package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Candidate extends AbstractCandidate{
    private String age;

    public Candidate(Recruitment recruitment, String firstName, String lastName) {
        super(recruitment, firstName, lastName);
//        age = calculateAge();
        age = String.valueOf(ThreadLocalRandom.current().nextInt(20,40));
    }

    public HashMap<Stages, Integer> getScores() {
        return scores;
    }

    public String calculateAge(){
        if (yearOfBirth <= 0) return "N/A";
        int age = LocalDateTime.now().getYear() - yearOfBirth;

        return String.valueOf(age);
    }

    public HashMap<Question, Integer> getEvaluatedQuestions() {
        return evaluatedQuestions;
    }

    public Recruitment getRecruitment() {
        return recruitment;
    }

    public Integer getEvaluationScore() {
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

    public int getExpectedSalary(){
        return expectedSalary;
    }

    public String getAdditionalNotes(){
        return additionalNotes;
    }

    public String getResumePath(){
        return pathToResumeFile;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public int getYearOfBirth(){
        return yearOfBirth;
    }
    public Integer getID(){ return id; }
    public Integer getValueCostRatio(){ return valueCostRatio; }

    @Override
    public String toString() {
        String joinDate = dateOfJoiningEvaluation.format(DateTimeFormatter.ISO_DATE);
        String score = isFinished ? String.valueOf(recruitment.calculateFinalCandidateScorePercent(this)) : "unfinished";
        String description = String.format("%d. %s %s (%s)   JOINED: %s   SCORE: %s", id, lastName, firstName, age, joinDate, score);
        return description;
    }
}
