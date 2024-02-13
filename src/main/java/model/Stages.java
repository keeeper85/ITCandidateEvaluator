package model;

public enum Stages {

    RESUME ("resume", 0),
    LANGUAGE ("language", 1),
    EXPERIENCE ("experience", 2),
    PROJECTS ("projects", 3),
    LIVE_CODING("coding", 4),
    QUESTIONS ("questions", 5),
    SALARY ("salary", 6),
    SOFT_SKILLS ("soft", 7),
    ;

    private String stageName;
    private int stageOrdinal;
    Stages(String stageName, int stageOrdinal) {
        this.stageName = stageName;
        this.stageOrdinal = stageOrdinal;
    }

    public String getStageName() {
        return stageName;
    }

    public int getStageOrdinal() {
        return stageOrdinal;
    }
}
