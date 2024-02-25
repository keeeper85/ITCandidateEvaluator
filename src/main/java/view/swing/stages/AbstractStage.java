package view.swing.stages;

import controller.CandidateDTO;
import model.Model;
import model.Stages;
import view.swing.ViewConstants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Map;

/**
 * AbstractStage class includes all methods which repeat themselves across the evaluation stages.
 * It follows Template Method design pattern -> initTemplate() method with two Hooks: starting and finishing
 * Thanks to the implementations here, the following stages are very short in code: Resume, Language, Experience, Projects and Soft Skills
 * The other stages use need to override some of the methods here to work as intended
 */

public abstract class AbstractStage extends JPanel implements Collectable {
    protected StageView stageView;
    protected Stages stage;
    protected Model model;
    protected CandidateDTO temporaryCandidate;
    protected int ordinal;
    protected String chooseFile = "Choose file:";
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
    private final int INFO_HEIGHT = 440;
    protected final int SLIDER_WIDTH = 800;
    protected final int SLIDER_HEIGHT = 40;
    protected final int SPACING = 60;

    public AbstractStage(StageView stageView) {
        this.stageView = stageView;
        this.model = stageView.getView().getModel();
        temporaryCandidate =  stageView.getTemporaryCandidate();
        setLayout(null);
        initTemplate();
    }

    protected void initTemplate(){
        startingHook();
        add(createTitleLabel(stage.getStageTitle()));
        add(createScoreSlider(stage.getStageName()));
        adjustScoreSliderValue();
        finishingHook();
    };

    protected JLabel createTitleLabel(String stageTitle){
        JLabel title = new JLabel(stageTitle);
        title.setFont(ViewConstants.FONT_LARGE);
        title.setBounds(POSITION_X,INITIAL_POSITION_Y,TITLE_WIDTH,TITLE_HEIGHT);

        return title;
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
    protected void startingHook(){}
    protected void finishingHook(){}

    protected JScrollPane createScrollableInfoLabel(String stageInfo){
        infoLabel = new JLabel(stageInfo);
        infoLabel.setFont(ViewConstants.FONT_STAGE_INFO);

        int positionY = INITIAL_POSITION_Y + TITLE_HEIGHT + SPACING;

        JScrollPane scrollPane = new JScrollPane(infoLabel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.setBounds(POSITION_X,positionY,INFO_WIDTH,INFO_HEIGHT);

        return scrollPane;
    }

    protected void adjustScoreSliderValue(){
        CandidateDTO temporaryCandidate = stageView.getTemporaryCandidate();
        Map<String, Integer> rawScores = temporaryCandidate.getRawScores();
        String sliderName = scoreSlider.getName();

        for (Map.Entry<String, Integer> entry : rawScores.entrySet()) {
            if (entry.getKey().equals(sliderName)) scoreSlider.setValue(entry.getValue());
        }
    }

    protected JLabel createScoreLabel(){
        scoreLabel = new JLabel("Move the slider to evaluate candidate's performance at this stage:");
        scoreLabel.setBounds(POSITION_X, SLIDER_POSITION_Y - SLIDER_HEIGHT, INFO_WIDTH, SLIDER_HEIGHT);

        return scoreLabel;
    }

    private void updateScoreLabel(int sliderValue){
        String description = "";
        /**
         * The 'magic numbers' below are self-explanatory considering scoreSlider minimum value = 0 and maximum = 100
         * These values are only for descriptive purposes and won't matter in calculating final result
         */

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
    public boolean collectData() {
        stageView.getTemporaryCandidate().getRawScores().put(scoreSlider.getName(), scoreSlider.getValue());
        return true;
    }
    @Override
    public int getOrdinal() {
        return ordinal;
    }
}
