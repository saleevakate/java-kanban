package manager;

import org.junit.jupiter.api.io.TempDir;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskHistoryTest {
    @TempDir
    protected Path tempDir;

    FileBackedTaskManager taskManager;
    private Path testFile;

    Task task = new Task(1, "Выбросить мусор", "Весь", TaskStatus.NEW
            , Duration.ofMinutes(10), LocalDateTime.of(2000, 1, 1, 1, 0));

    Epic epic = new Epic(2, "Собрать вещи", "Все", TaskStatus.NEW
            , Duration.ofMinutes(20), LocalDateTime.of(2000, 2, 2, 2, 0));

    Subtask subtask = new Subtask(3, "Помыть полы", "Все", 2, TaskStatus.NEW
            , Duration.ofMinutes(30), LocalDateTime.of(2000, 3, 3, 3, 0));

    @BeforeEach
    public void setUp() throws IOException {
        testFile = tempDir.resolve("test.csv");
        taskManager = new FileBackedTaskManager(testFile.toFile());
        Files.createFile(testFile);
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask, subtask.getEpicId());
    }

    @AfterEach
    public void delete() throws IOException {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        taskManager.deleteSubtasks();
        taskManager.prioritizedTasks.clear();
        Files.deleteIfExists(testFile);
    }

    @Test
    public void testGetHistory() {
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);
        assertFalse(taskManager.getHistory().isEmpty());
        assertTrue(taskManager.getHistory().size() == 3);
    }

}
