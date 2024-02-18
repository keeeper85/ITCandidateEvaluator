package view.swing.stages;

import view.swing.ViewConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class LiveCodingStagePanel extends AbstractStage {
    private final int TASK_PICK_MENU_X = 200;
    private final int TASK_PICK_MENU_Y = 100;
    private final int TASK_PICK_MENU_WIDTH = 500;
    private final int TASK_PICK_MENU_HEIGHT = 20;

    public LiveCodingStagePanel(StageView stageView) {
        super(stageView);
        ordinal = 5;
    }

    @Override
    protected void init() {
        add(createTitleLabel("Live Coding Evaluation Stage"));
        add(createTaskPickMenu());
        add(createInfoLabel(ViewConstants.LIVE_CODING_STAGE_INFO));
        add(createScoreSlider("coding"));
    }

    private JComboBox<String> createTaskPickMenu(){
        String[] tasksForTesting = model.getLiveCodingTasks().toArray(new String[0]);
        JComboBox<String> taskPickMenu = new JComboBox<>(tasksForTesting);
        taskPickMenu.setBounds(TASK_PICK_MENU_X, TASK_PICK_MENU_Y, TASK_PICK_MENU_WIDTH, TASK_PICK_MENU_HEIGHT);
        taskPickMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTask = taskPickMenu.getSelectedItem().toString();
                if (selectedTask.contains("Choose")){
                    infoLabel.setText(ViewConstants.LIVE_CODING_STAGE_INFO);
                }
                else infoLabel.setText(taskPickMenu.getSelectedItem().toString());
            }
        });
        return taskPickMenu;
    }
}
