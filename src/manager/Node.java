package manager;

import tasks.Task;

public class Node {
    Task value;
    Node prev;
    Node next;

    public Node(Task value, Node prev, Node next) {
        this.value = value;
        this.prev = prev;
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
