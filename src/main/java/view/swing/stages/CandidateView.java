package view.swing.stages;

import view.swing.View;
import view.swing.ViewConstants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CandidateView extends JPanel implements Collectable {

    private View view;
    private List<JLabel> labels = new ArrayList<>();
    private List<JTextField> textFields = new ArrayList<>();
    private JTextArea notes;
    private JTextField firstName;
    private JTextField lastName;
    private final int PANEL_X = 50;
    private final int PANEL_Y = 50;
    private final int LABEL_WIDTH = 300;
    private final int TEXT_INPUT_WIDTH = 600;
    private final int SMALL_ITEM_HEIGHT = 60;
    private final int SPACING = 30;
    private final int NOTES_POSITION_X = PANEL_X + LABEL_WIDTH;
    private final int NOTES_POSITION_Y = 420;
    private final int NOTES_WIDTH = TEXT_INPUT_WIDTH;
    private final int NOTES_HEIGHT = 250;
    private final int SWAP_BUTTON_POSITION_X = 615;
    private final int SWAP_BUTTON_POSITION_Y = 112;
    private final int SWAP_BUTTON_WIDTH = 80;
    private final int SWAP_BUTTON_HEIGHT = 25;

    public CandidateView(View view) {
        this.view = view;
        initCandidateView();
    }

    private void initCandidateView(){
        setLayout(null);

        setItemLabels();
        setTextInputFields();
        setNotesTextArea();
        setSwapButton();

        revalidate();
        repaint();
    }

    private void setItemLabels(){
        JLabel name = new JLabel("First name:");
        JLabel lastName = new JLabel("Last name:");
        JLabel yearOfBirth = new JLabel("(Optional) Year of birth:");
        JLabel nationality = new JLabel("(Optional) Nationality:");
        JLabel notes = new JLabel("(Optional) Notes:");

        labels.add(name);
        labels.add(lastName);
        labels.add(yearOfBirth);
        labels.add(nationality);
        labels.add(notes);

        int initialPositionY = PANEL_Y;

        for (JLabel label : labels) {
            label.setFont(ViewConstants.FONT_LARGE);
            label.setBounds(PANEL_X, initialPositionY, LABEL_WIDTH, SMALL_ITEM_HEIGHT);
            add(label);
            initialPositionY += SMALL_ITEM_HEIGHT + SPACING;
        }
    }

    private void setTextInputFields(){
        firstName = new JTextField();
        firstName.setName("name");
        lastName = new JTextField();
        lastName.setName("lastName");
        JTextField yearOfBirth = new JTextField();
        yearOfBirth.setName("year");
        JTextField nationality = new JTextField();
        nationality.setName("nationality");

        textFields.add(firstName);
        textFields.add(lastName);
        textFields.add(yearOfBirth);
        textFields.add(nationality);

        int initialPositionY = PANEL_Y;
        int initialPositionX = PANEL_X + LABEL_WIDTH;

        for (JTextField textField : textFields) {
            textField.setBounds(initialPositionX, initialPositionY, TEXT_INPUT_WIDTH, SMALL_ITEM_HEIGHT);
            textField.setFont(ViewConstants.FONT_LARGE);
            add(textField);
            initialPositionY += SMALL_ITEM_HEIGHT + SPACING;
        }
    }

    private void setNotesTextArea(){
        notes = new JTextArea();
        notes.setName("notes");
        notes.setFont(ViewConstants.FONT_LARGE);
        notes.setLineWrap(true);
        notes.setWrapStyleWord(true);

        JScrollPane notesWithScroll = new JScrollPane(notes);
        notesWithScroll.setBounds(NOTES_POSITION_X, NOTES_POSITION_Y, NOTES_WIDTH, NOTES_HEIGHT);
        add(notesWithScroll);
    }

    private void setSwapButton(){
        JButton swapButton = new JButton();
        swapButton.setText("SWAP");
        swapButton.setFont(ViewConstants.FONT_SMALL);
        swapButton.setBounds(SWAP_BUTTON_POSITION_X,SWAP_BUTTON_POSITION_Y,SWAP_BUTTON_WIDTH,SWAP_BUTTON_HEIGHT);
        swapButton.addActionListener((e -> {swapTextFields();}));
        add(swapButton);
    }

    private void swapTextFields() {
        String lastNameInsteadOfName = firstName.getText();
        String nameInsteadOfLastName = lastName.getText();

        firstName.setText(nameInsteadOfLastName);
        lastName.setText(lastNameInsteadOfName);

        revalidate();
        repaint();
    }


    @Override
    public HashMap<String, String> collectData() {
        HashMap<String, String> candidateData = new HashMap<>();
        for (JTextField textField : textFields) {
            candidateData.put(textField.getName(), textField.getText());
        }
        candidateData.put(notes.getName(), notes.getText());
        return candidateData;
    }
}
