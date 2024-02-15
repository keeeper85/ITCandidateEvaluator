package model;

import java.util.*;

public class Feedback {

    public static String generateFeedback(Candidate candidate){
        if (!candidate.isFinished()){
            System.out.println("Only fully evaluated candidates can have feedback.");
            return "";
        }

        Recruitment recruitment = candidate.getRecruitment();
        List<Candidate> allCandidates = recruitment.getCandidateList();
        int finalScorePercent = candidate.getEvaluationScore();
        int numberOfCandidates = allCandidates.size();
        int positionAmongAllCandidates = getPosition(candidate, allCandidates);

        StringBuilder feedback = new StringBuilder();
        feedback.append("Thank you for participation in our recruitment process!").append("\n");
        feedback.append("Here are your results:").append("\n");
        if (stagePresent(Stages.RESUME, recruitment)) feedback.append("Resume and social media evaluation stage: ").append(getStageScore(Stages.RESUME, candidate)).append("%\n");
        if (stagePresent(Stages.LANGUAGE, recruitment))  feedback.append("English language assessment stage: ").append(getStageScore(Stages.LANGUAGE, candidate)).append("%\n");
        if (stagePresent(Stages.EXPERIENCE, recruitment))  feedback.append("Previous work experience: ").append(getStageScore(Stages.EXPERIENCE, candidate)).append("%\n");
        if (stagePresent(Stages.PROJECTS, recruitment))  feedback.append("Own projects: ").append(getStageScore(Stages.PROJECTS, candidate)).append("%\n");
        if (stagePresent(Stages.LIVE_CODING, recruitment))  feedback.append("Live coding stage: ").append(getStageScore(Stages.LIVE_CODING, candidate)).append("%\n");
        if (stagePresent(Stages.QUESTIONS, recruitment))  {
            feedback.append("Technical questions stage: ").append(getStageScore(Stages.QUESTIONS, candidate)).append("%\n");
            feedback.append("You have been asked the following questions: ").append("\n");
            feedback.append(addQuestionFeedback(candidate));
        }
        feedback.append("\nYour final score is: ").append(finalScorePercent).append("%\n");
        feedback.append("You ranked: ").append(positionAmongAllCandidates).append(" among ").append(numberOfCandidates).append(" candidates interviewed.");

        return feedback.toString();
    }

    private static int getPosition(Candidate candidate, List<Candidate> allCandidates) {
        Collections.sort(allCandidates, (c1, c2) -> Integer.compare(c2.getEvaluationScore(), c1.getEvaluationScore()));
        int index = allCandidates.indexOf(candidate) + 1;

        return index;
    }

    private static boolean stagePresent(Stages stage, Recruitment recruitment) {
        Presets presets = recruitment.getPresets();
        HashMap<Stages, Integer> stages = presets.getPresetsValues();
        List<Stages> presentStages = new ArrayList<>();

        for (Map.Entry<Stages, Integer> entry : stages.entrySet()) {
            if (entry.getValue() > 0) presentStages.add(entry.getKey());
        }

        if (presentStages.contains(stage)) return true;
        return false;
    }

    private static int getStageScore(Stages stage, Candidate candidate) {
        HashMap<Stages, Integer> stagesScores = candidate.getScores();

        for (Map.Entry<Stages, Integer> entry : stagesScores.entrySet()) {
            Stages currentStage = entry.getKey();
            if (currentStage == stage) return entry.getValue();
        }

        return 0;
    }

    private static String addQuestionFeedback(Candidate candidate) {
        HashMap<Question, Integer> evaluatedQuestion = candidate.getEvaluatedQuestions();
        StringBuilder questionFeedbackBuilder = new StringBuilder();

        for (Map.Entry<Question, Integer> question : evaluatedQuestion.entrySet()) {
            String questionBody = question.getKey().getQuestionBody();
            int score = question.getValue();
            questionFeedbackBuilder.append("\t-").append(questionBody).append(" : ").append(score).append("%.\n");
        }

        return questionFeedbackBuilder.toString();
    }

}
