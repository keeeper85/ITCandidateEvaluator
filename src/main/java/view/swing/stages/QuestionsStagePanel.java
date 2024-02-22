package view.swing.stages;

import controller.CandidateDTO;
import model.Question;
import model.Stages;
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
    private final int MENU_Y = 60;
    private final int ITEM_WIDTH = 250;
    private final int ITEM_HEIGHT = 20;
    private final int SPACING = 5;
    private boolean isQuestionAsked = false;
    private List<Question> questions;
    private List<String> filesList;
    private List<Question> questionsDisplayedInChooser;
    private Map<Question, Integer> questionsEvaluatedForCollection;
    private List<String> questionsEvaluatedForDisplay;
    private String selectedFile = chooseFile;
    private String selectedQuestion = "";
    private JTextField questionNameField;
    private JComboBox<Question> questionChooser;

    public QuestionsStagePanel(StageView stageView) {
        super(stageView);

        CandidateDTO temporaryCandidate = stageView.getTemporaryCandidate();
        questionsEvaluatedForCollection = temporaryCandidate.getEvaluatedQuestions();
        questionsEvaluatedForDisplay = temporaryCandidate.getQuestionsEvaluatedForDisplay();
        if (questionsEvaluatedForDisplay.size() > 0) isQuestionAsked = true;
    }

    @Override
    protected void startingHook() {
        stage = Stages.QUESTIONS;
        ordinal = stage.getStageOrdinal();
        add(createScrollableInfoLabel(ViewConstants.QUESTIONS_STAGE_INFO));

        add(createQuestionNameField());
        initQuestions();
        add(createFileChooser());
        add(createQuestionChooser());
        add(createRandomQuestionButton());
    }

    private void initQuestions(){
        questions = model.getQuestionList();
        filesList = getFilesList(questions);
        questionsDisplayedInChooser = getQuestions(filesList.get(0), questions);
    }

    private List<String> getFilesList(List<Question> questions){
        List<String> filesList = new ArrayList<>();
        TreeSet<String> sortedUniqueFileNames = new TreeSet<>();
        filesList.add(chooseFile);
        selectedFile = filesList.get(0);

        for (Question question : questions) {
            String sourceFileName = question.getSourceFileName();
            sortedUniqueFileNames.add(sourceFileName);
        }
        filesList.addAll(sortedUniqueFileNames);

        return filesList;
    }

    private List<Question> getQuestions(String fileName, List<Question> questions) {
        List<Question> questionBodies = new ArrayList<>();
        for (Question question : questions) {
            if (fileName.equals(chooseFile)) {
                updateTextFields(ViewConstants.QUESTIONS_STAGE_INFO);
                break;
            }
            if (question.getSourceFileName().equals(fileName)) {
                questionBodies.add(question);
            }
        }

        return questionBodies;
    }

    private JComboBox<String> createFileChooser(){
        JComboBox<String> fileChooser = new JComboBox<>(createComboModel(filesList));
        fileChooser.setBounds(FILE_CHOOSER_X, MENU_Y, ITEM_WIDTH, ITEM_HEIGHT);
        fileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedFile = (String) fileChooser.getSelectedItem();
                questionsDisplayedInChooser = getQuestions(selectedFile,questions);
                questionChooser.setModel(createComboModel(questionsDisplayedInChooser));
            }
        });
        return fileChooser;
    }
    private JTextField createQuestionNameField(){
        questionNameField = new JTextField();
        questionNameField.setBounds(QUESTION_NAME_X, MENU_Y, ITEM_WIDTH, ITEM_HEIGHT);
        questionNameField.setText("...");
        questionNameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentInput = questionNameField.getText();
                updateTextFields(currentInput);
            }
        });

        return questionNameField;
    }
    private JComboBox<Question> createQuestionChooser(){
        questionChooser = new JComboBox<>(createComboModel(questionsDisplayedInChooser));
        questionChooser.setBounds(QUESTION_CHOOSER_X, MENU_Y, ITEM_WIDTH, ITEM_HEIGHT);
        questionChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Question question = (Question) questionChooser.getSelectedItem();
                selectedQuestion = question.getQuestionBody();
                updateTextFields(selectedQuestion);
            }
        });
        return questionChooser;
    }

    private <E> DefaultComboBoxModel<E> createComboModel(List<E> list){
        DefaultComboBoxModel<E> comboBoxModel = new DefaultComboBoxModel<>();
        for (E value : list) {
            comboBoxModel.addElement(value);
        }

        return comboBoxModel;
    }

    private void updateTextFields(String newText){
        if (!selectedFile.equals(chooseFile) && newText != null) {
            StringBuilder listBuilder = new StringBuilder();
            int questionCounter = 1;
            for (String questionEvaluated : questionsEvaluatedForDisplay) {
                listBuilder.append("<html>").append(questionCounter + ". ").append(questionEvaluated).append("<br>");
                questionCounter++;
            }
            if (questionCounter <= 2) listBuilder.append("<html>").append("<b>").append(newText).append("</b>").append("</html>");
            else listBuilder.append("<b>").append(newText).append("</b>").append("</html>");
            questionNameField.setText(newText);
            infoLabel.setText(listBuilder.toString());
        }
        else {
            questionNameField.setText("");
            infoLabel.setText(newText);
        }

    }

    private JButton createRandomQuestionButton(){
        JButton randomQuestion = new JButton();
        int positionY = MENU_Y + ITEM_HEIGHT + SPACING;
        randomQuestion.setBounds(QUESTION_NAME_X, positionY, ITEM_WIDTH, ITEM_HEIGHT);
        randomQuestion.setText("Pick random question");
        randomQuestion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int questionNumber = questionsDisplayedInChooser.size() - 1;
                    int randomIndex = ThreadLocalRandom.current().nextInt(0, questionNumber);
                    Question question = questionsDisplayedInChooser.get(randomIndex);
                    updateTextFields(question.getQuestionBody());
                } catch (IllegalArgumentException ignored){
                    JOptionPane.showMessageDialog(null,"Choose a file first.", "No file chosen", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return randomQuestion;
    }

    public void updateQuestionsEvaluated(){
        isQuestionAsked = true;
        String currentQuestion = questionNameField.getText();
        String errorMessage = "The question must have a name! Pick one from the list or type in your question.";
        if (currentQuestion.isEmpty()) JOptionPane.showMessageDialog(null, errorMessage, "Saving score: failure.", JOptionPane.ERROR_MESSAGE);
        else if (!questionsEvaluatedForCollection.containsKey(currentQuestion)){
            questionsEvaluatedForCollection.put(new Question("", currentQuestion), scoreSlider.getValue());
            questionsEvaluatedForDisplay.add(currentQuestion);
            scoreSlider.setValue(SLIDER_DEFAULT_VALUE);
            updateTextFields(selectedQuestion);
        }
        else {
            errorMessage = "You can't ask the same question twice.";
            JOptionPane.showMessageDialog(null, errorMessage, "Saving score: failure.", JOptionPane.ERROR_MESSAGE);
        }
    }

    public int getNumberOfQuestionsEvaluated(){
        return questionsEvaluatedForCollection.size();
    }

    @Override
    public boolean collectData() {
        CandidateDTO temporaryCandidate = stageView.getTemporaryCandidate();
        temporaryCandidate.getEvaluatedQuestions().putAll(questionsEvaluatedForCollection);
        temporaryCandidate.setQuestionsEvaluatedForDisplay(questionsEvaluatedForDisplay);
        int averageScore = temporaryCandidate.calculateQuestionsAverageScore();
        temporaryCandidate.getRawScores().put(scoreSlider.getName(), averageScore);

        if (!isQuestionAsked) JOptionPane.showMessageDialog(null, "Ask at least one questions to proceed.", "No questions asked", JOptionPane.WARNING_MESSAGE);

        return isQuestionAsked;
    }
}
