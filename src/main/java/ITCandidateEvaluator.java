import model.*;

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
        testMap.put("Own projects", 8);
        testMap.put("Live coding", 7);
        testMap.put("Technical questions", 10);
        testMap.put("Salary expectations", 7);
        testMap.put("Soft skills", 9);

        Presets presets = new Presets("testowe", testMap);
        Recruitment recruitment = model.createNewRecruitment("testowa", presets);
        int maxScore = recruitment.calculateMaxPossibleScore();



        HashMap<Stages, Integer> scores = new HashMap<>();
        scores.put(Stages.RESUME, 100);
        scores.put(Stages.LANGUAGE, 100);
        scores.put(Stages.EXPERIENCE, 100);
        scores.put(Stages.PROJECTS, 100);
        scores.put(Stages.LIVE_CODING, 100);
        scores.put(Stages.QUESTIONS, 100);
        scores.put(Stages.SALARY, 50);
        scores.put(Stages.SOFT_SKILLS, 50);

        Candidate candidate = new Candidate(recruitment, "John", "Smith");
        candidate.setScores(scores);
        int finalScore = recruitment.calculateFinalScore(candidate);
        int stageBonus = recruitment.calculatePresetStageModifiedScore(Stages.RESUME, 50);
        System.out.println(stageBonus);
        System.out.println(maxScore);
        System.out.println(finalScore);
    }
}
