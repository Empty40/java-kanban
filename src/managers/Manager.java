package managers;

import models.Epic;
import models.Subtask;
import models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Manager {

    private int taskId = 1;

    protected HashMap<Integer, Task> tasks = new HashMap<>();

    protected HashMap<Integer, Epic> epicList = new HashMap<>();

    protected HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpicList() {
        return epicList;
    }

    public HashMap<Integer, Subtask> getSubtaskList() {
        return subtaskList;
    }

    public int getTaskId() {
        return taskId;
    }

    // Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    public ArrayList<Task> showAllTask() { //    Получение списка всех задач.
        ArrayList<Task> taskPrint = new ArrayList<>(); // Понял свою ошибку, возвращал не сам список, а toString()
        for (Task taskValue : tasks.values()) { // теперь, надеюсь, сделал верный возврат списка
            taskPrint.add(taskValue);
        } return taskPrint;
    }

    public ArrayList<Epic> showAllEpic() { //    Получение списка всех задач.
        ArrayList<Epic> epicPrint = new ArrayList<>();
        for (Epic epicValue : epicList.values()) {
            epicPrint.add(epicValue);
        } return epicPrint;
    }

    public ArrayList<Subtask> showAllSubtask() { //    Получение списка всех задач.
        ArrayList<Subtask> subtaskPrint = new ArrayList<>();
        for (Subtask subtaskValue : subtaskList.values()) {
            subtaskPrint.add(subtaskValue);
        } return subtaskPrint;
    }

    public void deleteAllTask() { //    Удаление всех задач.
        tasks.clear();
    }

    public void deleteAllEpic() { //    Удаление всех Эпиков.
        epicList.clear(); // Тут тоже понял, мы же удаляем список эпиков и вместе с этим удаляются и список айдишников
        subtaskList.clear(); // подзадач
    }

    public void deleteAllSubtask() { //    Удаление всех Подзадач.
        for (Subtask index : subtaskList.values()) {
            int epicIndex = index.getSubtaskEpicId();
            for (int i = 0; i < epicList.size(); i++) {
                Epic subtaskInEpicForDelete = epicList.get(epicIndex);
                subtaskInEpicForDelete.deleteListSubtaskId();
            }
        }
        subtaskList.clear();
        epicStatus();
    }

    public Task showTaskById(int id) { //    Получение по идентификатору.
        return tasks.get(id);
    }

    public Epic showEpicById(int id) { //    Получение по идентификатору.
        return epicList.get(id);
    }

    public Subtask showSubtaskById(int id) { //    Получение по идентификатору.
        return subtaskList.get(id);
    }

    public void newTask(Task task) { //    Создание. Сам объект должен передаваться в качестве параметра.
        task.setTaskId(taskId);
        tasks.put(taskId, task);
        System.out.println("Задаче присвоен идентификатор " + taskId);
        taskId++;
    }

    public void newEpic(Epic epic) { //    Создание. Сам объект должен передаваться в качестве параметра.
        epic.setTaskId(taskId);
        epicList.put(taskId, epic);
        System.out.println(epic.getTaskId());
        System.out.println("Задаче присвоен идентификатор " + taskId);
        taskId++;
    }

    public void newSubtask(Subtask subtask) { //    Создание. Сам объект должен передаваться в качестве параметра.
        if (!epicList.containsKey(subtask.getSubtaskEpicId())) {
            System.out.println("Такого Эпика в списке нет!");
        } else {
            subtask.setTaskId(taskId);
            subtaskList.put(taskId, subtask);
            epicStatus();
            System.out.println("Задаче присвоен идентификатор " + taskId);
            taskId++;
        }
    }

    public void updateTask(Task task) { //    Обновление. Новая версия объекта с верным идентификатором
        if (!tasks.containsKey(task.getTaskId())) {
            System.out.println("Такой задачи нет в списке");
        } else {
            int taskIndex = task.getTaskId();
            tasks.put(taskIndex, task);
        }
    }

    public void updateEpic(Epic epic) {
        if (!epicList.containsKey(epic.getTaskId())) {
            System.out.println("Такой задачи нет в списке");
        } else {
            int epicIndex = epic.getTaskId();
            epicList.put(epicIndex, epic);
        }
    }

    // Создал проверку по айди, который мы вкладываем в сабтаск при создании задачи, но если честно опять не понимаю из-
    // за чего у нас айдишник эпика поменяется, т.к нам не доступно изменять айдишник задач, вообще, а при обновлении
    // задач у нас подаётся эпик с "верным" идентификатором
    public void updateSubtask(Subtask subtask) {
        if (!subtaskList.containsKey(subtask.getTaskId())) {
            System.out.println("Такой подзадачи нет в списке");
        } else {
            if (epicList.containsKey(subtask.getSubtaskEpicId())) {
                int subtaskId = subtask.getTaskId();
                subtaskList.put(subtaskId, subtask);
            } else {
                System.out.println("Такой задачи нет в списке");
            }
        }
        epicStatus();
    }

    // Удаление по идентификатору.
    public void deleteTaskId(int id) {
        tasks.remove(id);
    }

    public void deleteEpicId(int id) {
        Epic epicForDelete = epicList.get(id);
        ArrayList<Integer> subtaskIdSize = epicForDelete.getSubtaskId();
        for (int i = 0; i < subtaskIdSize.size(); i++) {
            Integer subtaskForDelete = subtaskIdSize.get(i);
            subtaskList.remove(subtaskForDelete);
        }
        epicList.remove(id);

    }

    public void deleteSubtaskId(int id) {
        subtaskList.remove(id);
        epicStatusRemove(id);

    }

    // Дополнительные методы:
    public ArrayList<Subtask> showAllSubtaskInEpic(int epicId) { // Получение списка всех подзадач
        Epic listOfEpic = epicList.get(epicId);
        ArrayList<Subtask> subtaskInEpic = null;
        if (listOfEpic != null) {
            subtaskInEpic = new ArrayList<>();
            ArrayList<Integer> index = listOfEpic.getSubtaskId();
            for (int i = 0; i < index.size(); i++) {
                Integer indexSubtask = index.get(i);
                subtaskInEpic.add(subtaskList.get(indexSubtask));
            }
        } else {
            System.out.println("Данного айди нет в списке");
        }
        return subtaskInEpic;
    }

    public void epicStatusRemove(int id) {
            for (Epic ifSubtaskInEpic : epicList.values()) {
                ArrayList<Integer> subtaskIdSize = ifSubtaskInEpic.getSubtaskId();
                if (subtaskIdSize.contains(id)) {

                for (int i = 0; i < subtaskIdSize.size(); i++) {
                    int subtaskIndex = subtaskIdSize.get(i);
                    if (subtaskIndex == id) {
                        ifSubtaskInEpic.deleteSubtaskId(i);
                    }
                }
                } else {
                    System.out.println("Такой подзадачи нет");
                }
            }
            epicStatus();
        }

    public void epicStatus() {


        for (Epic ifSubtaskInEpic : epicList.values()) {
            int doneCount = 0;
            int newCount = 0;

                int epicId = ifSubtaskInEpic.getTaskId();
                Epic valueEpic = epicList.get(epicId);

                ArrayList<Integer> subtaskIdSize = ifSubtaskInEpic.getSubtaskId();
            if (subtaskIdSize.size() == 0) {
                ifSubtaskInEpic.setTaskStatus("NEW");
                continue;
            }

                for (int i = 0; i < subtaskIdSize.size(); i++) {

                    int subtaskStatus = subtaskIdSize.get(i);

                    if (subtaskIdSize.size() == 0) {
                        ifSubtaskInEpic.setTaskStatus("NEW");
                        break;
                    } else if (Objects.equals(subtaskList.get(subtaskStatus).getTaskStatus(), "DONE")) {
                        doneCount++;
                        if (Objects.equals(doneCount, subtaskIdSize.size())) {
                            valueEpic.setTaskStatus("DONE");
                        } else { valueEpic.setTaskStatus("IN_PROGRESS"); }
                    } else if (Objects.equals(subtaskList.get(subtaskStatus).getTaskStatus(), "NEW")) {
                        newCount++;
                        if (Objects.equals(newCount, subtaskIdSize.size())) {
                            valueEpic.setTaskStatus("NEW");
                        } else { valueEpic.setTaskStatus("IN_PROGRESS"); }
                    } else {
                        valueEpic.setTaskStatus("IN_PROGRESS");
                    }
                }
        }
    }
}

