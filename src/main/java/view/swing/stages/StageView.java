package view.swing.stages;

import view.swing.View;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class StageView extends JPanel {
    private View view;
    private JPanel initialPanel;
    private JPanel currentStagePanel;
    private JPanel previousStagePanel;
    private List<JPanel> chosenStages = new ArrayList<>();
    private Sidepanel sidepanel;

    public StageView(View view) {
        this.view = view;
        sidepanel = new Sidepanel(this);
        initialPanel = new CandidateView(view);
        currentStagePanel = initialPanel;
        initStageView();
    }

    private void initStageView(){
        setLayout(null);
        currentStagePanel.setBounds(0, 0, 1000, 700);
        add(currentStagePanel);

        sidepanel.setBounds(1000, 0, 280, 700);
        add(sidepanel);
        setChosenPanelsForTesting();

        revalidate();
        repaint();
    }
    private void setChosenPanelsForTesting(){
        chosenStages.add(initialPanel);
        chosenStages.add(new ResumeStagePanel(this));
        chosenStages.add(new LanguageStagePanel(this));
        chosenStages.add(new ExperienceStagePanel(this));
        chosenStages.add(new ProjectsStagePanel(this));
        chosenStages.add(new LiveCodingStagePanel(this));
        chosenStages.add(new QuestionsStagePanel(this));
        chosenStages.add(new SalaryStagePanel(this));
        chosenStages.add(new SoftSkillsStagePanel(this));
    }

    public void setCurrentStagePanel(JPanel newPanel){
        previousStagePanel = currentStagePanel;
        remove(currentStagePanel);
        currentStagePanel = newPanel;
        currentStagePanel.setBounds(0, 0, 1000, 700);
        add(currentStagePanel);

        revalidate();
        repaint();
    }

    public List<JPanel> getChosenStages() {
        return chosenStages;
    }

    public JPanel getCurrentStagePanel() {
        return currentStagePanel;
    }

    public View getView() {
        return view;
    }
}
