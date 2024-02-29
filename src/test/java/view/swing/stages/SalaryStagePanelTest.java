package view.swing.stages;

import controller.CandidateDTO;
import controller.Controller;
import model.Candidate;
import model.Model;
import model.Presets;
import model.Recruitment;
import model.storage.FileStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.swing.View;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SalaryStagePanelTest {

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
    void updateScoreSlider() {
        SalaryStagePanel salaryStagePanel = new SalaryStagePanel(stageView);
        assertTrue(salaryStagePanel.updateScoreSlider());

        Candidate invalidSalaryCandidate = new Candidate(recruitment,"MaxSalaryLower", "ThanMinSalary");
        invalidSalaryCandidate.setMaxOfferedSalary(5000);
        invalidSalaryCandidate.setMinOfferedSalary(10000);
        temporaryCandidate = controller.createTemporaryCandidate(invalidSalaryCandidate, recruitment);
        stageView = new StageView(view, temporaryCandidate, recruitment);
        salaryStagePanel = new SalaryStagePanel(stageView);

        assertFalse(salaryStagePanel.updateScoreSlider());
    }

    @Test
    void collectData() {
        SalaryStagePanel salaryStagePanel = new SalaryStagePanel(stageView);
        salaryStagePanel.collectData();
        rawScores = temporaryCandidate.getRawScores();

        int defaultSalarySliderValue = 100;
        int actualSalarySliderValue = rawScores.get("salary");

        assertEquals(defaultSalarySliderValue, actualSalarySliderValue);
    }
}