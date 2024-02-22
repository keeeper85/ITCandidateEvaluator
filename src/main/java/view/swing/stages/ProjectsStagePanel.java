package view.swing.stages;

import model.Stages;
import view.swing.ViewConstants;

public class ProjectsStagePanel extends AbstractStage {
    public ProjectsStagePanel(StageView stageView) {
        super(stageView);
    }
    @Override
    protected void startingHook() {
        stage = Stages.PROJECTS;
        ordinal = stage.getStageOrdinal();
        add(createScrollableInfoLabel(ViewConstants.PROJECTS_STAGE_INFO));
    }

}
