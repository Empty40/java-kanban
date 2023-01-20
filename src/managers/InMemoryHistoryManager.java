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
        } else {
            int id = task.getTaskId();
            linkedList.removeNode(linkedList.getNode(id));
            linkedList.linkLast(task);
        }
    }
    // Есть вопрос, самый основной, а как в личку написать? ахах))
    // На сколько я помню у нас есть возможность 1 раз написать в личные сообщения за 1 спринт.
    // В остальном спасибо за ревью и за комментарии по код стайлу, он у меня пока хромает, но надеюсь
    // в целом это не говнокод :D

    @Override
    public void remove(int id) {
        linkedList.removeNode(linkedList.getNode(id));
    }

    @Override
    public ArrayList<Task> getHistory() {
        return linkedList.getTasks();
    }
}
