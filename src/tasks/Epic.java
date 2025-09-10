package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasksList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int id, String name, String description, TaskStatus taskStatus, Duration duration, LocalDateTime startTime) {
        super(id, name, description, taskStatus, duration, startTime);
        updateTime();
    }

    public void updateTime() {
        if (endTime == null) {
            endTime = getStartTime().plus(getDuration());
        }
        LocalDateTime start = null;
        LocalDateTime end = null;
        Duration totalDuration = Duration.ZERO;
        for (Subtask subtask : subtasksList) {
            if (subtask.getStartTime() != null && subtask.getDuration() != null) {
                if (start == null || subtask.getStartTime().isBefore(start)) {
                    start = subtask.getStartTime();
                }

                if (subtask.getEndTime() != null) {
                    if (end == null || subtask.getEndTime().isAfter(end)) {
                        end = subtask.getEndTime();
                    }
                }

                totalDuration = totalDuration.plus(subtask.getDuration());
            }
        }
        if (start != null) {
            setStartTime(start);
        }
        if (totalDuration != Duration.ZERO) {
            setDuration(totalDuration);
        }
        if (end != null) {
            this.endTime = end;
        }
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public List<Subtask> getSubtasks() {
        return subtasksList;
    }

    public void addSubtask(Subtask subtask) {
        if (!subtasksList.contains(subtask)) {
            subtasksList.add(subtask);
        }
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }
}