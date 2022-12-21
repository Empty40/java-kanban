public class Subtask extends Epic {
    int subtaskEpicId;

    public Subtask(String taskName, String taskDescription, String taskStatus, int subtaskEpicId) {
        super(taskName, taskDescription, taskStatus);
        this.subtaskEpicId = subtaskEpicId;
    }
    @Override
    public String toString() {
        return "Subtask{" +
                "subtaskName='" + taskName + '\'' +
                ", subtaskDescription='" + taskDescription + '\'' +
                ", subtaskStatus=" + taskStatus + '\'' +
                ", subtaskId=" + super.getTaskId() +
                '}';
    }
}
