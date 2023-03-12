import managers.FileBackedTasksManager;
import managers.HttpTaskManager;
import models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.File;
import java.io.IOException;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    @BeforeAll
    public static void beforeAll() {
        {
            try {
                new KVServer().start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    File file;

    @BeforeEach
    public void beforeEach() {
        test = new HttpTaskManager();
        test.resetTaskId();
        file = new File("saveTasks.csv");
        test.start();
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

    }

    @Test
    public void HttpTaskManagerTest() {

        Task task = new Task("Name", "Description", TaskStatus.NEW);
        test.newTask(task);

        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description", 2, TaskStatus.NEW);
        test.newSubtask(subtask);

        Task task1 = new Task("Name", "Description", TaskStatus.NEW);
        Epic epic1 = new Epic("Name", "Description", TaskStatus.NEW);
        Subtask subtask1 = new Subtask("Name", "Description", 2, TaskStatus.NEW);

        task1.setTaskName("ChangeName");
        task1.setTaskDescription("ChangeDescription");
        task1.setTaskStatus(TaskStatus.IN_PROGRESS);
        task1.setTaskId(1);

        epic1.setTaskName("ChangeName");
        epic1.setTaskDescription("ChangeDescription");
        epic1.setTaskStatus(TaskStatus.IN_PROGRESS);
        epic1.setTaskId(2);

        subtask1.setTaskName("ChangeName");
        subtask1.setTaskDescription("ChangeDescription");
        subtask1.setTaskStatus(TaskStatus.IN_PROGRESS);
        subtask1.setTaskId(3);

        test.updateTask(task1);
        test.updateEpic(epic1);
        test.updateSubtask(subtask1);

        Assertions.assertEquals(task1, test.showTaskById(1), "Задача не обновилась");
        Assertions.assertEquals(epic1, test.showEpicById(2), "Эпик не обновился");
        Assertions.assertEquals(subtask1, test.showSubtaskById(3), "Подзадача не обновилась");

        test.deleteAllTask();
        test.deleteAllEpic();
        test.deleteAllSubtask();

        Assertions.assertEquals(0, test.showAllTask().size()
                , "Задачи не удалились");
        Assertions.assertEquals(0, test.showAllEpic().size()
                , "Эпики не удалились");
        Assertions.assertEquals(0, test.showAllSubtask().size()
                , "Подзадачи не удалились");

        Task task2 = new Task("Name", "Description", TaskStatus.NEW);
        test.newTask(task2);
        Epic epic2 = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic2);

        test.deleteTaskId(4);
        test.deleteEpicId(5);

        Assertions.assertEquals(0, test.showAllTask().size()
                , "Задача не удалилась по идентификатору");
        Assertions.assertEquals(0, test.showAllEpic().size()
                , "Эпик не удалился по идентификатору");
    }

    @Test
    public void emptyFileBackedTasksManagerTest() {

        test.deleteAllTask();

        test.loadFromServer();

        Assertions.assertEquals(0, test.showAllTask().size()
                , "Задач не должно быть");

        Assertions.assertEquals(0, test.showAllEpic().size()
                , "Эпиков не должно быть");

        Assertions.assertEquals(0, test.showAllSubtask().size()
                , "Подзадач быть не должно");

        Assertions.assertEquals(0, test.getHistory().size()
                , "Список истории должен быть пуст");

    }

    @Test
    public void saveAndExtractionFileBackedTasksManagerTest() {

        Task task = new Task("Name", "Description", TaskStatus.NEW);
        test.newTask(task);

        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description", 2, TaskStatus.NEW);
        test.newSubtask(subtask);

        test.deleteSubtaskId(3);

        Assertions.assertEquals(0, test.showAllSubtask().size()
                , "Список подзадач должен быть пуст");

        test.resetTaskId();

        test.loadFromServer();

        Assertions.assertEquals(1, test.showAllTask().size()
                , "Задача не восстановилась из файла");
        Assertions.assertEquals(1, test.showAllEpic().size()
                , "Эпик не восстановился из файла");
        Assertions.assertEquals(0, test.getEpicList().get(2).getSubtaskEpicId()
                , "У эпика не должно быть подзадач");
        Assertions.assertEquals(0, test.showAllSubtask().size()
                , "Список подзадач должен быть пуст");
        Assertions.assertEquals(0, test.getHistory().size()
                , "Список истории должен быть пуст");
    }
}
