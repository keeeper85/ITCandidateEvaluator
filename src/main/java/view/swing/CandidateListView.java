package view.swing;

import controller.CandidateDTO;
import controller.Controller;
import model.Candidate;
import model.CandidateFactory;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CandidateListView extends JPanel {

    private View view;
    private Controller controller;
    private Recruitment recruitment;
    private List<Candidate> listForSorting;
    private List<Candidate> listForSortingUnfinished;
    private SortingOptions selectedOption = SortingOptions.Date_Ascending;
    private CandidateStatus showCandidates = CandidateStatus.All;
    private JList<Candidate> candidatesList;
    private DefaultListModel<Candidate> listModel;
    private Candidate selectedCandidate;
    private JButton openButton;
    private JButton cvButton;
    private JButton deleteButton;
    private JButton feedbackButton;
    private JScrollPane scrollPane;
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
        controller = view.getController();
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
        sortList(SortingOptions.Date_Ascending, CandidateStatus.All);

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
        listForSorting = new ArrayList<>();
        listForSortingUnfinished = new ArrayList<>();

        for (Candidate candidate : recruitment.getCandidateList()) {
            listForSorting.add(candidate);
            if (!candidate.isFinished()) listForSortingUnfinished.add(candidate);
            listModel.addElement(candidate);
        }
    }

    private void sortList(SortingOptions options, CandidateStatus status){
        List<Candidate> list;
        if (status == CandidateStatus.All) list = listForSorting;
        else list = listForSortingUnfinished;

        switch (options){
            case Name_Ascending:
                Collections.sort(list, (o1, o2) -> o1.getLastName().compareTo(o2.getLastName()));
                break;
            case Name_Descending:
                Collections.sort(list, (o1, o2) -> o2.getLastName().compareTo(o1.getLastName()));
                break;
            case Date_Ascending:
                Collections.sort(list, (o1, o2) -> o1.getID().compareTo(o2.getID()));
                break;
            case Date_Descending:
                Collections.sort(list, (o1, o2) -> o2.getID().compareTo(o1.getID()));
                break;
            case Score_Ascending:
                Collections.sort(list, (o1, o2) -> o1.getEvaluationScore().compareTo(o2.getEvaluationScore()));
                break;
            case Score_Descending:
                Collections.sort(list, (o1, o2) -> o2.getEvaluationScore().compareTo(o1.getEvaluationScore()));
                break;
            case ValueCost_Ascending:
                Collections.sort(list, (o1, o2) -> o1.getValueCostRatio().compareTo(o2.getValueCostRatio()));
                break;
            case ValueCost_Descending:
                Collections.sort(list, (o1, o2) -> o2.getValueCostRatio().compareTo(o1.getValueCostRatio()));
                break;
        }
        updateList(list);
    }

    private void updateList(List<Candidate> list){
        listModel = new DefaultListModel<>();
        for (Candidate candidate : list) {
            listModel.addElement(candidate);
        }
        remove(scrollPane);
        add(createScrollPane());

        repaint();
        revalidate();
    }

    private JScrollPane createScrollPane(){
        candidatesList = new JList<>(listModel);
        candidatesList.setFont(ViewConstants.FONT_LARGE);
        candidatesList.addListSelectionListener(e -> {
            selectedCandidate = candidatesList.getSelectedValue();
            openButton.setEnabled(true);
            deleteButton.setEnabled(true);
            feedbackButton.setEnabled(true);
            if (selectedCandidate.getPathToResumeFile() == null || selectedCandidate.getPathToResumeFile().length() < 5) cvButton.setEnabled(false);
            else cvButton.setEnabled(true);});

        scrollPane = new JScrollPane(candidatesList);
        scrollPane.setBounds(LIST_X,LIST_Y,LIST_WIDTH,LIST_HEIGHT);
        return scrollPane;
    }

    private JButton createOpenButton(){
        openButton = new JButton("Open");
        openButton.setEnabled(false);
        openButton.setFont(ViewConstants.FONT_LARGE);
        openButton.setBounds(BUTTON_X, BUTTON_Y ,BUTTON_WIDTH ,BUTTON_HEIGHT);
        openButton.addActionListener(e -> {
            if (selectedCandidate != null) {
                if (selectedCandidate.isFinished()) JOptionPane.showMessageDialog(null, "This candidate has finished evaluation. You can't edit data anymore.", "Information", JOptionPane.INFORMATION_MESSAGE);
                else{
                    CandidateDTO temporaryCandidate = controller.createTemporaryCandidate(selectedCandidate, recruitment);
                    StageView stageView = new StageView(view, temporaryCandidate, recruitment);
                    view.setCurrentPanel(stageView);
                }
            }
        });
        return openButton;
    }

    private JButton createDeleteButton(){
        deleteButton = new JButton("Delete");
        deleteButton.setFont(ViewConstants.FONT_LARGE);
        deleteButton.setEnabled(false);
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
        feedbackButton = new JButton("Feedback");
        feedbackButton.setFont(ViewConstants.FONT_LARGE);
        feedbackButton.setEnabled(false);
        int backButtonY = BUTTON_Y + SPACING + SPACING + SPACING + SPACING;
        feedbackButton.setBounds(BUTTON_X, backButtonY ,BUTTON_WIDTH ,BUTTON_HEIGHT);
        feedbackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                {if (selectedCandidate != null){
                    if (selectedCandidate.isFinished()){
                        String feedback = selectedCandidate.generateFeedback();
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
        cvButton = new JButton("Open CV");
        cvButton.setEnabled(false);
        cvButton.setFont(ViewConstants.FONT_LARGE);
        int backButtonY = BUTTON_Y + SPACING + SPACING + SPACING + SPACING + SPACING;
        cvButton.setBounds(BUTTON_X, backButtonY ,BUTTON_WIDTH ,BUTTON_HEIGHT);
        cvButton.addActionListener((e -> {
            try {
                if (selectedCandidate != null){
                    if (selectedCandidate.getPathToResumeFile() != null)
                        Desktop.getDesktop().open(new File(selectedCandidate.getPathToResumeFile()));
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
                CandidateDTO temporaryCandidate = controller.createTemporaryCandidate(null, recruitment);
                view.setCurrentPanel(new StageView(view, temporaryCandidate, recruitment));
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

            remove(scrollPane);
            recruitment.addCandidates(CandidateFactory.getCandidatesFromResumes(resumesDirectoryPath, recruitment));
            populateCandidateList();
            add(createScrollPane());
            revalidate();
            repaint();
        }
    }

    private JComboBox<SortingOptions> createSortingMenu(){

        JComboBox<SortingOptions> choiceMenu = new JComboBox<>(SortingOptions.values());
        choiceMenu.setBounds(LIST_X, TOP_ROW_Y, BUTTON_WIDTH, SORTING_MENU_HEIGHT);
        choiceMenu.setSelectedItem(SortingOptions.Date_Ascending);
        choiceMenu.addActionListener(e -> {
            selectedOption = (SortingOptions) choiceMenu.getSelectedItem();
            sortList(selectedOption, showCandidates);});

        return choiceMenu;
    }

    private JCheckBox createFinishedCheckBox(){
        JCheckBox showOnlyUnfinishedCandidates = new JCheckBox();
        showOnlyUnfinishedCandidates.setLabel("Show only unfinished candidates");
        int positionX = LIST_X + BUTTON_WIDTH + SMALL_SPACING;
        showOnlyUnfinishedCandidates.setBounds(positionX, TOP_ROW_Y, SORTING_MENU_LABEL_WIDTH,SORTING_MENU_HEIGHT);
        showOnlyUnfinishedCandidates.addActionListener(e -> {
            if (showOnlyUnfinishedCandidates.isSelected()) {
                showCandidates = CandidateStatus.Unfinished;
            } else {
                showCandidates = CandidateStatus.All;
            }
            sortList(selectedOption, showCandidates);
        });

        return showOnlyUnfinishedCandidates;
    }

    private enum SortingOptions{Name_Ascending, Name_Descending, Date_Ascending, Date_Descending, Score_Ascending, Score_Descending, ValueCost_Ascending, ValueCost_Descending}
    private enum CandidateStatus{All, Unfinished}
}
