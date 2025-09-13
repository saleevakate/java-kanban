package manager;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CSVFormatter {

    public static String toString(Task task) {
        StringBuilder builder = new StringBuilder();
        builder.append(task.getId()).append(",");
        builder.append(task.getType()).append(",");
        builder.append(task.getName()).append(",");
        builder.append(task.getTaskStatus()).append(",");
        builder.append(task.getDescription()).append(",");
        builder.append(task.getDuration().toString()).append(",");
        builder.append(task.getStartTime().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))).append(",");
        if (task instanceof Subtask subtask) {
            builder.append(subtask.getEpicId());
        }
        return builder.toString();
    }

    public static Task fromString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split(",");

        String statusStr = parts[3].trim();
        TaskStatus status = null;
        switch (statusStr) {
            case "NEW": status = TaskStatus.NEW; break;
            case "IN_PROGRESS": status = TaskStatus.IN_PROGRESS; break;
            case "DONE": status = TaskStatus.DONE; break;
        }

        String type = parts[1].trim();
        Task task = null;
        switch (type) {
            case "TASK": {
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[2].trim();
                String description = parts[4].trim();
                Duration duration = Duration.parse(parts[5].trim());
                LocalDateTime startTime = LocalDateTime.parse(parts[6],
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                task = new Task(id, name, description, duration, startTime);
                task.setTaskStatus(status);
                break;
            }
            case "EPIC": {
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[2].trim();
                String description = parts[4].trim();
                Duration duration = Duration.parse(parts[5].trim());
                LocalDateTime startTime = LocalDateTime.parse(parts[6],
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                task = new Epic(id, name, description, duration, startTime);
                task.setTaskStatus(status);
                break;
            }
            case "SUBTASK": {
                if (parts.length < 7) {
                    throw new IllegalArgumentException("Неверный формат строки для Subtask");
                }
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[2].trim();
                String description = parts[4].trim();
                Duration duration = Duration.parse(parts[5].trim());
                LocalDateTime startTime = LocalDateTime.parse(parts[6],
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                int epicId = Integer.parseInt(parts[7].trim());
                task = new Subtask(id, name, description, epicId, duration, startTime);
                task.setTaskStatus(status);
                break;
            }
        }
        return task;
    }
}
