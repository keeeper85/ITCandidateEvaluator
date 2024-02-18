package view.swing.stages;

import view.swing.ViewConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class LiveCodingStagePanel extends AbstractStage {
    private TreeMap<String,String> pair;
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
        String[] tasksForTesting = createSnippetsForTaskPickMenu(model.getLiveCodingTasks());
        JComboBox<String> taskPickMenu = new JComboBox<>(tasksForTesting);
        taskPickMenu.setBounds(TASK_PICK_MENU_X, TASK_PICK_MENU_Y, TASK_PICK_MENU_WIDTH, TASK_PICK_MENU_HEIGHT);
        taskPickMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTask = taskPickMenu.getSelectedItem().toString();
                if (selectedTask.contains("Choose")){
                    infoLabel.setText(ViewConstants.LIVE_CODING_STAGE_INFO);
                }
                else {
                    String text = pair.get(taskPickMenu.getSelectedItem().toString());
                    infoLabel.setText(text);
                }
            }
        });
        return taskPickMenu;
    }

    private String[] createSnippetsForTaskPickMenu(List<String> tasks){
        createSnippetAndBodyPair(tasks);
        String[] snippets = new String[tasks.size()];

        for (int i = 0; i < tasks.size(); i++) {
            String snippet = createSnippet(tasks.get(i));
            snippets[i] = snippet;
        }
        return snippets;
    }

    private void createSnippetAndBodyPair(List<String> tasks){
        pair = new TreeMap<>();

        for (String task : tasks) {
            pair.put(createSnippet(task),task);
        }
    }

    private String createSnippet(String taskBody){
        String[] allLines = taskBody.split("\n");

        return allLines[0];
    }
}
