package Manager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.TaskStatus;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    void getTasks();

    void createTask(Task newTask);

    void createEpic(Epic newEpic);

    Subtask createSubtask(Subtask newSubtask, int epicId);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    void updateTaskStatus(int id, TaskStatus taskStatus);

    void updateEpicStatus(int epicId);

    void updateSubtaskStatus(int id, TaskStatus taskStatus);

    Set<Integer> getSubtasksByEpicId(int id);

    List<Task> getHistory();
}
