package view.swing.stages;

import view.swing.ViewConstants;

import javax.swing.*;
import java.util.HashMap;

public class SoftSkillsStagePanel extends JPanel implements Collectable {

    private StageView stageView;

    public SoftSkillsStagePanel(StageView view) {
        this.stageView = view;
        init();
    }

    private void init(){
        setLayout(null);

        JLabel info = new JLabel(ViewConstants.SOFT_SKILLS_STAGE_INFO);
        info.setBounds(100,100,100,100);
        add(info);
    }
    @Override
    public HashMap<String, String> collectData() {
        return null;
    }
}
