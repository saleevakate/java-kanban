package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskHistoryTest {
    TaskManager taskManager = Managers.getDefaultManager();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    Duration minutes = Duration.ofMinutes(90);
    LocalDateTime time = LocalDateTime.of(2000, 1, 1, 0, 0);


    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    public void setUp() {
        task = new Task(1, "Имя", "Описание", TaskStatus.NEW, minutes, time);
        taskManager.createTask(task);
        epic = new Epic(2, "Эпик", "Описание", TaskStatus.NEW, minutes, time);
        taskManager.createEpic(epic);
        subtask = new Subtask(3, "Сабтаск", "Описание", 1, TaskStatus.NEW, minutes, time);
        taskManager.createSubtask(subtask, 2);
    }

    @AfterEach
    public void delete() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
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
