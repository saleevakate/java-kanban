package tasks;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int id, String name, String description, int epicId, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }
}
