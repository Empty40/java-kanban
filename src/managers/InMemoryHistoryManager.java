package managers;

import interfaces.HistoryManager;
import interfaces.CustomLinkedList;

import models.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList linkedList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        int id = task.getTaskId();
        linkedList.removeNode(linkedList.getNode(id));
        linkedList.linkLast(task);
    }
    // В моём случае, как я понимаю, моя нода это не отдельный класс, в связи с этим я не могу сделать переменную ноду
    // с типом Node и записать в неё полученную ноду

    @Override
    public void remove(int id) {
        linkedList.removeNode(linkedList.getNode(id));
    }

    @Override
    public ArrayList<Task> getHistory() {
        return linkedList.getTasks();
    }
}
