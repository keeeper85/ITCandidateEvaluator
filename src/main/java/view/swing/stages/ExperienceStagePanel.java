package view.swing.stages;

import model.Stages;
import view.swing.ViewConstants;

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
