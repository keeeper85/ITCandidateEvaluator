package model;

import java.time.LocalDateTime;
import java.util.HashMap;

public class AbstractCandidate {
    protected final Recruitment recruitment;
    protected final String firstName;
    protected final String lastName;
    protected int id;
    protected HashMap<Stages, Integer> scores;
    protected HashMap<Question, Integer> evaluatedQuestions = new HashMap<>();
    protected String pathToResumeFile;
    protected String additionalNotes = "";
    protected int evaluationTimeSeconds;
    protected int evaluationScore;
    protected int yearOfBirth;
    protected int expectedSalary;
    protected boolean isFinished;
    protected LocalDateTime dateOfJoiningEvaluation;
    protected LocalDateTime dateOfFinishingEvaluation;
    protected String age;

    public AbstractCandidate(Recruitment recruitment, String firstName, String lastName) {
        this.recruitment = recruitment;
        this.firstName = firstName;
        this.lastName = lastName;
        id = recruitment.getCandidateList().size();
        isFinished = false;
        dateOfJoiningEvaluation = LocalDateTime.now();
    }

    public void setScores(HashMap<Stages, Integer> scores) {
        this.scores = scores;
    }

    public void setEvaluatedQuestions(HashMap<Question, Integer> evaluatedQuestions) {
        this.evaluatedQuestions = evaluatedQuestions;
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
}
