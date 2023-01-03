package models;

import Interface.HistoryManager;
import Interface.TaskManager;
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
