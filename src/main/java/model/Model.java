package model;

import java.util.*;

public class Model extends Observable {

    private List<Recruitment> openRecruitmentProcesses; //todo SQL

    public Model() {
        loadQuestionsFromFiles();
        createTestingRecruitments();
    }

    private void createTestingRecruitments(){ //todo create semi-real testing recruitment
        openRecruitmentProcesses = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String name = "Rec_" + i;
            Recruitment recruitment = new Recruitment(this,name, Presets.createRandomPresetsForTesting());
            if (i == 0) {
                recruitment.addCandidates(CandidateFactory.getCandidatesFromResumes("src/main/resources/resumeTest", recruitment));
                recruitment.addSingleCandidate(new Candidate(recruitment, "Ben", "Filler"));
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

    public HashMap<String,HashMap<String, Integer>> getListOfPresets(){
        HashMap<String,HashMap<String, Integer>> listOfPresets = new HashMap<>();

        HashSet<Presets> presets = Presets.loadPresetsFromDirectory();
        for (Presets preset : presets) {
            HashMap<Stages, Integer> presetsValues = preset.getPresetsValues();
            HashMap<String, Integer> singlePreset = new HashMap<>();
            for (Map.Entry<Stages, Integer> entry : presetsValues.entrySet()) {
                String stageName = entry.getKey().getStageName();
                Integer presetValue = entry.getValue();
                singlePreset.put(stageName,presetValue);
            }
            String presetName = preset.getName();
            listOfPresets.put(presetName, singlePreset);
        }

        return listOfPresets;
    }

    public boolean savePresetsToFile(String fileName, HashMap<String, Integer> newDefaultPresets){
        return Presets.saveNewPresetsToFile(fileName, newDefaultPresets);
    }

    public Recruitment startNewRecruitment(String recruitmentName, String presetsName, HashMap<String, Integer> modifiersValues){
        Presets presets = new Presets(presetsName, modifiersValues);
        return new Recruitment(this, recruitmentName, presets);
    }
}
