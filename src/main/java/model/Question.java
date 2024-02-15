package model;

public class Question {

    private String sourceFileName;
    private String snippet;
    private String questionBody;
    private final int SNIPPET_MAX_LENGTH_WORDS = 5;

    public Question(String sourceFileName, String questionBody) {
        this.sourceFileName = sourceFileName;
        this.questionBody = questionBody;
        snippet = createSnippet(questionBody);
    }

    private String createSnippet(String questionBody){
        String[] allWords = questionBody.split(" ");
        StringBuilder snippetBuilder = new StringBuilder();

        if (allWords.length <= SNIPPET_MAX_LENGTH_WORDS) return questionBody;

        for (int i = 0; i <= SNIPPET_MAX_LENGTH_WORDS; i++) {
            if (i < SNIPPET_MAX_LENGTH_WORDS) snippetBuilder.append(allWords[i]).append(" ");
            else snippetBuilder.append(allWords[i]).append("...");
        }

        return snippetBuilder.toString();
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    @Override
    public String toString() {
        return questionBody;
    }
}
