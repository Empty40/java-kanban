package managers;

import exceptions.ManagerSaveException;

import interfaces.HistoryManager;

import models.*;

import java.io.*;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static File file;

    private static final String title = "id,type,name,status,description,epic";

    public FileBackedTasksManager(File files) {
        file = files;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        fileBackedTasksManager.fromString(file);


        return fileBackedTasksManager;
    }

    private void save() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(title + "\n");

            for (Integer t : tasks.keySet()) {
                fileWriter.write(toString(tasks.get(t)) + "\n");
            }

            for (Integer t : epicList.keySet()) {
                fileWriter.write(toString(epicList.get(t)) + "\n");
            }

            for (Integer t : subtaskList.keySet()) {
                fileWriter.write(toString(subtaskList.get(t)) + "\n");
            }

            fileWriter.write("\n" + historyToString(historyManager));

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл:" + file, e);
        }
    }

    static String historyToString(HistoryManager manager) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Task taskData : manager.getHistory()) {
            int taskId = taskData.getTaskId();
            String idToString = String.valueOf(taskId);
            stringBuilder.append(idToString).append(",");
        }
        if (stringBuilder.length() != 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> idList = new ArrayList<>();
        String[] idTask = value.split(",");
        for (String id : idTask) {
            idList.add(Integer.valueOf(id));
        }
        return idList;
    }

    private void addTaskInHistoryManager(List<Integer> arraysTask) {
        for (Integer taskId : arraysTask) {
            if (tasks.containsKey(taskId)) {
                historyManager.add(tasks.get(taskId));
            } else if (epicList.containsKey(taskId)) {
                historyManager.add(epicList.get(taskId));
            } else {
                historyManager.add(subtaskList.get(taskId));
            }
        }
    }

    public void fromString(File file) {
        //Спасибо за объяснение как корректно парсить данные и вынести обработку строк
        try {
            FileReader fl = new FileReader(file);
            BufferedReader br = new BufferedReader(fl);
            while (br.ready()) {
                String line = br.readLine();
                if (title.equals(line)) {
                    continue;
                }
                if (line.isEmpty()) {
                    String historyLine = br.readLine();
                    List<Integer> history = historyFromString(historyLine);
                    addTaskInHistoryManager(history);
                    save();
                    // метод save() для сохранения истории просмотров при перезапуске программы сразу после запуска
                    break;
                }
                String[] lines = line.split(",");
                int id = Integer.parseInt(lines[0]);
                TaskType type = TaskType.valueOf(lines[1]);
                String name = lines[2];
                TaskStatus status = TaskStatus.valueOf(lines[3]);
                String description = lines[4];
                int idEpic = 0;
                if (lines.length > 5) {
                    idEpic = Integer.parseInt(lines[5]);
                }
                switch (type) {
                    case TASK:
                        Task task = new Task(name, description, status);
                        task.setTaskId(id);
                        newTask(task);
                        break;
                    case EPIC:
                        Epic epic = new Epic(name, description, status);
                        epic.setTaskId(id);
                        newEpic(epic);
                        break;
                    case SUBTASK:
                        Subtask subtask = new Subtask(name, description, idEpic,
                                status);
                        subtask.setTaskId(id);
                        newSubtask(subtask);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString(Task task) {
        return task.getTaskId() + "," + task.getType() + "," + task.getTaskName() + "," +
                task.getTaskStatus() + "," + task.getTaskDescription() + "," + task.getSubtaskEpicId();
    }

    public void newTask(Task task) { //    Создание. Сам объект должен передаваться в качестве параметра.
        super.newTask(task);
        System.out.println(toString(task));
        save();
    }

    public void newEpic(Epic epic) { //    Создание. Сам объект должен передаваться в качестве параметра.
        super.newEpic(epic);
        System.out.println(toString(epic));
        save();
    }

    public void newSubtask(Subtask subtask) { //    Создание. Сам объект должен передаваться в качестве параметра.
        super.newSubtask(subtask);
        System.out.println(toString(subtask));
        save();
    }

    public void updateTask(Task task) { //    Обновление. Новая версия объекта с верным идентификатором
        super.updateTask(task);
        save();
    }

    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Task showTaskById(int id) { //    Получение по идентификатору.
        Task task = super.showTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic showEpicById(int id) { //    Получение по идентификатору.
        Epic epic = super.showEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask showSubtaskById(int id) { //    Получение по идентификатору.
        Subtask subtask = super.showSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void deleteAllTask() { //    Удаление всех задач.
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpic() { //    Удаление всех Эпиков.
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubtask() { //    Удаление всех Подзадач.
        super.deleteAllSubtask();
        save();
    }

    @Override
    public void deleteTaskId(int id) {
        super.deleteTaskId(id);
        save();
    }

    @Override
    public void deleteEpicId(int id) {
        super.deleteEpicId(id);
        save();
    }

    @Override
    public void deleteSubtaskId(int id) {
        super.deleteSubtaskId(id);
        save();
    }
}
