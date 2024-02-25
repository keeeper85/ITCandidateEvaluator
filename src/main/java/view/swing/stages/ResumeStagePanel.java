package view.swing.stages;

import model.Stages;
import view.swing.ViewConstants;

/**
 * Thanks to the AbstractStage super class implementation, this class is very verbose in code.
 * In this stage, recruiter is evaluating applicant's resume contents ('first impression') and/or social media activity
 */

public class ResumeStagePanel extends AbstractStage {

    public ResumeStagePanel(StageView stageView) {
        super(stageView);
    }

    @Override
    protected void startingHook() {
        stage = Stages.RESUME;
        ordinal = stage.getStageOrdinal();
        add(createScrollableInfoLabel(ViewConstants.RESUME_STAGE_INFO));
    }
}
