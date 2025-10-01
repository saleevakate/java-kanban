package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int id, String name, String description, int epicId, Duration duration, LocalDateTime startTime) {
        super(id, name, description, duration, startTime);
        this.epicId = epicId;
    }

    public LocalDateTime getEndTime() {
        endTime = startTime.plus(duration);
        return endTime;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }
}
