package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskHistoryTest {
    TaskManager taskManager = Managers.getDefaultManager();
    HistoryManager historyManager = Managers.getDefaultHistory();
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
        subtask = new Subtask(2, "Сабтаск", "Описание", 1, TaskStatus.NEW);
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
        taskManager.getTaskById(0);
        taskManager.getEpicById(1);
        taskManager.getSubtaskById(2);
        assertFalse(taskManager.getHistory().isEmpty());
        assertTrue(taskManager.getHistory().size() == 3);
    }

}
