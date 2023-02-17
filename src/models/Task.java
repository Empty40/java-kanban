package models;

public class Task {

    private int taskId;
    private String taskName;
    private String taskDescription;
    private TaskStatusAndType taskStatus;

    public Task(String taskName, String taskDescription, TaskStatusAndType status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        taskStatus = status;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public TaskStatusAndType getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatusAndType taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "models.Task{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}


