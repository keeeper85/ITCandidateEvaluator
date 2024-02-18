package view.swing.stages;

import view.swing.ViewConstants;

public class ProjectsStagePanel extends AbstractStage {
    public ProjectsStagePanel(StageView stageView) {
        super(stageView);
        ordinal = 4;
    }
    @Override
    protected void init() {
        add(createTitleLabel("Own Projects Evaluation Stage"));
        add(createScrollableInfoLabel(ViewConstants.PROJECTS_STAGE_INFO));
        add(createScoreSlider("projects"));
    }

}
