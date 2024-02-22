package model;

public enum Stages {

    RESUME ("resume","Resume and social media evaluation", 0),
    LANGUAGE ("language","English language assessment", 1),
    EXPERIENCE ("experience","Previous work experience", 2),
    PROJECTS ("projects","Own projects", 3),
    LIVE_CODING("coding","Live coding", 4),
    QUESTIONS ("questions","Technical questions", 5),
    SALARY ("salary","Salary expectations", 6),
    SOFT_SKILLS ("soft","Soft skills", 7),
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
