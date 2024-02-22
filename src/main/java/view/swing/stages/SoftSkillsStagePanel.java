package view.swing.stages;

import model.Stages;
import view.swing.ViewConstants;

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
