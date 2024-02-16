package view.swing.stages;

import model.AbstractCandidate;
import model.Candidate;
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
    private AbstractCandidate candidate;
    private final int MAIN_PANEL_POSITION_X = 0;
    private final int MAIN_PANEL_POSITION_Y = 0;
    private final int MAIN_PANEL_WIDTH = 1000;
    private final int MAIN_PANEL_HEIGHT = 700;
    private final int SIDEPANEL_POSITION_X = 1000;
    private final int SIDEPANEL_POSITION_Y = 0;
    private final int SIDEPANEL_WIDTH = 280;
    private final int SIDEPANEL_HEIGHT = 700;

    public StageView(View view, AbstractCandidate abstractCandidate) {
        this.view = view;
        this.candidate = abstractCandidate;
        sidepanel = new Sidepanel(this);
        initialPanel = new CandidateView(this, candidate);
        currentStagePanel = initialPanel;
        initStageView();
    }

    private void initStageView(){
        setLayout(null);
        currentStagePanel.setBounds(MAIN_PANEL_POSITION_X, MAIN_PANEL_POSITION_Y, MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT);
        add(currentStagePanel);

        sidepanel.setBounds(SIDEPANEL_POSITION_X, SIDEPANEL_POSITION_Y, SIDEPANEL_WIDTH, SIDEPANEL_HEIGHT);
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
        currentStagePanel.setBounds(MAIN_PANEL_POSITION_X, MAIN_PANEL_POSITION_Y, MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT);
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

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public AbstractCandidate getCandidate() {
        return candidate;
    }
}
