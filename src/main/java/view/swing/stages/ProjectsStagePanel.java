package view.swing.stages;

import view.swing.ViewConstants;

import javax.swing.*;
import java.util.HashMap;

public class ProjectsStagePanel extends AbstractStage {

    public ProjectsStagePanel(StageView stageView) {
        super(stageView);
    }
    @Override
    protected void init() {
        add(createTitleLabel("Own Projects Evaluation Stage"));
        add(createInfoLabel(ViewConstants.PROJECTS_STAGE_INFO));
        add(createScoreSlider("projects"));
    }

}
