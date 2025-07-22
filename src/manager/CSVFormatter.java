package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.List;

public class CSVFormatter {

    public static String toString(Task task) {
        StringBuilder builder = new StringBuilder();
        builder.append(task.getId()).append(",");

        if (task instanceof Epic) {
            builder.append(TaskType.EPIC).append(",");
        } else if (task instanceof Subtask) {
            builder.append(TaskType.SUBTASK).append(",");
        } else {
            builder.append(TaskType.TASK).append(",");
        }
        builder.append(task.getName()).append(",");
        builder.append(task.getTaskStatus()).append(",");
        builder.append(task.getDescription()).append(",");
        if (task instanceof Subtask subtask) {
            builder.append(subtask.getEpicId());
        }
        return builder.toString();
    }

    public static void fromString(String line) {
        TaskManager taskManager = Managers.getDefaultManager();
        String[] parts = line.split(",");
        String statusStr = parts[3].trim();
        TaskStatus status = null;
        switch (statusStr) {
            case "NEW":
                status = TaskStatus.NEW;
                break;
            case "IN_PROGRESS":
                status = TaskStatus.IN_PROGRESS;
                break;
            case "DONE":
                status = TaskStatus.DONE;
                break;
        }
        String type = parts[1].trim();
        switch (type) {
            case "TASK": {
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[2].trim();
                String description = parts[4].trim();
                Task task = new Task(id, name, description, status);
                taskManager.createTask(task);
            }
            case "EPIC": {
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[2].trim();
                String description = parts[4].trim();
                Epic epic = new Epic(id, name, description, status);
                taskManager.createEpic(epic);
            }
            case "SUBTASK": {
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[2].trim();
                String description = parts[4].trim();
                int epicId = Integer.parseInt(parts[5].trim());
                Subtask subtask = new Subtask(id, name, description, epicId, status);
                taskManager.createSubtask(subtask, epicId);
            }
        }
    }


}
