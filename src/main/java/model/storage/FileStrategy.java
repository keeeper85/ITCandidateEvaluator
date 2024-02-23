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
        boolean allReplaced = true;
        System.out.println("replacing");

        for (String recruitmentName : fileNamesForRemoval) {
            String filePath = RECRUITMENT_STORAGE_DIRECTORY + File.separator + recruitmentName + ".ser";
            File file = new File(filePath);

            boolean deleted = file.delete();
            if (!deleted) allReplaced = false; //todo Logger
        }
        toAdd.clear();
        toAdd.addAll(toReplace);

        return allReplaced && addRecords();
    }

    @Override
    protected boolean deleteRecords() {
        List<String> fileNamesForRemoval = getRecruitmentNames(toDelete);
        boolean allDeleted = true;

        for (String recruitmentName : fileNamesForRemoval) {
            String filePath = RECRUITMENT_STORAGE_DIRECTORY + File.separator + recruitmentName + ".ser";
            File file = new File(filePath);

            boolean deleted = file.delete();
            if (!deleted) allDeleted = false; //todo Logger
        }

        return allDeleted;
    }

    private boolean serializeToFile(Recruitment recruitment){
        String filePath = RECRUITMENT_STORAGE_DIRECTORY + recruitment.getName() + ".ser";
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(recruitment);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
