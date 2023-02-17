package managers;

import interfaces.HistoryManager;
import interfaces.CustomLinkedList;

import models.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList linkedList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        int id = task.getTaskId();
        linkedList.removeNode(linkedList.getNode(id));
        linkedList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        linkedList.removeNode(linkedList.getNode(id));
    }

    @Override
    public ArrayList<Task> getHistory() {
        return linkedList.getTasks();
    }
}
