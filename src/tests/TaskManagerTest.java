package tests;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import managers.FileBackedTasksManager;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.*;
import models.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskManagerTest<T extends TaskManager> {

    private final LocalDateTime testTime = LocalDateTime.of(2023, 3, 10, 12, 0);

    TaskManager testTaskManager;

    HistoryManager historyManager;

    FileBackedTasksManager testFile;

    File file;

    @BeforeEach
    public void beforeEach() {
        testTaskManager = new InMemoryTaskManager();
        historyManager = Managers.getDefaultHistory();
        file = new File("saveTasks.csv");
        testFile = new FileBackedTasksManager();
        testTaskManager.resetTaskId();
    }

    @Test
    public void deleteAllTaskTest() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        testTaskManager.newTask(task);

        Task task2 = new Task("Name", "Description", TaskStatus.NEW);
        testTaskManager.newTask(task2);

        Task task3 = new Task("Name", "Description", TaskStatus.NEW);
        testTaskManager.newTask(task3);

        testTaskManager.deleteAllTask();

        Assertions.assertEquals(0, testTaskManager.showAllTask().size(), "Задачи не удалились");
    } /////

    @Test
    public void deleteAllEpicTest() {
        Epic epic1 = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic1);

        Epic epic2 = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic2);

        Subtask subtask1 = new Subtask("Name", "Description",1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask1);

        Subtask subtask2 = new Subtask("Name", "Description",1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask2);

        Subtask subtask3 = new Subtask("Name", "Description",1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask3);

        testTaskManager.deleteAllEpic();

        Assertions.assertEquals(0, testTaskManager.showAllEpic().size(), "Эпики не удалились");
        Assertions.assertEquals(0, testTaskManager.showAllSubtask().size(), "Подзадачи эпиков не удалились");
    } /////

    @Test
    public void deleteAllSubtaskTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);

        Subtask subtask1 = new Subtask("Name", "Description",1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask1);

        Subtask subtask2 = new Subtask("Name", "Description",1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask2);

        Subtask subtask3 = new Subtask("Name", "Description",1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask3);

        testTaskManager.deleteAllSubtask();

        Assertions.assertEquals(0, testTaskManager.showAllSubtask().size(), "Подзадачи не удалились");
    } /////

    @Test
    public void showTaskByIdTest() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        testTaskManager.newTask(task);

        Task task2 = new Task("Name", "Description", TaskStatus.NEW);
        task2.setDuration(15);
        task2.setStartTime(testTime);
        testTaskManager.newTask(task2);

        Assertions.assertEquals(task, testTaskManager.showTaskById(task.getTaskId()), "Задача не найдена");
        Assertions.assertNotEquals(task, testTaskManager.showTaskById(0), "Задаче присвоен неверный идентификатор");
        Assertions.assertEquals(task2, testTaskManager.showTaskById(task2.getTaskId()), "Задача не найдена");
        Assertions.assertNotEquals(task2, testTaskManager.showTaskById(1), "Задаче присвоен неверный идентификатор");
    } /////

    @Test
    public void showEpicByIdTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);

        Assertions.assertEquals(testTaskManager.showEpicById(epic.getTaskId()), epic, "Задача не найдена");
        Assertions.assertNotEquals(testTaskManager.showTaskById(0), epic, "Задаче присвоен неверный идентификатор");
    } /////

    @Test
    public void showSubtaskByIdTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask);

        Task subtask2 = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        subtask2.setDuration(15);
        subtask2.setStartTime(testTime);
        testTaskManager.newTask(subtask2);


        Assertions.assertEquals(subtask, testTaskManager.showSubtaskById(subtask.getTaskId()), "Задача не найдена");
        Assertions.assertNotEquals(subtask, testTaskManager.showTaskById(0), "Задаче присвоен неверный идентификатор");
        Assertions.assertEquals(subtask, testTaskManager.showSubtaskById(subtask.getTaskId()), "Задача не найдена");
        Assertions.assertNotEquals(subtask, testTaskManager.showTaskById(0), "Задаче присвоен неверный идентификатор");
    } /////

    @Test
    public void newTaskTest() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        task.setDuration(15);
        task.setStartTime(testTime);
        testTaskManager.newTask(task);

        final Task savedTask = testTaskManager.showTaskById(task.getTaskId());

        Assertions.assertNotNull(savedTask, "Задача не найдена");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают");

        final List<Task> tasks = testTaskManager.showAllTask();

        Assertions.assertNotNull(tasks, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasks.size(), "Неверное кол-во задач");
        Assertions.assertEquals(task, tasks.get(0), "Задачи не совпадают");
    } /////

    @Test
    public void newEpicTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);

        final Epic savedEpic = testTaskManager.showEpicById(epic.getTaskId());

        Assertions.assertNotNull(savedEpic, "Эпик не найден");
        Assertions.assertEquals(epic, savedEpic, "Эпики не совпадают");

        final List<Epic> epics = testTaskManager.showAllEpic();

        Assertions.assertNotNull(epics, "Эпики не возвращаются");
        Assertions.assertEquals(1, epics.size(), "Неверное кол-во эпиков");
        Assertions.assertEquals(epic, epics.get(0), "Эпики не совпадают");

    } /////

    @Test
    public void newSubtaskTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description",1, TaskStatus.NEW);
        subtask.setDuration(15);
        subtask.setStartTime(testTime);
        testTaskManager.newSubtask(subtask);

        final Subtask savedSubtask = testTaskManager.showSubtaskById(subtask.getTaskId());

        Assertions.assertNotNull(savedSubtask, "Подзадача не найдена");
        Assertions.assertEquals(subtask, savedSubtask, "Подзадачи не совпадают");

        final List<Subtask> subtasks = testTaskManager.showAllSubtask();

        Assertions.assertNotNull(subtasks, "Подзадачи не возвращаются");
        Assertions.assertEquals(1, subtasks.size(), "Неверное кол-во подзадач");
        Assertions.assertEquals(subtask, subtasks.get(0), "Эпики не совпадают");
    } /////

    @Test
    public void updateTaskTest() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        testTaskManager.newTask(task);

        Task testTask = new Task("Name", "Description", TaskStatus.NEW);

        Assertions.assertNotEquals(0, task.getTaskId(),"Неверный идентификатор задачи");

        task.setTaskName("ChangeName");
        testTaskManager.updateTask(task);

        Assertions.assertNotEquals(task.getTaskName(), testTask.getTaskName(), "Имя задачи не обновилось");

        testTask.setTaskName("ChangeName");
        task.setTaskDescription("ChangeDescription");
        testTaskManager.updateTask(task);

        Assertions.assertNotEquals(task.getTaskDescription(), testTask.getTaskDescription()
                , "Описание задачи не обновилось");

        testTask.setTaskDescription("ChangeDescription");
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        testTaskManager.updateTask(task);

        Assertions.assertNotEquals(task.getTaskStatus(), testTask.getTaskStatus()
                , "Статус задачи не обновился");

    } /////

    @Test
    public void updateEpicTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);

        Epic testEpic = new Epic("Name", "Description", TaskStatus.NEW);

        Assertions.assertNotEquals(0, epic.getTaskId(),"Неверный идентификатор эпика");

        epic.setTaskName("ChangeName");
        testTaskManager.updateEpic(epic);

        Assertions.assertNotEquals(epic.getTaskName(), testEpic.getTaskName(), "Имя эпика не обновилось");

        testEpic.setTaskName("ChangeName");
        epic.setTaskDescription("ChangeDescription");
        testTaskManager.updateEpic(epic);

        Assertions.assertNotEquals(epic.getTaskDescription(), testEpic.getTaskDescription()
                , "Описание эпика не обновилось");

        testEpic.setTaskDescription("ChangeDescription");
        epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        testTaskManager.updateEpic(epic);

        Assertions.assertNotEquals(epic.getTaskStatus(), testEpic.getTaskStatus()
                , "Статус эпика не обновился");
    } /////

    @Test
    public void updateSubtaskTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description",1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask);

        Subtask testSubtask = new Subtask("Name", "Description",1, TaskStatus.NEW);
        testSubtask.setTaskId(2);

        Assertions.assertNotEquals(0, subtask.getSubtaskId(),"Неверный идентификатор подзадачи");

        subtask.setTaskName("ChangeName");
        testTaskManager.updateSubtask(subtask);

        Assertions.assertNotEquals(subtask.getTaskName(), testSubtask.getTaskName()
                , "Имя подзадачи не обновилось");

        testSubtask.setTaskName("ChangeName");
        subtask.setTaskDescription("ChangeDescription");
        testTaskManager.updateSubtask(subtask);

        Assertions.assertNotEquals(subtask.getTaskDescription(), testSubtask.getTaskDescription()
                , "Описание подзадачи не обновилось");

        testSubtask.setTaskDescription("ChangeDescription");
        subtask.setTaskStatus(TaskStatus.IN_PROGRESS);
        testTaskManager.updateSubtask(subtask);

        Assertions.assertNotEquals(subtask.getTaskStatus(), testSubtask.getTaskStatus()
                , "Статус подзадачи не обновился");
    } /////

    @Test
    public void deleteTaskIdTest() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        testTaskManager.newTask(task);

        testTaskManager.deleteTaskId(1);

        Assertions.assertEquals(0, testTaskManager.showAllTask().size(), "Задача не удалилась");
    } /////

    @Test
    public void deleteEpicIdTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description",1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask);

        Subtask subtask1 = new Subtask("Name", "Description",1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask1);

        testTaskManager.deleteEpicId(1);

        Assertions.assertEquals(0, testTaskManager.showAllEpic().size(), "Эпик не удалился");
        Assertions.assertEquals(0, testTaskManager.showAllSubtask().size(), "Подзадачи эпика не удалились");
    } /////

    @Test
    public void deleteSubtaskIdTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description",1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask);

        testTaskManager.deleteSubtaskId(2);

        Assertions.assertEquals(0, testTaskManager.showAllSubtask().size(), "Подзадача не удалилась");
    } /////

    @Test
    public void showAllSubtaskInEpicTest() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);

        Subtask subtask1 = new Subtask("Name", "Description",1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask1);

        Subtask subtask2 = new Subtask("Name", "Description",1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask2);

        Subtask subtask3 = new Subtask("Name", "Description",1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask3);

        List<Subtask> subtaskList = new ArrayList<>();
        subtaskList.add(subtask1);
        subtaskList.add(subtask2);
        subtaskList.add(subtask3);

        Assertions.assertEquals(subtaskList, testTaskManager.showAllSubtaskInEpic(1)
                , "Подзадачи не закреплены за эпиком");
    } /////

    @Test
    public void taskStatus() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        testTaskManager.newTask(task);

        Assertions.assertEquals(TaskStatus.NEW, task.getTaskStatus());

        task.setTaskStatus(TaskStatus.IN_PROGRESS);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, task.getTaskStatus());

        task.setTaskStatus(TaskStatus.DONE);

        Assertions.assertEquals(TaskStatus.DONE, task.getTaskStatus());
    } /////

    @Test
    public void statusEpicTaskWithEmptySubtaskList() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);
        int epicID = epic.getTaskId();

        Assertions.assertEquals(TaskStatus.NEW, testTaskManager.showEpicById(epicID).getTaskStatus());
    } /////

    @Test
    public void statusEpicTaskWithAllSubtaskStatusIsNew() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);
        int epicID = epic.getTaskId();

        Subtask subtask = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask);

        Subtask subtask2 = new Subtask("Name2", "Description2", 1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask2);

        Assertions.assertEquals(TaskStatus.NEW, testTaskManager.showEpicById(epicID).getTaskStatus()
                , "Статус задачи неверный");
    } /////

    @Test
    public void statusEpicTaskWithAllSubtaskStatusIsDone() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);
        int epicID = epic.getTaskId();

        Subtask subtask = new Subtask("Name", "Description", 1, TaskStatus.DONE);
        testTaskManager.newSubtask(subtask);

        Subtask subtask2 = new Subtask("Name2", "Description2", 1, TaskStatus.DONE);
        testTaskManager.newSubtask(subtask2);

        Assertions.assertEquals(TaskStatus.DONE, testTaskManager.showEpicById(epicID).getTaskStatus()
                , "Статус задачи неверный");
    } /////

    @Test
    public void statusEpicTaskWithSubtaskStatusIsDoneAndNew() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);
        int epicID = epic.getTaskId();

        Subtask subtask = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        testTaskManager.newSubtask(subtask);

        Subtask subtask2 = new Subtask("Name2", "Description2", 1, TaskStatus.DONE);
        testTaskManager.newSubtask(subtask2);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, testTaskManager.showEpicById(epicID).getTaskStatus()
                , "Статус задачи неверный");
    } /////

    @Test
    public void statusEpicTaskWithAllSubtaskStatusInProgress() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);
        int epicID = epic.getTaskId();

        Subtask subtask = new Subtask("Name", "Description", 1
                , TaskStatus.IN_PROGRESS);
        testTaskManager.newSubtask(subtask);

        Subtask subtask2 = new Subtask("Name2", "Description2", 1
                , TaskStatus.IN_PROGRESS);
        testTaskManager.newSubtask(subtask2);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, testTaskManager.showEpicById(epicID).getTaskStatus()
                , "Статус задачи неверный");
    } /////

    @Test
    public void subtaskStatus() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description", 1, TaskStatus.NEW);
        testTaskManager.newTask(subtask);

        Assertions.assertEquals(TaskStatus.NEW, subtask.getTaskStatus());

        subtask.setTaskStatus(TaskStatus.IN_PROGRESS);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, subtask.getTaskStatus());

        subtask.setTaskStatus(TaskStatus.DONE);

        Assertions.assertEquals(TaskStatus.DONE, subtask.getTaskStatus());
    } /////

    @Test
    public void containsEpicWithNewSubtask() {
        Epic epic = new Epic("Name", "Description", TaskStatus.NEW);
        testTaskManager.newEpic(epic);

        Epic epic2 = new Epic("Name2", "Description2", TaskStatus.NEW);
        testTaskManager.newEpic(epic);

        Subtask subtask = new Subtask("Name", "Description", 1
                , TaskStatus.IN_PROGRESS);
        testTaskManager.newSubtask(subtask);
        int subtaskId = subtask.getTaskId();

        Subtask subtask2 = new Subtask("Name2", "Description2", 1
                , TaskStatus.IN_PROGRESS);
        testTaskManager.newSubtask(subtask2);
        int subtaskId2 = subtask2.getTaskId();

        Subtask idSubtask = testTaskManager.showSubtaskById(subtaskId);
        Subtask idSubtask2 = testTaskManager.showSubtaskById(subtaskId2);
        Assertions.assertEquals(epic ,testTaskManager.showEpicById(idSubtask.getSubtaskEpicId())
                , "Эпик не найден, проверьте создание эпика");
        Assertions.assertEquals(epic ,testTaskManager.showEpicById(idSubtask2.getSubtaskEpicId())
                , "Эпик не найден, проверьте создание эпика");

        Assertions.assertNotEquals(epic2 ,testTaskManager.showEpicById(idSubtask.getSubtaskEpicId())
                , "Id эпика, к которому относится подзадача - неверный");
        Assertions.assertNotEquals(epic2 ,testTaskManager.showEpicById(idSubtask2.getSubtaskEpicId())
                , "Id эпика, к которому относится подзадача - неверный");
    } /////

    @Test
    public void addTaskToHistory() {
        Task task = new Task("Name", "Description", TaskStatus.NEW);
        testTaskManager.newTask(task);

        final Task savedTask = testTaskManager.showTaskById(task.getTaskId());
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
    } /////

    @Test
    public void deleteInHistoryFirstTask() {
        Task task1 = new Task("Name1", "Description1", TaskStatus.NEW);
        testTaskManager.newTask(task1);

        int taskId = task1.getTaskId();

        Task task2 = new Task("Name2", "Description2", TaskStatus.NEW);
        testTaskManager.newTask(task2);

        Task task3 = new Task("Name3", "Description3", TaskStatus.NEW);
        testTaskManager.newTask(task3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(taskId);

        List<Task> history = historyManager.getHistory();

        Assertions.assertEquals(task2, history.get(0), "Порядок истории должен быть -> task2 -> task3");
        Assertions.assertEquals(task3, history.get(1), "Порядок истории должен быть -> task2 -> task3");
    } /////

    @Test
    public void deleteInHistoryMiddleTask() {
        Task task1 = new Task("Name1", "Description1", TaskStatus.NEW);
        testTaskManager.newTask(task1);

        Task task2 = new Task("Name2", "Description2", TaskStatus.NEW);
        testTaskManager.newTask(task2);

        int taskId2 = task2.getTaskId();

        Task task3 = new Task("Name3", "Description3", TaskStatus.NEW);
        testTaskManager.newTask(task3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(taskId2);

        List<Task> history = historyManager.getHistory();

        Assertions.assertEquals(task1, history.get(0), "Порядок истории должен быть -> task1 -> task3");
        Assertions.assertEquals(task3, history.get(1), "Порядок истории должен быть -> task1 -> task3");
    } /////

    @Test
    public void deleteInHistoryLastTask() {
        Task task1 = new Task("Name1", "Description1", TaskStatus.NEW);
        testTaskManager.newTask(task1);

        Task task2 = new Task("Name2", "Description2", TaskStatus.NEW);
        testTaskManager.newTask(task2);

        Task task3 = new Task("Name3", "Description3", TaskStatus.NEW);
        testTaskManager.newTask(task3);

        int taskId3 = task3.getTaskId();

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(taskId3);

        List<Task> history = historyManager.getHistory();

        Assertions.assertEquals(task1, history.get(0), "Порядок истории должен быть -> task1 -> task2");
        Assertions.assertEquals(task2, history.get(1), "Порядок истории должен быть -> task1 -> task2");
    } /////

    @Test
    public void emptyHistory() {
        Assertions.assertEquals( 0, historyManager.getHistory().size(), "История должна быть пустой");
    } /////

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
    } /////
}
