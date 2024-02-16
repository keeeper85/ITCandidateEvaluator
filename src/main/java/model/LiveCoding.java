package model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class LiveCoding {

    private static final Path taskFilesDirectory = Paths.get("src/main/resources/liveCoding");

    public static List<String> getLiveCodingTasksList(){
        List<String> tasksList = new ArrayList<>();
        List<Path> files = getTaskFiles(taskFilesDirectory);

        for (Path file : files) {
            List<String> lines = new ArrayList<>();
            try {
                lines = Files.readAllLines(file);
                String task = changeListToString(lines);
                tasksList.add(task);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return tasksList;
    }

    public static List<Path> getTaskFiles(Path path){
        List<Path> files = new ArrayList<>();

        try (Stream<Path> paths = Files.list(path)) {
            paths.filter(file -> file.getFileName().toString().contains("task") && file.toString().endsWith(".txt"))
                    .forEach(file -> {
                        files.add(file);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return files;
    }

    public static String changeListToString(List<String> lines){
        StringBuilder builder = new StringBuilder();

        for (String line : lines) {
            builder.append(line).append("\n");
        }

        return builder.toString();
    }



}