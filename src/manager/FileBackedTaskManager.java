package manager;

import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;


public class FileBackedTaskManager extends InMemoryTaskManager {

    public static File savedTasksFile = new File("resources/data.csv");

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(savedTasksFile))) {
            writer.write("id,type,name,status,description,duration,startTime,epic");
            writer.newLine();
            for (Task task : tasks.values()) {
                writer.write(CSVFormatter.toString(task));
                writer.newLine();
            }
            for (Epic epic : epics.values()) {
                writer.write(CSVFormatter.toString(epic));
                writer.newLine();
            }
            for (Subtask subtask : subtasks.values()) {
                writer.write(CSVFormatter.toString(subtask));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File savedTasksFile) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(savedTasksFile.toPath())) {
            bufferedReader.readLine();
            FileBackedTaskManager manager = new FileBackedTaskManager();
            int maxId = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                Task task = CSVFormatter.fromString(line);
                if (task != null) {
                    if (task.getType() == TaskType.SUBTASK) {
                        Subtask subtask = (Subtask) task;
                        if (subtask.getId() > maxId) {
                            maxId = subtask.getId();
                        }
                        manager.createSubtask(subtask, subtask.getEpicId());
                    } else if (task.getType() == TaskType.EPIC) {
                        Epic epic = (Epic) task;
                        if (epic.getId() > maxId) {
                            maxId = epic.getId();
                        }
                        manager.createEpic(epic);
                    } else if (task.getType() == TaskType.TASK) {
                        Task task1 = task;
                        if (task1.getId() > maxId) {
                            maxId = task1.getId();
                        }
                        manager.createTask(task1);
                    }
                }
            }
            for (Subtask subtask : subtasks.values()) {
                Epic parentEpic = manager.getEpicById(subtask.getEpicId());
                if (parentEpic != null) {
                    parentEpic.addSubtask(subtask);
                }
            }
            idCounter = maxId;
            return manager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке из файла", e);
        }
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
}
