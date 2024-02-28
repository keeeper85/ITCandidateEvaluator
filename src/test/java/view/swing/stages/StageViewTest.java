package view.swing.stages;

import controller.CandidateDTO;
import controller.Controller;
import model.Model;
import model.Presets;
import model.Recruitment;
import model.storage.FileStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.swing.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StageViewTest {

    private Model model = new Model(new FileStrategy());
    private Controller controller = new Controller(model);
    private View view = new View(model, controller);
    private HashMap<String, Integer> modifiersValues;
    private StageView stageView;
    private Recruitment recruitment;
    private CandidateDTO temporaryCandidate;

    @BeforeEach
    public void setUp(){
        modifiersValues = new HashMap<>();
        modifiersValues.put("resume", 10);
        modifiersValues.put("language", 0);
        modifiersValues.put("experience", 10);
        modifiersValues.put("projects", 10);
        modifiersValues.put("coding", 0);
        modifiersValues.put("questions", 10);
        modifiersValues.put("salary", 10);
        modifiersValues.put("soft", 10);
        Presets presets = new Presets("test", modifiersValues);
        recruitment = new Recruitment(model, "Test Recruitment", presets);
        temporaryCandidate = controller.createTemporaryCandidate(null, recruitment);
        stageView = new StageView(view, temporaryCandidate, recruitment);
    }
    @Test
    void setCurrentStagePanel() {
        Collectable initialPanel = stageView.getCurrentStagePanel();
        boolean isCandidateViewSet = false;
        if (initialPanel instanceof CandidateView) isCandidateViewSet = true;

        assertTrue(isCandidateViewSet);

        stageView.setCurrentStagePanel(new ResumeStagePanel(stageView));
        boolean isResumeViewSet = false;
        if (stageView.getCurrentStagePanel() instanceof ResumeStagePanel) isResumeViewSet = true;

        assertTrue(isResumeViewSet);
    }

    @Test
    void getChosenStages() {
        List<Collectable> chosenStages = stageView.getChosenStages();
        int listSizeIncludingCandidatePanel = 1;

        for (Integer value : modifiersValues.values()) {
            if (value != 0) listSizeIncludingCandidatePanel++;
        }

        int actualSizeList = chosenStages.size();

        assertEquals(listSizeIncludingCandidatePanel, actualSizeList);

        boolean isProjectsStagePresent = false;
        boolean isCodingStagePresent = false;

        for (Collectable chosenStage : chosenStages) {
            if (chosenStage instanceof ProjectsStagePanel) isProjectsStagePresent = true;
            if (chosenStage instanceof LiveCodingStagePanel) isCodingStagePresent = true;
        }

        assertTrue(isProjectsStagePresent);
        assertFalse(isCodingStagePresent);
    }

    @Test
    void getCurrentStagePanel() {
        boolean isInitialPanelSet = false;
        if (stageView.getCurrentStagePanel() instanceof CandidateView) isInitialPanelSet = true;

        assertTrue(isInitialPanelSet);
    }

    @Test
    void getTemporaryCandidate() {
        CandidateDTO candidateDTO = stageView.getTemporaryCandidate();

        String firstName = candidateDTO.getFirstName();
        int maxSalary = candidateDTO.getMaxOfferedSalary();

        assertEquals("", firstName);
        assertEquals(10000, maxSalary);
    }
}