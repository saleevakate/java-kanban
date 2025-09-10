package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CSVFormatterTest {

    FileBackedTaskManager taskManager = Managers.getDefaultManager();
    private Path savedTasksFile;
    Task task = new Task(1, "Выбросить мусор", "Весь", TaskStatus.NEW
            , Duration.ofMinutes(10), LocalDateTime.of(2000, 1, 1, 1, 0));

    Epic epic = new Epic(2, "Собрать вещи", "Все", TaskStatus.NEW
            , Duration.ofMinutes(20), LocalDateTime.of(2000, 2, 2, 2, 0));

    Subtask subtask = new Subtask(3, "Помыть полы", "Все", 2, TaskStatus.NEW
            , Duration.ofMinutes(30), LocalDateTime.of(2000, 3, 3, 3, 0));

    @BeforeEach
    public void setUp() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask, subtask.getEpicId());
    }

    @Test
    void testToString() {
        String expectedTask = "1,TASK,Выбросить мусор,NEW,Весь,PT10M,2000-01-01T01:00,";
        String resultTask = CSVFormatter.toString(task);
        assertEquals(expectedTask, resultTask);
        String expectedEpic = "2,EPIC,Собрать вещи,NEW,Все,PT30M,2000-03-03T03:00,";
        String resultEpic = CSVFormatter.toString(epic);
        assertEquals(expectedEpic, resultEpic);
        String expectedSubtask = "3,SUBTASK,Помыть полы,NEW,Все,PT30M,2000-03-03T03:00,2";
        String resultSubtask = CSVFormatter.toString(subtask);
        assertEquals(expectedSubtask, resultSubtask);
    }

    @Test
    void testFromString() {
        String inputTask = "1,TASK,Выбросить мусор,NEW,Весь,PT10M,2000-01-01T01:00,";
        Task resultTask = CSVFormatter.fromString(inputTask);
        assertEquals(1, resultTask.getId());
        assertEquals("Выбросить мусор", resultTask.getName());
        assertEquals("Весь", resultTask.getDescription());
        assertEquals(TaskStatus.NEW, resultTask.getTaskStatus());
        assertEquals(Duration.ofMinutes(10), resultTask.getDuration());
        assertEquals(LocalDateTime.of(2000, 1, 1, 1, 0), resultTask.getStartTime());

        String inputEpic = "2,EPIC,Собрать вещи,NEW,Все,PT20M,2000-02-02T02:00";
        Task resultEpic = CSVFormatter.fromString(inputEpic);
        assertEquals(2, resultEpic.getId());
        assertEquals("Собрать вещи", resultEpic.getName());
        assertEquals("Все", resultEpic.getDescription());
        assertEquals(TaskStatus.NEW, resultEpic.getTaskStatus());
        assertEquals(Duration.ofMinutes(20), resultEpic.getDuration());
        assertEquals(LocalDateTime.of(2000, 2, 2, 2, 0), resultEpic.getStartTime());

        String inputSubtask = "3,SUBTASK,Помыть полы,NEW,Все,PT30M,2000-03-03T03:00,2";
        Task resultSubtask = CSVFormatter.fromString(inputSubtask);
        assertEquals(3, resultSubtask.getId());
        assertEquals("Помыть полы", resultSubtask.getName());
        assertEquals("Все", resultSubtask.getDescription());
        assertEquals(TaskStatus.NEW, resultSubtask.getTaskStatus());
        assertEquals(Duration.ofMinutes(30), resultSubtask.getDuration());
        assertEquals(LocalDateTime.of(2000, 3, 3, 3, 0), resultSubtask.getStartTime());
        assertEquals(2, ((Subtask) resultSubtask).getEpicId());
    }

}
