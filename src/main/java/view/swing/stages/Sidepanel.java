package view.swing.stages;

import view.swing.View;
import view.swing.ViewConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Sidepanel extends JPanel {
    private List<Collectable> stagesToComplete;
    private Collectable currentStage;
    private final int BUTTON_POSIOTION_X = 50;
    private final int BUTTON_POSIOTION_Y = 50;
    private final int SPACING = 90;
    private final int BUTTON_WIDTH = 200;
    private final int BUTTON_HEIGHT = 40;
    private boolean isTheLastStep = false;
    private boolean isQuestionStage = false;
    private View view;
    private List<JButton> buttons = new ArrayList<>();

    public Sidepanel(View view) {
        this.view = view;
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

        addAllStagesForTesting();
    }

    private void addAllStagesForTesting(){
        addStages(new ResumeView(view));
        addStages(new LanguageView(view));
        addStages(new ExperienceView(view));
        addStages(new ProjectsView(view));
        addStages(new LiveCodingView(view));
        addStages(new QuestionsView(view));
        addStages(new SalaryView(view));
        addStages(new SoftSkillsView(view));
    }

    private void placeButtons(){
        int dynamicPositionY = BUTTON_POSIOTION_Y;

        for (JButton button : buttons) {
            button.setBounds(BUTTON_POSIOTION_X, dynamicPositionY, BUTTON_WIDTH, BUTTON_HEIGHT);
            button.setFont(ViewConstants.FONT_LARGE);
            dynamicPositionY += SPACING;
        }
        revalidate();
        repaint();
    }

    private JButton createContinueButton(){
        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener((e -> {view.setCurrentPanel(new ResumeView(view));}));



        buttons.add(continueButton);
        return continueButton;
    }

    private JButton createBackButton(){
        JButton backButton = new JButton("Back");
        backButton.addActionListener((e -> {view.returnToPreviousPanel();}));

        buttons.add(backButton);
        return backButton;
    }

    private JButton createSaveExitButton(){
        JButton saveExitButton = new JButton("Save & Exit");
        saveExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.exit(0);
                JPanel collectable = view.getCurrentPanel();
                Collectable stage = null;
                if (collectable instanceof Collectable){
                    stage = (Collectable) collectable;
                }
                HashMap<String, String> collectedData = stage.collectData();

                for (Map.Entry<String, String> stringStringEntry : collectedData.entrySet()) {
                    System.out.println(stringStringEntry);
                }
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
        JButton nextQuestionButton = new JButton("Next Question");
        nextQuestionButton.setVisible(isQuestionStage);

        buttons.add(nextQuestionButton);
        return nextQuestionButton;
    }

    private JButton createFinishButton(){
        JButton finishButton = new JButton("Finish");
        finishButton.setVisible(isTheLastStep);

        buttons.add(finishButton);
        return finishButton;
    }

    public void setTheLastStep(boolean theLastStep) {
        isTheLastStep = theLastStep;
    }

    public void setQuestionStage(boolean questionStage) {
        isQuestionStage = questionStage;
    }

    public void setCurrentStage(Collectable currentStage) {
        this.currentStage = currentStage;
        stagesToComplete.add(currentStage);
    }

    public void addStages(Collectable collectable){
        stagesToComplete.add(collectable);
    }
}
