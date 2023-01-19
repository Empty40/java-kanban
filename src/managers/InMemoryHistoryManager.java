package managers;

import interfaces.HistoryManager;
import interfaces.CustomLinkedList;

import models.Task;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList<Task> linkedList = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        if (linkedList.getLinkedMap().containsKey(task.getTaskId())) {
            linkedList.removeNode(linkedList.getNode(task.getTaskId()));
            linkedList.linkLast(task);
        } else {
            linkedList.linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        if (linkedList.getLinkedMap().containsKey(id)) {
            linkedList.removeNode(linkedList.getNode(id));
        } else {
            System.out.println("Такой задачи нет в истории просмотров");
        }
    }

    @Override
    public void getHistory() {
        System.out.println(linkedList.getTasks());
    }
}
