package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

public class Epic extends Task {
    private Set<Integer> subtasksId = new HashSet<>();

    public Epic(int id, String name, String description, TaskStatus taskStatus, Duration duration, LocalDateTime startTime) {
        super(id, name, description, taskStatus, duration, startTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Set<Integer> getSubtasks() {
        return subtasksId;
    }

    public void addSubtask(int id) {
        subtasksId.add(id);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }
}
