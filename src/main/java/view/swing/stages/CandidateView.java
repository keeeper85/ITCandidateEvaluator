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
    private final int PANEL_X = 50;
    private final int PANEL_Y = 50;
    private final int LABEL_WIDTH = 300;
    private final int TEXT_INPUT_WIDTH = 600;
    private final int SMALL_ITEM_HEIGHT = 60;
    private final int SPACING = 30;
    private final Dimension SMALL_ITEM_SIZE = new Dimension(300, 50);
    private final Dimension NOTES_SIZE = new Dimension(200, 400);

    public CandidateView(View view) {
        this.view = view;
        initCandidateView();
    }

    private void initCandidateView(){
        setLayout(null);

        JPanel sidepanel = new Sidepanel(view);
        sidepanel.setBounds(1000, 0, 280, 700);
        add(sidepanel);
        setItemLabels();
        setTextInputFields();
        setNotesTextArea();

        revalidate();
        repaint();
    }

    private void setItemLabels(){
        JLabel name = new JLabel("First name:");
        JLabel surname = new JLabel("Last Name:");
        JLabel yearOfBirth = new JLabel("(Optional) Year of birth:");
        JLabel nationality = new JLabel("(Optional) Nationality:");
        JLabel notes = new JLabel("(Optional) Notes:");

        labels.add(name);
        labels.add(surname);
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
        JTextField name = new JTextField();
        name.setName("name");
        JTextField surname = new JTextField();
        surname.setName("surname");
        JTextField yearOfBirth = new JTextField();
        yearOfBirth.setName("year");
        JTextField nationality = new JTextField();
        nationality.setName("nationality");

        textFields.add(name);
        textFields.add(surname);
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
        notesWithScroll.setBounds(PANEL_X + LABEL_WIDTH, 420, TEXT_INPUT_WIDTH, 250);
        add(notesWithScroll);
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
