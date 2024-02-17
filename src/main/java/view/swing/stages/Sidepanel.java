package view.swing.stages;

import view.swing.View;
import view.swing.ViewConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Sidepanel extends JPanel {
    private List<Collectable> stagesToComplete = new ArrayList<>();
    private HashMap<String, String> allStagesScore = new HashMap<>();
    private Collectable currentStage = null;
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

                    JPanel nextStage = getNextStage();
                    if (nextStage != null) {
                        currentStage = (Collectable) nextStage;
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
        currentStage = (Collectable) stageView.getCurrentStagePanel();
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

    private JPanel getNextStage(){
        List<JPanel> stages = stageView.getChosenStages();
        JPanel currentStage = stageView.getCurrentStagePanel();
        int currentStageIndex = stages.indexOf(currentStage);
        int nextStageIndex = currentStageIndex + 1;

        if (nextStageIndex >= stages.size()) {
            isTheLastStep = true;
            return null;
        }
        isTheLastStep = false;
        JPanel nextStage = stages.get(nextStageIndex);
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
                JPanel previousStage = getPreviousStage();
                if (previousStage != null) {
                    currentStage = (Collectable) previousStage;
                    stageView.setCurrentStagePanel(previousStage);
                }
                updateButtons();
            }
        });

        buttons.add(backButton);
        return backButton;
    }
    private JPanel getPreviousStage(){
        List<JPanel> stages = stageView.getChosenStages();
        JPanel currentStage = stageView.getCurrentStagePanel();

        int currentStageIndex = stages.indexOf(currentStage);
        int previousStageIndex = currentStageIndex - 1;

        if (previousStageIndex < 0) {
            return null;
        }
        isTheLastStep = false;

        JPanel previousStage = stages.get(previousStageIndex);
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
                currentStage = (Collectable) stageView.getCurrentStagePanel();
                HashMap<String, String> collectedData = currentStage.collectData();
                String timePassed = String.valueOf(timerPanel.getSecondsElapsed());
                collectedData.put("evaluationTimeSeconds", timePassed);

                for (Map.Entry<String, String> stringStringEntry : collectedData.entrySet()) {
                    System.out.println(stringStringEntry);
                }
                stageView.getView().startOver();
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
                    View view = stageView.getView();
                    view.startOver();
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
                System.out.println("-----------------------");
                String timePassed = String.valueOf(timerPanel.getSecondsElapsed());
                allStagesScore.put("evaluationTimeSeconds", timePassed);
                for (Map.Entry<String, String> stringStringEntry : allStagesScore.entrySet()) {
                    System.out.println(stringStringEntry.getKey() + " = " + stringStringEntry.getValue());
                }
                System.exit(0);
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
