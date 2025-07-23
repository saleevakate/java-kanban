package manager;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskMenagerTest {
    private File tempFile;

    @Test
    void saveAndLoadEmptyFile() throws IOException {
        tempFile = Files.createTempFile("temp", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loaded.getAllTasks().isEmpty(), "Список не пустой.");
    }
}
