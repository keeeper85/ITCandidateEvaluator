package view.swing.stages;

import controller.CandidateDTO;
import model.Candidate;
import model.Model;
import view.swing.ViewConstants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractStage extends JPanel implements Collectable {
    protected StageView stageView;
    protected CandidateDTO temporaryCandidate;
    protected int ordinal;
    protected String chooseFile = "Choose file:";
    protected Model model;
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
        temporaryCandidate =  stageView.getCandidate();
        setLayout(null);
        init();
        adjustScoreSliderValue();
    }

    protected abstract void init();

    protected JLabel createTitleLabel(String stageTitle){
        JLabel title = new JLabel(stageTitle);
        title.setFont(ViewConstants.FONT_LARGE);
        title.setBounds(POSITION_X,INITIAL_POSITION_Y,TITLE_WIDTH,TITLE_HEIGHT);

        return title;
    }

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

    protected void adjustScoreSliderValue(){
        CandidateDTO temporaryCandidate = stageView.getCandidate();
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
        String value = String.valueOf(scoreSlider.getValue());
        score.put(scoreSlider.getName(), value);
        stageView.getCandidate().getRawScores().put(scoreSlider.getName(), scoreSlider.getValue());

        return score;
    }
    @Override
    public int getOrdinal() {
        return ordinal;
    }
}
