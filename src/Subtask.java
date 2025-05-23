public class Subtask extends Task {

    private int epicId;

    public Subtask(int id, String name, String description, int epicId, TaskStatus taskStatus) {
        super(id, name, description, taskStatus);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
