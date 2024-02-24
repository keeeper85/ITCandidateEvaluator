package view.swing.stages;

import controller.CandidateDTO;
import model.Model;
import model.Recruitment;
import model.Stages;
import view.swing.View;

import javax.swing.*;
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
    private CandidateDTO temporaryCandidate;
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
        this.temporaryCandidate = temporaryCandidate;
        this.recruitment = recruitment;

        sidepanel = new Sidepanel(this);
        initialPanel = new CandidateView(this, temporaryCandidate);
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
        Model.logger.info("StageView object created. Evaluation has started.");

        revalidate();
        repaint();
    }
    private void setStagesToBeEvaluated(){
        chosenStages.add(initialPanel);

        List<String> stagesForEvaluation = recruitment.getStagesForEvaluation();

        for (String stageName : stagesForEvaluation) {
            if (stageName.equals(Stages.RESUME.getStageName())) chosenStages.add(new ResumeStagePanel(this));
            if (stageName.equals(Stages.LANGUAGE.getStageName())) chosenStages.add(new LanguageStagePanel(this));
            if (stageName.equals(Stages.EXPERIENCE.getStageName())) chosenStages.add(new ExperienceStagePanel(this));
            if (stageName.equals(Stages.PROJECTS.getStageName())) chosenStages.add(new ProjectsStagePanel(this));
            if (stageName.equals(Stages.LIVE_CODING.getStageName())) chosenStages.add(new LiveCodingStagePanel(this));
            if (stageName.equals(Stages.QUESTIONS.getStageName())) chosenStages.add(new QuestionsStagePanel(this));
            if (stageName.equals(Stages.SALARY.getStageName())) chosenStages.add(new SalaryStagePanel(this));
            if (stageName.equals(Stages.SOFT_SKILLS.getStageName())) chosenStages.add(new SoftSkillsStagePanel(this));
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

    public List<Collectable> getChosenStages() { return chosenStages; }
    public Collectable getCurrentStagePanel() { return currentStagePanel; }
    public View getView() { return view; }
    public CandidateDTO getTemporaryCandidate() { return temporaryCandidate; }

}
