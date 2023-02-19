package managers;

import exceptions.ManagerSaveException;

import interfaces.HistoryManager;

import models.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static File file;

    private static final String title = "id,type,name,status,description,epic" + "\n";

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
        StringBuilder stringBuilder = new StringBuilder();
        for (Task taskData : manager.getHistory()) {
            int taskId = taskData.getTaskId();
            String idToString = String.valueOf(taskId);
            stringBuilder.append(idToString).append(",");
        }
        //Данное условие сделано для того, чтобы при записи в файл у нас не получилась ошибка StringIndexOutOfBoundsException
        if (stringBuilder.length() != 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);//а с помощью этого, я избавляюсь от запятой в конце
            //айдишников, когда записываю их в файл.
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
        String path = String.valueOf(file);
        try {
            String content = Files.readString(Path.of(path));
            FileReader fl = new FileReader(file);
            BufferedReader br = new BufferedReader(fl);
            while (br.readLine() != null) {
                String[] lineContent = content.split("\n");
                String last = null;
                String lines;
                while (null != (lines = br.readLine())) {
                    last = lines;
                }
                for (String tasks : lineContent) {
                    String[] line = tasks.split(",");
                    if (line[0].isEmpty() || line[0].equals("id")) {
                        continue;
                    }
                    int id = 0;
                    String name = "";
                    TaskStatus status = null;
                    String description = "";
                    int idEpic = 0;
                    if (line[1].equals("TASK") || line[1].equals("EPIC") || line[1].equals("SUBTASK")) {
                        id = Integer.parseInt(line[0]);
                        name = line[2];
                        status = TaskStatus.valueOf(line[3]);
                        description = line[4];
                        if (line.length > 5) {
                            idEpic = Integer.parseInt(line[5]);
                        }
                    }
                    //При парсинге последней строки вылетает ошибка, т.к там только айдишники задач, не могу придумать как мне
                    //вынести парсинг последней строки. В связи с этим делаю так, как пока могу.
                    switch (line[1]) {
                        case "TASK":
                            Task task = new Task(name, description, status);
                            task.setTaskId(id);
                            newTask(task);
                            break;
                        case "EPIC":
                            Epic epic = new Epic(name, description, status);
                            epic.setTaskId(id);
                            newEpic(epic);
                            break;
                        case "SUBTASK":
                            Subtask subtask = new Subtask(name, description, idEpic,
                                    status);
                            subtask.setTaskId(id);
                            newSubtask(subtask);
                            break;
                        default:
                            if (last != null) {
                                List<Integer> lastLine = historyFromString(last);
                                addTaskInHistoryManager(lastLine);
                                save();
                                //Если после прочтения файла мы сразу перезапустим программу, то у нас не сохранится история задач, и айдишники
                                //в файле не останутся, именно за этим тут метод save()
                            }
                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл:" + file, e);
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
