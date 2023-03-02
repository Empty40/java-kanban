import interfaces.HistoryManager;
import models.Managers;
import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class HistoryManagerTest extends TaskManagerTest {

    HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
        file = new File("saveTasks.csv");
        test = new managers.InMemoryTaskManager();
        test.resetTaskId();
    }

    @Test
    public void addTaskToHistory() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        test.newTask(task);

        final Task savedTask = test.showTaskById(task.getTaskId());
        int taskId = task.getTaskId();

        historyManager.add(savedTask);

        List<Task> history = historyManager.getHistory();

        Assertions.assertNotNull(history, "История не должна быть пустой");
        Assertions.assertEquals(1, history.size(), "История не должна быть пустая");

        historyManager.add(savedTask);

        Assertions.assertEquals(1, history.size(), "В истории не должны сохраняться дубли");

        historyManager.remove(taskId);

        history = historyManager.getHistory();

        Assertions.assertEquals(0, history.size(), "История должна быть пустая");
    }

    @Test
    public void deleteInHistoryFirstTask() {
        Task task1 = new Task("Name1", "Description1", TaskStatus.NEW);
        test.newTask(task1);

        int taskId = task1.getTaskId();

        Task task2 = new Task("Name2", "Description2", TaskStatus.NEW);
        test.newTask(task2);

        Task task3 = new Task("Name3", "Description3", TaskStatus.NEW);
        test.newTask(task3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(taskId);

        List<Task> history = historyManager.getHistory();

        Assertions.assertEquals(task2, history.get(0), "Порядок истории должен быть -> task2 -> task3");
        Assertions.assertEquals(task3, history.get(1), "Порядок истории должен быть -> task2 -> task3");
    }

    @Test
    public void deleteInHistoryMiddleTask() {
        Task task1 = new Task("Name1", "Description1", TaskStatus.NEW);
        test.newTask(task1);

        Task task2 = new Task("Name2", "Description2", TaskStatus.NEW);
        test.newTask(task2);

        int taskId2 = task2.getTaskId();

        Task task3 = new Task("Name3", "Description3", TaskStatus.NEW);
        test.newTask(task3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(taskId2);

        List<Task> history = historyManager.getHistory();

        Assertions.assertEquals(task1, history.get(0), "Порядок истории должен быть -> task1 -> task3");
        Assertions.assertEquals(task3, history.get(1), "Порядок истории должен быть -> task1 -> task3");
    }

    @Test
    public void deleteInHistoryLastTask() {
        Task task1 = new Task("Name1", "Description1", TaskStatus.NEW);
        test.newTask(task1);

        Task task2 = new Task("Name2", "Description2", TaskStatus.NEW);
        test.newTask(task2);

        Task task3 = new Task("Name3", "Description3", TaskStatus.NEW);
        test.newTask(task3);

        int taskId3 = task3.getTaskId();

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(taskId3);

        List<Task> history = historyManager.getHistory();

        Assertions.assertEquals(task1, history.get(0), "Порядок истории должен быть -> task1 -> task2");
        Assertions.assertEquals(task2, history.get(1), "Порядок истории должен быть -> task1 -> task2");
    }

    @Test
    public void emptyHistory() {
        Assertions.assertEquals(0, historyManager.getHistory().size(), "История должна быть пустой");
    }

}
