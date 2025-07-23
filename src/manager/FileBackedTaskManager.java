package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File savedTasksFile;

    public FileBackedTaskManager(File savedTasksFile) {
        this.savedTasksFile = savedTasksFile;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(savedTasksFile))) {
            writer.write("id,type,name,status,description,epic");
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
            writer.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File savedTasksFile) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(savedTasksFile.toPath())) {
            bufferedReader.readLine();
            Map<Integer, Task> tasks = new HashMap<>();
            Map<Integer, Epic> epics = new HashMap<>();
            Map<Integer, Subtask> subtasks = new HashMap<>();
            int maxId = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                Object obj = CSVFormatter.fromString(line);
                if (obj != null) {
                    if (obj instanceof Subtask) {
                        Subtask subtask = (Subtask) obj;
                        if (subtask.getId() > maxId) {
                            maxId = subtask.getId();
                        }
                        subtasks.put(subtask.getId(), subtask);
                    } else if (obj instanceof Epic) {
                        Epic epic = (Epic) obj;
                        if (epic.getId() > maxId) {
                            maxId = epic.getId();
                        }
                        epics.put(epic.getId(), epic);
                    } else if (obj instanceof Task) {
                        Task task = (Task) obj;
                        if (task.getId() > maxId) {
                            maxId = task.getId();
                        }
                        tasks.put(task.getId(), task);
                    }
                }
            }
            for (Subtask subtask : subtasks.values()) {
                Epic parentEpic = epics.get(subtask.getEpicId());
                if (parentEpic != null) {
                    parentEpic.addSubtask(subtask.getId());
                }
            }
            FileBackedTaskManager manager = new FileBackedTaskManager(savedTasksFile);
            InMemoryTaskManager.tasks = tasks;
            InMemoryTaskManager.epics = epics;
            InMemoryTaskManager.subtasks = subtasks;
            InMemoryTaskManager.idCounter = maxId;
            return manager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке из файла", e);
        }
    }

    @Override
    public int generateId() {
        super.generateId();
        return idCounter;
    }

    @Override
    public void getTasks() {
        super.getTasks();
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
