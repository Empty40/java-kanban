import java.util.ArrayList;
public class Epic extends Task{

    ArrayList<Integer> subtaskId = new ArrayList<>();
    public Epic(String taskName, String taskDescription, String taskStatus) {
        super(taskName, taskDescription, taskStatus);
    }
    @Override
    public String toString() {
        return "Epic{" +
                "epicName='" + taskName + '\'' +
                ", epicDescription='" + taskDescription + '\'' +
                ", epicStatus=" + taskStatus +
                '}';
    }



}
