package models;

import interfaces.HistoryManager;

import managers.InMemoryHistoryManager;
import managers.HttpTaskManager;

public class Managers {

    public static HttpTaskManager getDefault() {
        return new HttpTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
