package models;

public class Subtask extends Epic {

    private int subtaskEpicId;

    public Subtask(String taskName, String taskDescription, String taskStatus, int subtaskEpicId) {
        super(taskName, taskDescription, taskStatus);
        this.subtaskEpicId = subtaskEpicId;
    }

    public int getSubtaskEpicId() {
        return subtaskEpicId;
    }

    @Override
    public String toString() {
        return "models.Subtask{" +
                "subtaskName='" + getTaskName() + '\'' +
                ", subtaskDescription='" + getTaskDescription() + '\'' +
                ", subtaskStatus=" + getTaskStatus() + '\'' +
                ", subtaskId=" + super.getTaskId() +
                '}';
    }
}
