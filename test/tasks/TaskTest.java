package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    Task task;
    Duration minutes = Duration.ofMinutes(90);
    LocalDateTime time = LocalDateTime.of(2000, 1, 1, 0, 0);

    @BeforeEach
    public void setUp() {
        task = new Task(1, "Имя", "Описание", minutes, time);
    }

    @Test
    public void testGetEndTime() {
        LocalDateTime actualEndTime = task.getEndTime();
        assertEquals(2000, actualEndTime.getYear());
        assertEquals(01, actualEndTime.getMonthValue());
        assertEquals(01, actualEndTime.getDayOfMonth());
        assertEquals(1, actualEndTime.getHour());
        assertEquals(30, actualEndTime.getMinute());
    }
}
