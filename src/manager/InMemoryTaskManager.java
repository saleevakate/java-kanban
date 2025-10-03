package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int idCounter = 1;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();

    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public List<Task> getTasks() {
        List<Task> tasksList = new ArrayList<>();
        tasks.values().stream()
                .forEach(task -> tasksList.add(task));
        return tasksList;
    }

    @Override
    public List<Subtask> getSubtasks() {
        List<Subtask> subtasksList = new ArrayList<>();
        subtasks.values().stream()
                .forEach(subtask -> subtasksList.add(subtask));
        return subtasksList;
    }

    @Override
    public List<Epic> getEpics() {
        List<Epic> epicList = new ArrayList<>();
        epics.values().stream()
                .forEach(epicList::add);
        return epicList;
    }

    @Override
    public int generateId() {
        return idCounter++;
    }

    @Override
    public void createTask(Task task) {
        task.setId(generateId());
        if (!priorityCheck(task)) {
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        Epic parentTask = getEpicById(subtask.getEpicId());
        if (parentTask == null) {
            throw new IllegalArgumentException("Такого эпика нет");
        }
        if (subtask.getEpicId() == subtask.getId()) {
            throw new IllegalArgumentException("Подзадача не может быть своим же эпиком");
        }
        subtask.setId(generateId());
        if (!priorityCheck(subtask)) {
            parentTask.addSubtask(subtask);
            subtasks.put(subtask.getId(), subtask);
            prioritizedTasks.add(subtask);
            updateEpicStatus(parentTask.getId());
            parentTask.updateTime();
        }

    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
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
        tasks.values().forEach(task -> {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        });
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.values().forEach(epic -> {
            historyManager.remove(epic.getId());
            prioritizedTasks.remove(epic);
        });
        subtasks.values().forEach(subtask -> {
            historyManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
        });
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.values().forEach(subtask -> {
            historyManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
        });
        subtasks.clear();
        epics.values().forEach(epic -> {
            epic.setTaskStatus(TaskStatus.NEW);
            epic.updateTime();
        });
    }

    @Override
    public void deleteTaskById(int id) {
        historyManager.remove(id);
        prioritizedTasks.remove(getTaskById(id));
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        List<Subtask> subtasksByEpicId = getSubtasksByEpicId(id);
        prioritizedTasks.remove(getEpicById(id));
        historyManager.remove(id);
        epics.remove(id);
        subtasksByEpicId.stream()
                .forEach(subtask -> {
                    subtasks.remove(subtask);
                    historyManager.remove(id);
                    prioritizedTasks.remove(subtask);
                });
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = getSubtaskById(id);
        int epicId = subtask.getEpicId();
        updateEpicStatus(epicId);
        subtasks.remove(id);
        historyManager.remove(id);
        prioritizedTasks.remove(subtask);
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
            throw new IllegalArgumentException("Epic с ID " + epicId + " не найден");
        }
        List<Subtask> subtaskIds = epicTask.getSubtasks();
        if (subtaskIds.isEmpty()) {
            epicTask.setTaskStatus(TaskStatus.NEW);
            return;
        }
        boolean allNew = true;
        boolean allDone = true;
        boolean hasInProgress = false;
        for (Subtask subtask : subtaskIds) {
            if (subtask == null) {
                throw new IllegalStateException("Подзадача " + subtask + " не найдена");
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
            epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public void updateSubtaskStatus(int id, TaskStatus taskStatus) {
        Subtask needSubtask = getSubtaskById(id);
        needSubtask.setTaskStatus(taskStatus);
        updateEpicStatus(needSubtask.getEpicId());
    }


    @Override
    public List<Subtask> getSubtasksByEpicId(int id) {
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

    public boolean overLap(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();
        boolean overLap = start2.isAfter(end1) || start1.isAfter(end2);
        return overLap;
    }

    public boolean priorityCheck(Task task) {
        boolean priorityTask = false;
        Optional<Task> overlappingTask = prioritizedTasks.stream()
                .filter(task1 -> overLap(task, task1))
                .findFirst();
        if (overlappingTask.isPresent()) {
            Task overLappedTask = overlappingTask.get();
            String message = "Задача " + task.getName() + " пересекается с задачей " + overLappedTask.getName();
            throw new TaskValidationException(message);
        }
        return priorityTask;
    }

    public class TaskValidationException extends RuntimeException {
        public TaskValidationException(String message) {
            super(message);
        }
    }
}




