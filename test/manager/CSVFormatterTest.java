package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

public class CSVFormatterTest {
    private Task task;
    private Epic epic;
    Subtask subtask;

    @BeforeEach
    public void setUp() {
        task = new Task(1,"Таск","Описание",TaskStatus.NEW);
        epic = new Epic(2, "Эпик", "Описание", TaskStatus.NEW);
        subtask = new Subtask(3, "Сабтаск", "Описание", 2, TaskStatus.NEW);
    }

    @Test
    void testToString() {
        String expectedTask = "1,TASK,Таск,NEW,Описание,";
        String resultTask = CSVFormatter.toString(task);
        assertEquals(expectedTask, resultTask);
        String expectedEpic = "2,EPIC,Эпик,NEW,Описание,";
        String resultEpic = CSVFormatter.toString(epic);
        assertEquals(expectedEpic, resultEpic);
        String expectedSubtask = "3,SUBTASK,Сабтаск,NEW,Описание,2";
        String resultSubtask = CSVFormatter.toString(subtask);
        assertEquals(expectedSubtask, resultSubtask);
    }

    @Test
    void testFromString() {
        String inputTask = "1,TASK,Таск,NEW,Описание";
        Task resultTask = (Task) CSVFormatter.fromString(inputTask);
        assertEquals(1, resultTask.getId());
        assertEquals("Таск", resultTask.getName());
        assertEquals(TaskStatus.NEW, resultTask.getTaskStatus());
        assertEquals("Описание", resultTask.getDescription());

        String inputEpic = "2,EPIC,Эпик,NEW,Описание";
        Task resultEpic = (Epic) CSVFormatter.fromString(inputEpic);
        assertEquals(2, resultEpic.getId());
        assertEquals("Эпик", resultEpic.getName());
        assertEquals(TaskStatus.NEW, resultEpic.getTaskStatus());
        assertEquals("Описание", resultEpic.getDescription());

        String inputSubtask = "3,SUBTASK,Сабтаск,NEW,Описание,2";
        Task resultSubtask = (Subtask) CSVFormatter.fromString(inputSubtask);
        assertEquals(3, resultSubtask.getId());
        assertEquals("Сабтаск", resultSubtask.getName());
        assertEquals(TaskStatus.NEW, resultSubtask.getTaskStatus());
        assertEquals("Описание", resultSubtask.getDescription());
        assertEquals(2, ((Subtask) resultSubtask).getEpicId());
    }

}
