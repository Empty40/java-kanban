import managers.FileBackedTasksManager;
import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class FileBackedTaskManager extends TaskManagerTest {

    @BeforeEach
    public void beforeEach() {
        file = new File("saveTasks.csv");
        test = new FileBackedTasksManager();
        test.resetTaskId();
    }

    @Test
    public void emptyFileBackedTasksManagerTest() {
        if (file.canRead()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(0, fileBackedTasksManager.showAllTask().size()
                , "Задач не должно быть");

        Assertions.assertEquals(0, fileBackedTasksManager.showAllEpic().size()
                , "Эпиков не должно быть");

        Assertions.assertEquals(0, fileBackedTasksManager.showAllSubtask().size()
                , "Подзадач быть не должно");

        Assertions.assertEquals(0, fileBackedTasksManager.getHistory().size()
                , "Список истории должен быть пуст");

    } /////

    @Test
    public void saveAndExtractionFileBackedTasksManagerTest() {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        Task task = new Task("Name", "Description", TaskStatus.NEW);
        fileBackedTasksManager.newTask(task);

        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        fileBackedTasksManager.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description", 2, TaskStatus.NEW);
        fileBackedTasksManager.newSubtask(subtask);

        fileBackedTasksManager.deleteSubtaskId(3);

        Assertions.assertEquals(0, fileBackedTasksManager.showAllSubtask().size()
                , "Список подзадач должен быть пуст");

        fileBackedTasksManager.resetTaskId();

        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(1, fileBackedTasksManager2.showAllTask().size()
                , "Задача не восстановилась из файла");
        Assertions.assertEquals(1, fileBackedTasksManager2.showAllEpic().size()
                , "Эпик не восстановился из файла");
        Assertions.assertEquals(0, fileBackedTasksManager2.getEpicList().get(2).getSubtaskEpicId()
                , "У эпика не должно быть подзадач");
        Assertions.assertEquals(0, fileBackedTasksManager2.showAllSubtask().size()
                , "Список подзадач должен быть пуст");
        Assertions.assertEquals(0, fileBackedTasksManager2.getHistory().size()
                , "Список истории должен быть пуст");
    } /////

    @Test
    public void FileBackedTasksManagerTest() {
        if (file.canRead()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);

        Task task = new Task("Name", "Description", TaskStatus.NEW);
        fileBackedTasksManager.newTask(task);

        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        fileBackedTasksManager.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description", 2, TaskStatus.NEW);
        fileBackedTasksManager.newSubtask(subtask);

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

        fileBackedTasksManager.updateTask(task1);
        fileBackedTasksManager.updateEpic(epic1);
        fileBackedTasksManager.updateSubtask(subtask1);

        Assertions.assertEquals(task1, fileBackedTasksManager.showTaskById(1), "Задача не обновилась");
        Assertions.assertEquals(epic1, fileBackedTasksManager.showEpicById(2), "Эпик не обновился");
        Assertions.assertEquals(subtask1, fileBackedTasksManager.showSubtaskById(3), "Подзадача не обновилась");

        fileBackedTasksManager.deleteAllTask();
        fileBackedTasksManager.deleteAllEpic();
        fileBackedTasksManager.deleteAllSubtask();

        Assertions.assertEquals(0, fileBackedTasksManager.showAllTask().size()
                , "Задачи не удалились");
        Assertions.assertEquals(0, fileBackedTasksManager.showAllEpic().size()
                , "Эпики не удалились");
        Assertions.assertEquals(0, fileBackedTasksManager.showAllSubtask().size()
                , "Подзадачи не удалились");

        Task task2 = new Task("Name", "Description", TaskStatus.NEW);
        fileBackedTasksManager.newTask(task2);
        Epic epic2 = new Epic("Name", "Description", TaskStatus.NEW);
        fileBackedTasksManager.newEpic(epic2);

        fileBackedTasksManager.deleteTaskId(4);
        fileBackedTasksManager.deleteEpicId(5);

        Assertions.assertEquals(0, fileBackedTasksManager.showAllTask().size()
                , "Задача не удалилась по идентификатору");
        Assertions.assertEquals(0, fileBackedTasksManager.showAllEpic().size()
                , "Эпик не удалился по идентификатору");
    }
}
