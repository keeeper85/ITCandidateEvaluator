package view.swing;

import model.Model;
import model.Recruitment;
import model.Stages;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class PresetsView extends JPanel {

    private View view;
    private Model model;
    private String[] presetChoices;
    private Map<String,Map<String, Integer>> presets;
    private HashMap<String, Integer> currentSliderSettings = new HashMap<>();
    private String chosenPreset;
    private JTextField presetName;
    private List<JButton> buttons = new ArrayList<>();
    private List<JSlider> sliders = new ArrayList<>();
    private final int TOP_ROW_Y = 30;
    private final int TOP_ROW_X = 250;
    private final int SMALL_ITEM_WIDTH = 200;
    private final int SMALL_ITEM_HEIGHT = 40;
    private final int LARGE_SPACING = 50;
    private final int MEDIUM_SPACING = 15;
    private final int SMALL_SPACING = 5;
    private final int SLIDER_ROW_Y = TOP_ROW_Y + SMALL_ITEM_HEIGHT + LARGE_SPACING;
    private final int SLIDER_ROW_X = 600;
    private final int SLIDER_ROW_LABEL_X = 100;
    private final int SLIDER_LABEL_WIDTH = 450;
    private final int PRESET_SPACING = 450;
    private final int SLIDER_WIDTH = 500;
    private final int SLIDER_HEIGHT = 50;
    private final int SLIDER_MIN_VALUE = 0;
    private final int SLIDER_MAX_VALUE = 10;
    private final int SLIDER_DEFAULT_VALUE = 5;
    private final int SLIDER_MAJOR_SPACING = 2;
    private final int SLIDER_MINOR_SPACING = 1;
    private final int RECRUITMENT_NAME_DIALOG_WIDTH = 300;
    private final int RECRUITMENT_NAME_DIALOG_HEIGHT = 150;
    private final int RECRUITMENT_NAME_MAX_LENGTH_LETTERS = 20;
    private final int BOTTOM_ROW_X = 200;
    private final int BOTTOM_ROW_Y = 620;

    public PresetsView(View view) {
        this.view = view;
        model = view.getModel();
        initPresetsView();
    }

    private void initPresetsView(){
        setLayout(null);
        loadPresetsFromModel();
        addChoosePreset();
        addPresetNameField();
        addSliders();
        setButtons();
    }
    private void setButtons() {
        buttons.add(createInfoButton());
        buttons.add(createBackButton());
        buttons.add(createSavePresetButton());
        buttons.add(startRecruitment());

        int buttonXposition = BOTTOM_ROW_X;

        for (JButton button : buttons) {
            button.setFont(ViewConstants.FONT_LARGE);
            button.setBounds(buttonXposition, BOTTOM_ROW_Y, SMALL_ITEM_WIDTH, SMALL_ITEM_HEIGHT);
            add(button);
            buttonXposition += SMALL_ITEM_WIDTH + MEDIUM_SPACING;
        }
    }

    private void loadPresetsFromModel(){
        presets = model.getListOfPresets();

        List<String> presetNames = new ArrayList<>(presets.keySet());
        Collections.sort(presetNames);
        presetChoices = presetNames.toArray(new String[0]);
        chosenPreset = presetChoices[0];
    }

    private void applyLoadedPresets(){
        Map<String, Integer> slidersWithValues = getCurrentPreset();

        try{
            for (JSlider slider : sliders) {
                String sliderName = slider.getName().toLowerCase();
                int count = 0;

                for (Map.Entry<String, Integer> entry : slidersWithValues.entrySet()) {
                    String entryName = entry.getKey();
                    if (sliderName.contains(entryName)){
                        slider.setValue(entry.getValue());
                        break;
                    }
                    if (count == slidersWithValues.size() - 1) slider.setValue(0);
                    count++;
                }
            }
        } catch (NullPointerException nullPreset){
            //todo logger
            throw new RuntimeException();
        }

    }

    private Map<String, Integer> getCurrentPreset() {
        for (Map.Entry<String, Map<String, Integer>> entry : presets.entrySet()) {
            String presetName = entry.getKey();
            if (presetName.equals(chosenPreset)) return entry.getValue();
        }
        return null;
    }

    private void addChoosePreset() {
        JLabel choosePreset = new JLabel("Choose preset:");
        choosePreset.setFont(ViewConstants.FONT_LARGE);
        choosePreset.setBounds(TOP_ROW_X, TOP_ROW_Y, SMALL_ITEM_WIDTH, SMALL_ITEM_HEIGHT);
        add(choosePreset);

        JComboBox<String> choiceMenu = new JComboBox<>(presetChoices);
        choiceMenu.setFont(ViewConstants.FONT_LARGE);
        int choiceMenuX = TOP_ROW_X + SMALL_ITEM_WIDTH;
        choiceMenu.setBounds(choiceMenuX, TOP_ROW_Y, SMALL_ITEM_WIDTH, SMALL_ITEM_HEIGHT);
        choiceMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presetName.setText((String) choiceMenu.getSelectedItem());
                chosenPreset = (String) choiceMenu.getSelectedItem();
                applyLoadedPresets();
                repaint();
            }
        });
        add(choiceMenu);
    }
    private void addPresetNameField(){
        presetName = new JTextField(chosenPreset);
        presetName.setFont(ViewConstants.FONT_LARGE);
        int presetNameX = TOP_ROW_X + PRESET_SPACING;
        presetName.setBounds(presetNameX, TOP_ROW_Y, SMALL_ITEM_WIDTH, SMALL_ITEM_HEIGHT);
        add(presetName);
    }

    private void addSliders(){
        int sliderPositionY = SLIDER_ROW_Y;

        for (Stages label : Stages.values()) {
            add(createSliderLabel(label.getStageTitle(), sliderPositionY));
            add(createSlider(label.getStageTitle(), sliderPositionY));
            sliderPositionY += SLIDER_HEIGHT + SMALL_SPACING;
        }
    }

    private JSlider createSlider(String name, int sliderPositionY) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, SLIDER_MIN_VALUE, SLIDER_MAX_VALUE, SLIDER_DEFAULT_VALUE);
        slider.setName(name);
        slider.setBounds(SLIDER_ROW_X, sliderPositionY, SLIDER_WIDTH, SLIDER_HEIGHT);
        slider.setMajorTickSpacing(SLIDER_MAJOR_SPACING);
        slider.setMinorTickSpacing(SLIDER_MINOR_SPACING);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        currentSliderSettings.put(slider.getName(), slider.getValue());
        sliders.add(slider);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int sliderValue = ((JSlider) e.getSource()).getValue();
                currentSliderSettings.put(slider.getName(), sliderValue);
            }
        });

        return slider;
    }

    private JLabel createSliderLabel(String labelText, int sliderPositionY){
        JLabel label = new JLabel(labelText);
        label.setBounds(SLIDER_ROW_LABEL_X, sliderPositionY, SLIDER_LABEL_WIDTH, SMALL_ITEM_HEIGHT);
        label.setFont(ViewConstants.FONT_LARGE);
        return label;
    }

    private JButton createBackButton(){
        JButton backButton = new JButton("Back");
        backButton.addActionListener((e -> {view.returnToPreviousPanel();}));
        return backButton;
    }

    private JButton createInfoButton(){
        JButton infoButton = new JButton("How to?");
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, ViewConstants.PRESETS_HOWTO);
            }
        });
        return infoButton;
    }

    private JButton createSavePresetButton(){
        JButton saveButton = new JButton("Save Presets");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePresets();
            }
        });
        return saveButton;
    }

    private JButton startRecruitment(){
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRecruitmentNameDialog();
            }
        });
        return startButton;
    }

    private void setRecruitmentNameDialog() {
        JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        dialog.setTitle("Process name (date will be added automatically)");
        dialog.setLayout(new GridLayout(3, 2));

        JTextField nameField = new JTextField();
        nameField.setText("(1-20 characters)");
        nameField.selectAll();
        dialog.add(new JLabel("   Recruitment name:"));
        dialog.add(nameField);

        JButton backButton = new JButton("Back");
        JButton continueButton = new JButton("Continue");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                if (name.equals("") || name == null || name.length() > RECRUITMENT_NAME_MAX_LENGTH_LETTERS){}
                else{
                    CandidateListView candidateListView = new CandidateListView(view);
                    Recruitment recruitment = model.startNewRecruitment(name, presetName.getText(), currentSliderSettings);
                    if (recruitment == null) {
                        dialog.dispose();
                        JOptionPane.showMessageDialog(null, "This name is taken or invalid (use only characters allowed for naming files). Try different.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        candidateListView.setRecruitment(recruitment);
                        dialog.dispose();
                        view.setCurrentPanel(new RecruitmentsListView(view));
                    }
                }
            }
        });

        dialog.add(backButton);
        dialog.add(continueButton);

        dialog.setSize(RECRUITMENT_NAME_DIALOG_WIDTH, RECRUITMENT_NAME_DIALOG_HEIGHT);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public void savePresets() {
        HashMap<String, Integer> presets = new HashMap<>();
        String result;

        for (JSlider slider : sliders) {
            presets.put(slider.getName(), slider.getValue());
        }

        if (model.savePresetsToFile(presetName.getText(),presets)){
            result = "Presets file saved successfully.";
            loadPresetsFromModel();
        }
        else result = "There was a problem saving presets to file. Check the log.";

        JOptionPane.showMessageDialog(null, result, "Saving presets:", JOptionPane.INFORMATION_MESSAGE);
    }



}
