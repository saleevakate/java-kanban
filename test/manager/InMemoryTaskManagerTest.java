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

public class InMemoryTaskManagerTest {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    Task task;
    Epic epic;
    Subtask subtask;
    TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefaultManager();
        task = new Task(1, "Имя", "Описание", TaskStatus.NEW);
        taskManager.createTask(task);
        epic = new Epic(2, "Эпик", "Описание", TaskStatus.NEW);
        taskManager.createEpic(epic);
        subtask = new Subtask(3, "Сабтаск", "Описание", 2, TaskStatus.NEW);
        taskManager.createSubtask(subtask, 2);
    }

    @AfterEach
    public void delete() {
        tasks.clear();
        epics.clear();
        subtasks.clear();

    }


    @Test
    public void testCreateTask() {
        assertEquals(1, task.getId());
        assertEquals("Имя", task.getName());
        assertEquals("Описание", task.getDescription());
    }

    @Test
    public void testCreateEpic() {
        assertEquals(2, epic.getId());
        assertEquals("Эпик", epic.getName());
        assertEquals("Описание", epic.getDescription());
    }

    @Test
    public void testCreateSubtask() {
        assertEquals(3, subtask.getId());
        assertEquals("Сабтаск", subtask.getName());
        assertEquals("Описание", subtask.getDescription());
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
        Task newTask = new Task(1, "Имя10", "Описание10", TaskStatus.NEW);
        taskManager.updateTask(newTask);
        assertEquals("Имя10", taskManager.getTaskById(1).getName());
    }

    @Test
    public void testUpdateEpic() {
        Epic newEpic = new Epic(2, "Эпик10", "Описание10", TaskStatus.NEW);
        taskManager.updateEpic(newEpic);
        assertEquals("Эпик10", taskManager.getEpicById(2).getName());
    }

    @Test
    public void testUpdateSubtask() {
        Subtask newSubtask = new Subtask(3, "Сабтаск10", "Описание10", 2, TaskStatus.NEW);
        taskManager.updateSubtask(newSubtask);
        assertEquals("Сабтаск10", taskManager.getSubtaskById(3).getName());
    }

    @Test
    public void testDeleteTask() {
        taskManager.deleteTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testDeleteEpic() {
        taskManager.deleteEpics();
        assertTrue(epics.isEmpty());
        assertTrue(subtasks.isEmpty());
    }

    @Test
    public void testDeleteSubtask() {
        taskManager.deleteSubtasks();
        assertTrue(subtasks.isEmpty());
    }

    @Test
    public void testDeleteTaskById() {
        taskManager.deleteTaskById(task.getId());
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testDeleteEpicById() {
        taskManager.deleteEpicById(epic.getId());
        assertTrue(epics.isEmpty());
    }

    @Test
    public void testDeleteSubtaskById() {
        taskManager.deleteSubtaskById(subtask.getId());
        assertTrue(subtasks.isEmpty());
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
        taskManager.updateEpicStatus(epic.getId());
        assertEquals(TaskStatus.DONE, epic.getTaskStatus());
    }
}
