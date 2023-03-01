package models;

public class Subtask extends Epic {

    private final int subtaskEpicId;

    public Subtask(String taskName, String taskDescription, int subtaskEpicId, TaskStatus status) {
        super(taskName, taskDescription, status);
        this.subtaskEpicId = subtaskEpicId;
    }

    @Override
    public int getSubtaskEpicId() {
        return subtaskEpicId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "models.Subtask{" +
                "subtaskName='" + getTaskName() + '\'' +
                ", subtaskDescription='" + getTaskDescription() + '\'' +
                ", subtaskStatus=" + getTaskStatus() + '\'' +
                ", subtaskId=" + super.getTaskId() +
                ", duration='" + getDuration() + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                '}';
    }
}
