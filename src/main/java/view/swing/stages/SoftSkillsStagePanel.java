package view.swing.stages;

import view.swing.ViewConstants;

import javax.swing.*;
import java.util.HashMap;

public class SoftSkillsStagePanel extends AbstractStage { ;
    public SoftSkillsStagePanel(StageView stageView) {
        super(stageView);
        ordinal = 8;
    }

    @Override
    protected void init() {
        add(createTitleLabel("Soft Skills Evaluation Stage"));
        add(createInfoLabel(ViewConstants.SOFT_SKILLS_STAGE_INFO));
        add(createScoreSlider("soft"));
    }
}
