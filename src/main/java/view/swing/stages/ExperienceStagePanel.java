package view.swing.stages;

import view.swing.ViewConstants;

import javax.swing.*;
import java.util.HashMap;

public class ExperienceStagePanel extends AbstractStage {
    public ExperienceStagePanel(StageView stageView) {
        super(stageView);
        ordinal = 3;
    }

    @Override
    protected void init() {
        add(createTitleLabel("Previous Work Experience Evaluation Stage"));
        add(createInfoLabel(ViewConstants.EXPERIENCE_STAGE_INFO));
        add(createScoreSlider("experience"));
    }
}
