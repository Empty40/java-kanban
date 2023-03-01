package managers;

import models.*;

import java.time.LocalDateTime;
import java.util.*;

import interfaces.TaskManager;
import interfaces.HistoryManager;

public class InMemoryTaskManager implements TaskManager {

    Comparator<Task> comparator = (o1, o2) -> {
        if (o1.getStartTime() == null && o2.getStartTime() == null)
            return Integer.compare(o1.getTaskId(), o2.getTaskId());
        if (o1.getStartTime() == null && o2.getStartTime() != null)
            return 1;
        if (o1.getStartTime() != null && o2.getStartTime() == null)
            return -1;
        return o1.getStartTime().compareTo(o2.getStartTime());
    };

    protected HistoryManager historyManager = Managers.getDefaultHistory();

    protected HashMap<Integer, Task> tasks = new HashMap<>();

    protected HashMap<Integer, Epic> epicList = new HashMap<>();

    protected HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    TreeSet<Task> PrioritizedTasks = new TreeSet<>(comparator);

    public boolean chechTaskStartTime(Task task) {
        for (Task allTask : PrioritizedTasks) {
            if (allTask.getStartTime() != null && task.getStartTime() != null) {
                if (task.getStartTime().isAfter(allTask.getStartTime()) ||
                        task.getStartTime().isEqual(allTask.getStartTime())) {
                    if (task.getStartTime().isBefore(allTask.getEndTime())) {
                        System.out.println("Задача пересекаетcя по времени с задачей -> " + allTask + " Укажите другое " +
                                "время старта задачи.");
                        return false;
                    }
                }
                if (task.getEndTime().isAfter(allTask.getStartTime()) ||
                        task.getEndTime().isEqual(allTask.getStartTime())) {
                    if (task.getEndTime().isBefore(allTask.getEndTime())) {
                        System.out.println("Задача пересекаетcя по времени с задачей -> " + allTask + " Укажите другое " +
                                "время старта задачи.");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void treeSetRemoveTaskWithId(int id) {
        PrioritizedTasks.removeIf(allTask -> allTask.getTaskId() == id);
    }

    public void treeSetRemoveAllTask() {
        PrioritizedTasks.removeIf(allTask -> allTask.getType() == TaskType.TASK);
    }

    public void treeSetRemoveAllEpic() {
        PrioritizedTasks.removeIf(allTask -> allTask.getType() == TaskType.EPIC);
    }

    public void treeSetRemoveAllSubtask() {
        PrioritizedTasks.removeIf(allTask -> allTask.getType() == TaskType.SUBTASK);
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return PrioritizedTasks;
    }

    protected static LocalDateTime MAX_TIME_VALUE = LocalDateTime.now().plusYears(1);

    private static int taskId = 1;

    public void resetTaskId() {
        taskId = 1;
    }

    public static LocalDateTime getMaxTimeValue() {
        return MAX_TIME_VALUE;
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpicList() {
        return epicList;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtaskList() {
        return subtaskList;
    }

    @Override
    public ArrayList<Task> showAllTask() { //    Получение списка всех задач.
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> showAllEpic() { //    Получение списка всех задач.
        return new ArrayList<>(epicList.values());
    }

    @Override
    public ArrayList<Subtask> showAllSubtask() { //    Получение списка всех задач.
        return new ArrayList<>(subtaskList.values());
    }

    @Override
    public void deleteAllTask() {
        for (Integer index : tasks.keySet()) {
            historyManager.remove(index);
        }
        tasks.clear();
        treeSetRemoveAllTask();
    } /////

    @Override
    public void deleteAllEpic() {
        for (Integer index : epicList.keySet()) {
            historyManager.remove(index);
        }
        for (Integer index : subtaskList.keySet()) {
            historyManager.remove(index);
        }
        epicList.clear();
        subtaskList.clear();
        treeSetRemoveAllEpic();
    } /////

    @Override
    public void deleteAllSubtask() {
        for (Integer index : subtaskList.keySet()) {
            historyManager.remove(index);
        }
        for (Subtask index : subtaskList.values()) {
            int epicIndex = index.getSubtaskEpicId();
            for (int i = 0; i < epicList.size(); i++) {
                Epic subtaskInEpicForDelete = epicList.get(epicIndex);
                subtaskInEpicForDelete.deleteListSubtaskId();
            }
        }
        subtaskList.clear();
        treeSetRemoveAllSubtask();
        epicStatus();
        epicDuration();
        epicStartTime();
        epicEndTime();
    } /////

    @Override
    public Task showTaskById(int id) { //    Получение по идентификатору.
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic showEpicById(int id) { //    Получение по идентификатору.
        historyManager.add(epicList.get(id));
        return epicList.get(id);
    }

    @Override
    public Subtask showSubtaskById(int id) { //    Получение по идентификатору.
        historyManager.add(subtaskList.get(id));
        return subtaskList.get(id);
    }

    @Override
    public void newTask(Task task) {
        boolean timeLine = chechTaskStartTime(task);
        if (timeLine) {
            task.setTaskId(taskId);
            tasks.put(taskId, task);
            System.out.println("Задаче присвоен идентификатор " + taskId);
            PrioritizedTasks.add(task);
            taskId++;
        }
    } /////

    @Override
    public void newEpic(Epic epic) {
        epic.setTaskId(taskId);
        epicList.put(taskId, epic);
        System.out.println(epic.getTaskId());
        System.out.println("Задаче присвоен идентификатор " + taskId);
        taskId++;
    } /////

    @Override
    public void newSubtask(Subtask subtask) {
        boolean timeLine = chechTaskStartTime(subtask);
        if (timeLine) {
            if (!epicList.containsKey(subtask.getSubtaskEpicId())) {
                System.out.println("Такого Эпика в списке нет!");
            } else {
                subtask.setTaskId(taskId);
                subtaskList.put(taskId, subtask);
                Epic subtaskIdInEpic = epicList.get(subtask.getSubtaskEpicId());
                subtaskIdInEpic.setSubtaskId(subtask.getTaskId());
                chechTaskStartTime(subtask);
                PrioritizedTasks.add(subtask);
                epicStatus();
                epicDuration();
                epicStartTime();
                epicEndTime();
                System.out.println("Задаче присвоен идентификатор " + taskId);
                taskId++;
            }
        }
    } /////

    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getTaskId())) {
            System.out.println("Такой задачи нет в списке");
        } else {
            int taskIndex = task.getTaskId();
            tasks.put(taskIndex, task);
            treeSetRemoveTaskWithId(taskIndex);
            PrioritizedTasks.add(task);
        }
    } /////

    @Override
    public void updateEpic(Epic epic) {
        if (!epicList.containsKey(epic.getTaskId())) {
            System.out.println("Такой задачи нет в списке");
        } else {
            int epicIndex = epic.getTaskId();
            epicList.put(epicIndex, epic);
        }
    } /////

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtaskList.containsKey(subtask.getTaskId())) {
            System.out.println("Такой подзадачи нет в списке");
        } else {
            if (epicList.containsKey(subtask.getSubtaskEpicId())) {
                int subtaskId = subtask.getTaskId();
                subtaskList.put(subtaskId, subtask);
                treeSetRemoveTaskWithId(subtaskId);
                PrioritizedTasks.add(subtask);
            } else {
                System.out.println("Такой задачи нет в списке");
            }
        }
        epicStatus();
        epicDuration();
        epicStartTime();
        epicEndTime();
    } /////

    @Override
    public void deleteTaskId(int id) {
        tasks.remove(id);
        historyManager.remove(id);
        treeSetRemoveTaskWithId(id);
    } /////

    @Override
    public void deleteEpicId(int id) {
        Epic epicForDelete = epicList.get(id);
        ArrayList<Integer> subtaskIdSize = epicForDelete.getSubtaskId();
        for (Integer subtaskForDelete : subtaskIdSize) {
            subtaskList.remove(subtaskForDelete);
            historyManager.remove(subtaskForDelete);
        }
        epicList.remove(id);
        historyManager.remove(id);
    } /////

    @Override
    public void deleteSubtaskId(int id) {
        subtaskList.remove(id);
        epicStatusRemove(id);
        historyManager.remove(id);
        treeSetRemoveTaskWithId(id);
    } /////

    // Дополнительные методы:
    @Override
    public ArrayList<Subtask> showAllSubtaskInEpic(int epicId) { // Получение списка всех подзадач
        Epic listOfEpic = epicList.get(epicId);
        ArrayList<Subtask> subtaskInEpic = null;
        if (listOfEpic != null) {
            subtaskInEpic = new ArrayList<>();
            ArrayList<Integer> index = listOfEpic.getSubtaskId();
            for (Integer indexSubtask : index) {
                subtaskInEpic.add(subtaskList.get(indexSubtask));
            }
        } else {
            System.out.println("Данного айди нет в списке");
        }
        return subtaskInEpic;
    }

    private void epicStatusRemove(int id) {
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
        epicDuration();
        epicStartTime();
        epicEndTime();
    }

    public void epicStatus() {
        for (Epic ifSubtaskInEpic : epicList.values()) {

            int doneCount = 0;
            int newCount = 0;

            int epicId = ifSubtaskInEpic.getTaskId();
            Epic valueEpic = epicList.get(epicId);
            ArrayList<Integer> subtaskIdSize = ifSubtaskInEpic.getSubtaskId();
            if (subtaskIdSize.size() == 0) {
                ifSubtaskInEpic.setTaskStatus(TaskStatus.NEW);
                continue;
            }
            for (int i = 0; i < subtaskIdSize.size(); i++) {
                int subtaskStatus = subtaskIdSize.get(i);
                if (subtaskIdSize.size() == 0) {
                    ifSubtaskInEpic.setTaskStatus(TaskStatus.NEW);
                    break;
                } else if (Objects.equals(subtaskList.get(subtaskStatus).getTaskStatus(), TaskStatus.DONE)) {
                    doneCount++;
                    if (Objects.equals(doneCount, subtaskIdSize.size())) {
                        valueEpic.setTaskStatus(TaskStatus.DONE);
                    } else {
                        valueEpic.setTaskStatus(TaskStatus.IN_PROGRESS);
                    }
                } else if (Objects.equals(subtaskList.get(subtaskStatus).getTaskStatus(), TaskStatus.NEW)) {
                    newCount++;
                    if (Objects.equals(newCount, subtaskIdSize.size())) {
                        valueEpic.setTaskStatus(TaskStatus.NEW);
                    } else {
                        valueEpic.setTaskStatus(TaskStatus.IN_PROGRESS);
                    }
                } else {
                    valueEpic.setTaskStatus(TaskStatus.IN_PROGRESS);
                }
            }
        }
    }

    public void epicDuration() {
        for (Epic SubtaskInEpic : epicList.values()) {
            int duration = 0;
            ArrayList<Integer> subtaskIdList = SubtaskInEpic.getSubtaskId();
            for (Integer subtaskId : subtaskIdList) {
                duration += subtaskList.get(subtaskId).getDuration();
            }
            SubtaskInEpic.setDuration(duration);
        }
    }

    public void epicStartTime() {
        for (Epic epics : epicList.values()) {
            LocalDateTime startTime = epics.getStartTime();
            ArrayList<Integer> subtaskIdList = epics.getSubtaskId();
            if (subtaskIdList.size() == 1) {
                for (Integer subtaskId : subtaskIdList) {
                    startTime = subtaskList.get(subtaskId).getStartTime();
                    epics.setStartTime(startTime);
                }
            } else if (subtaskIdList.size() > 1) {
                for (Integer subtaskId : subtaskIdList) {
                    LocalDateTime startTimeSubtask = subtaskList.get(subtaskId).getStartTime();
                    if (startTime != null && startTimeSubtask != null) {
                        if (startTime.isAfter(startTimeSubtask)) {
                            startTime = startTimeSubtask;
                            epics.setStartTime(startTime);
                        }
                    }
                }
            } else {
                epics.setStartTime(null);
            }
        }
    }

    public void epicEndTime() {
        for (Epic epics : epicList.values()) {
            LocalDateTime endTime = epics.getEndTimeEpic();
            ArrayList<Integer> subtaskIdList = epics.getSubtaskId();
            if (subtaskIdList.size() == 1) {
                for (Integer subtaskId : subtaskIdList) {
                    Subtask subtask = subtaskList.get(subtaskId);
                    if (subtask.getStartTime() != null) {
                        endTime = subtask.getEndTime();
                        epics.setEndTime(endTime);
                    }
                }
            } else if (subtaskIdList.size() > 1) {
                for (Integer subtaskId : subtaskIdList) {
                    if (endTime != null && subtaskList.get(subtaskId).getEndTime() != null) {
                    LocalDateTime endTimeSubtask = subtaskList.get(subtaskId).getEndTime();
                        if (endTime.isBefore(endTimeSubtask)) {
                            endTime = endTimeSubtask;
                            epics.setEndTime(endTime);
                        }
                    }
                }
            } else {
                epics.setEndTime(null);
                break;
            }
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }
}

