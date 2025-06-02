package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskHistory implements HistoryManager {

    private final List<Task> tasksHistory = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(tasksHistory);
    }

    @Override
    public void addTaskToHistory(Task task) {
        if (tasksHistory.size() == 10) {
            tasksHistory.remove(0);
        }
        tasksHistory.add(task);
    }
}
