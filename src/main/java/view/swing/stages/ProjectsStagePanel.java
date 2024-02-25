package view.swing.stages;

import model.Stages;
import view.swing.ViewConstants;

/**
 * Thanks to the AbstractStage super class implementation, this class is very verbose in code.
 * In this stage, recruiter is evaluating applicant's projects: either on GitHub/Lab or given as a recrutation task beforehand
 */

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
