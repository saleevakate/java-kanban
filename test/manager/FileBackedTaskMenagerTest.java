package manager;

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

class FileBackedTaskManagerTest {

    @TempDir
    protected Path tempDir;

    private FileBackedTaskManager taskManager;
    private Path savedTasksFile;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    @BeforeEach
    void setUp() {
        savedTasksFile = tempDir.resolve("tasks.csv");
        taskManager = new FileBackedTaskManager(savedTasksFile.toFile());

        Task task = new Task(1, "Таск", "Описание", TaskStatus.NEW);
        Epic epic = new Epic(2, "Эпик", "Описание", TaskStatus.NEW);
        Subtask subtask = new Subtask(3, "Сабтаск", "Описание", 2, TaskStatus.NEW);

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask, 2);
    }

    @Test
    void testSave() throws IOException {
            String content = Files.readString(savedTasksFile);
            String[] lines = content.split("\n");
            assertEquals("id,type,name,status,description,epic", lines[0].trim());
            assertEquals(4, lines.length);
            String expectedTaskLine = "1,TASK,Таск,NEW,Описание,";
            String expectedEpicLine = "2,EPIC,Эпик,NEW,Описание,";
            String expectedSubtaskLine = "3,SUBTASK,Сабтаск,NEW,Описание,2";
            assertEquals(expectedTaskLine, lines[1].trim());
            assertEquals(expectedEpicLine, lines[2].trim());
            assertEquals(expectedSubtaskLine, lines[3].trim());
            assertTrue(content.contains(expectedTaskLine));
            assertTrue(content.contains(expectedEpicLine));
            assertTrue(content.contains(expectedSubtaskLine));
    }

    @Test
    void testLoadFromFile() throws IOException {
        assertTrue(Files.exists(savedTasksFile));
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(savedTasksFile.toFile());

        Task loadedTask = loadedManager.getTaskById(1);
        assertNotNull(loadedTask, "Задача не загружена");
        assertEquals(1, loadedTask.getId());
        assertEquals("Таск", loadedTask.getName());
        assertEquals(TaskStatus.NEW, loadedTask.getTaskStatus());
        assertEquals("Описание", loadedTask.getDescription());

        Epic loadedEpic = loadedManager.getEpicById(2);
        assertNotNull(loadedEpic, "Эпик не загружен");
        assertEquals(2, loadedEpic.getId());
        assertEquals("Эпик", loadedEpic.getName());
        assertEquals(TaskStatus.NEW, loadedEpic.getTaskStatus());
        assertEquals("Описание", loadedEpic.getDescription());

        Subtask loadedSubtask = loadedManager.getSubtaskById(3);
        assertNotNull(loadedSubtask, "Подзадача не загружена");
        assertEquals(3, loadedSubtask.getId());
        assertEquals("Сабтаск", loadedSubtask.getName());
        assertEquals(TaskStatus.NEW, loadedSubtask.getTaskStatus());
        assertEquals("Описание", loadedSubtask.getDescription());
        assertEquals(2, loadedSubtask.getEpicId());
    }
}
