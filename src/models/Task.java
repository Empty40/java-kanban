package models;

public class Task {
    private int taskId;
    private String taskName;

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    private String taskDescription;

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    private String taskStatus;

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

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

    // По поводу equals(), сначала укажу Ваш комментарий:
    // "в наших классах задач нам может пригодиться только для правильного сравнения объекта и в некоторых методах хешмапы
    // например в containsValue(Object value), который возвращает true, если объект был найден в хешмапе по значению,
    // он как раз будет сравниваться по equals() или метод remove(Object key, Object value), который так же будет искать
    // и удалять объект по ключу и по значению, сравнивая через equals()"

    // Я считаю что метод equals() в данной ситуации не нужно переопределять, опять же если ошибаюсь то поправьте)
    // Что написано в ТЗ: "У каждого типа задач есть идентификатор. Это целое число, уникальное для всех типов задач.
    // По нему мы находим, обновляем, удаляем задачи. При создании задачи менеджер присваивает ей новый идентификатор."
    // Из этого, лично я понимаю таким образом, следует, что нам не нужно сравнивать объекты по значениям, ввиду
    // присутствия идентификаторов, по ним мы собственно и осуществляем обновление/удаление/нахождение объекта
    @Override
    public String toString() {
        return "models.Task{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus=" + taskStatus +
                '}';


    }
}


