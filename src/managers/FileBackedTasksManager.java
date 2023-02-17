package managers;

import Exceprions.ManagerSaveException;

import interfaces.HistoryManager;
import interfaces.TaskManager;

import models.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private static File file;

    String title = "id,type,name,status,description,epic" + "\n";

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
            fileWriter.write(title);

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
        String historyIdToString = "";
        for (Task i : manager.getHistory()) {
            int q = i.getTaskId();
            String a = String.valueOf(q);
            historyIdToString = historyIdToString + a + ",";
        }
        if (historyIdToString != null && historyIdToString.length() != 0) {
            historyIdToString = historyIdToString.substring(0, historyIdToString.length() - 1);
        }
        return historyIdToString;
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> idList = new ArrayList<>();
        String[] idTask = value.split(",");
        for (String s : idTask) {
            idList.add(Integer.valueOf(s));
        }
        return idList;
    }

    private void addTaskInHistoryManager(List<Integer> arraysTask) {
        for (Integer i : arraysTask) {
            if (tasks.containsKey(i)) {
                super.historyManager.add(tasks.get(i));
            } else if (epicList.containsKey(i)) {
                super.historyManager.add(epicList.get(i));
            } else {
                super.historyManager.add(subtaskList.get(i));
            }
        }
    }

    public void fromString(File file) {
        String path = String.valueOf(file);
        try {
            String content = Files.readString(Path.of(path));
            FileReader fl = new FileReader(file);
            BufferedReader br = new BufferedReader(fl);
            while (br.readLine() != null) {
//                String test = br.readLine();
                String[] qwe = content.split("\n");
                String last = null, lines;
                while (null != (lines = br.readLine())) {
                    last = lines;
                }
                for (String tasks : qwe) {
                    String[] line = tasks.split(",");
                    if (line[0].isEmpty()) {
                        continue;
                    }
                    switch (line[1]) {
                        case "TASK":
                            Task task = new Task(line[2], line[4], TaskStatusAndType.valueOf(line[3]));
                            task.setTaskId(Integer.parseInt(line[0]));
                            super.newTask(task);
                            break;
                        case "EPIC":
                            Epic epic = new Epic(line[2], line[4], TaskStatusAndType.valueOf(line[3]));
                            epic.setTaskId(Integer.parseInt(line[0]));
                            super.newEpic(epic);
                            break;
                        case "SUBTASK":
                            Subtask subtask = new Subtask(line[2], line[4], Integer.parseInt(line[5]),
                                    TaskStatusAndType.valueOf(line[3]));
                            subtask.setTaskId(Integer.parseInt(line[0]));
                            super.newSubtask(subtask);
                            break;
                        case "type":
                            continue;
                        default:
                            if (last != null) {
                                List<Integer> past = historyFromString(last);
                                addTaskInHistoryManager(past);
                            } else {
                                throw new NullPointerException();
                            }
                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString(Task task) {
        String id = Integer.toString(task.getTaskId());
        String type = String.valueOf(TaskStatusAndType.TASK);
        String name = task.getTaskName();
        String status = String.valueOf(task.getTaskStatus());
        String description = task.getTaskDescription();
        return id + "," + type + "," + name + "," + status + "," + description;
    }

    public String toString(Epic task) {
        String id = Integer.toString(task.getTaskId());
        String type = String.valueOf(TaskStatusAndType.EPIC);
        String name = task.getTaskName();
        String status = String.valueOf(task.getTaskStatus());
        String description = task.getTaskDescription();
        return id + "," + type + "," + name + "," + status + "," + description;
    }

    public String toString(Subtask task) {
        String id = Integer.toString(task.getTaskId());
        String type = String.valueOf(TaskStatusAndType.SUBTASK);
        String name = task.getTaskName();
        String status = String.valueOf(task.getTaskStatus());
        String description = task.getTaskDescription();
        String byEpic = String.valueOf(task.getSubtaskEpicId());
        return id + "," + type + "," + name + "," + status + "," + description + "," + byEpic;
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
        super.showTaskById(id);
        save();
        return super.showTaskById(id);
    }

    @Override
    public Epic showEpicById(int id) { //    Получение по идентификатору.
        super.showEpicById(id);
        save();
        return super.showEpicById(id);
    }

    @Override
    public Subtask showSubtaskById(int id) { //    Получение по идентификатору.
        super.showSubtaskById(id);
        save();
        return super.showSubtaskById(id);
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
