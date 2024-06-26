package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * LiveCoding is a simple helper class with the purpose to find files with 'task' in their names.
 * Found txt file is scanned and turned into a String value.
 * Html tags are taken into consideration later while presenting this String in a JLabel object.
 */

final class LiveCoding {
    private static final Path TASK_FILES_DIRECTORY = Paths.get("src/main/resources/liveCoding");

    private LiveCoding() {}

    public static List<String> getLiveCodingTasksList(){
        List<String> tasksList = new ArrayList<>();
        List<Path> files = getTaskFiles(TASK_FILES_DIRECTORY);

        for (Path file : files) {
            List<String> lines;
            try {
                lines = Files.readAllLines(file);
                String task = changeListToString(lines);
                tasksList.add(task);
            } catch (IOException e) {
                Model.logger.error("Error reading task file: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }

        return tasksList;
    }

    private static List<Path> getTaskFiles(Path path){
        List<Path> files = new ArrayList<>();

        try (Stream<Path> paths = Files.list(path)) {
            paths.filter(file -> file.getFileName().toString().contains("task") && file.toString().endsWith(".txt"))
                    .forEach(file -> {
                        files.add(file);
                    });
        } catch (IOException e) {
            Model.logger.error("Error getting task files: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return files;
    }

    private static String changeListToString(List<String> lines){
        StringBuilder builder = new StringBuilder();

        for (String line : lines) {
            builder.append(line).append("\n");
        }

        return builder.toString();
    }
}
