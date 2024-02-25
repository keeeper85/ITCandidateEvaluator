package view.swing.stages;

import model.Stages;
import view.swing.ViewConstants;

/**
 * Thanks to the AbstractStage super class implementation, this class is very verbose in code.
 * In this stage, recruiter is evaluating applicant's previous work experience, both application related and from other branches
 */

public class ExperienceStagePanel extends AbstractStage {
    public ExperienceStagePanel(StageView stageView) {
        super(stageView);
    }

    @Override
    protected void startingHook() {
        stage = Stages.EXPERIENCE;
        ordinal = stage.getStageOrdinal();
        add(createScrollableInfoLabel(ViewConstants.EXPERIENCE_STAGE_INFO));
    }
}
