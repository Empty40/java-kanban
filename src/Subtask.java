public class Subtask extends Epic {
    String subtaskOfEpic;

    public Subtask(String taskName, String taskDescription, String taskStatus, String subtaskOfEpic) {
        super(taskName, taskDescription, taskStatus);
    }

    public String getSubtaskOfEpic() {
        return subtaskOfEpic;
    }
    public Subtask(int taskId, String taskName, String taskDescription, String taskStatus) {
        super(taskId, taskName, taskDescription, taskStatus);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "subtaskName='" + taskName + '\'' +
                ", subtaskDescription='" + taskDescription + '\'' +
                ", subtaskStatus=" + taskStatus +
                '}';
    }
}
