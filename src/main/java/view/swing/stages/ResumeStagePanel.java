package view.swing.stages;

import view.swing.ViewConstants;

import javax.swing.*;
import java.util.HashMap;

public class ResumeStagePanel extends JPanel implements Collectable {

    private StageView stageView;

    public ResumeStagePanel(StageView view) {
        this.stageView = view;
        init();
    }

    private void init(){
        setLayout(null);

        JLabel info = new JLabel(ViewConstants.RESUME_STAGE_INFO);
        info.setBounds(100,100,100,100);
        add(info);
    }

    @Override
    public HashMap<String, String> collectData() {
        return null;
    }
}