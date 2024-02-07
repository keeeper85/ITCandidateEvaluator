package view.swing.stages;

import view.swing.ViewConstants;

import javax.swing.*;
import java.util.HashMap;

public class SalaryStagePanel extends AbstractStage {

    public SalaryStagePanel(StageView stageView) {
        super(stageView);
    }

    @Override
    protected void init() {
        add(createTitleLabel("Salary Expectations Evaluation Stage"));
        add(createInfoLabel(ViewConstants.SALARY_STAGE_INFO));
        add(createScoreSlider("salary"));
    }

    //override slider with label: Less is Better
}
