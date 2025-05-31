package Manager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.List;

public interface HistoryManager {

    List<Task>  getHistory();

    <T extends Task> void addTaskToHistory(T task);
}
