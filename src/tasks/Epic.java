package tasks;

import java.util.Set;
import java.util.HashSet;

public class Epic extends Task {
    private Set<Integer> subtasksId = new HashSet<>();

    public Epic(int id, String name, String description) {
        super(id, name, description, TaskStatus.NEW);
    }

    public Set<Integer> getSubtasks() {
        return subtasksId;
    }

    public void addSubtask(int id) {
        subtasksId.add(id);
    }
}
