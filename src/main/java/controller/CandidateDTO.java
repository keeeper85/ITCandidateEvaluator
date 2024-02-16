package controller;

import model.AbstractCandidate;
import model.Candidate;
import model.Recruitment;
import model.Stages;

import java.util.HashMap;

public class CandidateDTO extends AbstractCandidate {
    private HashMap<String, String> rawScores = new HashMap<>();
    public CandidateDTO(Recruitment recruitment, String firstName, String lastName) {
        super(recruitment, firstName, lastName);
    }

    public void saveData(Candidate candidate){
        if (isCandidateAlreadyCreated()) transferDataToRealCandidate(candidate);
        else createNewCandidate(false);

    }

    public void saveDataAndFinish(Candidate candidate){
        if (isCandidateAlreadyCreated()) transferDataToRealCandidate(candidate);
        else createNewCandidate(true);
    }

    private void createNewCandidate(boolean isFinished) {

    }

    private void transferDataToRealCandidate(Candidate candidate) {


    }

    private boolean isCandidateAlreadyCreated(){

        return false;
    }

    public void setRawScores(HashMap<String, String> rawScores) {
        this.rawScores = rawScores;
    }
}