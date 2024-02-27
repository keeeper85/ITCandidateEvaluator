package model.storage;

import model.Model;
import model.Presets;
import model.Recruitment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileStrategyTest {

    private FileStrategy fileStrategy;
    private Presets testPresets = new Presets("testPresets", new HashMap<>());
    private Recruitment recruitment1;
    private Recruitment recruitment2;
    private Model mockModel;

    @BeforeEach
    public void setUp() {
        fileStrategy = new FileStrategy();
        mockModel = mock(Model.class);
        recruitment1 = new Recruitment(mockModel, "Recruitment 1", testPresets);
        recruitment2 = new Recruitment(mockModel, "Recruitment 2", testPresets);
    }

    @Test
    public void testGetRecruitmentList() throws Exception {
        List<Recruitment> expectedRecruitments = Arrays.asList(recruitment1, recruitment2);

        Path tempDir = Files.createTempDirectory("tempDir");
        Path tempFile1 = tempDir.resolve("Recruitment 1.ser");
        Path tempFile2 = tempDir.resolve("Recruitment 2.ser");

        try (ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(tempFile1.toFile()));
             ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(tempFile2.toFile()))) {

            oos1.writeObject(expectedRecruitments.get(0));
            oos2.writeObject(expectedRecruitments.get(1));
        }

        FileStrategy fileStrategySpy = Mockito.spy(fileStrategy);
        when(fileStrategySpy.getFiles()).thenReturn(Arrays.asList(tempFile1, tempFile2));

        List<Recruitment> actualRecruitments = fileStrategySpy.getRecruitmentList(mockModel);

        assertEquals(expectedRecruitments.size(), actualRecruitments.size());
        for (int i = 0; i < expectedRecruitments.size(); i++) {
            assertEquals(expectedRecruitments.get(i).getName(), actualRecruitments.get(i).getName());
        }
    }


}