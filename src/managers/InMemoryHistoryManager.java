package managers;

import java.util.ArrayList;

import Interface.HistoryManager;
import models.Task;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> history = new ArrayList<>();

    public void update() {
        if (history.size() == 10) {
            history.remove(0);
        }
    }

    @Override
    public void add(Task task) {
        update();
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
