package view.swing;

import view.swing.stages.CandidateView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class CandidateListView extends JPanel {

    private View view;
    private JList<String> candidatesList;
    private DefaultListModel<String> listModel;
    private String[] sortingOptions = {"Name-Ascending", "Name-Descending", "Score-Ascending", "Score-Descending"};
    private final int TOP_ROW_Y = 20;
    private final int LIST_X = 30;
    private final int LIST_Y = 60;
    private final int LIST_WIDTH = 900;
    private final int LIST_HEIGHT = 600;
    private final int SPACING = 60;
    private final int BUTTON_X = LIST_X + LIST_WIDTH + SPACING;
    private final int BUTTON_Y = LIST_Y + SPACING;
    private final int BUTTON_WIDTH = 200;
    private final int BUTTON_HEIGHT = 40;

    public CandidateListView(View view) {
        this.view = view;
        initCandidatesListView();
    }

    private void initCandidatesListView(){
        setLayout(null);
        addCandidates();
        candidatesList = new JList<>(listModel);
        candidatesList.setFont(ViewConstants.FONT_LARGE);
        add(createScrollPane());
        add(createSortingMenu());
        add(createFinishedCheckBox());

        add(createOpenButton());
        add(createAddCandidateButton());
        add(createAddManyButton());
        add(createDeleteButton());
        add(createBackButton());
    }

    private void addCandidates(){
        listModel = new DefaultListModel<>();
        for (int i = 0; i < 30; i++) {
            listModel.addElement("Candidate " + i);
        }
    }

    private JScrollPane createScrollPane(){
        JScrollPane scrollPane = new JScrollPane(candidatesList);
        scrollPane.setBounds(LIST_X,LIST_Y,LIST_WIDTH,LIST_HEIGHT);
        return scrollPane;
    }

    private JButton createOpenButton(){
        JButton openButton = new JButton("Open");
        openButton.setFont(ViewConstants.FONT_LARGE);
        openButton.setBounds(BUTTON_X, BUTTON_Y ,BUTTON_WIDTH ,BUTTON_HEIGHT);
        return openButton;
    }

    private JButton createDeleteButton(){
        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(ViewConstants.FONT_LARGE);
        int deleteButtonY = BUTTON_Y + SPACING + SPACING + SPACING;
        deleteButton.setBounds(BUTTON_X, deleteButtonY ,BUTTON_WIDTH ,BUTTON_HEIGHT);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String warning = "This operation can not be undone!\nType 'delete' to remove the candidate.";
                String userInput = (String) JOptionPane.showInputDialog(null, warning, "Confirm:", JOptionPane.WARNING_MESSAGE);
                int selectedRecrutationIndex = candidatesList.getSelectedIndex();
                if (userInput != null && userInput.equals("delete") && selectedRecrutationIndex >= 0) {
                    JOptionPane.showMessageDialog(null, "Candidate removed!");
                    listModel.remove(selectedRecrutationIndex);
                    candidatesList.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Deletion canceled or invalid input.");
                }
            }
        });

        return deleteButton;
    }

    private JButton createBackButton(){
        JButton backButton = new JButton("Back");
        backButton.setFont(ViewConstants.FONT_LARGE);
        int backButtonY = BUTTON_Y + SPACING + SPACING + SPACING + SPACING;
        backButton.setBounds(BUTTON_X, backButtonY ,BUTTON_WIDTH ,BUTTON_HEIGHT);
        backButton.addActionListener((e -> {view.returnToPreviousPanel();}));
        return backButton;
    }

    private JButton createAddCandidateButton(){
        JButton addCandidateButton = new JButton("Add one");
        addCandidateButton.setFont(ViewConstants.FONT_LARGE);
        int backButtonY = BUTTON_Y + SPACING;
        addCandidateButton.setBounds(BUTTON_X, backButtonY ,BUTTON_WIDTH ,BUTTON_HEIGHT);
        addCandidateButton.addActionListener((e -> {view.setCurrentPanel(new CandidateView(view));}));
        return addCandidateButton;
    }

    private JButton createAddManyButton(){
        JButton addManyButton = new JButton("Add many");
        addManyButton.setFont(ViewConstants.FONT_LARGE);
        int addManyButtonY = BUTTON_Y + SPACING + SPACING;
        addManyButton.setBounds(BUTTON_X, addManyButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        addManyButton.addActionListener((e -> {showDirectoryChooser();}));

        return addManyButton;
    }

    private void showDirectoryChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select directory with resumes (.pdf files only):");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        String resumesDirectoryPath;

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            resumesDirectoryPath = selectedDirectory.getAbsolutePath();
            JOptionPane.showMessageDialog(this, "Selected directory: " + resumesDirectoryPath);
        }
    }

    private JComboBox<String> createSortingMenu(){

        JComboBox<String> choiceMenu = new JComboBox<>(sortingOptions);
        choiceMenu.setBounds(LIST_X, TOP_ROW_Y, BUTTON_WIDTH, 20);

        return choiceMenu;
    }

    private JCheckBox createFinishedCheckBox(){
        JCheckBox showOnlyUnfinishedCandidates = new JCheckBox();
        showOnlyUnfinishedCandidates.setLabel("Show only unfinished candidates");
        int positionX = LIST_X + BUTTON_WIDTH + 10;
        showOnlyUnfinishedCandidates.setBounds(positionX, TOP_ROW_Y, 250,20);

        return showOnlyUnfinishedCandidates;
    }
}
