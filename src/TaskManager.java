import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class TaskManager {
    private static int idCounter = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    TaskStatus status = TaskStatus.NEW;

    private static int generateId() {
        return idCounter++;
    }

    public void getTasks() {
        System.out.println("Список всех задач:");
        for (Task task : tasks.values()) {
            System.out.println(task.getName());
        }
    }

    public void createTask(String name, String description) {
        Task newTask = new Task(generateId(), name, description, status);
        tasks.put(newTask.getId(), newTask);
        System.out.println("Задача добавлена");
    }

    public void createEpic(String name, String description) {
        Epic newEpic = new Epic(generateId(), name, description, status);
        tasks.put(newEpic.getId(), newEpic);
    }

    public Task createSubtask(int id, String name, String description) {
        Subtask newSubtask = new Subtask(generateId(), name, description, id, status);
        Epic parentTask = (Epic) getTaskById(id);
        parentTask.addSubtask(newSubtask.getId());
        tasks.put(newSubtask.getId(), newSubtask);
        return newSubtask;
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Task updateTask(int id, String name, String description) {
        Task needTask = getTaskById(id);
        needTask.setName(name);
        needTask.setDescription(description);
        System.out.println("Задача обновлена");
        return needTask;
    }

    public void deleteTask() {
        tasks.clear();
    }

    public void deleteTaskToId(int id) {
        this.tasks.remove(id);
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





