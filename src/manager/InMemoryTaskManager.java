package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 0;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int generateId() {
        return idCounter++;
    }

    @Override
    public void createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public Subtask createSubtask(Subtask subtask, int epicId) {
        Epic parentTask = getEpicById(epicId);
        if (parentTask == null) {
            throw new IllegalArgumentException("Такого эпика нет");
        }
        if (subtask.getEpicId() == subtask.getId()) {
            throw new IllegalArgumentException("Подзадача не может быть своим же эпиком");
        }
        parentTask.addSubtask(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epicId);
        return subtask;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.addTaskToHistory(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.addTaskToHistory(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.addTaskToHistory(subtask);
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        Task savedTask = tasks.get(id);
        if (savedTask == null) {
            throw new IllegalArgumentException("Tasks.Task с ID " + id + " не найден");
        }
        tasks.put(id, task);
    }

    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        Epic savedEpic = epics.get(id);
        if (savedEpic == null) {
            throw new IllegalArgumentException("Tasks.Epic с ID " + id + " не найден");
        }
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
        epics.put(id, savedEpic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            throw new IllegalArgumentException("Tasks.Subtask с ID " + id + " не найден");
        }
        subtasks.put(id, subtask);
        int epicId = subtask.getEpicId();
        updateEpicStatus(epicId);
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.setTaskStatus(TaskStatus.NEW);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Set<Integer> subtasksId = getSubtasksByEpicId(id);
        epics.remove(id);
        for (int subtaskId : subtasksId) {
            subtasks.remove(subtaskId);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = getSubtaskById(id);
        int epicId = subtask.getEpicId();
        updateEpicStatus(epicId);
        subtasks.remove(id);
    }

    @Override
    public void updateTaskStatus(int id, TaskStatus taskStatus) {
        Task needTask = getTaskById(id);
        needTask.setTaskStatus(taskStatus);
    }

    @Override
    public void updateEpicStatus(int epicId) {
        Epic epicTask = getEpicById(epicId);
        if (epicTask == null) {
            throw new IllegalArgumentException("Tasks.Epic с ID " + epicId + " не найден");
        }
        Set<Integer> subtaskIds = epicTask.getSubtasks();
        // Проверяем все подзадачи
        if (subtaskIds.isEmpty()) {
            epicTask.setTaskStatus(TaskStatus.NEW); // Если нет подзадач, ставим статус NEW
            return;
        }
        boolean allNew = true;
        boolean allDone = true;
        boolean hasInProgress = false;

        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = getSubtaskById(subtaskId);
            if (subtask == null) {
                throw new IllegalStateException("Подзадача с ID " + subtaskId + " не найдена");
            }

            TaskStatus status = subtask.getTaskStatus();
            if (status != TaskStatus.NEW) {
                allNew = false;
            }
            if (status != TaskStatus.DONE) {
                allDone = false;
            }
            if (status == TaskStatus.IN_PROGRESS) {
                hasInProgress = true;
                break;
            }
        }
        if (allNew) {
            epicTask.setTaskStatus(TaskStatus.NEW);
        } else if (allDone) {
            epicTask.setTaskStatus(TaskStatus.DONE);
        } else if (hasInProgress) {
            epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
        } else {
            epicTask.setTaskStatus(TaskStatus.IN_PROGRESS); // Если есть смешанные статусы
        }
    }

    @Override
    public void updateSubtaskStatus(int id, TaskStatus taskStatus) {
        Subtask needSubtask = getSubtaskById(id);
        needSubtask.setTaskStatus(taskStatus);
        updateEpicStatus(needSubtask.getEpicId());
    }


    @Override
    public Set<Integer> getSubtasksByEpicId(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            return epic.getSubtasks();
        }
        return null;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}




