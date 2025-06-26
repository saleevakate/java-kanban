package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public interface HistoryManager {

    ArrayList<Task> getHistory();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void removeTask(Task task);

    void removeEpic(Epic epic);

    void removeSubtask(Subtask subtask);

    void removeNode(int id);
}
