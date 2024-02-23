package model;

import model.storage.StorageStrategy;

import java.io.Serializable;
import java.util.*;

public class Model extends Observable {
    private StorageStrategy storageStrategy;
    private List<Recruitment> openRecruitmentProcesses; //todo SQL
    private QuestionFactory questionFactory;

    public Model(StorageStrategy storageStrategy) {
        this.storageStrategy = storageStrategy;
        questionFactory = new QuestionFactory();
        loadQuestionsFromFiles(questionFactory);
        loadRecruitmentList();
        startMonitoringRecruitments();
    }

    private void loadRecruitmentList(){
        openRecruitmentProcesses = storageStrategy.getRecruitmentList(this);
        for (Recruitment openRecruitmentProcess : openRecruitmentProcesses) {
            openRecruitmentProcess.setModified(false);
            openRecruitmentProcess.setModel(this);
        }
    }

    public synchronized boolean updateRecruitmentList(){
        return storageStrategy.updateRecruitmentList(openRecruitmentProcesses, this);
    }

    private void loadQuestionsFromFiles(QuestionFactory questionFactory){
        Thread questionLoader = new Thread(questionFactory);
        questionLoader.start();
    }
    private void startMonitoringRecruitments(){
        Thread recruitmentMonitor = new Thread(new RecruitmentMonitor(this));
        recruitmentMonitor.setDaemon(true);
        recruitmentMonitor.start();
    }

    public Recruitment startNewRecruitment(String recruitmentName, String presetsName, HashMap<String, Integer> modifiersValues){
        Presets presets = new Presets(presetsName, modifiersValues);
        if (canThisNameBeUsed(recruitmentName)){
            Recruitment recruitment = new Recruitment(this, recruitmentName, presets);
            openRecruitmentProcesses.add(recruitment);
            if (!updateRecruitmentList()) return null;

            return recruitment;
        }
        return null;
    }

    private boolean canThisNameBeUsed(String name) {
        for (Recruitment openRecruitmentProcess : openRecruitmentProcesses) {
            String takenName = openRecruitmentProcess.getName();
            if (name.equals(takenName)) return false;
        }
        return true;
    }

    public boolean deleteExistingRecruitment(Recruitment recruitment){
        openRecruitmentProcesses.remove(recruitment);

        return updateRecruitmentList();
    }

    public List<Recruitment> getOpenRecruitmentProcesses() {
        return openRecruitmentProcesses;
    }

    public Map<String,Map<String, Integer>> getListOfPresets(){
        Map<String,Map<String, Integer>> listOfPresets = new HashMap<>();

        Set<Presets> presets = Presets.loadPresetsFromDirectory();
        for (Presets preset : presets) {
            Map<Stages, Integer> presetsValues = preset.getPresetsValues();
            Map<String, Integer> singlePreset = new HashMap<>();
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

    public void closeDatabaseConnection(){
        storageStrategy.closeConnection();
    }

    public boolean savePresetsToFile(String fileName, HashMap<String, Integer> newDefaultPresets){
        return Presets.saveNewPresetsToFile(fileName, newDefaultPresets);
    }
    public List<String> getLiveCodingTasks(){
        return LiveCoding.getLiveCodingTasksList();
    }

    public List<Question> getQuestionList(){
        return questionFactory.getPreparedList();
    }


    private class RecruitmentMonitor implements Runnable{
        Model model;
        int numberOfRecruitments = openRecruitmentProcesses.size();
        public RecruitmentMonitor(Model model) {
            this.model = model;
        }
        @Override
        public void run() {
            while (true){
                int currentNumberOfRecruitments = openRecruitmentProcesses.size();
                if (currentNumberOfRecruitments != numberOfRecruitments){
                    model.updateRecruitmentList();
                    model.setChanged();
                    model.notifyObservers();
                    numberOfRecruitments = currentNumberOfRecruitments;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
