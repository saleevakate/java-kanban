package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File savedTasksFile;

    public FileBackedTaskManager(File savedTasksFile) {
        this.savedTasksFile = savedTasksFile;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(savedTasksFile))) {
            writer.write(CSVFormatter.getHeader());
            writer.newLine();

            for (Task task : tasks.values()) {
                writer.write(CSVFormatter.toString(task));
                writer.newLine();
            }
            for (Epic epic : epics.values()) {
                writer.write(CSVFormatter.toString(epic));
                writer.newLine();
            }
            for (Subtask subtask: subtasks.values()) {
                writer.write(CSVFormatter.toString(subtask));
                writer.newLine();
            }
            writer.newLine();
            //writer.write(CSVFormatter.toStringHistory(historyManager.getHistory()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File savedTasksFile) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(savedTasksFile);
        try {
            // Наполняем задачами
            Files.readString(savedTasksFile.toPath());
            // Восстанавливаем историю

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке из файла", e);
        }
        return taskManager;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask, int epicId) {
        super.createSubtask(subtask, epicId);
        save();
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
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void updateTaskStatus(int id, TaskStatus taskStatus) {
        super.updateTaskStatus(id, taskStatus);
        save();
    }

    @Override
    public void updateEpicStatus(int epicId) {
        super.updateEpicStatus(epicId);
        save();
    }

    @Override
    public void updateSubtaskStatus(int id, TaskStatus taskStatus) {
        super.updateSubtaskStatus(id, taskStatus);
        save();
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
