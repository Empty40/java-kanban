package interfaces;

import models.Task;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    void getHistory();
}