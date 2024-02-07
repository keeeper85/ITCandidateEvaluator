package view.swing.stages;

import view.swing.View;
import view.swing.ViewConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Sidepanel extends JPanel {
    private List<Collectable> stagesToComplete = new ArrayList<>();
    private Collectable currentStage = null;
    private JButton nextQuestionButton;
    private JButton finishButton;
    private JButton continueButton;
    private TimerPanel timerPanel;
    private final int BUTTON_POSITION_X = 50;
    private final int BUTTON_POSITION_Y = 150;
    private final int SPACING = 90;
    private final int BUTTON_WIDTH = 200;
    private final int BUTTON_HEIGHT = 40;
    private final int TEXT_FIELD_CHAR_LIMIT = 20;
    private final int NOTES_FIELD_CHAR_LIMIT = 200;
    private boolean isTheLastStep = false;
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
        add(createNextQuestionButton());
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
                if (areFieldInputsCorrect()){
                    JPanel nextStage = getNextStage();
                    if (nextStage != null) {
                        currentStage = (Collectable) nextStage;
                        stageView.setCurrentStagePanel(nextStage);
                    }
                    updateButtons();
                    System.out.println(timerPanel.getSecondsElapsed());
                }
                else{
                    JOptionPane.showMessageDialog(null, ViewConstants.INPUT_ERROR_MESSAGE, "Text input error", JOptionPane.WARNING_MESSAGE);
                }

            }
        });
        buttons.add(continueButton);
        return continueButton;
    }

    private boolean areFieldInputsCorrect() {
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

        return stages.get(currentStageIndex + 1);
    }
    private JButton createBackButton(){
        JButton backButton = new JButton("Back");
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

        return previousStage;
    }

    private JButton createSaveExitButton(){
        JButton saveExitButton = new JButton("Save & Exit");
        saveExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<String, String> collectedData = currentStage.collectData();

                for (Map.Entry<String, String> stringStringEntry : collectedData.entrySet()) {
                    System.out.println(stringStringEntry);
                }
                System.exit(0);
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

    private JButton createNextQuestionButton(){
        nextQuestionButton = new JButton("Next Question");
        nextQuestionButton.setVisible(false);

        buttons.add(nextQuestionButton);
        return nextQuestionButton;
    }

    private JButton createFinishButton(){
        finishButton = new JButton("Finish");
        finishButton.setVisible(false);

        buttons.add(finishButton);
        return finishButton;
    }

    private void updateButtons(){
        if (isQuestionStage) nextQuestionButton.setVisible(true);
        else nextQuestionButton.setVisible(false);
        if (isTheLastStep) {
            finishButton.setVisible(true);
            continueButton.setVisible(false);
        }
        else {
            finishButton.setVisible(false);
            continueButton.setVisible(true);
        }

        repaint();
        revalidate();
    }

    private void addTimer(){
        JLabel evaluationTime = new JLabel("Total evaluation time:");
        evaluationTime.setBounds(85, 5, BUTTON_WIDTH, 20);
        add(evaluationTime);

        timerPanel = new TimerPanel();
        timerPanel.setBounds(BUTTON_POSITION_X, 25, BUTTON_WIDTH, BUTTON_HEIGHT);
        add(timerPanel);
    }

    public void setTheLastStep(boolean theLastStep) {
        isTheLastStep = theLastStep;
    }

    public void setQuestionStage(boolean questionStage) {
        isQuestionStage = questionStage;
    }

    public void setFirstStage(Collectable firstStage) {
        this.currentStage = firstStage;
        stagesToComplete.add(0,firstStage);
    }

    public void addStages(Collectable collectable){
        stagesToComplete.add(collectable);
    }
}
