import java.util.HashMap;
import java.util.Set;

public class TaskManager {
    private static int idCounter = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public static int generateId() {
        return idCounter++;
    }

    public void getTasks() {
        System.out.println("Список всех задач:");
        for (Task task : tasks.values()) {
            System.out.println(task.getName());
        }
        for (Epic epic : epics.values()) {
            System.out.println(epic.getName());
        }
        for (Subtask subtask : subtasks.values()) {
            System.out.println(subtask.getName());
        }
    }

    public void createTask(Task newTask) {
        newTask.setId(generateId());
        tasks.put(newTask.getId(), newTask);
        System.out.println("Задача добавлена");
    }

    public void createEpic(Epic newEpic) {
        newEpic.setId(generateId());
        epics.put(newEpic.getId(), newEpic);
    }

    public Subtask createSubtask(Subtask newSubtask, int epicId) {
        Epic parentTask = getEpicById(epicId);
        parentTask.addSubtask(newSubtask.getId());
        subtasks.put(newSubtask.getId(), newSubtask);
        epics.containsValue(TaskStatus.NEW);
        return newSubtask;
    }

    public Task getTaskById(int id) {
        return tasks.get(id);

    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void updateTask(Task task) {
        int id = task.getId();
        Task savedTask = tasks.get(id);
        if (savedTask == null) {
            System.out.println("Такой задачи нет");
        }
        tasks.put(id, task);
    }

    public void updateEpic(Epic epic) {
        int id = epic.getId();
        Epic savedEpic = epics.get(id);
        if (savedEpic == null) {
            throw new IllegalArgumentException("Epic с ID " + id + " не найден");
        }
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
        epics.put(id, savedEpic);
    }

    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            System.out.println("Такой задачи нет");
        }
        subtasks.put(id, subtask);
        int epicId = subtask.getEpicId();
        updateEpicStatus(epicId);
    }

    public void deleteTask() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public void deleteTaskById(int id) {
            tasks.remove(id);
    }
    public void deleteEpicById(int id) {
            epics.remove(id);
    }

    public void deleteSubtaskById(int id) {
            Subtask subtask = getSubtaskById(id);
            int epicId = subtask.getEpicId();
            updateEpicStatus(epicId);
            subtasks.remove(id);
    }

    public void updateTaskStatus(int id, TaskStatus taskStatus) {
        Task needTask = getTaskById(id);
        needTask.setTaskStatus(taskStatus);
    }

    public void updateEpicStatus(int epicId) {
        Epic epicTask = getEpicById(epicId);
        if (epicTask == null) {
            throw new IllegalArgumentException("Epic с ID " + epicId + " не найден");
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

    public void updateSubtaskStatus(int id, TaskStatus taskStatus) {
        Subtask needSubtask = getSubtaskById(id);
        needSubtask.setTaskStatus(taskStatus);
        updateEpicStatus(needSubtask.getEpicId());
    }


    public Set<Integer> getSubtasksById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            return epic.getSubtasks();
        }
        return null;
    }
}




