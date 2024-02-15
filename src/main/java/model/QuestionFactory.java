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
        HashSet<Path> questionFiles = findQuestionFiles(QUESTION_FILES_DIRECTORY);
        HashMap<String, HashSet<String>> questionsFromFiles = readQuestionFiles(questionFiles);
        preparedList = createQuestions(questionsFromFiles);
    }

    private HashSet<Path> findQuestionFiles(Path questionFilesDirectory) {
        HashSet<Path> files = new HashSet<>();

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

    private HashMap<String, HashSet<String>> readQuestionFiles(HashSet<Path> questionFiles) {
        HashMap<String, HashSet<String>> filedQuestions = new HashMap<>();

        for (Path questionFile : questionFiles) {
            String fileName = questionFile.getFileName().toString();
            HashSet<String> questions = readFile(questionFile);
            filedQuestions.put(fileName,questions);
        }

        return filedQuestions;
    }

    private HashSet<String> readFile(Path path){
        HashSet<String> questions;
        try {
            questions = formatQuestions(Files.readAllLines(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return questions;
    }

    private HashSet<String> formatQuestions(List<String> allLines){
        HashSet<String> formattedQuestions = new HashSet<>();
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

    private List<Question> createQuestions(HashMap<String, HashSet<String>> questionsFromFiles) {
        List<Question> preparedQuestions = new ArrayList<>();

        for (Map.Entry<String, HashSet<String>> entry : questionsFromFiles.entrySet()) {
            String questionFile = entry.getKey();
            HashSet<String> questions = entry.getValue();

            for (String question : questions) {
                Question preparedQuestion = new Question(questionFile, question);
                preparedQuestions.add(preparedQuestion);
            }
        }
        return preparedQuestions;
    }

    public List<Question> getPreparedList() {
        return preparedList;
    }

    @Override
    public void run() {
        System.out.println("Thread started");
        long dateStart = new Date().getTime();
        prepareQuestionList();
        long dateEnd = new Date().getTime();
        System.out.println("List prepared. Thread finished. Time elapsed: " + (dateEnd - dateStart));
    }
}
