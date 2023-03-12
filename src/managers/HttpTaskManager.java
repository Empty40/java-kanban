package managers;

import models.*;
import server.KVTaskClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private final String url = "http://localhost:8078";
    KVTaskClient kvTaskClient;

    public HttpTaskManager() {
    }

    public void start() {
        {
            try {
                kvTaskClient = new KVTaskClient(url);
            } catch (IOException | InterruptedException e) {
                System.out.println("Не удалось создать Http-клиент");
            }
        }
    }

    public void saveToServer() {
        StringBuilder taskStringBuilder = new StringBuilder();
        int taskNullCount = 0;
        for (Integer t : tasks.keySet()) {
            if (t != null) {
                taskStringBuilder.append(toString(tasks.get(t))).append("\n");
            } else {
                taskNullCount++;
            }
        }
        if (taskNullCount == tasks.size()) {
            taskStringBuilder.append("-");
        }
        System.out.println(taskStringBuilder);
        kvTaskClient.put("tasks", String.valueOf(taskStringBuilder));
        System.out.println(taskStringBuilder);

        StringBuilder epicStringBuilder = new StringBuilder();
        int epicNullCount = 0;
        for (Integer t : epicList.keySet()) {
            if (t != null) {
                epicStringBuilder.append(toString(epicList.get(t))).append("\n");
            } else {
                epicNullCount++;
            }
        }
        if (epicNullCount == epicList.size()) {
            epicStringBuilder.append("-");
        }
        kvTaskClient.put("epics", String.valueOf(epicStringBuilder));
        System.out.println(epicStringBuilder);

        StringBuilder SubtaskStringBuilder = new StringBuilder();
        int subtaskNullCount = 0;
        for (Integer t : subtaskList.keySet()) {
            if (t != null) {
                SubtaskStringBuilder.append(toString(subtaskList.get(t))).append("\n");
            } else {
                subtaskNullCount++;
            }
        }
        if (subtaskNullCount == subtaskList.size()) {
            SubtaskStringBuilder.append("-");
        }
        kvTaskClient.put("subtasks", String.valueOf(SubtaskStringBuilder));
        System.out.println(SubtaskStringBuilder);

        StringBuilder historyStringBuilder = new StringBuilder();
        for (Task taskData : historyManager.getHistory()) {
            int taskId = taskData.getTaskId();
            String idToString = String.valueOf(taskId);
            historyStringBuilder.append(idToString).append(",");
        }
        if (historyStringBuilder.length() != 0) {
            historyStringBuilder.setLength(historyStringBuilder.length() - 1);
        } else {
            historyStringBuilder.append("-");
        }
        kvTaskClient.put("history", historyStringBuilder.toString());
        System.out.println(historyStringBuilder);
    }

    public void loadFromServer() {
        String taskFromServer = kvTaskClient.load("tasks");
        System.out.println(taskFromServer);
        fromString(taskFromServer);

        String epicFromServer = kvTaskClient.load("epics");
        System.out.println(epicFromServer);
        fromString(epicFromServer);

        String subtaskFromServer = kvTaskClient.load("subtasks");
        System.out.println(epicFromServer);
        fromString(subtaskFromServer);

        String historyFromServer = kvTaskClient.load("history");
        System.out.println(historyFromServer);
        List<Integer> history = historyFromString(historyFromServer);
        addTaskInHistoryManager(history);
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> idList = new ArrayList<>();
        if (value != null & !value.equals("")) {
            String[] idTask = value.split(",");
            for (String id : idTask) {
                if (id.equals("-")) {
                    continue;
                } else {
                    idList.add(Integer.valueOf(id));
                }
            }
        } else {
            return idList;
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

    public void fromString(String lineFromString) {
        List<Task> taskArrayList = new ArrayList<>();
        List<Epic> epicArrayList = new ArrayList<>();
        List<Subtask> subtaskArrayList = new ArrayList<>();

        String[] line = lineFromString.split("\n");
        for (String s : line) {
            if (s.isEmpty()) {
                continue;
            }
            if (s.equals("-")) {
                continue;
            }
            String[] lines = s.split(",");
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
        for (Task taskToAdd : taskArrayList) {
            if (tasks.containsKey(taskToAdd.getTaskId())) {
                tasks.put(taskToAdd.getTaskId(), taskToAdd);
            } else {
                newTask(taskToAdd);
            }
        }
        for (Epic epicToAdd : epicArrayList) {
            if (epicList.containsKey(epicToAdd.getTaskId())) {
                epicList.put(epicToAdd.getTaskId(), epicToAdd);
            } else {
                newEpic(epicToAdd);
            }
        }
        for (Subtask subtaskToAdd : subtaskArrayList) {
            if (subtaskList.containsKey(subtaskToAdd.getTaskId())) {
                subtaskList.put(subtaskToAdd.getTaskId(), subtaskToAdd);
            } else {
                newSubtask(subtaskToAdd);
            }
        }
    }

    public void newTask(Task task) {
        super.newTask(task);
        saveToServer();
    }

    public void newEpic(Epic epic) {
        super.newEpic(epic);
        saveToServer();
    }

    public void newSubtask(Subtask subtask) {
        super.newSubtask(subtask);
        saveToServer();
    }

    public void updateTask(Task task) {
        super.updateTask(task);
        saveToServer();
    }

    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        saveToServer();
    }

    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        saveToServer();
    }

    @Override
    public Task showTaskById(int id) {
        Task task = super.showTaskById(id);
        saveToServer();
        return task;
    }

    @Override
    public Epic showEpicById(int id) {
        Epic epic = super.showEpicById(id);
        saveToServer();
        return epic;
    }

    @Override
    public Subtask showSubtaskById(int id) {
        Subtask subtask = super.showSubtaskById(id);
        saveToServer();
        return subtask;
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        saveToServer();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        saveToServer();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        saveToServer();
    }

    @Override
    public void deleteTaskId(int id) {
        super.deleteTaskId(id);
        saveToServer();
    }

    @Override
    public void deleteEpicId(int id) {
        super.deleteEpicId(id);
        saveToServer();
    }

    @Override
    public void deleteSubtaskId(int id) {
        super.deleteSubtaskId(id);
        saveToServer();
    }

    @Override
    public ArrayList<Task> showAllTask() {
        saveToServer();
        return super.showAllTask();
    }

    @Override
    public ArrayList<Epic> showAllEpic() {
        saveToServer();
        return super.showAllEpic();
    }

    @Override
    public ArrayList<Subtask> showAllSubtask() {
        saveToServer();
        return super.showAllSubtask();
    }
}
