package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class CSVFormatter {
    static TaskManager taskManager = Managers.getDefaultManager();

    public static String toString(Task task) {
        StringBuilder builder = new StringBuilder();
        TaskType type = null;
        if (task instanceof Epic) {
            type = TaskType.EPIC;
        } else if (task instanceof Subtask) {
            type = TaskType.SUBTASK;
        } else {
            type = TaskType.TASK;
        }
        builder.append(task.getId()).append(",");
        builder.append(type).append(",");
        builder.append(task.getName()).append(",");
        builder.append(task.getTaskStatus()).append(",");
        builder.append(task.getDescription()).append(",");
        if (task instanceof Subtask subtask) {
            builder.append(subtask.getEpicId());
        }

        return builder.toString();
    }

    public static Object fromString(String line) {
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
        Object task = null;
        switch (type) {
            case "TASK": {
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[2].trim();
                String description = parts[4].trim();
                task = new Task(id, name, description, status);
                break;
            }
            case "EPIC": {
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[2].trim();
                String description = parts[4].trim();
                task = new Epic(id, name, description, status);
                break;
            }
            case "SUBTASK": {
                if (parts.length < 6) {
                    throw new IllegalArgumentException("Неверный формат строки для Subtask");
                }
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[2].trim();
                String description = parts[4].trim();
                int epicId = Integer.parseInt(parts[5].trim());
                task = new Subtask(id, name, description, epicId, status);
                break;
            }
        }
        return task;
    }


}
