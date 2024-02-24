package model;

import model.storage.StorageStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Model is the second class created (after initial ITCandidateEvaluator) and works as a hub for getters needed in view package.
 * Model also sets the chosen StorageStrategy and is responsible for getting recruitment list and updating it.
 * Connection between Model and View is based upon the Observer pattern.
 * Inner class "RecruitmentMonitor" (thread) every 0.1 second checks the recruitment list for modification - if any are found, View gets an update.
 * Model variable in Recruitment class is set to 'transient' and is not being serialized - each time recruitment objects get deserialized, current model is being assigned to them.
 * Logger and StorageStrategy is set to transient too - there's no need for them to be saved.
 */

public class Model extends Observable {

    public static final Logger logger = LogManager.getLogger(Model.class);
    private transient StorageStrategy storageStrategy;
    private List<Recruitment> openRecruitmentProcesses;
    private transient QuestionFactory questionFactory;

    public Model(StorageStrategy storageStrategy) {
        logger.info("Model created. App is starting...");
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
        Model.logger.info("Recruitment list loaded successfully.");
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
            if (!updateRecruitmentList()) {
                Model.logger.warn("Returned null recruitment. Problem with updating the list.");
                return null;
            }

            return recruitment;
        }
        return null;
    }

    private boolean canThisNameBeUsed(String name) {
        for (Recruitment openRecruitmentProcess : openRecruitmentProcesses) {
            String takenName = openRecruitmentProcess.getName();
            if (name.equals(takenName)) {
                Model.logger.warn("The selected name: " + takenName + " is already taken.");
                return false;
            }
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
            Model.logger.info("RecruitmentMonitor thread started work.");
            while (true){
                int currentNumberOfRecruitments = openRecruitmentProcesses.size();
                if (currentNumberOfRecruitments != numberOfRecruitments){
                    Model.logger.info("RecruitmentMonitor detected change in recruitments number.");
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
