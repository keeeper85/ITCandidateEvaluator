package view.swing;

import view.swing.stages.CandidateView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PresetsView extends JPanel {

    private View view;
    private String[] choices = {"Junior", "Regular", "Senior", "Custom"};
    private String chosenPreset = choices[0];
    private JTextField presetName;
    private List<JButton> buttons = new ArrayList<>();
    private final int TOP_ROW_Y = 30;
    private final int TOP_ROW_X = 250;
    private final int SMALL_ITEM_WIDTH = 200;
    private final int SMALL_ITEM_HEIGHT = 40;
    private final int LARGE_SPACING = 50;
    private final int MEDIUM_SPACING = 15;
    private final int SMALL_SPACING = 5;
    private final int SLIDER_ROW_Y = TOP_ROW_Y + SMALL_ITEM_HEIGHT + LARGE_SPACING;
    private final int SLIDER_ROW_X = 600;
    private final int SLIDER_ROW_LABLE_X = 100;
    private final int SLIDER_LABLE_WIDTH = 450;
    private final int SLIDER_WIDTH = 500;
    private final int SLIDER_HEIGHT = 50;
    private final int BOTTOM_ROW_X = 200;
    private final int BOTTOM_ROW_Y = 620;

    public PresetsView(View view) {
        this.view = view;
        initPresetsView();
    }

    private void initPresetsView(){
        setLayout(null);
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
            button.setFont(ViewConstants.BUTTON_FONT_LARGE);
            button.setBounds(buttonXposition, BOTTOM_ROW_Y, SMALL_ITEM_WIDTH, SMALL_ITEM_HEIGHT);
            add(button);
            buttonXposition += SMALL_ITEM_WIDTH + MEDIUM_SPACING;
        }
    }

    private void addChoosePreset() {
        JLabel choosePreset = new JLabel("Choose preset:");
        choosePreset.setFont(ViewConstants.BUTTON_FONT_LARGE);
        choosePreset.setBounds(TOP_ROW_X, TOP_ROW_Y, SMALL_ITEM_WIDTH, SMALL_ITEM_HEIGHT);
        add(choosePreset);

        JComboBox<String> choiceMenu = new JComboBox<>(choices);
        choiceMenu.setFont(ViewConstants.BUTTON_FONT_LARGE);
        int choiceMenuX = TOP_ROW_X + SMALL_ITEM_WIDTH;
        choiceMenu.setBounds(choiceMenuX, TOP_ROW_Y, SMALL_ITEM_WIDTH, SMALL_ITEM_HEIGHT);
        choiceMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update text field with the selected item
                presetName.setText((String) choiceMenu.getSelectedItem());
                repaint();
            }
        });
        add(choiceMenu);
    }
    private void addPresetNameField(){
        presetName = new JTextField(chosenPreset);
        presetName.setFont(ViewConstants.BUTTON_FONT_LARGE);
        int presetNameX = TOP_ROW_X + (2 * SMALL_ITEM_WIDTH) + LARGE_SPACING;
        presetName.setBounds(presetNameX, TOP_ROW_Y, SMALL_ITEM_WIDTH, SMALL_ITEM_HEIGHT);
        add(presetName);
    }

    private void addSliders(){
        String[] labels = {"Resume and social media evaluation", "English language assessment", "Previous work experience",
        "Own projects", "Live coding", "Technical questions", "Salary expectations", "Soft skills"};

        int sliderPositionY = SLIDER_ROW_Y;

        for (String label : labels) {
            add(createSliderLabel(label, sliderPositionY));
            add(createSliderPanel(sliderPositionY));
            sliderPositionY += SLIDER_HEIGHT + SMALL_SPACING;
        }
    }

    private JSlider createSliderPanel(int sliderPositionY) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
        slider.setBounds(SLIDER_ROW_X, sliderPositionY, SLIDER_WIDTH, SLIDER_HEIGHT);
        slider.setMajorTickSpacing(2);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        // Add a ChangeListener to retrieve the slider value
//        slider.addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                int sliderValue = ((JSlider) e.getSource()).getValue();
//                // You can use the sliderValue as needed
//                System.out.println(label + ": " + sliderValue);
//            }
//        });

        return slider;
    }

    private JLabel createSliderLabel(String lableText, int sliderPositionY){
        JLabel label = new JLabel(lableText);
        label.setBounds(SLIDER_ROW_LABLE_X, sliderPositionY, SLIDER_LABLE_WIDTH, SMALL_ITEM_HEIGHT);
        label.setFont(ViewConstants.BUTTON_FONT_LARGE);
        return label;
    }

    private JButton createBackButton(){
        JButton backButton = new JButton("Back");
        backButton.addActionListener((e -> {view.setCurrentPanel(new InitialView(view));}));
        return backButton;
    }

    private JButton createInfoButton(){
        JButton infoButton = new JButton("How to?");
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(infoButton.getParent(), ViewConstants.PRESETS_HOWTO);
            }
        });
        return infoButton;
    }

    private JButton createSavePresetButton(){
        JButton saveButton = new JButton("Save Presets");
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
                if (name.equals("") || name == null || name.length() > 20){

                }
                else{
                    dialog.dispose();
                    view.setCurrentPanel(new CandidateView());
                }
            }
        });

        dialog.add(backButton);
        dialog.add(continueButton);

        dialog.setSize(300, 150);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }




}
