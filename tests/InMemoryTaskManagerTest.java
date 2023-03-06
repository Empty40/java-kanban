import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        test = new InMemoryTaskManager();
        test.resetTaskId();
    }

    @Test
    public void prioritizedTaskTest() {
        Task task1 = new Task("Name", "Description", TaskStatus.NEW);
        task1.setDuration(30);
        task1.setStartTime(LocalDateTime.now().plusWeeks(1));
        test.newTask(task1);

        Task task2 = new Task("Name", "Description", TaskStatus.NEW);
        task2.setDuration(30);
        task2.setStartTime(LocalDateTime.now().plusWeeks(2));
        test.newTask(task2);

        ArrayList<Task> prioritizedTaskList = test.getPrioritizedTasks();

        Assertions.assertEquals(prioritizedTaskList.get(0), task1, "Первая должна быть задача -> task1");
        Assertions.assertEquals(prioritizedTaskList.get(1), task2, "Вторая должна быть задача -> task2");

        Task task3 = new Task("Name", "Description", TaskStatus.NEW);
        task3.setDuration(30);
        task3.setStartTime(LocalDateTime.now().plusWeeks(1).plusDays(1));
        test.newTask(task3);

        prioritizedTaskList = test.getPrioritizedTasks();

        Assertions.assertEquals(prioritizedTaskList.get(0), task1, "Первая должна быть задача -> task1");
        Assertions.assertEquals(prioritizedTaskList.get(1), task3, "Вторая должна быть задача -> task3");
        Assertions.assertEquals(prioritizedTaskList.get(2), task2, "Первая должна быть задача -> task2");
    }

    @Test
    public void getHistoryTest() {
        Task task1 = new Task("Name1", "Description1", TaskStatus.NEW); //id 1
        test.newTask(task1);
        test.showTaskById(1);

        Task task2 = new Task("Name2", "Description2", TaskStatus.NEW); //id 2
        test.newTask(task2);
        test.showTaskById(2);

        Task task3 = new Task("Name3", "Description3", TaskStatus.NEW); //id 3
        test.newTask(task3);
        test.showTaskById(3);

        test.deleteTaskId(2);

        List<Task> history = test.getHistory();

        Assertions.assertEquals(task1, history.get(0), "Порядок истории должен быть -> task1 -> task3");
        Assertions.assertEquals(task3, history.get(1), "Порядок истории должен быть -> task1 -> task3");

        Task task4 = new Task("Name4", "Description4", TaskStatus.NEW); //id 4
        test.newTask(task4);
        test.showTaskById(4);

        test.showTaskById(3);

        history = test.getHistory();

        Assertions.assertEquals(task1, history.get(0), "Порядок истории должен быть -> task1 -> task4 -> task3");
        Assertions.assertEquals(task4, history.get(1), "Порядок истории должен быть -> task1 -> task4 -> task3");
        Assertions.assertEquals(task3, history.get(2), "Порядок истории должен быть -> task1 -> task4 -> task3");
    }
}
