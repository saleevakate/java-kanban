package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public class CSVFormatter {

    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

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

    /*public static Task fromString(String value) {

        return ;
    }
     */

}
