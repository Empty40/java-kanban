package models;

public class Subtask extends Epic {

    private final int subtaskEpicId;

    public Subtask(String taskName, String taskDescription, int subtaskEpicId, TaskStatusAndType status) {
        super(taskName, taskDescription, status);
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
