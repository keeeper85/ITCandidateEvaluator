package view.swing.stages;

import controller.CandidateDTO;
import model.Stages;
import view.swing.ViewConstants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SalaryStagePanel extends AbstractStage { ;
    private final int TEXT_FIELDS_Y = 640;
    private final int FROM_FIELD_X = 110;
    private final int TO_FIELD_X = 815;
    private final int FIELD_WIDTH = 80;
    private final int FIELD_HEIGHT = 30;
    private final int AVERAGE_FACTOR = 2;
    private final int PERCENT = 100;
    private JTextField fromField;
    private JTextField toField;
    private int from;
    private int to;
    private int expectedSalary;

    public SalaryStagePanel(StageView stageView) {
        super(stageView);

        from = temporaryCandidate.getMinOfferedSalary();
        to = temporaryCandidate.getMaxOfferedSalary();
        expectedSalary = temporaryCandidate.getExpectedSalary();

        fromField.setText(String.valueOf(from));
        toField.setText(String.valueOf(to));
        updateScoreSlider();
        scoreSlider.setValue(expectedSalary);
    }

    @Override
    protected void startingHook() {
        stage = Stages.SALARY;
        ordinal = stage.getStageOrdinal();
        add(createScrollableInfoLabel(ViewConstants.SALARY_STAGE_INFO));

        add(createScoreLabel());
        scoreLabel.setText("Move the slider to match (roughly) the candidate's salary expectations:");

        add(createFromField());
        add(createToField());
    }

    @Override
    protected void finishingHook() {
        remove(scoreSlider);
        add(createScoreSlider(stage.getStageName()));
    }

    private JTextField createFromField(){
        fromField = new JTextField();
        fromField.setText(String.valueOf(from));
        fromField.setBounds(FROM_FIELD_X, TEXT_FIELDS_Y, FIELD_WIDTH, FIELD_HEIGHT);
        return fromField;
    }

    @Override
    protected JSlider createScoreSlider(String sliderName) {
        scoreSlider = new JSlider();
        scoreSlider.setName(sliderName);

        int positionX = POSITION_X + POSITION_X;
        scoreSlider.setBounds(positionX,SLIDER_POSITION_Y,SLIDER_WIDTH,SLIDER_HEIGHT);

        try{
            from = Integer.parseInt(fromField.getText());
            to = Integer.parseInt(toField.getText());
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, ViewConstants.SALARY_INPUT_ERROR_MESSAGE, "Salary input error", JOptionPane.WARNING_MESSAGE);
        }

        if (from >= to) JOptionPane.showMessageDialog(null, ViewConstants.SALARY_INPUT_ERROR_MESSAGE, "Salary input error", JOptionPane.WARNING_MESSAGE);
        else{
            scoreSlider.setMinimum(from);
            scoreSlider.setMaximum(to);

            scoreSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    String expectedSalary = "Expected salary: " + scoreSlider.getValue();
                    scoreLabel.setText(expectedSalary);
                    updateScoreSlider();
                }
            });
        }

        return scoreSlider;
    }

    private JTextField createToField(){
        toField = new JTextField();
        toField.setText("10000");
        toField.setBounds(TO_FIELD_X, TEXT_FIELDS_Y, FIELD_WIDTH, FIELD_HEIGHT);
        return toField;
    }

    public boolean updateScoreSlider(){
        try{
            from = Integer.parseInt(fromField.getText());
            to = Integer.parseInt(toField.getText());
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, ViewConstants.SALARY_INPUT_ERROR_MESSAGE, "Salary input error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (from >= to) {
            JOptionPane.showMessageDialog(null, ViewConstants.SALARY_INPUT_ERROR_MESSAGE, "Salary input error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else{
            scoreSlider.setMinimum(from);
            scoreSlider.setMaximum(to);
        }
        return true;
    }

    @Override
    public boolean collectData() {
        boolean areNumbersOK = updateScoreSlider();

        CandidateDTO temporaryCandidate = stageView.getTemporaryCandidate();
        temporaryCandidate.setMinOfferedSalary(from);
        temporaryCandidate.setMaxOfferedSalary(to);
        temporaryCandidate.setExpectedSalary(scoreSlider.getValue());

        double expectedSalary = scoreSlider.getValue();
        double average = (to + from) / AVERAGE_FACTOR;
        int ratio = (int) (expectedSalary / average * PERCENT);
        temporaryCandidate.getRawScores().put(scoreSlider.getName(), ratio);

        return areNumbersOK;
    }
}
