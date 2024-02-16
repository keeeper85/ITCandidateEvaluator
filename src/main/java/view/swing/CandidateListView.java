package view.swing;

import model.Candidate;
import model.Recruitment;
import view.swing.stages.StageView;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CandidateListView extends JPanel {

    private View view;
    private Recruitment recruitment;
    private JList<Candidate> candidatesList;
    private DefaultListModel<Candidate> listModel;
    private Candidate candidateSelected;
    private final int TOP_ROW_Y = 20;
    private final int LIST_X = 30;
    private final int LIST_Y = 60;
    private final int LIST_WIDTH = 900;
    private final int LIST_HEIGHT = 600;
    private final int SPACING = 60;
    private final int SMALL_SPACING = 10;
    private final int BUTTON_X = LIST_X + LIST_WIDTH + SPACING;
    private final int BUTTON_Y = LIST_Y + SPACING;
    private final int BUTTON_WIDTH = 200;
    private final int BUTTON_HEIGHT = 40;
    private final int SORTING_MENU_LABEL_WIDTH = 250;
    private final int SORTING_MENU_HEIGHT = 20;

    public CandidateListView(View view) {
        this.view = view;
    }

    public void setRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
        populateCandidateList();
        initCandidatesListView();

        repaint();
        revalidate();
    }

    private void initCandidatesListView(){
        setLayout(null);
        add(createScrollPane());
        add(createSortingMenu());
        add(createFinishedCheckBox());

        add(createOpenButton());
        add(createAddCandidateButton());
        add(createAddManyButton());
        add(createDeleteButton());
        add(createShowFeedbackButton());
        add(createCVButton());
        add(createBackButton());
    }

    private void populateCandidateList(){
        listModel = new DefaultListModel<>();
        List<Candidate> candidates = recruitment.getCandidateList();
        for (Candidate candidate : candidates) {
            listModel.addElement(candidate);
        }
    }

    private JScrollPane createScrollPane(){
        candidatesList = new JList<>(listModel);
        candidatesList.setFont(ViewConstants.FONT_LARGE);
        candidatesList.addListSelectionListener(e -> {candidateSelected = candidatesList.getSelectedValue();});

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
    private JButton createShowFeedbackButton(){
        JButton feedbackButton = new JButton("Feedback");
        feedbackButton.setFont(ViewConstants.FONT_LARGE);
        int backButtonY = BUTTON_Y + SPACING + SPACING + SPACING + SPACING;
        feedbackButton.setBounds(BUTTON_X, backButtonY ,BUTTON_WIDTH ,BUTTON_HEIGHT);
        feedbackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                {if (candidateSelected != null){
                    if (candidateSelected.isFinished()){
                        String feedback = candidateSelected.generateFeedback();
                        int choice = JOptionPane.showOptionDialog(null, feedback, "Copy to Clipboard",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                                new String[]{"Back", "Copy"}, "Back");

                        if (choice == 1) {
                            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            StringSelection selection = new StringSelection(feedback);
                            clipboard.setContents(selection, null);
                        }
                    }
                    else JOptionPane.showMessageDialog(null, "Only fully evaluated candidates have feedback", "Evaluation hasn't finished yet.", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        return feedbackButton;
    }

    private JButton createCVButton(){
        JButton cvButton = new JButton("Open CV");
        cvButton.setFont(ViewConstants.FONT_LARGE);
        int backButtonY = BUTTON_Y + SPACING + SPACING + SPACING + SPACING + SPACING;
        cvButton.setBounds(BUTTON_X, backButtonY ,BUTTON_WIDTH ,BUTTON_HEIGHT);
        cvButton.addActionListener((e -> {
            try {
                if (candidateSelected != null){
                    if (candidateSelected.getResumePath() != null)
                        Desktop.getDesktop().open(new File(candidateSelected.getResumePath()));
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }));
        return cvButton;
    }

    private JButton createBackButton(){
        JButton backButton = new JButton("Back");
        backButton.setFont(ViewConstants.FONT_LARGE);
        int backButtonY = BUTTON_Y + SPACING + SPACING + SPACING + SPACING + SPACING + SPACING;
        backButton.setBounds(BUTTON_X, backButtonY ,BUTTON_WIDTH ,BUTTON_HEIGHT);
        backButton.addActionListener((e -> {view.returnToPreviousPanel();}));
        return backButton;
    }

    private JButton createAddCandidateButton(){
        JButton addCandidateButton = new JButton("Add one");
        addCandidateButton.setFont(ViewConstants.FONT_LARGE);
        int backButtonY = BUTTON_Y + SPACING;
        addCandidateButton.setBounds(BUTTON_X, backButtonY ,BUTTON_WIDTH ,BUTTON_HEIGHT);
        addCandidateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.setCurrentPanel(new StageView(view));
            }
        });
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

        JComboBox<String> choiceMenu = new JComboBox<>(ViewConstants.CANDIDATES_SORTING_OPTIONS);
        choiceMenu.setBounds(LIST_X, TOP_ROW_Y, BUTTON_WIDTH, SORTING_MENU_HEIGHT);

        return choiceMenu;
    }

    private JCheckBox createFinishedCheckBox(){
        JCheckBox showOnlyUnfinishedCandidates = new JCheckBox();
        showOnlyUnfinishedCandidates.setLabel("Show only unfinished candidates");
        int positionX = LIST_X + BUTTON_WIDTH + SMALL_SPACING;
        showOnlyUnfinishedCandidates.setBounds(positionX, TOP_ROW_Y, SORTING_MENU_LABEL_WIDTH,SORTING_MENU_HEIGHT);

        return showOnlyUnfinishedCandidates;
    }
}
