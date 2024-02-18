package view.swing.stages;

import view.swing.ViewConstants;

public class SoftSkillsStagePanel extends AbstractStage { ;
    public SoftSkillsStagePanel(StageView stageView) {
        super(stageView);
        ordinal = 8;
    }

    @Override
    protected void init() {
        add(createTitleLabel("Soft Skills Evaluation Stage"));
        add(createScrollableInfoLabel(ViewConstants.SOFT_SKILLS_STAGE_INFO));
        add(createScoreSlider("soft"));
    }
}
