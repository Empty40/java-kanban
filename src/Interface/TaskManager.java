package Interface;

import models.Epic;
import models.Subtask;
import models.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    int taskId = 1;

    HashMap<Integer, Task> tasks = new HashMap<>();

    HashMap<Integer, Epic> epicList = new HashMap<>();

    HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpicList();

    HashMap<Integer, Subtask> getSubtaskList();

    int getTaskId();

    // Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    ArrayList<Task> showAllTask();


    ArrayList<Epic> showAllEpic();

    ArrayList<Subtask> showAllSubtask();

    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubtask();

    Task showTaskById(int id);

    Epic showEpicById(int id);

    Subtask showSubtaskById(int id);

    void newTask(Task task);

    void newEpic(Epic epic);

    void newSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTaskId(int id);

    void deleteEpicId(int id);

    void deleteSubtaskId(int id);

    // Дополнительные методы:
    ArrayList<Subtask> showAllSubtaskInEpic(int epicId);

    void epicStatusRemove(int id);

    void epicStatus();

    void getHistory();
}

