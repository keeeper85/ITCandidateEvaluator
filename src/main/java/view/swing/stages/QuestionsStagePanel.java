package view.swing.stages;

import view.swing.ViewConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class QuestionsStagePanel extends AbstractStage {

    private final int FILE_CHOOSER_X = 50;
    private final int QUESTION_CHOOSER_X = 350;
    private final int QUESTION_NAME_X = 650;
    private final int MENU_Y = 100;
    private final int ITEM_WIDTH = 250;
    private final int ITEM_HEIGHT = 20;
    private final int SPACING = 5;
    private TreeMap<String, List<String>> filesWithQuestionsForTesting;
    private HashMap<String, String> questionsEvaluated = new HashMap<>();
    private String selectedFile;
    private String selectedQuestion;
    private JTextField questionNameField;
    private JComboBox<String> questionChooser;

    public QuestionsStagePanel(StageView stageView) {
        super(stageView);
    }

    @Override
    protected void init() {
        add(createTitleLabel("Technical Questions Evaluation Stage"));
        add(createInfoLabel(ViewConstants.QUESTIONS_STAGE_INFO));
        add(createScoreSlider("questions"));

        initTestingMap();
        add(createFileChooser());
        add(createQuestionNameField());
        add(createQuestionChooser());
        add(createRandomQuestionButton());
    }

    private void initTestingMap(){
        filesWithQuestionsForTesting = new TreeMap<>();
        List<String> file1 = new ArrayList<>();
        List<String> file2 = new ArrayList<>();
        List<String> file3 = new ArrayList<>();
        List<String> file4 = new ArrayList<>();

        filesWithQuestionsForTesting.put("Java/Basics", file1);
        filesWithQuestionsForTesting.put("Java/Advanced", file2);
        filesWithQuestionsForTesting.put("SQL", file3);
        filesWithQuestionsForTesting.put("Spring", file4);

        for (Map.Entry<String, List<String>> entry : filesWithQuestionsForTesting.entrySet()) {
            List<String> list = entry.getValue();
            for (int i = 0; i < 5; i++) {
                String question = entry.getKey() + " [q]Question_" + i;
                list.add(question);
            }
        }
    }

    private JComboBox<String> createFileChooser(){
        String[] filesForTesting = {"Choose file:","Java/Basics", "Java/Advanced", "SQL", "Spring"};
        List<String> fileNames = new ArrayList<>();
        fileNames.addAll(filesWithQuestionsForTesting.keySet());
        JComboBox<String> fileChooser = new JComboBox<>(createComboModel(fileNames));
        fileChooser.setBounds(FILE_CHOOSER_X, MENU_Y, ITEM_WIDTH, ITEM_HEIGHT);
        fileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedFile = fileChooser.getSelectedItem().toString();
                remove(questionChooser);
                add(createQuestionChooser());
            }
        });
        return fileChooser;
    }
    private JTextField createQuestionNameField(){
        questionNameField = new JTextField();
        questionNameField.setBounds(QUESTION_NAME_X, MENU_Y, ITEM_WIDTH, ITEM_HEIGHT);
        questionNameField.setText("...");
        return questionNameField;
    }
    private JComboBox<String> createQuestionChooser(){
        List<String> questionList = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : filesWithQuestionsForTesting.entrySet()) {
            String fileName = entry.getKey();
            if (fileName.equals(selectedFile)) {questionList = entry.getValue();}
        }
        questionChooser = new JComboBox<>(createComboModel(questionList));
        questionChooser.setBounds(QUESTION_CHOOSER_X, MENU_Y, ITEM_WIDTH, ITEM_HEIGHT);
        questionChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedQuestion = "[question]" + questionChooser.getSelectedItem().toString();
                updateTextFields(selectedQuestion);
            }
        });
        return questionChooser;
    }
    private DefaultComboBoxModel<String> createComboModel(List<String> list){
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        for (String value : list) {
            comboBoxModel.addElement(value);
        }

        return comboBoxModel;
    }

    private void updateTextFields(String newText){
        questionNameField.setText(newText);
        infoLabel.setText(newText);
    }

    private JButton createRandomQuestionButton(){
        JButton randomQuestion = new JButton();
        int positionY = MENU_Y + ITEM_HEIGHT + SPACING;
        randomQuestion.setBounds(QUESTION_NAME_X, positionY, ITEM_WIDTH, ITEM_HEIGHT);
        randomQuestion.setText("Pick random question");
        randomQuestion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numberOfQuestionsInTheList = 0;
                List<String> questionList = null;
                for (Map.Entry<String, List<String>> entry : filesWithQuestionsForTesting.entrySet()) {
                    String fileName = entry.getKey();
                    if (fileName.equals(selectedFile)) {
                        questionList = entry.getValue();
                        numberOfQuestionsInTheList = questionList.size();}
                }
                if (questionList != null && questionList.size() > 0){
                    int randomIndex = ThreadLocalRandom.current().nextInt(0, numberOfQuestionsInTheList);
                    String randomQuestion = questionList.get(randomIndex);
                    updateTextFields(randomQuestion);
                }
            }
        });
        return randomQuestion;
    }

    public void updateQuestionsEvaluated(){
        String stringFromScoreSliderValue = String.valueOf(scoreSlider.getValue());
        questionsEvaluated.put(questionNameField.getText(), stringFromScoreSliderValue);
        scoreSlider.setValue(SLIDER_DEFAULT_VALUE);
    }

    public int getNumberOfQuestionsEvaluated(){
        return questionsEvaluated.size();
    }

    @Override
    public HashMap<String, String> collectData() {
        selectedQuestion = questionNameField.getText();
        if (!questionsEvaluated.containsKey(selectedQuestion)){
            String taggedKey = "[q]" + selectedQuestion;
            questionsEvaluated.put(taggedKey, String.valueOf(scoreSlider.getValue()));
        }
        return questionsEvaluated;
    }
}
