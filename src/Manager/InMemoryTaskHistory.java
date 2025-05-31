package Manager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.List;

public class InMemoryTaskHistory implements HistoryManager{
    @Override
    public List<Task> getHistory() {
        return List.of();
    }

    @Override
    public void addTaskToHistory(Task task) {

    }

    @Override
    public void addEpicToHistory(Epic epic) {

    }

    @Override
    public void addSubtaskToHistory(Subtask subtask) {

    }
}
