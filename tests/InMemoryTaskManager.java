import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManager extends TaskManagerTest {

    @BeforeEach
    public void beforeEach() {
        file = new File("saveTasks.csv");
        test = new managers.InMemoryTaskManager();
        test.resetTaskId();
    }

    private final LocalDateTime testTime = LocalDateTime.now().plusWeeks(1);

    @Test
    public void deleteAllTaskTest() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        test.newTask(task);

        Task task2 = new Task("Name", "Description", TaskStatus.NEW);
        test.newTask(task2);

        Task task3 = new Task("Name", "Description", TaskStatus.NEW);
        test.newTask(task3);

        test.deleteAllTask();

        Assertions.assertEquals(0, test.showAllTask().size(), "Задачи не удалились");
    } /////

    @Test
    public void deleteAllEpicTest() {
        Epic epic1 = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic1);

        Epic epic2 = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic2);

        Subtask subtask1 = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask1);

        Subtask subtask2 = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask2);

        Subtask subtask3 = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask3);

        test.deleteAllEpic();

        Assertions.assertEquals(0, test.showAllEpic().size(), "Эпики не удалились");
        Assertions.assertEquals(0, test.showAllSubtask().size(), "Подзадачи эпиков не удалились");
    } /////

    @Test
    public void deleteAllSubtaskTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);

        Subtask subtask1 = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask1);

        Subtask subtask2 = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask2);

        Subtask subtask3 = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask3);

        test.deleteAllSubtask();

        Assertions.assertEquals(0, test.showAllSubtask().size(), "Подзадачи не удалились");
    } /////

    @Test
    public void showTaskByIdTest() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        test.newTask(task);

        Task task2 = new Task("Name", "Description", TaskStatus.NEW);
        task2.setDuration(15);
        task2.setStartTime(testTime);
        test.newTask(task2);

        Assertions.assertEquals(task, test.showTaskById(task.getTaskId()), "Задача не найдена");
        Assertions.assertNotEquals(task, test.showTaskById(0), "Задаче присвоен неверный идентификатор");
        Assertions.assertEquals(task2, test.showTaskById(task2.getTaskId()), "Задача не найдена");
        Assertions.assertNotEquals(task2, test.showTaskById(1), "Задаче присвоен неверный идентификатор");
    } /////

    @Test
    public void showEpicByIdTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);

        Assertions.assertEquals(test.showEpicById(epic.getTaskId()), epic, "Задача не найдена");
        Assertions.assertNotEquals(test.showTaskById(0), epic, "Задаче присвоен неверный идентификатор");
    } /////

    @Test
    public void showSubtaskByIdTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask);

        Task subtask2 = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        subtask2.setDuration(15);
        subtask2.setStartTime(testTime);
        test.newTask(subtask2);


        Assertions.assertEquals(subtask, test.showSubtaskById(subtask.getTaskId()), "Задача не найдена");
        Assertions.assertNotEquals(subtask, test.showTaskById(0), "Задаче присвоен неверный идентификатор");
        Assertions.assertEquals(subtask, test.showSubtaskById(subtask.getTaskId()), "Задача не найдена");
        Assertions.assertNotEquals(subtask, test.showTaskById(0), "Задаче присвоен неверный идентификатор");
    } /////

    @Test
    public void newTaskTest() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        task.setDuration(15);
        task.setStartTime(testTime);
        test.newTask(task);

        final Task savedTask = test.showTaskById(task.getTaskId());

        Assertions.assertNotNull(savedTask, "Задача не найдена");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают");

        final List<Task> tasks = test.showAllTask();

        Assertions.assertNotNull(tasks, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasks.size(), "Неверное кол-во задач");
        Assertions.assertEquals(task, tasks.get(0), "Задачи не совпадают");
    } /////

    @Test
    public void newEpicTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);

        final Epic savedEpic = test.showEpicById(epic.getTaskId());

        Assertions.assertNotNull(savedEpic, "Эпик не найден");
        Assertions.assertEquals(epic, savedEpic, "Эпики не совпадают");

        final List<Epic> epics = test.showAllEpic();

        Assertions.assertNotNull(epics, "Эпики не возвращаются");
        Assertions.assertEquals(1, epics.size(), "Неверное кол-во эпиков");
        Assertions.assertEquals(epic, epics.get(0), "Эпики не совпадают");

    } /////

    @Test
    public void newSubtaskTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        subtask.setDuration(15);
        subtask.setStartTime(testTime);
        test.newSubtask(subtask);

        final Subtask savedSubtask = test.showSubtaskById(subtask.getTaskId());

        Assertions.assertNotNull(savedSubtask, "Подзадача не найдена");
        Assertions.assertEquals(subtask, savedSubtask, "Подзадачи не совпадают");

        final List<Subtask> subtasks = test.showAllSubtask();

        Assertions.assertNotNull(subtasks, "Подзадачи не возвращаются");
        Assertions.assertEquals(1, subtasks.size(), "Неверное кол-во подзадач");
        Assertions.assertEquals(subtask, subtasks.get(0), "Эпики не совпадают");
    } /////

    @Test
    public void updateTaskTest() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        test.newTask(task);

        Task testTask = new Task("Name", "Description", TaskStatus.NEW);

        Assertions.assertNotEquals(0, task.getTaskId(), "Неверный идентификатор задачи");

        task.setTaskName("ChangeName");
        test.updateTask(task);

        Assertions.assertNotEquals(task.getTaskName(), testTask.getTaskName(), "Имя задачи не обновилось");

        testTask.setTaskName("ChangeName");
        task.setTaskDescription("ChangeDescription");
        test.updateTask(task);

        Assertions.assertNotEquals(task.getTaskDescription(), testTask.getTaskDescription()
                , "Описание задачи не обновилось");

        testTask.setTaskDescription("ChangeDescription");
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        test.updateTask(task);

        Assertions.assertNotEquals(task.getTaskStatus(), testTask.getTaskStatus()
                , "Статус задачи не обновился");

    } /////

    @Test
    public void updateEpicTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);

        Epic testEpic = new Epic("Name", "Description", TaskStatus.NEW);

        Assertions.assertNotEquals(0, epic.getTaskId(), "Неверный идентификатор эпика");

        epic.setTaskName("ChangeName");
        test.updateEpic(epic);

        Assertions.assertNotEquals(epic.getTaskName(), testEpic.getTaskName(), "Имя эпика не обновилось");

        testEpic.setTaskName("ChangeName");
        epic.setTaskDescription("ChangeDescription");
        test.updateEpic(epic);

        Assertions.assertNotEquals(epic.getTaskDescription(), testEpic.getTaskDescription()
                , "Описание эпика не обновилось");

        testEpic.setTaskDescription("ChangeDescription");
        epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        test.updateEpic(epic);

        Assertions.assertNotEquals(epic.getTaskStatus(), testEpic.getTaskStatus()
                , "Статус эпика не обновился");
    } /////

    @Test
    public void updateSubtaskTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask);

        Subtask testSubtask = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        testSubtask.setTaskId(2);

        Assertions.assertNotEquals(0, subtask.getSubtaskId(), "Неверный идентификатор подзадачи");

        subtask.setTaskName("ChangeName");
        test.updateSubtask(subtask);

        Assertions.assertNotEquals(subtask.getTaskName(), testSubtask.getTaskName()
                , "Имя подзадачи не обновилось");

        testSubtask.setTaskName("ChangeName");
        subtask.setTaskDescription("ChangeDescription");
        test.updateSubtask(subtask);

        Assertions.assertNotEquals(subtask.getTaskDescription(), testSubtask.getTaskDescription()
                , "Описание подзадачи не обновилось");

        testSubtask.setTaskDescription("ChangeDescription");
        subtask.setTaskStatus(TaskStatus.IN_PROGRESS);
        test.updateSubtask(subtask);

        Assertions.assertNotEquals(subtask.getTaskStatus(), testSubtask.getTaskStatus()
                , "Статус подзадачи не обновился");
    } /////

    @Test
    public void deleteTaskIdTest() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        test.newTask(task);

        test.deleteTaskId(1);

        Assertions.assertEquals(0, test.showAllTask().size(), "Задача не удалилась");
    } /////

    @Test
    public void deleteEpicIdTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask);

        Subtask subtask1 = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask1);

        test.deleteEpicId(1);

        Assertions.assertEquals(0, test.showAllEpic().size(), "Эпик не удалился");
        Assertions.assertEquals(0, test.showAllSubtask().size(), "Подзадачи эпика не удалились");
    } /////

    @Test
    public void deleteSubtaskIdTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask);

        test.deleteSubtaskId(2);

        Assertions.assertEquals(0, test.showAllSubtask().size(), "Подзадача не удалилась");
    } /////

    @Test
    public void showAllSubtaskInEpicTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);

        Subtask subtask1 = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask1);

        Subtask subtask2 = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask2);

        Subtask subtask3 = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask3);

        List<Subtask> subtaskList = new ArrayList<>();
        subtaskList.add(subtask1);
        subtaskList.add(subtask2);
        subtaskList.add(subtask3);

        Assertions.assertEquals(subtaskList, test.showAllSubtaskInEpic(1)
                , "Подзадачи не закреплены за эпиком");
    } /////

    @Test
    public void taskStatus() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        test.newTask(task);

        Assertions.assertEquals(TaskStatus.NEW, task.getTaskStatus());

        task.setTaskStatus(TaskStatus.IN_PROGRESS);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, task.getTaskStatus());

        task.setTaskStatus(TaskStatus.DONE);

        Assertions.assertEquals(TaskStatus.DONE, task.getTaskStatus());
    } /////

    @Test
    public void statusEpicTaskWithEmptySubtaskList() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);
        int epicID = epic.getTaskId();

        Assertions.assertEquals(TaskStatus.NEW, test.showEpicById(epicID).getTaskStatus());
    } /////

    @Test
    public void statusEpicTaskWithAllSubtaskStatusIsNew() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);
        int epicID = epic.getTaskId();

        Subtask subtask = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask);

        Subtask subtask2 = new Subtask("Name2", "Description2", 1, TaskStatus.NEW);
        test.newSubtask(subtask2);

        Assertions.assertEquals(TaskStatus.NEW, test.showEpicById(epicID).getTaskStatus()
                , "Статус задачи неверный");
    } /////

    @Test
    public void statusEpicTaskWithAllSubtaskStatusIsDone() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);
        int epicID = epic.getTaskId();

        Subtask subtask = new Subtask("Name", "Description", 1, TaskStatus.DONE);
        test.newSubtask(subtask);

        Subtask subtask2 = new Subtask("Name2", "Description2", 1, TaskStatus.DONE);
        test.newSubtask(subtask2);

        Assertions.assertEquals(TaskStatus.DONE, test.showEpicById(epicID).getTaskStatus()
                , "Статус задачи неверный");
    } /////

    @Test
    public void statusEpicTaskWithSubtaskStatusIsDoneAndNew() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);
        int epicID = epic.getTaskId();

        Subtask subtask = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newSubtask(subtask);

        Subtask subtask2 = new Subtask("Name2", "Description2", 1, TaskStatus.DONE);
        test.newSubtask(subtask2);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, test.showEpicById(epicID).getTaskStatus()
                , "Статус задачи неверный");
    } /////

    @Test
    public void statusEpicTaskWithAllSubtaskStatusInProgress() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);
        int epicID = epic.getTaskId();

        Subtask subtask = new Subtask("Name", "Description", 1
                , TaskStatus.IN_PROGRESS);
        test.newSubtask(subtask);

        Subtask subtask2 = new Subtask("Name2", "Description2", 1
                , TaskStatus.IN_PROGRESS);
        test.newSubtask(subtask2);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, test.showEpicById(epicID).getTaskStatus()
                , "Статус задачи неверный");
    } /////

    @Test
    public void subtaskStatus() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        test.newTask(subtask);

        Assertions.assertEquals(TaskStatus.NEW, subtask.getTaskStatus());

        subtask.setTaskStatus(TaskStatus.IN_PROGRESS);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, subtask.getTaskStatus());

        subtask.setTaskStatus(TaskStatus.DONE);

        Assertions.assertEquals(TaskStatus.DONE, subtask.getTaskStatus());
    } /////

    @Test
    public void containsEpicWithNewSubtask() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        test.newEpic(epic);

        Epic epic2 = new Epic("Name2", "Description2", TaskStatus.NEW);
        test.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description", 1
                , TaskStatus.IN_PROGRESS);
        test.newSubtask(subtask);
        int subtaskId = subtask.getTaskId();

        Subtask subtask2 = new Subtask("Name2", "Description2", 1
                , TaskStatus.IN_PROGRESS);
        test.newSubtask(subtask2);
        int subtaskId2 = subtask2.getTaskId();

        Subtask idSubtask = test.showSubtaskById(subtaskId);
        Subtask idSubtask2 = test.showSubtaskById(subtaskId2);
        Assertions.assertEquals(epic, test.showEpicById(idSubtask.getSubtaskEpicId())
                , "Эпик не найден, проверьте создание эпика");
        Assertions.assertEquals(epic, test.showEpicById(idSubtask2.getSubtaskEpicId())
                , "Эпик не найден, проверьте создание эпика");

        Assertions.assertNotEquals(epic2, test.showEpicById(idSubtask.getSubtaskEpicId())
                , "Id эпика, к которому относится подзадача - неверный");
        Assertions.assertNotEquals(epic2, test.showEpicById(idSubtask2.getSubtaskEpicId())
                , "Id эпика, к которому относится подзадача - неверный");
    }
}
