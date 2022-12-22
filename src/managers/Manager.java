package managers;

import models.Epic;
import models.Subtask;
import models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Manager {
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

    private int taskId = 1;

    public int getTaskId() {
        return taskId;
    }
// Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.

    //    Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    public String showAllTask() { //    Получение списка всех задач.
        ArrayList<Task> taskPrint = new ArrayList<>();
        for (Task taskValue : tasks.values()) {
            taskPrint.add(taskValue);
        } return taskPrint.toString();
    }

    public String showAllEpic() { //    Получение списка всех задач.
        ArrayList<Task> epicPrint = new ArrayList<>();
        for (Epic epicValue : epicList.values()) {
            epicPrint.add(epicValue);
        } return epicPrint.toString();
    }

    public String showAllSubtask() { //    Получение списка всех задач.
        ArrayList<Task> subtaskPrint = new ArrayList<>();
        for (Subtask subtaskValue : subtaskList.values()) {
            subtaskPrint.add(subtaskValue);
        } return subtaskPrint.toString();
    }

    public void deleteAllTask() { //    Удаление всех задач.
        tasks.clear();
    }

    public void deleteAllEpic() { //    Удаление всех Эпиков.
        deleteAllSubtask(); // Я поменял местами методы, чтобы сначала выполнялась полная очистка подзадач и айдишников
        epicList.clear(); // подзадач в Эпике, а уже после происходила очистка Хешмапы эпиков
    }

    public void deleteAllSubtask() { //    Удаление всех Подзадач.
        for (Subtask index : subtaskList.values()) {
            int epicIndex = index.getSubtaskEpicId();
            for (int i = 0; i < epicList.size(); i++) {
                Epic subtaskInEpicForDelete = epicList.get(epicIndex);
                ArrayList<Integer> SubtaskIdSize = subtaskInEpicForDelete.getSubtaskId();
                SubtaskIdSize.clear();
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
        subtask.setTaskId(taskId);
        subtaskList.put(taskId, subtask);
        epicStatus();
        System.out.println("Задаче присвоен идентификатор " + taskId);
        taskId++;
    }

    public void updateTask(Task task) { //    Обновление. Новая версия объекта с верным идентификатором
        int taskIndex = task.getTaskId();
        tasks.put(taskIndex, task);

    }

    // Хочу уточнить, как мы сможем передать сюда новую задачу, если в main мы сначала запрашиваем идентификатор задачи?
    // Я может просто ошибаюсь, но тогда прошу, обьясните. Исходя из логики ТЗ:
    // "Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра."
    // Мы же объект передаем с "верным идентификатором" что подразумевает, что мы уже знаем что данный объект существует
    // в одном из списков задач и нам известен его идентификатор, так же буду ссылаться на ТЗ:
    // "При обновлении можете считать, что на вход подаётся новый объект, который должен полностью заменить старый."
    // "Если вы храните эпики в HashMap, где ключами являются идентификаторы,
    // то обновление — это запись нового эпика tasks.put(task.getId(), task))"
    // Тем самым получается, что мы не должны в уже существующих задачах вносить изменения а должны изменять существующ-
    // ие объекты путем полного их обновления новым объектом с "верным идентификатором"

    public void updateEpic(Epic epic) {
        int epicIndex = epic.getTaskId();
        epicList.put(epicIndex, epic);
    }

    public void updateSubtask(Subtask subtask) {

        int subtaskId = subtask.getTaskId();
        subtaskList.put(subtaskId, subtask);
        epicStatus();
    }

    public void deleteTaskId(int id) { //    Удаление по идентификатору.
        tasks.remove(id);
    }

    public void deleteEpicId(int id) { //    Удаление по идентификатору.
        Epic epicForDelete = epicList.get(id);
        ArrayList<Integer> subtaskIdSize = epicForDelete.getSubtaskId();
        for (int i = 0; i < subtaskIdSize.size(); i++) {
            Integer subtaskForDelete = subtaskIdSize.get(i);
            subtaskList.remove(subtaskForDelete);
        }
        epicList.remove(id);

    }

    public void deleteSubtaskId(int id) { //    Удаление по идентификатору.
        subtaskList.remove(id);
        epicStatusRemove(id); // В данном методе происходит удаление подзадачи из списка айдишников эпика

    }

    //    Дополнительные методы:
    public ArrayList<Subtask> showAllSubtaskInEpic(String epicName) { // Получение списка всех подзадач
        ArrayList<Subtask> subtaskInEpic = new ArrayList<>();         // определённого эпика.
        for (Epic testing : epicList.values()) {
            if (Objects.equals(testing.getTaskName(), epicName)) {
                ArrayList<Integer> subtaskIdSize = testing.getSubtaskId();
                for (int i = 0; i < subtaskIdSize.size(); i++) {
                    int index = subtaskIdSize.get(i);
                    subtaskInEpic.add(subtaskList.get(index));
                }
            }

        } System.out.println(subtaskInEpic);
        return subtaskInEpic;
    }

        public void epicStatusRemove(int id) {
            for (Epic ifSubtaskInEpic : epicList.values()) {
                ArrayList<Integer> subtaskIdSize = ifSubtaskInEpic.getSubtaskId();
                if (subtaskIdSize.contains(id)) {

                for (int i = 0; i < subtaskIdSize.size(); i++) {
                    int subtaskIndex = subtaskIdSize.get(i);
                    if (subtaskIndex == id) {
                        subtaskIdSize.remove(i);
                    }
                }
                } else {
                    System.out.println("Такой подзадачи нет");
                }
            } epicStatus();
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
                break;
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

