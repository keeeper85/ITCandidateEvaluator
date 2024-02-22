package view.swing.stages;

import model.Stages;
import view.swing.ViewConstants;

public class LanguageStagePanel extends AbstractStage{
    public LanguageStagePanel(StageView stageView) {
        super(stageView);
    }
    @Override
    protected void startingHook() {
        stage = Stages.LANGUAGE;
        ordinal = stage.getStageOrdinal();
        add(createScrollableInfoLabel(ViewConstants.LANGUAGE_STAGE_INFO));
    }
}
