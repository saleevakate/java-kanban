import java.util.Scanner;
import java.util.HashMap;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Поехали!");
            System.out.println("1-список всех задач");
            System.out.println("2-сщздать задачу");
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
                    switch (type) {
                        case 1:
                            taskManager.createTask();
                            break;
                        case 2:
                            taskManager.createEpic();
                            break;
                        case 3:
                            System.out.println("Введите id эпик задачи");
                            int parentId = scanner.nextInt();
                            taskManager.createSubtask(parentId);
                            break;
                    }
                    break;
                }
                case 3: {
                    System.out.println("Введите id задачи");
                    int id = scanner.nextInt();
                    System.out.println("Имя " + taskManager.getTaskById(id).getName());
                    System.out.println("Описание " + taskManager.getTaskById(id).getDescription());
                    break;
                }
                case 4: {
                    System.out.println("Введите ID задачи:");
                    int id = scanner.nextInt();
                    System.out.println(taskManager.updateTask(id));
                }
                case 5: {
                    taskManager.deleteTask();
                    System.out.println("Все задачи удалены");
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
                        System.out.println(taskManager.getTaskById(i));
                    }
                }
            }
        }
    }
}
