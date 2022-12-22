package models;

import java.util.ArrayList;
public class Epic extends Task {

    private ArrayList<Integer> subtaskId = new ArrayList<>();

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(int subtaskId) {
        this.subtaskId.add(subtaskId);
    }

    public Epic(String taskName, String taskDescription, String taskStatus) {
        super(taskName, taskDescription, taskStatus);
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
