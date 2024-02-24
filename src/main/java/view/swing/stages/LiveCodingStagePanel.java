package view.swing.stages;

import model.Model;
import model.Stages;
import view.swing.ViewConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class LiveCodingStagePanel extends AbstractStage {
    private TreeMap<String,String> pair;
    private String currentTaskBody = "";
    private String selectedSnippet = "Choose task: ";
    private JButton copyToClipboardButton;
    private final int TASK_PICK_MENU_X = 150;
    private final int TASK_PICK_MENU_Y = 60;
    private final int TASK_PICK_MENU_WIDTH = 400;
    private final int TASK_PICK_MENU_HEIGHT = 20;
    private final int COPY_BUTTON_X = TASK_PICK_MENU_X + TASK_PICK_MENU_WIDTH + 10;
    private final int COPY_BUTTON_WIDTH = 200;

    public LiveCodingStagePanel(StageView stageView) {
        super(stageView);
    }

    @Override
    protected void startingHook() {
        stage = Stages.LIVE_CODING;
        ordinal = stage.getStageOrdinal();
        add(createScrollableInfoLabel(ViewConstants.LIVE_CODING_STAGE_INFO));

        add(createTaskPickMenu());
        add(createCopyToClipboardButton());
    }

    private JComboBox<String> createTaskPickMenu(){
        String[] tasksForTesting = createSnippetsForTaskPickMenu(model.getLiveCodingTasks());
        JComboBox<String> taskPickMenu = new JComboBox<>(tasksForTesting);
        taskPickMenu.setBounds(TASK_PICK_MENU_X, TASK_PICK_MENU_Y, TASK_PICK_MENU_WIDTH, TASK_PICK_MENU_HEIGHT);
        taskPickMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSnippet = taskPickMenu.getSelectedItem().toString();
                currentTaskBody = pair.get(selectedSnippet);
                infoLabel.setText(currentTaskBody);

                updateCopyButton();
            }
        });
        return taskPickMenu;
    }

    private String[] createSnippetsForTaskPickMenu(List<String> tasks){
        createSnippetAndBodyPair(tasks);
        List<String> sortedKeysList = new ArrayList<>(pair.keySet());
        sortedKeysList.sort(Comparator.comparingInt(String::length));

        return sortedKeysList.toArray(new String[0]);
    }

    private void createSnippetAndBodyPair(List<String> tasks){
        pair = new TreeMap<>();
        pair.put("Choose task: ", ViewConstants.LIVE_CODING_STAGE_INFO);

        for (String task : tasks) {
            pair.put(createSnippet(task),task);
        }
    }

    private String createSnippet(String taskBody){
        String[] allLines = taskBody.split("\n");

        return allLines[0];
    }

    private JButton createCopyToClipboardButton(){
        copyToClipboardButton = new JButton("Copy to Clipboard");
        copyToClipboardButton.setFont(ViewConstants.FONT_SMALL);
        copyToClipboardButton.setEnabled(false);
        copyToClipboardButton.setBounds(COPY_BUTTON_X, TASK_PICK_MENU_Y, COPY_BUTTON_WIDTH, TASK_PICK_MENU_HEIGHT);
        copyToClipboardButton.addActionListener(e -> {copyToClipboard();});

        return copyToClipboardButton;
    }

    private void copyToClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String htmlTagsRemoved = currentTaskBody.replace("<br>","").replace("<html>","").replace("</html>","");
        StringSelection selection = new StringSelection(htmlTagsRemoved);
        clipboard.setContents(selection, null);
        Model.logger.info("Selected live coding task copied to system clipboard.");
    }

    private void updateCopyButton(){
        if (selectedSnippet.contains("Choose")) copyToClipboardButton.setEnabled(false);
        else {
            Model.logger.info("Selected live coding task: " + selectedSnippet);
            copyToClipboardButton.setEnabled(true);
        }
    }
}
