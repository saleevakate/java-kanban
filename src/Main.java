import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);


        while (true) {
            System.out.println("Поехали!");
            System.out.println("1-список всех задач");
            System.out.println("2-создать задачу");
            System.out.println("3-получить задачу по id");
            System.out.println("4-обновить задачу");
            System.out.println("5-удалить все задачи");
            System.out.println("6-удалить задачу по id");
            System.out.println("7-изменить статус задачи");
            System.out.println("8-список подзадач для эпика");

            int command = scanner.nextInt();

            switch (command) {
                case 1: {
                    taskManager.getTasks();
                    break;
                }
                case 2: {
                    System.out.print("Введите тип задачи: ");
                    System.out.println("1-обычный, 2-эпик, 3-подзадача");
                    int type = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Введите имя задачи");
                    String name = scanner.nextLine();
                    System.out.println("Введите описание задачи");
                    String description = scanner.nextLine();
                    switch (type) {
                        case 1:
                            Task newTask = new Task(taskManager.generateId(), name, description, TaskStatus.NEW);
                            taskManager.createTask(newTask);
                            break;
                        case 2:
                            Epic newEpic = new Epic(taskManager.generateId(), name, description, TaskStatus.NEW);
                            taskManager.createEpic(newEpic);
                            break;
                        case 3:
                            System.out.println("Введите id эпик задачи");
                            int id = scanner.nextInt();
                            Subtask newSubtask = new Subtask(taskManager.generateId(), name, description, id, TaskStatus.NEW);
                            taskManager.createSubtask(newSubtask, id);
                            break;
                    }
                    break;
                }
                case 3: {
                    System.out.println("Введите id задачи");
                    int id = scanner.nextInt();
                    System.out.print("Введите тип задачи: ");
                    System.out.println("1-обычный, 2-эпик, 3-подзадача");
                    int type = scanner.nextInt();
                    switch (type) {
                        case 1:
                            System.out.println("Имя " + taskManager.getTaskById(id).getName());
                            System.out.println("Описание " + taskManager.getTaskById(id).getDescription());
                            System.out.println("Статус " + taskManager.getTaskById(id).getTaskStatus());
                            break;
                        case 2:
                            System.out.println("Имя " + taskManager.getEpicById(id).getName());
                            System.out.println("Описание " + taskManager.getEpicById(id).getDescription());
                            System.out.println("Статус " + taskManager.getEpicById(id).getTaskStatus());
                            break;
                        case 3:
                            System.out.println("Имя " + taskManager.getSubtaskById(id).getName());
                            System.out.println("Описание " + taskManager.getSubtaskById(id).getDescription());
                            System.out.println("Статус " + taskManager.getSubtaskById(id).getTaskStatus());
                            break;
                    }
                }
                case 4: {
                    System.out.print("Введите тип задачи: ");
                    System.out.println("1-обычный, 2-эпик, 3-подзадача");
                    int type = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Введите ID задачи:");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Введите новое имя задачи:");
                    String name = scanner.nextLine();
                    System.out.println("Введите новое описание задачи:");
                    String description = scanner.nextLine();
                    System.out.println("Введите статус задачи");
                    TaskStatus taskStatus = TaskStatus.valueOf(scanner.nextLine());
                    switch (type) {
                        case 1:
                            Task task = new Task(id, name, description, taskStatus);
                            taskManager.updateTask(task);
                            break;
                        case 2:
                            Epic existingEpic = taskManager.getEpicById(id);
                            existingEpic.setName(name);
                            existingEpic.setDescription(description);
                            existingEpic.setTaskStatus(taskStatus);
                            taskManager.updateEpic(existingEpic);
                            break;
                        case 3:
                            Subtask searchSubtask = taskManager.getSubtaskById(id);
                            int epicId = searchSubtask.getEpicId();
                            Subtask subtask = new Subtask(id, name, description, epicId, taskStatus);
                            taskManager.updateSubtask(subtask);
                            break;
                    }
                    break;
                }
                case 5: {
                    taskManager.deleteTask();
                    System.out.println("Все задачи удалены");
                    break;
                }
                case 6: {
                    System.out.println("Введите id задачи");
                    int id = scanner.nextInt();
                    taskManager.deleteTaskToId(id);
                    break;
                }
                case 7: {
                    System.out.println("Введите id задачи");
                    int id = scanner.nextInt();
                    System.out.println("Введите новый статус 1-NEW, 2-IN_PROGRESS, 3-DONE");
                    int status = scanner.nextInt();
                    switch (status) {
                        case 1:
                            taskManager.updateStatus(id, TaskStatus.NEW);
                            break;
                        case 2:
                            taskManager.updateStatus(id, TaskStatus.IN_PROGRESS);
                            break;
                        case 3:
                            taskManager.updateStatus(id, TaskStatus.DONE);
                            break;
                    }
                    break;
                }
                case 8: {
                    System.out.println("Введите id задачи");
                    int id = scanner.nextInt();
                    Set<Integer> subtasksId = taskManager.getSubtasksById(id);
                    for (int i : subtasksId) {
                        System.out.println(taskManager.getSubtaskById(i).getName());
                    }
                    break;
                }
            }
        }
    }
}
