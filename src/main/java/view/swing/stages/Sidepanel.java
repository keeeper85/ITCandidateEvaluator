package view.swing.stages;

import controller.CandidateDTO;
import view.swing.RecruitmentsListView;
import view.swing.View;
import view.swing.ViewConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Sidepanel extends JPanel {
    private View view;
    private List<Collectable> stagesToComplete = new ArrayList<>();
    private HashMap<String, String> allStagesScore = new HashMap<>();
    private Collectable currentStage = null;
    private CandidateDTO temporaryCandidate;
    private JButton saveScoreButton;
    private JButton finishButton;
    private JButton continueButton;
    private JButton backButton;
    private TimerPanel timerPanel;
    private final int TIMER_LABEL_POSITION_X = 85;
    private final int TIMER_LABEL_POSITION_Y = 5;
    private final int TIMER_PANEL_POSITION_Y = 25;
    private final int TIMER_LABEL_WIDTH = 200;
    private final int TIMER_LABEL_HEIGHT = 20;
    private final int BUTTON_POSITION_X = 50;
    private final int BUTTON_POSITION_Y = 150;
    private final int SPACING = 90;
    private final int BUTTON_WIDTH = 200;
    private final int BUTTON_HEIGHT = 40;
    private final int TEXT_FIELD_CHAR_LIMIT = 20;
    private final int NOTES_FIELD_CHAR_LIMIT = 200;
    private boolean isTheLastStep = false;
    private boolean isTheFirstStep = true;
    private boolean isQuestionStage = false;
    private boolean areStagesPrepared = false;
    private StageView stageView;
    private List<JButton> buttons = new ArrayList<>();

    public Sidepanel(StageView stageView) {
        this.stageView = stageView;
        view = stageView.getView();
        temporaryCandidate = stageView.getCandidate();

        initSidepanel();
    }

    private void initSidepanel(){
        setLayout(null);

        add(createContinueButton());
        add(createBackButton());
        add(createSaveExitButton());
        add(createDiscardButton());
        add(createSaveScoreButton());
        add(createFinishButton());
        placeButtons();
        addTimer();
    }

    private void placeButtons(){
        int dynamicPositionY = BUTTON_POSITION_Y;

        for (JButton button : buttons) {
            button.setBounds(BUTTON_POSITION_X, dynamicPositionY, BUTTON_WIDTH, BUTTON_HEIGHT);
            button.setFont(ViewConstants.FONT_LARGE);
            dynamicPositionY += SPACING;
        }
        revalidate();
        repaint();
    }

    private JButton createContinueButton(){
        continueButton = new JButton("Continue");
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStage = (Collectable) stageView.getCurrentStagePanel();
                if (areFieldInputsCorrect()){
                    HashMap<String, String> collectedData = currentStage.collectData();
                    allStagesScore.putAll(collectedData);
                    for (Map.Entry<String, String> stringStringEntry : collectedData.entrySet()) {
                        System.out.println(stringStringEntry);
                    }

                    Collectable nextStage = getNextStage();
                    if (nextStage != null) {
                        currentStage = nextStage;
                        stageView.setCurrentStagePanel(nextStage);
                    }
                    updateButtons();
                }
                else{
                    if (currentStage instanceof QuestionsStagePanel) JOptionPane.showMessageDialog(null, ViewConstants.QUESTION_INPUT_ERROR_MESSAGE, "No questions asked error", JOptionPane.WARNING_MESSAGE);
                    else JOptionPane.showMessageDialog(null, ViewConstants.INPUT_ERROR_MESSAGE, "Text input error", JOptionPane.WARNING_MESSAGE);
                }

            }
        });
        buttons.add(continueButton);
        return continueButton;
    }

    private boolean areFieldInputsCorrect() {
        //comment for testing
        currentStage = stageView.getCurrentStagePanel();
        if (currentStage instanceof CandidateView){
            System.out.println("test");
            CandidateView candidateView = (CandidateView) currentStage;
            HashMap<String, String> candidateData = candidateView.collectData();
            for (Map.Entry<String, String> entry : candidateData.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.contains("Name")) {
                    if (value == null || value.length() == 0 || value.length() > TEXT_FIELD_CHAR_LIMIT) return false;
                }
                else if (key.contains("nationality")){
                    if (value.length() > TEXT_FIELD_CHAR_LIMIT) return false;
                }
                else if (key.contains("notes")){
                    if (value.length() > NOTES_FIELD_CHAR_LIMIT) return false;
                }
                else if (key.contains("year")){
                    if (value.length() == 0) return true;
                    try{
                        int yearOfBirth = Integer.parseInt(value);
                        int tooOld = 1900;
                        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                        if (yearOfBirth < tooOld || yearOfBirth >= currentYear) return false;
                    } catch (NumberFormatException e){
                        return false;
                    }
                }
            }
        }
        if (currentStage instanceof QuestionsStagePanel){
            QuestionsStagePanel questionsStage = (QuestionsStagePanel) currentStage;
            return questionsStage.isQuestionAsked();
        }

        return true;
    }

    private Collectable getNextStage(){
        List<Collectable> stages = stageView.getChosenStages();

        Collectable currentStage = stageView.getCurrentStagePanel();
        int currentStageIndex = stages.indexOf(currentStage);
        int nextStageIndex = currentStageIndex + 1;

        if (nextStageIndex >= stages.size()) {
            isTheLastStep = true;
            return null;
        }
        isTheLastStep = false;
        Collectable nextStage = stages.get(nextStageIndex);
        if (nextStage instanceof QuestionsStagePanel) {
            isQuestionStage = true;
        }
        else isQuestionStage = false;
        isTheFirstStep = false;

        return stages.get(nextStageIndex);
    }
    private JButton createBackButton(){
        backButton = new JButton("Back");
        backButton.setEnabled(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Collectable previousStage = getPreviousStage();
                if (previousStage != null) {
                    currentStage = previousStage;
                    stageView.setCurrentStagePanel(previousStage);
                }
                updateButtons();
            }
        });

        buttons.add(backButton);
        return backButton;
    }
    private Collectable getPreviousStage(){
        List<Collectable> stages = stageView.getChosenStages();
        Collectable currentStage = stageView.getCurrentStagePanel();

        int currentStageIndex = stages.indexOf(currentStage);
        int previousStageIndex = currentStageIndex - 1;

        if (previousStageIndex < 0) {
            return null;
        }
        isTheLastStep = false;

        Collectable previousStage = stages.get(previousStageIndex);
        if (previousStage instanceof QuestionsStagePanel) {
            isQuestionStage = true;
        }
        else isQuestionStage = false;

        if (previousStage instanceof CandidateView) isTheFirstStep = true;

        return previousStage;
    }

    private JButton createSaveExitButton(){
        JButton saveExitButton = new JButton("Save & Exit");
        saveExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                temporaryCandidate.saveData();
                currentStage = (Collectable) stageView.getCurrentStagePanel();
                HashMap<String, String> collectedData = currentStage.collectData();
                String timePassed = String.valueOf(timerPanel.getSecondsElapsed());
                collectedData.put("evaluationTimeSeconds", timePassed);

                View view = stageView.getView();
                view.setCurrentPanel(new RecruitmentsListView(view));
            }
        });

        buttons.add(saveExitButton);
        return saveExitButton;
    }

    private JButton createDiscardButton(){
        JButton discardButton = new JButton("Discard");
        discardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String warning = "Do you want to cancel this evaluation?\nThis operation can not be undone!\nType 'discard' to undo all the changes you've made.";
                String userInput = (String) JOptionPane.showInputDialog(null, warning, "Confirm:", JOptionPane.WARNING_MESSAGE);
                if (userInput.equals("discard")) {
                    view.setCurrentPanel(new RecruitmentsListView(view));
                } else {
                    JOptionPane.showMessageDialog(null, "Deletion canceled or invalid input.");
                }
            }
        });

        buttons.add(discardButton);
        return discardButton;
    }

    private JButton createSaveScoreButton(){
        saveScoreButton = new JButton("Save Score");
        saveScoreButton.setVisible(false);
        saveScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStage = (Collectable) stageView.getCurrentStagePanel();
                if (currentStage instanceof QuestionsStagePanel){
                    QuestionsStagePanel questionStage = ((QuestionsStagePanel) currentStage);
                    String title = "You've asked " + questionStage.getNumberOfQuestionsEvaluated() + " questions so far.";
                    int choice = JOptionPane.showConfirmDialog(null, "Do you want to save this score? (this operation can not be undone)", title, JOptionPane.YES_NO_OPTION);
                    if (choice == 0) questionStage.updateQuestionsEvaluated();
                }
            }
        });

        buttons.add(saveScoreButton);
        return saveScoreButton;
    }

    private JButton createFinishButton(){
        finishButton = new JButton("Finish");
        finishButton.setVisible(false);
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Do you want to finish the evaluation for the current candidate? (you won't be able to edit the scores later)", "Is everything ready?", JOptionPane.YES_NO_OPTION);
                if (choice == 0){
                    temporaryCandidate.setEvaluationTimeSeconds(timerPanel.getSecondsElapsed());
                    temporaryCandidate.saveDataAndCompleteEvaluation();
                    view.setCurrentPanel(new RecruitmentsListView(view));
                    view.resetPreviousPanels(); //todo refresh recruitment and candidate list view models
                }
            }
        });

        buttons.add(finishButton);
        return finishButton;
    }

    private void updateButtons(){
        if (isQuestionStage) saveScoreButton.setVisible(true);
        else saveScoreButton.setVisible(false);
        if (isTheLastStep) {
            finishButton.setVisible(true);
            continueButton.setVisible(false);
        }
        else {
            finishButton.setVisible(false);
            continueButton.setVisible(true);
        }
        if (isTheFirstStep){
            backButton.setEnabled(false);
        }
        else backButton.setEnabled(true);

        repaint();
        revalidate();
    }

    private void addTimer(){
        JLabel evaluationTime = new JLabel("Total evaluation time:");
        evaluationTime.setBounds(TIMER_LABEL_POSITION_X, TIMER_LABEL_POSITION_Y, TIMER_LABEL_WIDTH, TIMER_LABEL_HEIGHT);
        add(evaluationTime);

        timerPanel = new TimerPanel();
        timerPanel.setBounds(BUTTON_POSITION_X, TIMER_PANEL_POSITION_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        add(timerPanel);
    }

    public void addStages(Collectable collectable){
        stagesToComplete.add(collectable);
    }
}
