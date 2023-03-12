package interfaces;

import models.Task;

import java.util.HashMap;
import java.util.ArrayList;

public class CustomLinkedList {

    static class Node {

        public Task data;
        public Node next;
        public Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node head;
    private Node tail;

    private final HashMap<Integer, Node> linkedMap = new HashMap<>();

    public ArrayList<Integer> getId() {
        ArrayList<Integer> taskId = null;
        if (linkedMap != null) {
            taskId.addAll(linkedMap.keySet());
        }
        return taskId;
    }

    public Node getNode(int id) {
        return linkedMap.get(id);
    }

    public void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newTail = new Node(oldTail, task, null);
        tail = newTail;
        if (oldTail == null) {
            head = newTail;
        } else {
            oldTail.next = newTail;
        }
        linkedMap.put(task.getTaskId(), newTail);
    }

    public void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node == head) {
            head = node.next;
            if (node.next == null) {
                tail = null;
            } else {
                node.next.prev = null;
            }
        } else if (node == tail) {
            tail = node.prev;
            if (node.prev == null) {
                head = null;
            } else {
                node.prev.next = null;
            }
        } else if (node != head & node != tail) {
            return;
        } else {
            node.next.prev = node.prev;
            node.prev.next = node.next;
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskList = new ArrayList<>();
        Node node = head;
        while (node != null) {
            taskList.add(node.data);
            node = node.next;
        }
        return taskList;
    }
}