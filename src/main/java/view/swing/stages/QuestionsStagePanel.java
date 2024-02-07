package view.swing.stages;

import view.swing.ViewConstants;

import javax.swing.*;
import java.util.HashMap;

public class QuestionsStagePanel extends AbstractStage {

    public QuestionsStagePanel(StageView stageView) {
        super(stageView);
    }

    @Override
    protected void init() {
        add(createTitleLabel("Technical Questions Evaluation Stage"));
        add(createInfoLabel(ViewConstants.QUESTIONS_STAGE_INFO));
        add(createScoreSlider("questions"));
    }
}
