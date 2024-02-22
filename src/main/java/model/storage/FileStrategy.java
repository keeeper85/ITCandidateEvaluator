package model.storage;

import model.Recruitment;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileStrategy extends AbstractStrategy{

    private static final String RECRUITMENT_STORAGE_DIRECTORY = "src/main/resources/storage/";


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
        return false;
    }

    @Override
    protected boolean deleteRecords() {
        return false;
    }

    private boolean serializeToFile(Recruitment recruitment){
        String filePath = RECRUITMENT_STORAGE_DIRECTORY + recruitment.getName() + ".ser";
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(recruitment);
            System.out.println("Object serialized successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
