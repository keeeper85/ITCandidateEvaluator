package view.swing.stages;

import view.swing.ViewConstants;

public class LanguageStagePanel extends AbstractStage{
    public LanguageStagePanel(StageView stageView) {
        super(stageView);
        ordinal = 2;
    }
    @Override
    protected void init() {
        add(createTitleLabel("English Language Fluency Evaluation Stage"));
        add(createScrollableInfoLabel(ViewConstants.LANGUAGE_STAGE_INFO));
        add(createScoreSlider("language"));
    }
}
