package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class QuestionFactory implements Runnable{

    private List<Question> preparedList = new ArrayList<>();
    private static final Path QUESTION_FILES_DIRECTORY = Paths.get("src/main/resources/questions");

    private void prepareQuestionList(){
        Set<Path> questionFiles = findQuestionFiles(QUESTION_FILES_DIRECTORY);
        Map<String, Set<String>> questionsFromFiles = readQuestionFiles(questionFiles);
        preparedList = createQuestions(questionsFromFiles);
    }

    private Set<Path> findQuestionFiles(Path questionFilesDirectory) {
        Set<Path> files = new HashSet<>();

        try (Stream<Path> paths = Files.list(questionFilesDirectory)) {
            paths.filter(file -> file.getFileName().toString().contains("question") && file.toString().endsWith(".txt"))
                    .forEach(file -> {
                        files.add(file);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return files;
    }

    private Map<String, Set<String>> readQuestionFiles(Set<Path> questionFiles) {
        Map<String, Set<String>> filedQuestions = new HashMap<>();

        for (Path questionFile : questionFiles) {
            String fileName = questionFile.getFileName().toString();
            Set<String> questions = readFile(questionFile);
            filedQuestions.put(fileName,questions);
        }

        return filedQuestions;
    }

    private Set<String> readFile(Path path){
        Set<String> questions;
        try {
            questions = formatQuestions(Files.readAllLines(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return questions;
    }

    private Set<String> formatQuestions(List<String> allLines){
        Set<String> formattedQuestions = new HashSet<>();
        StringBuilder questionsBuilder = new StringBuilder();

        for (String line : allLines) {
            if (!line.isEmpty()) questionsBuilder.append(line).append("\n");
            else {
                formattedQuestions.add(questionsBuilder.toString().trim());
                questionsBuilder = new StringBuilder();
            }
        }
        return formattedQuestions;
    }

    private List<Question> createQuestions(Map<String, Set<String>> questionsFromFiles) {
        List<Question> preparedQuestions = new ArrayList<>();

        for (Map.Entry<String, Set<String>> entry : questionsFromFiles.entrySet()) {
            String questionFile = entry.getKey();
            Set<String> questions = entry.getValue();

            for (String question : questions) {
                Question preparedQuestion = new Question(questionFile, question);
                preparedQuestions.add(preparedQuestion);
            }
        }
        return preparedQuestions;
    }

    public List<Question> getPreparedList() { return preparedList; }

    @Override
    public void run() {
        //todo logger
        prepareQuestionList();
    }
}
