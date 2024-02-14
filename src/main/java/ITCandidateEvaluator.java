import controller.Controller;
import model.*;
import view.swing.View;

import javax.swing.*;
import java.util.HashMap;

public class ITCandidateEvaluator {
    public static void main(String[] args) {
        Model model = new Model();
//        Controller controller = new Controller(model);
//        View view = controller.getView();
//
//        SwingUtilities.invokeLater(() ->{view.initView();});

        HashMap<String, Integer> testMap = new HashMap<>();
        testMap.put("Resume and social media evaluation", 1);
        testMap.put("English language assessment", 8);
        testMap.put("Previous work experience", 10);
        testMap.put("Own projects", 10);
        testMap.put("Live coding", 7);
        testMap.put("Technical questions", 10);
        testMap.put("Salary expectations", 7);
        testMap.put("Soft skills", 10);

        Presets presets = new Presets("testowe", testMap);
        Recruitment recruitment = model.createNewRecruitment("testowa", presets);
        int maxScore = recruitment.calculateMaxPossibleScore();



        HashMap<Stages, Integer> scores = new HashMap<>();
        scores.put(Stages.RESUME, 10);
        scores.put(Stages.LANGUAGE, 85);
        scores.put(Stages.EXPERIENCE, 99);
        scores.put(Stages.PROJECTS, 90);
        scores.put(Stages.LIVE_CODING, 88);
        scores.put(Stages.QUESTIONS, 77);
        scores.put(Stages.SALARY, 111);
        scores.put(Stages.SOFT_SKILLS, 1);

        Candidate candidate = new Candidate(recruitment, "John", "Smith");
        candidate.setScores(scores);
        int finalScore = recruitment.calculateFinalCandidateScorePercent(candidate);
        System.out.println("max score: " + maxScore);
        System.out.println(finalScore + "%");
        int ratio = recruitment.calculateCostValueRatio(candidate);
        System.out.println(ratio + "% salary");
    }
}
