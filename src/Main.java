import manager.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager taskManager = Managers.getDefaultManager();
        Scanner scanner = new Scanner(System.in);
        taskManager.loadFromFile(taskManager.savedTasksFile);
        Task task1 = new Task(1, "Таск1", "Весь",
                Duration.ofMinutes(10), LocalDateTime.of(2000, 1, 1, 1, 0));
        Task task2 = new Task(2, "Таск2", "Все",
                Duration.ofMinutes(90), LocalDateTime.of(2000, 2, 2, 2, 0));

        Epic epic = new Epic(3, "Эпик", "Все",
                Duration.ofMinutes(90), LocalDateTime.of(2000, 3, 3, 3, 0));

        Subtask subtask1 = new Subtask(4, "Сабтаск1", "Все", 3,
                Duration.ofMinutes(90), LocalDateTime.of(2000, 4, 4, 4, 0));

        Subtask subtask2 = new Subtask(5, "Сабтаск2", "Все", 3,
                Duration.ofMinutes(50), LocalDateTime.of(2000, 5, 5, 5, 0));

        while (true) {
            System.out.println("Привет!");
            System.out.println("1-список всех задач");
            System.out.println("2-создать задачу");
            System.out.println("3-конец эпика");
            System.out.println("4-сабтаск по id");
            System.out.println("5-приоритет задач");
            System.out.println("6-удалить таску");
            System.out.println("7-удалить эпик");
            System.out.println("8-удалить сабтаск");
            int command = scanner.nextInt();
            switch (command) {
                case 1: {
                    System.out.println(taskManager.getTasks());
                    System.out.println(taskManager.getSubtasks());
                    System.out.println(taskManager.getEpics());
                    break;
                }
                case 2: {
                    taskManager.createTask(task1);
                    taskManager.createTask(task2);
                    taskManager.createEpic(epic);
                    taskManager.createSubtask(subtask1);
                    taskManager.createSubtask(subtask2);
                    break;
                }
                case 3: {
                    System.out.println(epic.getStartTime());
                    System.out.println(epic.getDuration());
                    System.out.println(epic.getEndTime());
                    break;
                }
                case 4: {
                    System.out.println(taskManager.getSubtasksByEpicId(epic.getId()));
                    break;
                }
                case 5: {
                    for (Task task : taskManager.prioritizedTasks) {
                        System.out.println(task.getName() + task.getStartTime());
                    }
                    break;
                }
                case 6: {
                    taskManager.deleteTasks();
                    break;
                }
                case 7: {
                    taskManager.deleteEpics();
                    break;
                }
                case 8: {
                    taskManager.deleteSubtasks();
                    break;
                }
            }
        }
    }
}
