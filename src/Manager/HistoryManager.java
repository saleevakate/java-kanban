package Manager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void addTaskToHistory(Task task);

    void addEpicToHistory(Epic epic);

    void addSubtaskToHistory(Subtask subtask);

}
