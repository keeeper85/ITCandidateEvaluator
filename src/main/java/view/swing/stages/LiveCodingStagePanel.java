package view.swing.stages;

import view.swing.ViewConstants;

import javax.swing.*;
import java.util.HashMap;

public class LiveCodingStagePanel extends AbstractStage {

    public LiveCodingStagePanel(StageView stageView) {
        super(stageView);
    }

    @Override
    protected void init() {
        add(createTitleLabel("Live Coding Evaluation Stage"));
        add(createInfoLabel(ViewConstants.LIVE_CODING_STAGE_INFO));
        add(createScoreSlider("coding"));
    }
}
