package view.swing.stages;

import controller.CandidateDTO;
import controller.Controller;
import model.Model;
import model.Presets;
import model.Recruitment;
import model.Stages;
import model.storage.FileStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.swing.View;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResumeStagePanelTest {

    private Model model = new Model(new FileStrategy());
    private Controller controller = new Controller(model);
    private View view = new View(model, controller);
    private HashMap<String, Integer> modifiersValues;
    private Map<String, Integer> rawScores;
    private StageView stageView;
    private Recruitment recruitment;
    private CandidateDTO temporaryCandidate;

    @BeforeEach
    public void setUp(){
        modifiersValues = new HashMap<>();
        modifiersValues.put("resume", 10);
        modifiersValues.put("language", 10);
        modifiersValues.put("experience", 10);
        modifiersValues.put("projects", 10);
        modifiersValues.put("coding", 10);
        modifiersValues.put("questions", 10);
        modifiersValues.put("salary", 10);
        modifiersValues.put("soft", 10);
        Presets presets = new Presets("test", modifiersValues);
        recruitment = new Recruitment(model, "Test Recruitment", presets);
        temporaryCandidate = controller.createTemporaryCandidate(null, recruitment);
        stageView = new StageView(view, temporaryCandidate, recruitment);
    }

    @Test
    void collectDataTest(){
        ResumeStagePanel resumeStagePanel = new ResumeStagePanel(stageView);
        resumeStagePanel.collectData();

        rawScores = temporaryCandidate.getRawScores();
        boolean isSliderNameSet = false;
        boolean isDefaultScoreSet = false;
        int defaultSliderScore = 50;

        for (Map.Entry<String, Integer> entry : rawScores.entrySet()) {
            if (entry.getKey().equals(Stages.RESUME.getStageName())){
                isSliderNameSet = true;
                if (entry.getValue() == defaultSliderScore){
                    isDefaultScoreSet = true;
                }
            }
        }
        assertTrue(isSliderNameSet);
        assertTrue(isDefaultScoreSet);
    }

}