package model.storage;

import model.Model;
import model.Recruitment;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * FileStrategy uses serialization and deserialization to files for storage purposes.
 * Serialization is simple, however deserialization can be risky, so use this strategy with caution and make backups.
 * If you modify any of the serializable classes, deserialization of already existing objects may be impossible.
 * There's no need to close connection here so the closeConnection() method is left empty.
 */

public class FileStrategy extends AbstractStrategy{
    private static final String RECRUITMENT_STORAGE_DIRECTORY = "src/main/resources/storage/";

    @Override
    public List<Recruitment> getRecruitmentList(Model model) {
        List<Path> serializedFiles = getFiles();
        List<Recruitment> recruitmentList = new ArrayList<>();

        for (Path filePath : serializedFiles) {
            try (FileInputStream fileIn = new FileInputStream(filePath.toFile());
                 ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

                Recruitment recruitment = (Recruitment) objectIn.readObject();
                recruitment.setModel(model);
                recruitmentList.add(recruitment);

            } catch (Exception e) {
                Model.logger.error("Problem with deserializing recruitment list from file: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return recruitmentList;
    }

    private List<Path> getFiles(){
        List<Path> files = new ArrayList<>();
        Path path = Paths.get(RECRUITMENT_STORAGE_DIRECTORY);

        try (Stream<Path> paths = Files.list(path)) {
            paths.filter(file -> file.toString().endsWith(".ser"))
                    .forEach(file -> {
                        files.add(file);
                    });
        } catch (IOException e) {
            Model.logger.error("Problem with reading recruitment file: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return files;
    }

    @Override
    protected boolean addRecords() {
        boolean isOK = true;

        for (Recruitment recruitment : toAdd) {
            isOK = serializeToFile(recruitment);
        }

        return isOK;
    }

    @Override
    protected boolean replaceRecords() {
        List<String> fileNamesForRemoval = getRecruitmentNames(toReplace);
        boolean allDeleted = deleteFiles(fileNamesForRemoval);
        toAdd.clear();
        toAdd.addAll(toReplace);

        return allDeleted && addRecords();
    }

    @Override
    protected boolean deleteRecords() {
        List<String> fileNamesForRemoval = getRecruitmentNames(toDelete);
        return deleteFiles(fileNamesForRemoval);
    }

    private boolean deleteFiles(List<String> fileNames){
        boolean allDeleted = true;

        for (String recruitmentName : fileNames) {
            String filePath = RECRUITMENT_STORAGE_DIRECTORY + File.separator + recruitmentName + ".ser";
            File file = new File(filePath);

            boolean deleted = file.delete();
            if (!deleted) {
                allDeleted = false;
                Model.logger.error("Error at deleting file: " + file.getName());
            }
        }
        return allDeleted;
    }

    private boolean serializeToFile(Recruitment recruitment){
        String filePath = RECRUITMENT_STORAGE_DIRECTORY + recruitment.getName() + ".ser";
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(recruitment);

        } catch (Exception e) {
            Model.logger.error("Problem with recruitment serialization: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void closeConnection() {}
}
