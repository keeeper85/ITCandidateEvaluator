package model;

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
