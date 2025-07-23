package manager;

import manager.FileBackedTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class FileBackedTaskManagerTest {

    @TempDir
    protected Path tempDir;

    private FileBackedTaskManager manager;
    private Path savedTasksFile;

    @BeforeEach
    void setUp() {
        savedTasksFile = tempDir.resolve("tasks.csv");
        manager = new FileBackedTaskManager(savedTasksFile.toFile());

        Task task = new Task(1, "Таск", "Описание", TaskStatus.NEW);
        Epic epic = new Epic(2, "Эпик", "Описание", TaskStatus.NEW);
        Subtask subtask = new Subtask(3, "Сабтаск", "Описание", 2, TaskStatus.NEW);

        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask, 2);
    }

    @Test
    void testSave() {
        try {
            String content = Files.readString(savedTasksFile);
            String[] lines = content.split("\n");
            assertEquals("id,type,name,status,description,epic", lines[0].trim());
            assertEquals(5, lines.length);
            String expectedTaskLine = "1,TASK,Таск,NEW,Описание,";
            String expectedEpicLine = "2,EPIC,Эпик,NEW,Описание,";
            String expectedSubtaskLine = "3,SUBTASK,Сабтаск,NEW,Описание,2";
            assertEquals(expectedTaskLine, lines[1].trim());
            assertEquals(expectedEpicLine, lines[2].trim());
            assertEquals(expectedSubtaskLine, lines[3].trim());
            assertTrue(content.contains(expectedTaskLine));
            assertTrue(content.contains(expectedEpicLine));
            assertTrue(content.contains(expectedSubtaskLine));
            assertTrue(lines[4].isBlank());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
