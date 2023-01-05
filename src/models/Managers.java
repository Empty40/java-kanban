package models;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
