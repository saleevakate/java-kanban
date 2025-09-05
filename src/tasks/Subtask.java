package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private int epicId;
    private Duration duration;
    private LocalDateTime startTime;

    public Subtask(int id, String name, String description, int epicId, TaskStatus taskStatus, Duration duration, LocalDateTime startTime) {
        super(id, name, description, taskStatus, duration, startTime);
        this.epicId = epicId;
    }

    @Override
    public LocalDateTime getEndTime() {
        LocalDateTime endTime = startTime.plus(duration);
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
