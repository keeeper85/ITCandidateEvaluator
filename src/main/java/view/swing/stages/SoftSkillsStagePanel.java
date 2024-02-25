package view.swing.stages;

import model.Stages;
import view.swing.ViewConstants;

/**
 * Thanks to the AbstractStage super class implementation, this class is very verbose in code.
 * In this stage, recruiter is evaluating applicant's soft skills - how well will they suit the team.
 * The collectData() returns a global modifier which will add bonus value to the previously gathered points
 */

public class SoftSkillsStagePanel extends AbstractStage { ;
    public SoftSkillsStagePanel(StageView stageView) {
        super(stageView);
    }

    @Override
    protected void startingHook() {
        stage = Stages.SOFT_SKILLS;
        ordinal = stage.getStageOrdinal();
        add(createScrollableInfoLabel(ViewConstants.SOFT_SKILLS_STAGE_INFO));
    }
}
