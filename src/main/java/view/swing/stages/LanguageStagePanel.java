package view.swing.stages;

import view.swing.ViewConstants;

import javax.swing.*;
import java.util.HashMap;

public class LanguageStagePanel extends JPanel implements Collectable{

    private StageView stageView;

    public LanguageStagePanel(StageView stageView) {
        this.stageView = stageView;
        init();
    }

    private void init(){
        setLayout(null);

        JLabel info = new JLabel(ViewConstants.LANGUAGE_STAGE_INFO);
        info.setBounds(100,100,100,100);
        add(info);
    }


    @Override
    public HashMap<String, String> collectData() {
        return null;
    }
}
