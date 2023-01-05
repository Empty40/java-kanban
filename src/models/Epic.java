package models;

import java.util.ArrayList;
public class Epic extends Task {

    private ArrayList<Integer> subtaskId = new ArrayList<>();

    public Epic(String taskName, String taskDescription, TaskStatus status) {
        super(taskName, taskDescription, status);
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(int subtaskId) {
        this.subtaskId.add(subtaskId);
    }

    public void deleteSubtaskId(int id) {
        subtaskId.remove(id);
    }

    public void deleteListSubtaskId() {
        subtaskId.clear();
    }

    @Override
    public String toString() {
        return "models.Epic{" +
                "epicName='" + getTaskName() + '\'' +
                ", epicDescription='" + getTaskDescription() + '\'' +
                ", epicStatus=" + getTaskStatus() +
                '}';
    }
}
