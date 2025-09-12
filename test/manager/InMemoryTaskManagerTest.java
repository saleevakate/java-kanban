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

public class InMemoryTaskManagerTest {

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

    Duration minutes = Duration.ofMinutes(9);
    LocalDateTime time = LocalDateTime.of(2000, 9, 9, 9, 0);

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
    public void testCreateTask() {
        assertEquals(1, task.getId());
        assertEquals("Выбросить мусор", task.getName());
        assertEquals("Весь", task.getDescription());
    }

    @Test
    public void testCreateEpic() {
        assertEquals(2, epic.getId());
        assertEquals("Собрать вещи", epic.getName());
        assertEquals("Все", epic.getDescription());
    }

    @Test
    public void testCreateSubtask() {
        assertEquals(3, subtask.getId());
        assertEquals("Помыть полы", subtask.getName());
        assertEquals("Все", subtask.getDescription());
        assertEquals(2, subtask.getEpicId());
    }

    @Test
    public void testGetTaskById() {
        int id = task.getId();
        assertEquals(task, taskManager.getTaskById(id));
    }

    @Test
    public void testGetEpicById() {
        int id = epic.getId();
        assertEquals(epic, taskManager.getEpicById(id));
    }

    @Test
    public void testGetSubtaskById() {
        int id = subtask.getId();
        assertEquals(subtask, taskManager.getSubtaskById(id));
    }

    @Test
    public void testUpdateTask() {
        Task newTask = new Task(1, "Имя10", "Описание10", TaskStatus.NEW, minutes, time);
        taskManager.updateTask(newTask);
        assertEquals("Имя10", taskManager.getTaskById(1).getName());
    }

    @Test
    public void testUpdateEpic() {
        Epic newEpic = new Epic(2, "Эпик10", "Описание10", TaskStatus.NEW, minutes, time);
        taskManager.updateEpic(newEpic);
        assertEquals("Эпик10", taskManager.getEpicById(2).getName());
    }

    @Test
    public void testUpdateSubtask() {
        Subtask newSubtask = new Subtask(3, "Сабтаск10", "Описание10", 2, TaskStatus.NEW, minutes, time);
        taskManager.updateSubtask(newSubtask);
        assertEquals("Сабтаск10", taskManager.getSubtaskById(3).getName());
    }

    @Test
    public void testDeleteTask() {
        taskManager.deleteTasks();
        assertTrue(taskManager.tasks.isEmpty());
    }

    @Test
    public void testDeleteEpic() {
        taskManager.deleteEpics();
        assertTrue(taskManager.epics.isEmpty());
        assertTrue(taskManager.subtasks.isEmpty());
    }

    @Test
    public void testDeleteSubtask() {
        taskManager.deleteSubtasks();
        assertTrue(taskManager.subtasks.isEmpty());
    }

    @Test
    public void testDeleteTaskById() {
        taskManager.deleteTaskById(task.getId());
        assertTrue(taskManager.tasks.isEmpty());
    }

    @Test
    public void testDeleteEpicById() {
        taskManager.deleteEpicById(epic.getId());
        assertTrue(taskManager.epics.isEmpty());
    }

    @Test
    public void testDeleteSubtaskById() {
        taskManager.deleteSubtaskById(subtask.getId());
        assertTrue(taskManager.subtasks.isEmpty());
    }


    @Test
    public void testUpdateTaskStatus() {
        taskManager.updateTaskStatus(task.getId(), TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, task.getTaskStatus());
    }

    @Test
    public void testUpdateSubtaskStatus() {
        taskManager.updateSubtaskStatus(subtask.getId(), TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, subtask.getTaskStatus());
    }

    @Test
    public void testUpdateEpicStatus() {
        taskManager.updateSubtaskStatus(subtask.getId(), TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, epic.getTaskStatus());
    }
}
