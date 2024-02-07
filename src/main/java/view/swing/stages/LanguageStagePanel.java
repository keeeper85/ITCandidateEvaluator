package view.swing.stages;

import view.swing.ViewConstants;

import java.util.HashMap;

public class LanguageStagePanel extends AbstractStage{
    public LanguageStagePanel(StageView stageView) {
        super(stageView);
    }
    @Override
    protected void init() {
        add(createTitleLabel("English Language Fluency Evaluation Stage"));
        add(createInfoLabel(ViewConstants.LANGUAGE_STAGE_INFO));
        add(createScoreSlider("language"));
    }
}
