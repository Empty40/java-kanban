package managers;

import exceptions.ManagerSaveException;

import interfaces.HistoryManager;

import models.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static File file;

    private static final String title = "id,type,name,status,description,epic,duration,startTime,endTime";

    public FileBackedTasksManager(File files) {
        file = files;
    }

    public FileBackedTasksManager() {

    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        if (file.canRead()) {
            fileBackedTasksManager.fromString(file);
        }
        return fileBackedTasksManager;
    } //+

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
    } //+

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
    } //+

    static List<Integer> historyFromString(String value) {
        List<Integer> idList = new ArrayList<>();
        if (value != null) {
            String[] idTask = value.split(",");
            for (String id : idTask) {
                idList.add(Integer.valueOf(id));
            }
        } else {
            return idList;
        }
        return idList;

    } //+

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
    } //+

    public void fromString(File file) {
        List<Task> taskArrayList = new ArrayList<>();
        List<Epic> epicArrayList = new ArrayList<>();
        List<Subtask> subtaskArrayList = new ArrayList<>();
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
                    break;
                }
                String[] lines = line.split(",");
                int id = Integer.parseInt(lines[0]);
                TaskType type = TaskType.valueOf(lines[1]);
                String name = lines[2];
                TaskStatus status = TaskStatus.valueOf(lines[3]);
                String description = lines[4];
                int idEpic;
                int duration = 0;
                LocalDateTime startTime = null;
                if (lines.length > 6) {
                    duration = Integer.parseInt(lines[6]);
                }
                if (lines.length > 8) {
                    startTime = LocalDateTime.parse(lines[7]);
                }
                switch (type) {
                    case TASK:
                        Task task = new Task(name, description, status);
                        task.setTaskId(id);
                        taskArrayList.add(task);
                        task.setDuration(duration);
                        if (startTime != null) {
                            task.setStartTime(startTime);
                        }
                        break;
                    case EPIC:
                        Epic epic = new Epic(name, description, status);
                        epic.setTaskId(id);
                        epicArrayList.add(epic);
                        break;
                    case SUBTASK:
                        idEpic = Integer.parseInt(lines[5]);
                        Subtask subtask = new Subtask(name, description, idEpic,
                                status);
                        subtask.setTaskId(id);
                        subtaskArrayList.add(subtask);
                        subtask.setDuration(duration);
                        if (startTime != null) {
                            subtask.setStartTime(startTime);
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла" + file, e);
        }
        for (Task taskToAdd : taskArrayList) {
            newTask(taskToAdd);
        }
        for (Epic epicToAdd : epicArrayList) {
            newEpic(epicToAdd);
        }
        for (Subtask subtaskToAdd : subtaskArrayList) {
            newSubtask(subtaskToAdd);
        }
    } //+

    public String toString(Task task) {
        LocalDateTime taskStartTime = task.getStartTime();
        if (taskStartTime != null) {
            return task.getTaskId() + "," + task.getType() + "," + task.getTaskName() + "," +
                    task.getTaskStatus() + "," + task.getTaskDescription() + "," + task.getSubtaskEpicId() + ","
                    + task.getDuration() + "," + task.getStartTime() + "," + task.getEndTime();
        } else {
            return task.getTaskId() + "," + task.getType() + "," + task.getTaskName() + "," +
                    task.getTaskStatus() + "," + task.getTaskDescription() + "," + task.getSubtaskEpicId() + ","
                    + task.getDuration();
        }
    } //+

    public void newTask(Task task) {
        super.newTask(task);
        save();
    }

    public void newEpic(Epic epic) {
        super.newEpic(epic);
        save();
    }

    public void newSubtask(Subtask subtask) {
        super.newSubtask(subtask);
        save();
    }

    public void updateTask(Task task) {
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
    public Task showTaskById(int id) {
        Task task = super.showTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic showEpicById(int id) {
        Epic epic = super.showEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask showSubtaskById(int id) {
        Subtask subtask = super.showSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubtask() {
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
