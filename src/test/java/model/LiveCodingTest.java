package model;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LiveCodingTest {

    @Test
    public void testGetLiveCodingTasksList() {
        List<String> tasksList = LiveCoding.getLiveCodingTasksList();
        boolean hasTask1 = false;
        boolean hasTask2 = false;

        for (String s : tasksList) {
            if (s.contains("getPartOfString")) hasTask1 = true;
            if (s.contains("ZipFile")) hasTask2 = true;
        }
        assertTrue(hasTask1);
        assertTrue(hasTask2);
    }

}