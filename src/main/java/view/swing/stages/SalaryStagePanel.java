package view.swing.stages;

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
    private JTextField fromField;
    private JTextField toField;
    private int from = 0;
    private int to = 0;

    public SalaryStagePanel(StageView stageView) {
        super(stageView);
    }

    @Override
    protected void init() {
        add(createTitleLabel("Salary Expectations Evaluation Stage"));
        add(createInfoLabel(ViewConstants.SALARY_STAGE_INFO));
        add(createScoreLabel());
        scoreLabel.setText("Move the slider to match the candidate's salary expectations:");

        add(createFromField());
        add(createToField());
        add(createScoreSlider("salary"));
    }

    private JTextField createFromField(){
        fromField = new JTextField();
        fromField.setText("5000");
        fromField.setBounds(FROM_FIELD_X, TEXT_FIELDS_Y, FIELD_WIDTH, FIELD_HEIGHT);
        return fromField;
    }

    @Override
    protected JSlider createScoreSlider(String sliderName) {
//        return super.createScoreSlider(sliderName);
        scoreSlider = new JSlider();
        scoreSlider.setName(sliderName);
        scoreSlider.setMajorTickSpacing(1000);
        scoreSlider.setMinorTickSpacing(500);
//        scoreSlider.setPaintTicks(true);
//        scoreSlider.setPaintLabels(true);
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
        toField.setText("9000");
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
        HashMap<String, String> score = new HashMap<>();
        double expectedSalary = scoreSlider.getValue();
        double average = (to + from) / 2;
        int ratio = (int) (expectedSalary / average * 100);
        score.put(scoreSlider.getName(), String.valueOf(ratio));

        return score;
    }
}
