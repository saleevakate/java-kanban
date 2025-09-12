package manager;

import org.junit.jupiter.api.AfterEach;
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
import java.time.Duration;
import java.time.LocalDateTime;

class FileBackedTaskManagerTest {

    @TempDir
    protected Path tempDir;

    FileBackedTaskManager taskManager;
    private Path testFile;
    Task task = new Task(1, "Выбросить мусор", "Весь", TaskStatus.NEW,
            Duration.ofMinutes(10), LocalDateTime.of(2000, 1, 1, 1, 0));

    Epic epic = new Epic(2, "Собрать вещи", "Все", TaskStatus.NEW,
            Duration.ofMinutes(20), LocalDateTime.of(2000, 2, 2, 2, 0));

    Subtask subtask = new Subtask(3, "Помыть полы", "Все", 2, TaskStatus.NEW,
            Duration.ofMinutes(30), LocalDateTime.of(2000, 3, 3, 3, 0));

    @BeforeEach
    void setUp() throws IOException {
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
        Files.delete(testFile);
    }

    @Test
    void testSave() throws IOException {
        String content = Files.readString(testFile);
        String[] lines = content.split("\n");
        assertEquals("id,type,name,status,description,duration,startTime,epic", lines[0].trim());
        String expectedTaskLine = "1,TASK,Выбросить мусор,NEW,Весь,PT10M,2000-01-01T01:00,";
        String expectedEpicLine = "2,EPIC,Собрать вещи,NEW,Все,PT30M,2000-03-03T03:00,";
        String expectedSubtaskLine = "3,SUBTASK,Помыть полы,NEW,Все,PT30M,2000-03-03T03:00,2";
        assertEquals(expectedTaskLine, lines[1].trim());
        assertEquals(expectedEpicLine, lines[2].trim());
        assertEquals(expectedSubtaskLine, lines[3].trim());
    }

    @Test
    void testLoadFromFile() throws IOException {
        Task loadedTask = taskManager.getTaskById(1);
        assertEquals(1, loadedTask.getId());
        assertEquals("Выбросить мусор", loadedTask.getName());
        assertEquals(TaskStatus.NEW, loadedTask.getTaskStatus());
        assertEquals("Весь", loadedTask.getDescription());
        assertEquals(Duration.ofMinutes(10), loadedTask.getDuration());
        assertEquals(LocalDateTime.of(2000, 1, 1, 1, 0), loadedTask.getStartTime());
        Epic loadedEpic = taskManager.getEpicById(2);
        assertEquals(2, loadedEpic.getId());
        assertEquals("Собрать вещи", loadedEpic.getName());
        assertEquals(TaskStatus.NEW, loadedEpic.getTaskStatus());
        assertEquals("Все", loadedEpic.getDescription());
        assertEquals(Duration.ofMinutes(30), loadedEpic.getDuration());
        assertEquals(LocalDateTime.of(2000, 3, 3, 3, 0), loadedEpic.getStartTime());
        Subtask loadedSubtask = taskManager.getSubtaskById(3);
        assertNotNull(loadedSubtask, "Подзадача не загружена");
        assertEquals(3, loadedSubtask.getId());
        assertEquals("Помыть полы", loadedSubtask.getName());  // Исправлено
        assertEquals(TaskStatus.NEW, loadedSubtask.getTaskStatus());
        assertEquals("Все", loadedSubtask.getDescription());   // Исправлено
        assertEquals(2, loadedSubtask.getEpicId());
        assertEquals(Duration.ofMinutes(30), loadedSubtask.getDuration());
        assertEquals(LocalDateTime.of(2000, 3, 3, 3, 0), loadedSubtask.getStartTime());
    }
}
