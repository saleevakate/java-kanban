package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File savedTasksFile;

    public FileBackedTaskManager(File savedTasksFile) {
        this.savedTasksFile = savedTasksFile;
    }

    public static FileBackedTaskManager loadFromFile(File savedTasksFile) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(savedTasksFile);
        // Наполнить данными из файла
        return taskManager;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(savedTasksFile))) {

            writer.write(CSVFormatter.getHeader());
            writer.newLine();
            CSVFormatter.toStringTask(tasks);
            writer.newLine();
            CSVFormatter.toStringEpic(epics);
            writer.newLine();
            CSVFormatter.toStringSubtask(subtasks);
            writer.newLine();
            // 3. Пишем историю
            writer.write(CSVFormatter.toStringHistory(historyManaёger.getHistory()));
            writer.newLine();

        } catch (IOException e) {
            // Выбросить собственное исключение
        }
    }


    @Override
    public void createTask(Task task) {
        super.createTask(task);
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
    }

    @Override
    public Subtask createSubtask(Subtask subtask, int epicId) {
        return super.createSubtask(subtask, epicId);
    }

    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(int id) {
        return super.getEpicById(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        return super.getSubtaskById(id);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
    }

    @Override
    public void updateTaskStatus(int id, TaskStatus taskStatus) {
        super.updateTaskStatus(id, taskStatus);
    }

    @Override
    public void updateEpicStatus(int epicId) {
        super.updateEpicStatus(epicId);
    }

    @Override
    public void updateSubtaskStatus(int id, TaskStatus taskStatus) {
        super.updateSubtaskStatus(id, taskStatus);
    }

    @Override
    public Set<Integer> getSubtasksByEpicId(int id) {
        return super.getSubtasksByEpicId(id);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }
}
