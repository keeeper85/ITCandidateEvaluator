package view.swing.stages;

import model.Stages;
import view.swing.ViewConstants;

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
