package model;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Candidate extends AbstractCandidate{
    private String age;

    public Candidate(Recruitment recruitment, String firstName, String lastName) {
        super(recruitment, firstName, lastName);
        age = calculateAge();
        recruitment.getCandidateList().add(this);
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
