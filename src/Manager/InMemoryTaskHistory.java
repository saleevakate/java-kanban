package Manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskHistory implements HistoryManager {

    private final List<Task> tasksHistory = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return tasksHistory;
    }

    @Override
    public <T extends Task> void addTaskToHistory(T task) {
        if (tasksHistory.size() == 10) {
            tasksHistory.remove(0);
            tasksHistory.add(task);
        } else {
            tasksHistory.add(task);
        }
    }
}
