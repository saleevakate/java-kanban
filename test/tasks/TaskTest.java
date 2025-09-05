package tasks;

import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    Task task;
    TaskManager taskManager = new InMemoryTaskManager();
    Duration minutes = Duration.ofMinutes(90);
    LocalDateTime time = LocalDateTime.of(2000, 1, 1, 0, 0);

    @BeforeEach
    public void setUp() {
        task = new Task(1, "Имя", "Описание", TaskStatus.NEW, minutes, time);

    }

    @Test
    public void testCreateTask() {
        assertEquals(1, task.getId());
        assertEquals("Имя", task.getName());
        assertEquals("Описание", task.getDescription());
        assertEquals(minutes, task.getDuration());
    }
}
