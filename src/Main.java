import manager.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager taskManager = Managers.getDefaultManager();
        Scanner scanner = new Scanner(System.in);
        taskManager.loadFromFile(taskManager.savedTasksFile);
        Task task1 = new Task (1, "Выбросить мусор", "Весь", TaskStatus.NEW
                ,Duration.ofMinutes(10), LocalDateTime.of(2000, 1, 1, 0, 0));
        Task task2 = new Task(2, "Помыть полы", "Все", TaskStatus.DONE
                ,Duration.ofMinutes(90), LocalDateTime.of(2000, 1, 1, 12, 0));
        Epic epic = new Epic(3, "Собрать вещи", "Все", TaskStatus.NEW
                ,Duration.ofMinutes(40), LocalDateTime.of(2000, 1, 2, 12, 0));
        Subtask subtask = new Subtask(4, "Помыть полы", "Все", 3, TaskStatus.DONE
                ,Duration.ofMinutes(90), LocalDateTime.of(2000, 1, 1, 12, 0));

        while (true) {
            System.out.println("Привет!");
            System.out.println("1-список всех задач");
            System.out.println("2-создать задачу");
            System.out.println("3-выход");
            int command = scanner.nextInt();
            switch (command) {
                case 1: {
                    taskManager.getAllTasks();
                    break;
                }
                case 2: {
                    System.out.print("Введите тип задачи: ");
                    System.out.println("1-обычный, 2-эпик, 3-подзадача");
                    int type = scanner.nextInt();
                    scanner.nextLine();
                    switch (type) {
                        case 1:
                            taskManager.createTask(task1);
                            taskManager.createTask(task2);
                            break;
                        case 2:
                            taskManager.createEpic(epic);
                            break;
                        case 3:
                            taskManager.createSubtask(subtask, subtask.getEpicId());
                            break;
                    }
                    break;
                }
                case 3: {
                    System.out.println();
                }
            }
        }
    }
}
