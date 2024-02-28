package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    public void testCreateSnippet_ShortQuestion() {
        String questionBody = "This is a short question.";
        String snippet = Question.createSnippet(questionBody);
        assertEquals(questionBody, snippet);
    }

    @Test
    public void testCreateSnippet_LongQuestion() {
        String questionBody = "This is a long question with more than five words in it.";
        String snippet = Question.createSnippet(questionBody);
        assertEquals("This is a long question with...", snippet);
    }

    @Test
    public void testCreateSnippet_SingleWord() {
        String questionBody = "Word";
        String snippet = Question.createSnippet(questionBody);
        assertEquals(questionBody, snippet);
    }
}