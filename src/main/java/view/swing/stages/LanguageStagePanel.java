package view.swing.stages;

import model.Stages;
import view.swing.ViewConstants;

/**
 * Thanks to the AbstractStage super class implementation, this class is very verbose in code.
 * In this stage, recruiter is evaluating applicant's ability to use English language communication skills
 */

public class LanguageStagePanel extends AbstractStage{
    public LanguageStagePanel(StageView stageView) {
        super(stageView);
    }
    @Override
    protected void startingHook() {
        stage = Stages.LANGUAGE;
        ordinal = stage.getStageOrdinal();
        add(createScrollableInfoLabel(ViewConstants.LANGUAGE_STAGE_INFO));
    }
}
