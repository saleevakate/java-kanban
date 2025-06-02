package manager;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    List<Task>  getHistory();

    void addTaskToHistory(Task task);
}
