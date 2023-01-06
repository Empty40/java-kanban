package managers;

import java.util.ArrayList;
import java.util.List;

import interfaces.HistoryManager;

import models.Task;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() == 10) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
