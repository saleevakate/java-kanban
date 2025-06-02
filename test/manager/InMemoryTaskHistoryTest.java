package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryTaskHistoryTest {
    TaskManager taskManager = Managers.getDefaultManager();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    public void setUp() {
        task = new Task(0, "Имя", "Описание", TaskStatus.NEW);
        taskManager.createTask(task);
        epic = new Epic(1, "Эпик", "Описание");
        taskManager.createEpic(epic);
        subtask = new Subtask(3, "Сабтаск", "Описание", 2, TaskStatus.NEW);
        taskManager.createSubtask(subtask, 1);
    }

    @AfterEach
    public void delete() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    @Test
    public void testGetHistory() {
        assertFalse(taskManager.getHistory().isEmpty());
        assertTrue(taskManager.getHistory().size() == 3);
    }
}
