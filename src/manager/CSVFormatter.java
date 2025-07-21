package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
import java.util.Map;

public class CSVFormatter {

    public static String getHeader() {
        return "Id,type,name,status,description,id epic/subtask";
    }

    public static String toStringTask(Map<Integer, Task> tasks) {
        // Превратить таску в csv строку
        StringBuilder builder = new StringBuilder();
        for (Task task : tasks.values()) {
            builder.append(task.getId()).append(",");
            builder.append(task.getClass()).append(",");
            builder.append(task.getName()).append(",");
            builder.append(task.getDescription()).append(".");
        }
        return builder.toString();
    }

    public static String toStringEpic(Map<Integer, Epic> epics) {
        StringBuilder builder = new StringBuilder();
        for (Epic epic : epics.values()) {
            builder.append(epic.getId()).append(",");
            builder.append(epic.getClass()).append(",");
            builder.append(epic.getName()).append(",");
            builder.append(epic.getDescription()).append(",");
            builder.append(epic.getSubtasks()).append(".");
        }
        return builder.toString();
    }

    public static String toStringSubtask(Map<Integer, Subtask> subtasks) {
        StringBuilder builder = new StringBuilder();
        for (Subtask subtask : subtasks.values()) {
            builder.append(subtask.getId()).append(",");
            builder.append(subtask.getClass()).append(",");
            builder.append(subtask.getName()).append(",");
            builder.append(subtask.getDescription()).append(",");
            builder.append(subtask.getEpicId()).append(".");
        }
        return builder.toString();
    }

    public static String toStringHistory(List<Task> tasksHistory) {
        // Превратить историю в csv строку

        return "";
    }

}
