package model;

import java.time.format.DateTimeFormatter;
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
        String joinDate = candidate.getDateOfJoiningEvaluation().format(DateTimeFormatter.ISO_DATE);
        String finishDate = candidate.getDateOfJoiningEvaluation().format(DateTimeFormatter.ISO_DATE);

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
        feedback.append("You ranked: ").append(positionAmongAllCandidates).append(" among ").append(numberOfCandidates).append(" candidates interviewed.\n\n");
        feedback.append("FOR RECRUITER ONLY!\n");
        if (stagePresent(Stages.SALARY, recruitment))  {
            feedback.append("The candidate joined the process on ").append(joinDate).append("\n");
            feedback.append("The candidate got evaluated on ").append(finishDate).append("\n");
            feedback.append("Evaluation time: ").append(changeSecondsToTime(candidate.getEvaluationTimeSeconds())).append("\n");
            feedback.append("Salary expected: ").append(candidate.getExpectedSalary()).append("\n");
            feedback.append(generateSalaryExpectationDescription(getStageScore(Stages.SALARY, candidate))).append("\n");
            feedback.append("Value/cost ratio: ").append(recruitment.calculateCostValueRatio(candidate)).append("%\n");
        }
        if (stagePresent(Stages.SOFT_SKILLS, recruitment)) feedback.append("Soft skills: ").append(getStageScore(Stages.SOFT_SKILLS, candidate)).append("%\n");
        String additionalNotes = candidate.getAdditionalNotes();
        if (additionalNotes.length() > 0) feedback.append("Additional notes: ").append(additionalNotes).append("\n");

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
        Map<Stages, Integer> stagesScores = candidate.getScores();

        for (Map.Entry<Stages, Integer> entry : stagesScores.entrySet()) {
            Stages currentStage = entry.getKey();
            if (currentStage == stage) return entry.getValue();
        }

        return 0;
    }

    private static String addQuestionFeedback(Candidate candidate) {
        Map<Question, Integer> evaluatedQuestion = candidate.getEvaluatedQuestions();
        StringBuilder questionFeedbackBuilder = new StringBuilder();

        for (Map.Entry<Question, Integer> question : evaluatedQuestion.entrySet()) {
            String questionBody = question.getKey().getQuestionBody();
            int score = question.getValue();
            questionFeedbackBuilder.append("   -").append(questionBody).append(" : ").append(score).append("%.\n");
        }

        return questionFeedbackBuilder.toString();
    }

    private static String changeSecondsToTime(int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Seconds should be a non-negative integer.");
        }

        int days = seconds / (60 * 60 * 24);
        int hours = (seconds % (60 * 60 * 24)) / (60 * 60);
        int minutes = (seconds % (60 * 60)) / 60;
        int remainingSeconds = seconds % 60;

        StringBuilder time = new StringBuilder();

        if (days > 0) {
            time.append(days).append(" day").append(days > 1 ? "s" : "").append(" ");
        }
        if (hours > 0) {
            time.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(" ");
        }
        if (minutes > 0) {
            time.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(" ");
        }
        if (remainingSeconds > 0) {
            time.append(remainingSeconds).append(" second").append(remainingSeconds > 1 ? "s" : "");
        }

        return time.toString().trim();
    }

    private static String generateSalaryExpectationDescription(int salarySliderValue){
        int percentage = 100;
        int expectedSalaryToAverageRatio = salarySliderValue - percentage;
        if (expectedSalaryToAverageRatio > 0) return "Salary expectations: " + expectedSalaryToAverageRatio + "% more than average.";
        if (expectedSalaryToAverageRatio < 0) return "Salary expectations: " + -expectedSalaryToAverageRatio + "% less than average.";
        else return "Salary expectations equals the average.";
    }

}
