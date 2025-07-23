package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class CSVFormatter {
    static TaskManager taskManager = Managers.getDefaultManager();

    public static String toStringTask(Task task) {
        StringBuilder builder = new StringBuilder();
        builder.append(task.getId()).append(",");
        builder.append(TaskType.TASK).append(",");
        builder.append(task.getName()).append(",");
        builder.append(task.getTaskStatus()).append(",");
        builder.append(task.getDescription()).append(",");

        return builder.toString();
    }

    public static String toStringEpic(Epic epic) {
        StringBuilder builder = new StringBuilder();
        builder.append(epic.getId()).append(",");
        builder.append(TaskType.EPIC).append(",");
        builder.append(epic.getName()).append(",");
        builder.append(epic.getTaskStatus()).append(",");
        builder.append(epic.getDescription()).append(",");

        return builder.toString();
    }

    public static String toStringSubtask(Subtask subtask) {
        StringBuilder builder = new StringBuilder();
        builder.append(subtask.getId()).append(",");
        builder.append(TaskType.SUBTASK).append(",");
        builder.append(subtask.getName()).append(",");
        builder.append(subtask.getTaskStatus()).append(",");
        builder.append(subtask.getDescription()).append(",");
        builder.append(subtask.getEpicId());

        return builder.toString();
    }

    public static void fromString(String line) {
        String[] parts = line.split(",");

        String statusStr = parts[3];
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

        String type = parts[1];
        switch (type) {
            case "TASK": {
                int id = Integer.parseInt(parts[0]);
                String name = parts[2];
                String description = parts[4];
                Task task = new Task(id, name, description, status);
                taskManager.createTask(task);
                break;
            }
            case "EPIC": {
                int id = Integer.parseInt(parts[0]);
                String name = parts[2];
                String description = parts[4];
                Epic epic = new Epic(id, name, description, status);
                taskManager.createEpic(epic);
                break;
            }
            case "SUBTASK": {
                if (parts.length < 6) {
                    throw new IllegalArgumentException("Неверный формат строки для Subtask");
                }
                int id = Integer.parseInt(parts[0]);
                String name = parts[2];
                String description = parts[4];
                int epicId = Integer.parseInt(parts[5]);
                Subtask subtask = new Subtask(id, name, description, epicId, status);
                taskManager.createSubtask(subtask, epicId);
                break;
            }
        }
    }


}
