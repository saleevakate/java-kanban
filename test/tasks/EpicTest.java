package tasks;

import manager.FileBackedTaskManager;
import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {

    FileBackedTaskManager taskManager = Managers.getDefaultManager();
    Epic epic = new Epic(2, "Собрать вещи", "Все",
            Duration.ofMinutes(30), LocalDateTime.of(2000, 1, 1, 1, 0));

    Subtask subtask1 = new Subtask(4, "Помыть полы", "Все", 2,
            Duration.ofMinutes(30), LocalDateTime.of(2000, 2, 2, 2, 0));

    Subtask subtask2 = new Subtask(5, "Помыть полы", "Все", 2,
            Duration.ofMinutes(3), LocalDateTime.of(2000, 3, 3, 3, 0));

    @BeforeEach
    public void setUp() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
    }

    @AfterEach
    public void delete() {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        taskManager.deleteSubtasks();
    }

    @Test
    public void testGetEndTime() {
        assertEquals("2000-03-03T03:03", epic.getEndTime().toString());
    }
}