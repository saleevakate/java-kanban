import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultManager();
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
            System.out.println("9-получить историю поиска задач");

            int command = scanner.nextInt();

            switch (command) {
                case 1: {
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
                            Task newTask = new Task(0, name, description, TaskStatus.NEW);
                            taskManager.createTask(newTask);
                            break;
                        case 2:
                            Epic newEpic = new Epic(0, name, description);
                            taskManager.createEpic(newEpic);
                            break;
                        case 3:
                            System.out.println("Введите id эпик задачи");
                            int epicId = scanner.nextInt();
                            Subtask newSubtask = new Subtask(0, name, description, epicId, TaskStatus.NEW);
                            taskManager.createSubtask(newSubtask, epicId);
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
                            Task task = taskManager.getTaskById(id);
                            System.out.println("Имя " + task.getName());
                            System.out.println("Описание " + task.getDescription());
                            System.out.println("Статус " + task.getTaskStatus());
                            break;
                        case 2:
                            Epic epic = taskManager.getEpicById(id);
                            System.out.println("Имя " + epic.getName());
                            System.out.println("Описание " + epic.getDescription());
                            System.out.println("Статус " + epic.getTaskStatus());
                            break;
                        case 3:
                            Subtask subtask = taskManager.getSubtaskById(id);
                            System.out.println("Имя " + subtask.getName());
                            System.out.println("Описание " + subtask.getDescription());
                            System.out.println("Статус " + subtask.getTaskStatus());
                            break;
                    }
                    break;
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
                    System.out.print("Введите тип задачи: ");
                    System.out.println("1-обычный, 2-эпик, 3-подзадача");
                    int type = scanner.nextInt();
                    switch (type) {
                        case 1:
                        taskManager.deleteTasks();
                        break;
                        case 2:
                        taskManager.deleteEpics();
                        break;
                        case 3:
                        taskManager.deleteSubtasks();
                        break;
                    }
                    break;
                }
                case 6: {
                    System.out.println("Введите id задачи");
                    int id = scanner.nextInt();
                    System.out.print("Введите тип задачи: ");
                    System.out.println("1-обычный, 2-эпик, 3-подзадача");
                    int type = scanner.nextInt();
                    switch (type) {
                        case 1:
                            taskManager.deleteTaskById(id);
                            break;
                        case 2:
                            taskManager.deleteEpicById(id);
                            break;
                        case 3:
                            taskManager.deleteSubtaskById(id);
                            break;
                    }
                    break;
                }
                case 7: {
                    System.out.println("Введите id задачи");
                    int id = scanner.nextInt();
                    System.out.print("Введите тип задачи: ");
                    System.out.println("1-обычный, 2-эпик, 3-подзадача");
                    int type = scanner.nextInt();
                    System.out.println("Введите новый статус 1-NEW, 2-IN_PROGRESS, 3-DONE");
                    int status = scanner.nextInt();
                    switch (status) {
                        case 1:
                            if (type == 1) {
                                taskManager.updateTaskStatus(id, TaskStatus.NEW);
                            } else if (type == 2) {
                                taskManager.updateEpicStatus(id);
                            } else if (type == 3) {
                                taskManager.updateSubtaskStatus(id, TaskStatus.NEW);
                            }
                            break;
                        case 2:
                            if (type == 1) {
                                taskManager.updateTaskStatus(id, TaskStatus.IN_PROGRESS);
                            } else if (type == 2) {
                                taskManager.updateEpicStatus(id);
                            } else if (type == 3) {
                                taskManager.updateSubtaskStatus(id, TaskStatus.IN_PROGRESS);
                            }
                            break;
                        case 3:
                            if (type == 1) {
                                taskManager.updateTaskStatus(id, TaskStatus.DONE);
                            } else if (type == 2) {
                                taskManager.updateEpicStatus(id);
                            } else if (type == 3) {
                                taskManager.updateSubtaskStatus(id, TaskStatus.DONE);
                            }
                            break;
                    }
                    break;
                }
                case 8: {
                    System.out.println("Введите id задачи");
                    int id = scanner.nextInt();
                    Set<Integer> subtasksId = taskManager.getSubtasksByEpicId(id);
                    for (int i : subtasksId) {
                        System.out.println(taskManager.getSubtaskById(i).getName());
                    }
                    break;
                }
                case 9: {
                    List<Task> tasksList = taskManager.getHistory();
                    for (Task task : tasksList) {
                        System.out.println(task.getName());
                    }
                }

            }
        }
    }
}
