package view.swing.stages;

import model.AbstractCandidate;
import model.Candidate;
import view.swing.View;
import view.swing.ViewConstants;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CandidateView extends JPanel implements Collectable {

    public final int ordinal = 0;
    private StageView stageView;
    private AbstractCandidate candidate;
    private List<JLabel> labels = new ArrayList<>();
    private List<JTextField> textFields = new ArrayList<>();
    private JTextArea notes;
    private JTextField firstName;
    private JTextField lastName;
    private JTextField resumePath;
    private JTextField yearOfBirth;
    private final int PANEL_X = 50;
    private final int PANEL_Y = 50;
    private final int LABEL_WIDTH = 300;
    private final int TEXT_INPUT_WIDTH = 600;
    private final int SMALL_ITEM_SIZE = 60;
    private final int SPACING = 30;
    private final int NOTES_POSITION_X = PANEL_X + LABEL_WIDTH;
    private final int NOTES_POSITION_Y = 420;
    private final int NOTES_WIDTH = TEXT_INPUT_WIDTH;
    private final int NOTES_HEIGHT = 250;
    private final int SWAP_BUTTON_POSITION_X = 615;
    private final int SWAP_BUTTON_POSITION_Y = 112;
    private final int SWAP_BUTTON_WIDTH = 80;
    private final int SWAP_BUTTON_HEIGHT = 25;
    private final int PICK_BUTTON_POSITION_X = 890;
    private final int PICK_BUTTON_POSITION_Y = 320;

    public CandidateView(StageView stageView, AbstractCandidate candidate) {
        this.stageView = stageView;
        this.candidate = candidate;
        candidate = stageView.getCandidate();
        initCandidateView();
        if (candidate != null) copyCandidateData();
    }

    private void initCandidateView(){
        setLayout(null);

        setItemLabels();
        setTextInputFields();
        setNotesTextArea();
        setSwapButton();
        setPickResumeFileButton();

        revalidate();
        repaint();
    }

    private void copyCandidateData(){
        System.out.println("copied");
        firstName.setText(candidate.getFirstName());
        lastName.setText(candidate.getLastName());
        yearOfBirth.setText(String.valueOf(candidate.getYearOfBirth()));
        resumePath.setText(candidate.getPathToResumeFile());
        notes.setText(candidate.getAdditionalNotes());

        repaint();
        revalidate();
    }

    private void setItemLabels(){
        JLabel name = new JLabel("First name:");
        JLabel lastName = new JLabel("Last name:");
        JLabel yearOfBirth = new JLabel("(Optional) Year of birth:");
        JLabel resume = new JLabel("(Optional) Resume file:");
        JLabel notes = new JLabel("(Optional) Notes:");

        labels.add(name);
        labels.add(lastName);
        labels.add(yearOfBirth);
        labels.add(resume);
        labels.add(notes);

        int initialPositionY = PANEL_Y;

        for (JLabel label : labels) {
            label.setFont(ViewConstants.FONT_LARGE);
            label.setBounds(PANEL_X, initialPositionY, LABEL_WIDTH, SMALL_ITEM_SIZE);
            add(label);
            initialPositionY += SMALL_ITEM_SIZE + SPACING;
        }
    }

    private void setTextInputFields(){
        firstName = new JTextField();
        firstName.setName("name");
        lastName = new JTextField();
        lastName.setName("lastName");
        yearOfBirth = new JTextField();
        yearOfBirth.setName("year");
        resumePath = new JTextField();
        resumePath.setName("resumePath");

        textFields.add(firstName);
        textFields.add(lastName);
        textFields.add(yearOfBirth);
        textFields.add(resumePath);

        int initialPositionY = PANEL_Y;
        int initialPositionX = PANEL_X + LABEL_WIDTH;

        for (JTextField textField : textFields) {
            textField.setBounds(initialPositionX, initialPositionY, TEXT_INPUT_WIDTH, SMALL_ITEM_SIZE);
            textField.setFont(ViewConstants.FONT_LARGE);
            add(textField);
            initialPositionY += SMALL_ITEM_SIZE + SPACING;
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

    private void setPickResumeFileButton(){
        JButton pickResumeFileButton = new JButton();
        pickResumeFileButton.setText("PICK");
        pickResumeFileButton.setFont(ViewConstants.FONT_SMALL);
        pickResumeFileButton.setBounds(PICK_BUTTON_POSITION_X, PICK_BUTTON_POSITION_Y, SMALL_ITEM_SIZE, SMALL_ITEM_SIZE);
        resumePath.setBounds(PANEL_X + LABEL_WIDTH, PICK_BUTTON_POSITION_Y, TEXT_INPUT_WIDTH - SMALL_ITEM_SIZE, SMALL_ITEM_SIZE);

        pickResumeFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select resume file (.pdf files only):");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF files (*.pdf)", "pdf");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    resumePath.setText(fileChooser.getSelectedFile().toString());
                }
            }
        });

        add(pickResumeFileButton);
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
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

    @Override
    public int getOrdinal() {
        return ordinal;
    }
}
