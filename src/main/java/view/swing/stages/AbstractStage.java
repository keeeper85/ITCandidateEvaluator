package view.swing.stages;

import view.swing.ViewConstants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.HashMap;

public abstract class AbstractStage extends JPanel implements Collectable {

    private StageView stageView;
    protected JLabel infoLabel;
    protected JLabel scoreLabel;
    protected JSlider scoreSlider;
    protected final int SLIDER_DEFAULT_VALUE = 50;
    protected final int POSITION_X = 50;
    private final int INITIAL_POSITION_Y = 0;
    protected final int SLIDER_POSITION_Y = 600;
    private final int TITLE_WIDTH = 900;
    private final int TITLE_HEIGHT = 50;
    private final int INFO_WIDTH = 900;
    private final int INFO_HEIGHT = 500;
    protected final int SLIDER_WIDTH = 800;
    protected final int SLIDER_HEIGHT = 40;

    public AbstractStage(StageView stageView) {
        this.stageView = stageView;
        setLayout(null);
        init();
    }

    protected abstract void init();

    protected JLabel createTitleLabel(String stageTitle){
        JLabel title = new JLabel(stageTitle);
        title.setFont(ViewConstants.FONT_LARGE);
        title.setBounds(POSITION_X,INITIAL_POSITION_Y,TITLE_WIDTH,TITLE_HEIGHT);
        return title;
    }

    protected JLabel createInfoLabel(String stageInfo){
        infoLabel = new JLabel(stageInfo);
        infoLabel.setFont(ViewConstants.FONT_STAGE_INFO);

        int positionY = INITIAL_POSITION_Y + TITLE_HEIGHT;
        infoLabel.setBounds(POSITION_X,positionY,INFO_WIDTH,INFO_HEIGHT);
        return infoLabel;
    }

    protected JSlider createScoreSlider(String sliderName){
        add(createScoreLabel());

        scoreSlider = new JSlider();
        scoreSlider.setName(sliderName);
        int positionX = POSITION_X + POSITION_X;
        scoreSlider.setBounds(positionX,SLIDER_POSITION_Y,SLIDER_WIDTH,SLIDER_HEIGHT);
        scoreSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateScoreLabel(scoreSlider.getValue());
            }
        });

        return scoreSlider;
    }

    protected JLabel createScoreLabel(){
        scoreLabel = new JLabel("Move the slider to evaluate candidate's performance at this stage:");
        scoreLabel.setBounds(POSITION_X, SLIDER_POSITION_Y - SLIDER_HEIGHT, INFO_WIDTH, SLIDER_HEIGHT);

        return scoreLabel;
    }

    private void updateScoreLabel(int sliderValue){
        String description = "";
        //The 'magic numbers' below are self-explanatory considering scoreSlider minimum value = 0 and maximum = 100
        //These values are only for descriptive purposes and won't matter in calculating final result

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
        HashMap<String, String> score = new HashMap<>();
        int value = scoreSlider.getValue();
        score.put(scoreSlider.getName(), String.valueOf(value));

        return score;
    }
}
