import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Manager {
    Scanner scan = new Scanner(System.in);
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epicList = new HashMap<>();
    HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    int taskId = 1;


    // Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.

    //    Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    public String showAllTask() { //    Получение списка всех задач.
        ArrayList<Task> taskPrint = new ArrayList<>();
        for (Task taskValue : tasks.values()) {
            taskPrint.add(taskValue);
        } System.out.println(taskPrint);
          return taskPrint.toString();
    }

    public String showAllEpic() { //    Получение списка всех задач.
        ArrayList<Task> epicPrint = new ArrayList<>();
        for (Epic epicValue : epicList.values()) {
            epicPrint.add(epicValue);
        } System.out.println(epicPrint);
          return epicPrint.toString();
    }

    public String showAllSubtask() { //    Получение списка всех задач.
        ArrayList<Task> subtaskPrint = new ArrayList<>();
        for (Subtask subtaskValue : subtaskList.values()) {
            subtaskPrint.add(subtaskValue);
        } System.out.println(subtaskPrint);
          return subtaskPrint.toString();
    }

    public void deleteAllTask() { //    Удаление всех задач.
        tasks.clear();
    }

    public void deleteAllEpic() { //    Удаление всех Эпиков.
        epicList.clear();
        deleteAllSubtask();
    }

    public void deleteAllSubtask() { //    Удаление всех Подзадач.
        for (Subtask index : subtaskList.values()) {
            int epicIndex = index.subtaskEpicId;
            for (int i = 0; i < epicList.size(); i++) {
                Epic subtaskInEpicForDelete = epicList.get(epicIndex);
                subtaskInEpicForDelete.subtaskId.clear();
            }
        }
        subtaskList.clear();
        epicStatus();
    }

    public Task showTaskId(int id) { //    Получение по идентификатору.
        return tasks.get(id);
    }

    public Epic showEpicId(int id) { //    Получение по идентификатору.
        return epicList.get(id);
    }

    public Subtask showSubtaskId(int id) { //    Получение по идентификатору.
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
    }

    public void updateTask(Task task) { //    Обновление. Новая версия объекта с верным идентификатором
        int taskIndex = task.getTaskId();
        tasks.put(taskIndex, task); //    передаётся в виде параметра.
    }

    public void updateEpic(Epic epic) {
        int epicIndex = epic.getTaskId();
        epicList.put(epicIndex, epic);
    }

    public void updateSubtask(Subtask subtask) {

        int subtaskId = subtask.getTaskId();
        this.subtaskList.put(subtaskId, subtask);
        epicStatus();
    }

    public void deleteTaskId(int id) { //    Удаление по идентификатору.
        tasks.remove(id);
    }

    public void deleteEpicId(int id) { //    Удаление по идентификатору.
        Epic epicForDelete = epicList.get(id);
        for (int i = 0; i < epicForDelete.subtaskId.size(); i++) {
            Integer subtaskForDelete = epicForDelete.subtaskId.get(i);
            subtaskList.remove(subtaskForDelete);
        }
        epicList.remove(id);

    }

    public void deleteSubtaskId(int id) { //    Удаление по идентификатору.
        subtaskList.remove(id);
        epicStatusRemove(id);

    }

    //    Дополнительные методы:
    public ArrayList<Subtask> showAllSubtaskInEpic(String epicName) { //    Получение списка всех подзадач определённого эпика.
        ArrayList<Subtask> subtaskInEpic = new ArrayList<>();
        for (Epic testing : epicList.values()) {
            if (Objects.equals(testing.taskName, epicName)) {
                for (int i = 0; i < testing.subtaskId.size(); i++) {
                    int index = testing.subtaskId.get(i);
                    subtaskInEpic.add(subtaskList.get(index));
                }
            }

        } System.out.println(subtaskInEpic);
        return subtaskInEpic;
    }

        public void epicStatusRemove(int id) {
            for (Epic ifSubtaskInEpic : epicList.values()) {
                if (ifSubtaskInEpic.subtaskId.contains(id)) {

                for (int i = 0; i < ifSubtaskInEpic.subtaskId.size(); i++) {
                    int subtaskIndex = ifSubtaskInEpic.subtaskId.get(i);
                    if (subtaskIndex == id) {
                        ifSubtaskInEpic.subtaskId.remove(i);
                    }
                }
                } else {
                    System.out.println("Такой подзадачи нет");
                }
            } epicStatus();
        }

    public void epicStatus() {
        int doneCount = 0;
        int newCount = 0;

        for (Epic ifSubtaskInEpic : epicList.values()) {

                int epicId = ifSubtaskInEpic.getTaskId();
                Epic valueEpic = epicList.get(epicId);

            if (ifSubtaskInEpic.subtaskId.size() == 0) {
                ifSubtaskInEpic.taskStatus = "NEW";
                break;
            }

                for (int i = 0; i < valueEpic.subtaskId.size(); i++) {

                    int subtaskStatus = valueEpic.subtaskId.get(i);

                    if (ifSubtaskInEpic.subtaskId.size() == 0) {
                        ifSubtaskInEpic.taskStatus = "NEW";
                        break;
                    } else if (Objects.equals(subtaskList.get(subtaskStatus).taskStatus, "DONE")) {
                        doneCount++;
                        if (Objects.equals(doneCount, valueEpic.subtaskId.size())) {
                            valueEpic.taskStatus = "DONE";
                        } else { valueEpic.taskStatus = "IN_PROGRESS"; }
                    } else if (Objects.equals(subtaskList.get(subtaskStatus).taskStatus, "NEW")) {
                        newCount++;
                        if (Objects.equals(newCount, valueEpic.subtaskId.size())) {
                            valueEpic.taskStatus = "NEW";
                        } else { valueEpic.taskStatus = "IN_PROGRESS"; }
                    } else {
                        valueEpic.taskStatus = "IN_PROGRESS";
                    }
                }
        }
    }
}

