import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class TaskManager {
    static int idCounter = 0;
    HashMap<Integer, Task> tasks = new HashMap<>();
    Scanner scanner = new Scanner(System.in);
    TaskStatus status = TaskStatus.NEW;

    private static int generateId() {
        return idCounter++;
    }

    public void getTasks() {
        System.out.println("Список всех задач:");
        for (Task task : tasks.values()) {
            System.out.println("Id задачи " + task.getId());
            System.out.println("Имя " + task.getName());
            System.out.println("Описание " + task.getDescription());
            System.out.println("Статус " + task.getTaskStatus());
        }
    }

    public void createTask() {
        System.out.println("Введите имя задачи");
        String name = scanner.nextLine();
        System.out.println("Введите описание задачи");
        String description = scanner.nextLine();
        Task newTask = new Task(generateId(), name, description, status);
        tasks.put(newTask.getId(), newTask);
        System.out.println("Задача добавлена");
    }

    public void createEpic() {
        System.out.println("Введите имя задачи");
        String name = scanner.nextLine();
        System.out.println("Введите описание задачи");
        String description = scanner.nextLine();
        Epic newEpic = new Epic(generateId(), name, description, status);
        tasks.put(newEpic.getId(), newEpic);
        System.out.println("Эпик задача добавлена");
    }

    public Task createSubtask(int parentId) {
        System.out.println("Введите имя задачи");
        String name = scanner.nextLine();
        System.out.println("Введите описание задачи");
        String description = scanner.nextLine();
        Subtask newSubtask = new Subtask(generateId(), name, description, parentId, status);
        Epic parentTask = (Epic) getTaskById(parentId);
        tasks.put(newSubtask.getId(), newSubtask);
        System.out.println("Подзадача добавлена к задаче " + getTaskById(parentId));
        return newSubtask;
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Task updateTask(int id) {
        System.out.println("Введите наименование задачи:");
        String newName = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String newDescription = scanner.nextLine();
        Task needTask = getTaskById(id);
        needTask.setName(newName);
        needTask.setDescription(newDescription);
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
            needTask.setTaskStatus(status);
            if (needTask instanceof Subtask) {
                updateStatusEpic(((Subtask) needTask).getId());
                return needTask;
        } else {
            return needTask;
        }
    }

    public void updateStatusEpic(int epicId) {
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





