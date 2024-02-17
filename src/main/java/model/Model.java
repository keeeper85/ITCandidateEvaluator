package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class Model extends Observable {

    private List<Recruitment> openRecruitmentProcesses; //todo SQL

    public Model() {
        loadQuestionsFromFiles();
        createTestingRecruitments();
    }

    private void createTestingRecruitments(){
        openRecruitmentProcesses = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String name = "Rec_" + i;
            Recruitment recruitment = new Recruitment(this,name, Presets.createRandomPresetsForTesting());
            if (i == 0) {
                recruitment.addCandidates(CandidateFactory.getCandidatesFromResumes("src/main/resources/resumeTest", recruitment));
                recruitment.getCandidateList().add(new Candidate(recruitment, "Ben", "Filler"));
            }
            if (i == 1) recruitment.getCandidateList().add(new Candidate(recruitment, "Ben", "Filler"));
            openRecruitmentProcesses.add(recruitment);
        }
    }

    private void loadQuestionsFromFiles(){
        Thread questionLoader = new Thread(new QuestionFactory());
        questionLoader.start();
    }

    public Recruitment createNewRecruitment(String name, Presets presets){
        if (nameIsAvailable(name)){
            Recruitment recruitment = new Recruitment(this, name, presets);
            openRecruitmentProcesses.add(recruitment);

            return recruitment;
        }
        return null;
    }

    private boolean nameIsAvailable(String name) {
        for (Recruitment openRecruitmentProcess : openRecruitmentProcesses) {
            String takenName = openRecruitmentProcess.getName();
            if (name.equals(takenName)) return false;
        }
        return true;
    }

    public Recruitment openExistingRecruitment(String name){
        for (Recruitment openRecruitmentProcess : openRecruitmentProcesses) {
            String openRecruitmentProcessName = openRecruitmentProcess.getName();
            if (name.equals(openRecruitmentProcessName)) return openRecruitmentProcess;
        }
        return null;
    }

    public boolean deleteExistingRecruitment(Recruitment recruitment){
        return openRecruitmentProcesses.remove(recruitment);
    }

    public List<Recruitment> getOpenRecruitmentProcesses() {
        return openRecruitmentProcesses;
    }
}
