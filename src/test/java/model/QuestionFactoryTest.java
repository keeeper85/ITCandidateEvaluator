package model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class QuestionFactoryTest {

    @Test
    public void testGetPreparedList() throws InterruptedException {
        QuestionFactory questionFactory = new QuestionFactory();
        Thread getQuestions = new Thread(questionFactory);
        getQuestions.start();
        Thread.sleep(200);
        List<Question> preparedList = questionFactory.getPreparedList();

        assertEquals(false, preparedList.isEmpty(), "Prepared list should not be empty");

        Set<String> fileNames = new HashSet<>();
        Set<String> questionBodies = new HashSet<>();

        for (Question question : preparedList) {
            String fileName = question.getSourceFileName();
            fileNames.add(fileName);
            String questionBody = question.getQuestionBody();
            questionBodies.add(questionBody);
        }

        assertEquals(5, fileNames.size());
        boolean hasOverHundredQuestions = false;

        if (questionBodies.size() > 100) hasOverHundredQuestions = true;
        assertTrue(hasOverHundredQuestions);
    }

}