public class Task {
    private int taskId;
    protected String taskName;
    protected String taskDescription;
    protected String taskStatus;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Task(String taskName, String taskDescription, String taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
    }

    // Не совсем понял для чего нужно переопределять equals, если Вам не будет сложно обьясните пожалуйста, благодарю
    // И по разнесению по пакетам, как мне это более корректно сделать?
    @Override
    public String toString() {
        return "Task{" + // имя класса
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus=" + taskStatus +
                '}';


    }
}


