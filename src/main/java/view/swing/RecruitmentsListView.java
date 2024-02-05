package view.swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecruitmentsListView extends JPanel {

    private View view;
    private JList<String> recrutationsList;
    private DefaultListModel<String> listModel;
    private final int LIST_X = 30;
    private final int LIST_Y = 30;
    private final int LIST_WIDTH = 900;
    private final int LIST_HEIGHT = 600;
    private final int SPACING = 60;
    private final int BUTTON_X = LIST_X + LIST_WIDTH + SPACING;
    private final int BUTTON_Y = LIST_Y + SPACING;
    private final int BUTTON_WIDTH = 200;
    private final int BUTTON_HEIGHT = 40;

    public RecruitmentsListView(View view) {
        this.view = view;
        initRecruitmentListView();
    }

    private void initRecruitmentListView(){
        setLayout(null);
        addRecrutations();
        recrutationsList = new JList<>(listModel);
        recrutationsList.setFont(ViewConstants.FONT_LARGE);
        add(createScrollPane());
        add(createOpenButton());
        add(createNewRecruitmentButton());
        add(createDeleteButton());
        add(createBackButton());
    }

    private void addRecrutations(){
        listModel = new DefaultListModel<>();
        for (int i = 0; i < 30; i++) {
            listModel.addElement("Recruitment " + i);
        }
    }

    private JScrollPane createScrollPane(){
        JScrollPane scrollPane = new JScrollPane(recrutationsList);
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
        int deleteButtonY = BUTTON_Y + SPACING + SPACING;
        deleteButton.setBounds(BUTTON_X, deleteButtonY ,BUTTON_WIDTH ,BUTTON_HEIGHT);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String warning = "This operation can not be undone!\nType 'delete' to remove the entire recruitment process.";
                String userInput = (String) JOptionPane.showInputDialog(null, warning, "Confirm:", JOptionPane.WARNING_MESSAGE);
                int selectedRecrutationIndex = recrutationsList.getSelectedIndex();
                if (userInput != null && userInput.equals("delete") && selectedRecrutationIndex >= 0) {
                    JOptionPane.showMessageDialog(null, "Recruitment process deleted!");
                    listModel.remove(selectedRecrutationIndex);
                    recrutationsList.repaint();
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
        newRecruitmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                view.setCurrentPanel(view.getPreviousPanel());
            }
        });
        return newRecruitmentButton;
    }




}
