package view.swing.stages;

import view.swing.ViewConstants;

public class ExperienceStagePanel extends AbstractStage {
    public ExperienceStagePanel(StageView stageView) {
        super(stageView);
        ordinal = 3;
    }

    @Override
    protected void init() {
        add(createTitleLabel("Previous Work Experience Evaluation Stage"));
        add(createScrollableInfoLabel(ViewConstants.EXPERIENCE_STAGE_INFO));
        add(createScoreSlider("experience"));
    }
}
