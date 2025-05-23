import java.util.HashMap;
import java.util.Set;

public class TaskManager {
    private static int idCounter = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    TaskStatus status = TaskStatus.NEW;

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
        tasks.put(newTask.getId(), newTask);
        System.out.println("Задача добавлена");
    }

    public void createEpic(Epic newEpic) {
        epics.put(newEpic.getId(), newEpic);
    }

    public Subtask createSubtask(Subtask newSubtask, int id) {
        Epic parentTask = getEpicById(id);
        parentTask.addSubtask(newSubtask.getId());
        subtasks.put(newSubtask.getId(), newSubtask);
        //updateStatus(id, TaskStatus.NEW);
        return newSubtask;
    }

    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            return task;
        } else {
            return null;
        }
    }

    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            return epic;
        } else {
            return null;
        }
    }

    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            return subtask;
        } else {
            return null;
        }
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
    }

    public void deleteTask() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public void deleteTaskToId(int id) {
        if (tasks.containsKey(id) == true) {
            tasks.remove(id);
        } else if (epics.containsKey(id) == true) {
            epics.remove(id);
        } else if (subtasks.containsKey(id) == true) {
            subtasks.remove(id);
            updateStatusEpic(id);
        }
    }

    public Task updateStatus(int id, TaskStatus taskStatus) {
        Task needTask = getTaskById(id);
        if (!(needTask instanceof Epic)) {
            needTask.setTaskStatus(taskStatus);
            if (needTask instanceof Subtask) {
                updateStatusEpic(((Subtask) needTask).getEpicId());
                return needTask;
            }
            return needTask;
        } else {
            return needTask;
        }
    }

    private void updateStatusEpic(int epicId) {
        Epic epicTask = (Epic) getTaskById(epicId);
        if (epicTask.getSubtasks().stream().allMatch(taskId -> getTaskById(taskId).getTaskStatus() == TaskStatus.NEW)) {
            epicTask.setTaskStatus(TaskStatus.NEW);
        } else if (epicTask.getSubtasks().stream().allMatch(taskId -> getTaskById(taskId).getTaskStatus() == TaskStatus.DONE)) {
            epicTask.setTaskStatus(TaskStatus.DONE);
        } else {
            epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public Set<Integer> getSubtasksById(int id) {
        Task needTask = getTaskById(id);
        if (needTask instanceof Epic) {
            return ((Epic) needTask).getSubtasks();
        } else {
            return null;
        }
    }
}




