import manager.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager taskManager = Managers.getDefaultManager();
        Scanner scanner = new Scanner(System.in);
        taskManager.loadFromFile(taskManager.savedTasksFile);
        Task task1 = new Task(1, "Выбросить мусор", "Весь", TaskStatus.NEW
                , Duration.ofMinutes(10), LocalDateTime.of(2000, 1, 1, 0, 0));
        Task task2 = new Task(2, "Помыть полы", "Все", TaskStatus.DONE
                , Duration.ofMinutes(90), LocalDateTime.of(2000, 1, 1, 12, 0));

        Epic epic = new Epic(3, "Собрать вещи", "Все", TaskStatus.NEW
                , Duration.ofMinutes(40), LocalDateTime.of(2000, 1, 3, 12, 0));

        Subtask subtask1 = new Subtask(4, "Помыть полы", "Все", 3, TaskStatus.DONE
                , Duration.ofMinutes(90), LocalDateTime.of(2000, 1, 1, 12, 0));

        Subtask subtask2 = new Subtask(5, "Помыть полы", "Все", 3, TaskStatus.DONE
                , Duration.ofMinutes(50), LocalDateTime.of(2000, 2, 12, 13, 30));

        while (true) {
            System.out.println("Привет!");
            System.out.println("1-список всех задач");
            System.out.println("2-создать задачу");
            System.out.println("3-конец задачи по id");
            System.out.println("4-сабтаск по id");
            int command = scanner.nextInt();
            switch (command) {
                case 1: {
                    taskManager.getAllTasks();
                    break;
                }
                case 2: {
                    taskManager.createTask(task1);
                    taskManager.createTask(task2);
                    taskManager.createEpic(epic);
                    taskManager.createSubtask(subtask1, subtask1.getEpicId());
                    taskManager.createSubtask(subtask2, subtask2.getEpicId());
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
                }
            }
        }
    }
}
