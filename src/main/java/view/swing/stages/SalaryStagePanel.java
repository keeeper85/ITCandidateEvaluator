package view.swing.stages;

import controller.CandidateDTO;
import view.swing.ViewConstants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.HashMap;

public class SalaryStagePanel extends AbstractStage {
    private final int TEXT_FIELDS_Y = 640;
    private final int FROM_FIELD_X = 110;
    private final int TO_FIELD_X = 815;
    private final int FIELD_WIDTH = 80;
    private final int FIELD_HEIGHT = 30;
    private final int AVERAGE_FACTOR = 2;
    private final int PERCENT = 100;
    private final String EXAMPLE_MIN_SALARY = "5000";
    private final String EXAMPLE_MAX_SALARY = "9000";
    private JTextField fromField;
    private JTextField toField;
    private int from = 0;
    private int to = 0;

    public SalaryStagePanel(StageView stageView) {
        super(stageView);
        ordinal = 7;
    }

    @Override
    protected void init() {
        add(createTitleLabel("Salary Expectations Evaluation Stage"));
        add(createScrollableInfoLabel(ViewConstants.SALARY_STAGE_INFO));
        add(createScoreLabel());
        scoreLabel.setText("Move the slider to match (roughly) the candidate's salary expectations:");

        add(createFromField());
        add(createToField());
        add(createScoreSlider("salary"));
    }

    private JTextField createFromField(){
        fromField = new JTextField();
        fromField.setText(EXAMPLE_MIN_SALARY);
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
        toField.setText(EXAMPLE_MAX_SALARY);
        toField.setBounds(TO_FIELD_X, TEXT_FIELDS_Y, FIELD_WIDTH, FIELD_HEIGHT);
        return toField;
    }

    private void updateScoreSlider(){
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
        }
    }

    @Override
    public HashMap<String, String> collectData() {
        updateScoreSlider();

        CandidateDTO temporaryCandidate = stageView.getCandidate();
        temporaryCandidate.setMinOfferedSalary(from);
        temporaryCandidate.setMaxOfferedSalary(to);
        temporaryCandidate.setExpectedSalary(scoreSlider.getValue());

        HashMap<String, String> score = new HashMap<>();
        double expectedSalary = scoreSlider.getValue();
        double average = (to + from) / AVERAGE_FACTOR;
        int ratio = (int) (expectedSalary / average * PERCENT);
        score.put(scoreSlider.getName(), String.valueOf(ratio));
        temporaryCandidate.getRawScores().put(scoreSlider.getName(), String.valueOf(ratio));
        score.put("money", String.valueOf(expectedSalary));

        return score;
    }
}
