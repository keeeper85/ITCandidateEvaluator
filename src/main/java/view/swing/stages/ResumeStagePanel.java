package view.swing.stages;

import view.swing.ViewConstants;

public class ResumeStagePanel extends AbstractStage {

    public ResumeStagePanel(StageView stageView) {
        super(stageView);
    }

    @Override
    protected void init() {
        add(createTitleLabel("Resume & Social Media Evaluation Stage"));
        add(createInfoLabel(ViewConstants.RESUME_STAGE_INFO));
        add(createScoreSlider("resume"));
    }
}
