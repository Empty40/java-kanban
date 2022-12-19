import java.util.HashMap;
import java.util.Objects;

public class Manager {

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epic = new HashMap<>();
    HashMap<Integer, Subtask> subtask = new HashMap<>();

    int taskId;
    public Manager() {
    }

    // Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.

    //    Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    public void showAllTask() { //    Получение списка всех задач.
        for (int i = 1; i <= tasks.size(); i ++) {
            Task test = tasks.get(i);
            System.out.println(test.toString());
        }
    }

    public void showAllEpic() { //    Получение списка всех задач.
        for (int i = 1; i <= epic.size(); i ++) {
            Task test = epic.get(i);
            System.out.println(test.toString());
        }
    }

    public void showAllSubtask() { //    Получение списка всех задач.
        for (Subtask test : subtask.values()) {
            System.out.println(test.toString());
        }
    }

    public void deleteAllTask() { //    Удаление всех задач.
        tasks.clear();
    }

    public void deleteAllEpic() { //    Удаление всех задач.
        epic.clear();
    }

    public void deleteAllSubtask() { //    Удаление всех задач.
        subtask.clear();
        epicStatus(0);
    }

    public void showTaskId(int id) { //    Получение по идентификатору.
        System.out.println(tasks.get(id));
    }

    public void showEpicId(int id) { //    Получение по идентификатору.
        System.out.println(epic.get(id));
    }

    public void showSubtaskId(int id) { //    Получение по идентификатору.
        System.out.println(subtask.get(id));
    }

    public void newTask(Task task) { //    Создание. Сам объект должен передаваться в качестве параметра.
        tasks.put(taskId, task);
    }

    public void newEpic(Epic epic) { //    Создание. Сам объект должен передаваться в качестве параметра.
        this.epic.put(taskId, epic);
    }

    public void newSubtask(Subtask subtask) { //    Создание. Сам объект должен передаваться в качестве параметра.
        this.subtask.put(taskId, subtask);
        epicStatus(taskId);
    }

    public void updateTask(int id, Task task) { //    Обновление. Новая версия объекта с верным идентификатором
        tasks.put(id, task); //    передаётся в виде параметра.
    }

    public void updateEpic(int id, Epic epic) {
        this.epic.put(id, epic);

    }

    public void updateSubtask(int id, Subtask subtask) {
        this.subtask.put(id, subtask);
        epicStatus(id);
    }

    public void deleteTaskId(int id) { //    Удаление по идентификатору.
        tasks.remove(id);
    }

    public void deleteEpicId(int id) { //    Удаление по идентификатору.
        epic.remove(id);
    }

    public void deleteSubtaskId(int id) { //    Удаление по идентификатору.
        subtask.remove(id);
        epicStatusRemove(id);

    }

    //    Дополнительные методы:
    public void showAllSubtaskInEpic(String epicName) { //    Получение списка всех подзадач определённого эпика.
        for (Epic testing : epic.values()) {
            if (Objects.equals(testing.taskName, epicName)) {
                for (int i = 0; i < testing.subtaskId.size(); i++) {
                    int index = testing.subtaskId.get(i);
                    System.out.println(subtask.get(index));
                }
            }
        }
    }

        public void epicStatus(int id) {
            int doneCount = 0;

            for (Epic ifSubtaskInEpic : epic.values()) {
                if (ifSubtaskInEpic.subtaskId.contains(id)) {

                    int epicId = ifSubtaskInEpic.taskId;
                    Epic valueEpic = epic.get(epicId);

                    for (int i = 0; i < valueEpic.subtaskId.size(); i++) {

                        int subtaskStatus = valueEpic.subtaskId.get(i);

                        if (Objects.equals(subtask.get(subtaskStatus).taskStatus, "IN_PROGRESS")) {
                            valueEpic.taskStatus = "IN_PROGRESS";
                            break;
                        } else if (Objects.equals(subtask.get(subtaskStatus).taskStatus, "DONE")) {
                            doneCount++;
                            if (Objects.equals(doneCount, valueEpic.subtaskId.size())) {
                                valueEpic.taskStatus = "DONE";
                            }
                        } else {
                            continue;
                        }
                    }
                } else if (ifSubtaskInEpic.subtaskId.size() == 0) {
                    ifSubtaskInEpic.taskStatus = "NEW";
                } else {
                    break;
                }
            }
        }

    public void epicStatusRemove(int id) {
        int doneCount = 0;
        int newCount = 0;

        for (Epic ifSubtaskInEpic : epic.values()) {
            if (ifSubtaskInEpic.subtaskId.contains(id)) {

                for (int i = 0; i < ifSubtaskInEpic.subtaskId.size(); i++) {
                    int test = ifSubtaskInEpic.subtaskId.get(i);
                    if (test == id) {
                        ifSubtaskInEpic.subtaskId.remove(i);
                    } else {
                        continue;
                    }
                }

                int epicId = ifSubtaskInEpic.taskId;
                Epic valueEpic = epic.get(epicId);

                for (int i = 0; i < valueEpic.subtaskId.size(); i++) {

                    int subtaskStatus = valueEpic.subtaskId.get(i);

                    if (Objects.equals(subtask.get(subtaskStatus).taskStatus, "IN_PROGRESS")) {
                        valueEpic.taskStatus = "IN_PROGRESS";
                        break;
                    } else if (Objects.equals(subtask.get(subtaskStatus).taskStatus, "DONE")) {
                        doneCount++;
                        if (Objects.equals(doneCount, valueEpic.subtaskId.size())) {
                            valueEpic.taskStatus = "DONE";
                        }
                    } else if (Objects.equals(subtask.get(subtaskStatus).taskStatus, "NEW")) {
                        newCount++;
                        if (Objects.equals(newCount, valueEpic.subtaskId.size())) {
                            valueEpic.taskStatus = "NEW";
                        } else {
                            continue;
                        }
//
                    }
                }
            } else if (ifSubtaskInEpic.subtaskId.size() == 0) {
                ifSubtaskInEpic.taskStatus = "NEW";
            } else {
                break;
            }
        }
    }
}
