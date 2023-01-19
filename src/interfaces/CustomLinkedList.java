package interfaces;

import java.util.HashMap;
import java.util.ArrayList;

public class CustomLinkedList<Task> {

    private Node head;

    private Node tail;

    private HashMap<Integer, Node> linkedMap = new HashMap<>();

    public Node getNode(int id) {
        return linkedMap.get(id);
    }

    public HashMap<Integer, Node> getLinkedMap() {
        return linkedMap;
    }

    private ArrayList<Task> taskList = new ArrayList<>();

    class Node {

        public Task data;
        public Node next;
        public Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    public void linkLast(models.Task task) {
        final Node oldTail = tail;
        final Node newTail = new Node(oldTail, (Task) task, null);
        tail = newTail;
        if (oldTail == null)
            head = newTail;
        else
            oldTail.next = newTail;
        linkedMap.put(task.getTaskId(), newTail);
    }

    public void removeNode(Node node) {
        if (node == head) {
            head = node.next;
            node.next.prev = null;
        } else if (node == tail) {
            tail = node.prev;
            node.prev.next = null;
        } else {
            node.next.prev = node.prev;
            node.prev.next = node.next;
        }
    }

    public ArrayList<Task> getTasks() {
        taskList.clear();
        Node node = head;
        while (node != null) {
            taskList.add(node.data);
            node = node.next;
        }
        return taskList;
    }
}