package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskHistory implements HistoryManager {

    private final HashMap<Integer, Node> nodes = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> historyList = new ArrayList<>();
        if (first == null) {
            System.out.println("Нет просмотренных задач");
        }
        Node node = first;
        while (node != null) {
            historyList.add(node.value);
            node = node.next;
        }
        return historyList;
    }

    @Override
    public void addTask(Task task) {
        removeNode(task.getId());
        linkLast(task);
        nodes.put(task.getId(), last);
    }

    @Override
    public void addEpic(Epic epic) {
        removeNode(epic.getId());
        linkLast(epic);
        nodes.put(epic.getId(), last);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        removeNode(subtask.getId());
        linkLast(subtask);
        nodes.put(subtask.getId(), last);
    }

    public void removeNode(int id) {
        Node node = nodes.get(id);
        if (node == null) {
            return;
        } else if (node == first && node == last) {
            nodes.remove(id, node);
            first = null;
            last = null;
        } else if (node == first) {
            first = first.next;
            first.setPrev(null);
            nodes.remove(id, node);
        } else if (node == last) {
            last = last.prev;
            last.setNext(null);
            nodes.remove(id, node);
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            nodes.remove(id, node);
        }
    }

    @Override
    public void removeTask(Task task) {
        removeNode(task.getId());
    }

    @Override
    public void removeEpic(Epic epic) {
        removeNode(epic.getId());
    }

    @Override
    public void removeSubtask(Subtask subtask) {
        removeNode(subtask.getId());
    }

    private void linkLast(Task task) {
        Node node = new Node(task, last, null);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
    }
}
