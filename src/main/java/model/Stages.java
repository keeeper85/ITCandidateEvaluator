package model;

/**
 * Stages contain all present steps of the evaluation
 * Before adding new stage look for stageName @param usages in the view package
 * StageTitle @param is used for JLabel objects
 * Ordinal numbers are hardcoded as the sequence of the steps is well-thought and must be kept
 */

public enum Stages {

    RESUME ("resume","Resume and social media evaluation", 1),
    LANGUAGE ("language","English language assessment", 2),
    EXPERIENCE ("experience","Previous work experience", 3),
    PROJECTS ("projects","Own projects", 4),
    LIVE_CODING("coding","Live coding", 5),
    QUESTIONS ("questions","Technical questions", 6),
    SALARY ("salary","Salary expectations", 7),
    SOFT_SKILLS ("soft","Soft skills", 8),
    ;

    private String stageName;
    private String stageTitle;
    private int stageOrdinal;
    Stages(String stageName, String stageTitle, int stageOrdinal) {
        this.stageName = stageName;
        this.stageTitle = stageTitle;
        this.stageOrdinal = stageOrdinal;
    }

    public String getStageName() {
        return stageName;
    }

    public String getStageTitle() { return stageTitle; }

    public int getStageOrdinal() {
        return stageOrdinal;
    }
}
