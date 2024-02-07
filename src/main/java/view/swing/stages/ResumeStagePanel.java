package view.swing.stages;

import view.swing.ViewConstants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.HashMap;

public class ResumeStagePanel extends JPanel implements Collectable {

    private StageView stageView;
    private JLabel scoreLabel;
    JSlider scoreSlider;
    private final int POSITION_X = 50;
    private final int INITIAL_POSITION_Y = 0;
    private final int SCORE_LABEL_POSITION_Y = 520;
    private final int TITLE_WIDTH = 500;
    private final int TITLE_HEIGHT = 50;
    private final int INFO_WIDTH = 900;
    private final int INFO_HEIGHT = 500;
    private final int SLIDER_WIDTH = 800;
    private final int SLIDER_HEIGHT = 40;

    public ResumeStagePanel(StageView view) {
        this.stageView = view;
        init();
    }

    private void init(){
        setLayout(null);
        add(createTitleLabel());
        add(createInfoLabel());
        add(createScoreSlider());
        add(createScoreLabel());
    }

    private JLabel createTitleLabel(){
        JLabel title = new JLabel("Resume & Social Media Evaluation Stage");
        title.setFont(ViewConstants.FONT_LARGE);
        title.setBounds(POSITION_X,INITIAL_POSITION_Y,TITLE_WIDTH,TITLE_HEIGHT);
        return title;
    }

    private JLabel createInfoLabel(){
        JLabel info = new JLabel(ViewConstants.RESUME_STAGE_INFO);
        info.setFont(ViewConstants.FONT_STAGE_INFO);

        int positionY = INITIAL_POSITION_Y + TITLE_HEIGHT;
        info.setBounds(POSITION_X,positionY,INFO_WIDTH,INFO_HEIGHT);
        return info;
    }

    private JSlider createScoreSlider(){
        scoreSlider = new JSlider();
        scoreSlider.setName("resumeSlider");
        int positionX = POSITION_X + POSITION_X;
        int positionY = INITIAL_POSITION_Y + TITLE_HEIGHT + INFO_HEIGHT;
        scoreSlider.setBounds(positionX,positionY,SLIDER_WIDTH,SLIDER_HEIGHT);
        scoreSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateScoreLabel(scoreSlider.getValue());
            }
        });

        return scoreSlider;
    }

    private JLabel createScoreLabel(){
        scoreLabel = new JLabel("Move the slider to evaluate candidate's performance at this stage:");
        scoreLabel.setBounds(POSITION_X, SCORE_LABEL_POSITION_Y, INFO_WIDTH, SLIDER_HEIGHT);

        return scoreLabel;
    }

    private void updateScoreLabel(int sliderValue){
        String description = "";

        if (sliderValue < 15) description = ViewConstants.SLIDER_VALUE_DESCRIPTION[0];
        else if (sliderValue >= 15 && sliderValue < 30) description = ViewConstants.SLIDER_VALUE_DESCRIPTION[1];
        else if (sliderValue >= 30 && sliderValue < 45) description = ViewConstants.SLIDER_VALUE_DESCRIPTION[2];
        else if (sliderValue >= 45 && sliderValue < 60) description = ViewConstants.SLIDER_VALUE_DESCRIPTION[3];
        else if (sliderValue >= 60 && sliderValue < 75) description = ViewConstants.SLIDER_VALUE_DESCRIPTION[4];
        else if (sliderValue >= 75 && sliderValue < 90) description = ViewConstants.SLIDER_VALUE_DESCRIPTION[5];
        else if (sliderValue >= 90) description = ViewConstants.SLIDER_VALUE_DESCRIPTION[6];

        scoreLabel.setText("Current proficiency: " + description);
    }

    @Override
    public HashMap<String, String> collectData() {
        HashMap<String, String> resumeScore = new HashMap<>();
        int value = scoreSlider.getValue();
        resumeScore.put("resume", String.valueOf(value));

        return resumeScore;
    }
}
