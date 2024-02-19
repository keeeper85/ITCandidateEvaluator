package view.swing.stages;

import controller.CandidateDTO;
import model.AbstractCandidate;
import model.Candidate;
import model.Model;
import model.Recruitment;
import view.swing.View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StageView extends JPanel {
    private View view;
    private Model model;
    private Collectable initialPanel;
    private Collectable currentStagePanel;
    private Collectable previousStagePanel;
    private List<Collectable> chosenStages = new ArrayList<>();
    private Sidepanel sidepanel;
    private CandidateDTO candidate;
    private Recruitment recruitment;
    private final int MAIN_PANEL_POSITION_X = 0;
    private final int MAIN_PANEL_POSITION_Y = 0;
    private final int MAIN_PANEL_WIDTH = 1000;
    private final int MAIN_PANEL_HEIGHT = 700;
    private final int SIDEPANEL_POSITION_X = 1000;
    private final int SIDEPANEL_POSITION_Y = 0;
    private final int SIDEPANEL_WIDTH = 280;
    private final int SIDEPANEL_HEIGHT = 700;

    public StageView(View view, CandidateDTO temporaryCandidate, Recruitment recruitment) {
        this.view = view;
        this.model = view.getModel();
        this.candidate = temporaryCandidate;
        this.recruitment = recruitment;

        sidepanel = new Sidepanel(this);
        initialPanel = new CandidateView(this, candidate);
        currentStagePanel = initialPanel;
        initStageView();
    }

    private void initStageView(){
        setLayout(null);
        JPanel currentPanel = (JPanel) currentStagePanel;
        currentPanel.setBounds(MAIN_PANEL_POSITION_X, MAIN_PANEL_POSITION_Y, MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT);
        add(currentPanel);

        sidepanel.setBounds(SIDEPANEL_POSITION_X, SIDEPANEL_POSITION_Y, SIDEPANEL_WIDTH, SIDEPANEL_HEIGHT);
        add(sidepanel);
        setStagesToBeEvaluated();

        revalidate();
        repaint();
    }
    private void setStagesToBeEvaluated(){
        chosenStages.add(initialPanel);

        List<String> stagesForEvaluation = recruitment.getStagesForEvaluation();

        for (String stageName : stagesForEvaluation) {
            if (stageName.equals("resume")) chosenStages.add(new ResumeStagePanel(this));
            if (stageName.equals("language")) chosenStages.add(new LanguageStagePanel(this));
            if (stageName.equals("experience")) chosenStages.add(new ExperienceStagePanel(this));
            if (stageName.equals("projects")) chosenStages.add(new ProjectsStagePanel(this));
            if (stageName.equals("coding")) chosenStages.add(new LiveCodingStagePanel(this));
            if (stageName.equals("questions")) chosenStages.add(new QuestionsStagePanel(this));
            if (stageName.equals("salary")) chosenStages.add(new SalaryStagePanel(this));
            if (stageName.equals("soft")) chosenStages.add(new SoftSkillsStagePanel(this));
        }
        Collections.sort(chosenStages, (c1, c2) -> c1.getOrdinal() - c2.getOrdinal());
    }

    public void setCurrentStagePanel(Collectable newPanel){
        previousStagePanel = currentStagePanel;
        JPanel currentPanel = (JPanel) currentStagePanel;
        remove(currentPanel);
        currentStagePanel = newPanel;
        currentPanel = (JPanel) currentStagePanel;
        currentPanel.setBounds(MAIN_PANEL_POSITION_X, MAIN_PANEL_POSITION_Y, MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT);
        add(currentPanel);

        revalidate();
        repaint();
    }

    public List<Collectable> getChosenStages() {
//        List<JPanel> stages = new ArrayList<>();
//        for (Collectable chosenStage : chosenStages) {
//            stages.add((JPanel) chosenStage);
//        }

        return chosenStages;
    }

    public Collectable getCurrentStagePanel() {
        return currentStagePanel;
    }

    public View getView() {
        return view;
    }

    public void setCandidate(CandidateDTO candidate) {
        this.candidate = candidate;
    }

    public CandidateDTO getCandidate() {
        return candidate;
    }
}
