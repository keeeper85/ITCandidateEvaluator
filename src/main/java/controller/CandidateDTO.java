package controller;

import model.AbstractCandidate;
import model.Candidate;
import model.Recruitment;
import model.Stages;

import java.util.HashMap;

public class CandidateDTO {
    private HashMap<String, String> rawScores = new HashMap<>();
    private Candidate candidate;
    private Recruitment recruitment;
    private String firstName = "";
    private String lastName = "";
    private int yearOfBirth = 0;
    private String pathToResumeFile = "";
    private String notes = "";

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

    public void saveDataAndFinish(Candidate candidate){
        if (isCandidateAlreadyCreated()) transferDataToRealCandidate(candidate);
        else createNewCandidate(true);
    }

    private void createNewCandidate(boolean isFinished) {
        Candidate newCandidate = new Candidate(recruitment,firstName,lastName);
        recruitment.addSingleCandidate(newCandidate);
    }

    private void transferDataToRealCandidate(Candidate candidate) {


    }

    private boolean isCandidateAlreadyCreated(){

        return false;
    }

    public void setRawScores(HashMap<String, String> rawScores) {
        this.rawScores = rawScores;
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
}
