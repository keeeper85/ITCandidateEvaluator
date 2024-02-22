package view.swing;

import model.Model;
import model.Recruitment;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecruitmentsListView extends JPanel {

    private View view;
    private Model model;
    private JList<Recruitment> recruitmentJList;
    private List<Recruitment> listForSorting;
    private List<Recruitment> listForSortingUnfinished;
    private DefaultListModel<Recruitment> listModel;
    private Recruitment selectedRecruitment;
    private JScrollPane scrollPane;
    private JButton openButton;
    private JButton deleteButton;
    private SortingOptions selectedOption = SortingOptions.Name_Ascending;
    private RecruitmentStatus showRecruitments = RecruitmentStatus.All;
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

    public RecruitmentsListView(View view) {
        this.view = view;
        model = view.getModel();
        initRecruitmentListView();
    }

    private void initRecruitmentListView(){
        setLayout(null);
        populateRecruitmentList();
        add(createScrollPane());
        add(createSortingMenu());
        add(createFinishedCheckBox());

        add(createOpenButton());
        add(createNewRecruitmentButton());
        add(createDeleteButton());
        add(createBackButton());
    }

    public void populateRecruitmentList(){
        listModel = new DefaultListModel<>();
        listForSorting = new ArrayList<>();
        listForSortingUnfinished = new ArrayList<>();

        for (Recruitment openRecruitmentProcess : model.getOpenRecruitmentProcesses()) {
            listForSorting.add(openRecruitmentProcess);
            if (!openRecruitmentProcess.isFinished()) listForSortingUnfinished.add(openRecruitmentProcess);
            listModel.addElement(openRecruitmentProcess);
        }
    }

    private void sortList(SortingOptions options, RecruitmentStatus status){
        List<Recruitment> list;
        if (status == RecruitmentStatus.All) list = listForSorting;
        else list = listForSortingUnfinished;

        switch (options){
            case Name_Ascending:
                Collections.sort(list, (o1, o2) -> o1.getName().compareTo(o2.getName()));
                break;
            case Name_Descending:
                Collections.sort(list, (o1, o2) -> o2.getName().compareTo(o1.getName()));
                break;
            case Date_Ascending:
                Collections.sort(list, (o1, o2) -> o1.getDateOfCreation().compareTo(o2.getDateOfCreation()));
                break;
            case Date_Descending:
                Collections.sort(list, (o1, o2) -> o2.getDateOfCreation().compareTo(o1.getDateOfCreation()));
                break;
        }
        updateList(list);
    }

    private void updateList(List<Recruitment> list){
        listModel = new DefaultListModel<>();
        for (Recruitment process : list) {
            listModel.addElement(process);
        }
        remove(scrollPane);
        add(createScrollPane());

        repaint();
        revalidate();
    }

    private JScrollPane createScrollPane(){
        recruitmentJList = new JList<>(listModel);
        recruitmentJList.setFont(ViewConstants.FONT_LARGE);
        recruitmentJList.addListSelectionListener(e -> {
            selectedRecruitment = recruitmentJList.getSelectedValue();
            openButton.setEnabled(true);
            deleteButton.setEnabled(true);});

        scrollPane = new JScrollPane(recruitmentJList);
        scrollPane.setBounds(LIST_X,LIST_Y,LIST_WIDTH,LIST_HEIGHT);
        return scrollPane;
    }

    private JButton createOpenButton(){
        openButton = new JButton("Open");
        openButton.setFont(ViewConstants.FONT_LARGE);
        openButton.setEnabled(false);
        openButton.setBounds(BUTTON_X, BUTTON_Y ,BUTTON_WIDTH ,BUTTON_HEIGHT);

        openButton.addActionListener(e -> {
            if (selectedRecruitment != null) {
                CandidateListView candidateListView = new CandidateListView(view);
                view.setCurrentPanel(candidateListView);
                candidateListView.setRecruitment(selectedRecruitment);
            }
        });

        return openButton;
    }

    private JButton createDeleteButton(){
        deleteButton = new JButton("Delete");
        deleteButton.setFont(ViewConstants.FONT_LARGE);
        deleteButton.setEnabled(false);
        int deleteButtonY = BUTTON_Y + SPACING + SPACING;
        deleteButton.setBounds(BUTTON_X, deleteButtonY ,BUTTON_WIDTH ,BUTTON_HEIGHT);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String warning = "This operation can not be undone!\nType 'delete' to remove the entire recruitment process.";
                String userInput = JOptionPane.showInputDialog(null, warning, "Confirm:", JOptionPane.WARNING_MESSAGE);
                int selectedRecrutationIndex = recruitmentJList.getSelectedIndex();
                Recruitment recrutationForRemoval = listModel.get(selectedRecrutationIndex);

                if (userInput != null
                        && userInput.equals("delete")
                        && selectedRecrutationIndex >= 0
                        && model.deleteExistingRecruitment(recrutationForRemoval)) {

                    listModel.remove(selectedRecrutationIndex);
                    listForSorting.remove(recrutationForRemoval);
                    listForSortingUnfinished.remove(recrutationForRemoval);
                    recruitmentJList.repaint();
                    JOptionPane.showMessageDialog(null, "Recruitment process deleted!");

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
        int backButtonY = BUTTON_Y + SPACING + SPACING + SPACING;
        backButton.setBounds(BUTTON_X, backButtonY ,BUTTON_WIDTH ,BUTTON_HEIGHT);
        backButton.addActionListener((e -> {view.returnToPreviousPanel();}));
        return backButton;
    }

    private JButton createNewRecruitmentButton(){
        JButton newRecruitmentButton = new JButton("Create new");
        newRecruitmentButton.setFont(ViewConstants.FONT_LARGE);
        int newRecruitmentButtonY = BUTTON_Y + SPACING;
        newRecruitmentButton.setBounds(BUTTON_X, newRecruitmentButtonY ,BUTTON_WIDTH ,BUTTON_HEIGHT);
        newRecruitmentButton.addActionListener(e -> {view.setCurrentPanel(new PresetsView(view));});
        return newRecruitmentButton;
    }

    private JComboBox<SortingOptions> createSortingMenu(){
        JComboBox<SortingOptions> choiceMenu = new JComboBox<>(SortingOptions.values());
        choiceMenu.setBounds(LIST_X, TOP_ROW_Y, BUTTON_WIDTH, SORTING_MENU_HEIGHT);
        choiceMenu.addActionListener(e -> {
            selectedOption = (SortingOptions) choiceMenu.getSelectedItem();
            sortList(selectedOption, showRecruitments);});

        return choiceMenu;
    }

    private JCheckBox createFinishedCheckBox(){
        JCheckBox showOnlyUnfinishedProcesses = new JCheckBox();
        showOnlyUnfinishedProcesses.setLabel("Show only unfinished recruitments");
        int positionX = LIST_X + BUTTON_WIDTH + SMALL_SPACING;
        showOnlyUnfinishedProcesses.setBounds(positionX, TOP_ROW_Y, SORTING_MENU_LABEL_WIDTH,SORTING_MENU_HEIGHT);
        showOnlyUnfinishedProcesses.addActionListener(e -> {
            if (showOnlyUnfinishedProcesses.isSelected()) {
                showRecruitments = RecruitmentStatus.Unfinished;
            } else {
                showRecruitments = RecruitmentStatus.All;
            }
            sortList(selectedOption, showRecruitments);
        });

        return showOnlyUnfinishedProcesses;
    }

    private enum SortingOptions{Name_Ascending, Name_Descending, Date_Ascending, Date_Descending}
    private enum RecruitmentStatus{All, Unfinished}

}
