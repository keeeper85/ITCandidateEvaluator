package model;

import java.time.format.DateTimeFormatter;

public class Candidate extends AbstractCandidate{

    public Candidate(Recruitment recruitment, String firstName, String lastName) {
        super(recruitment, firstName, lastName);
    }

    public String generateFeedback(){
        isFinished = true;
        evaluationScore = recruitment.calculateFinalCandidateScorePercent(this);
        return FeedbackHelper.generateFeedback(this);
    }
    @Override
    public String toString() {
        age = calculateAge();
        String joinDate = dateOfJoiningEvaluation.format(DateTimeFormatter.ISO_DATE);
        String score = isFinished ? String.valueOf(recruitment.calculateFinalCandidateScorePercent(this)) : "unfinished";
        String description = String.format("%d. %s %s (%s)   JOINED: %s   SCORE: %s", id, lastName, firstName, age, joinDate, score);
        return description;
    }
}
