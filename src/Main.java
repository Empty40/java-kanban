import Interface.TaskManager;
import models.Epic;
import models.Managers;
import models.Subtask;
import models.Task;

import java.util.HashMap;
import java.util.Scanner;
public class Main {



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = Managers.getDefault();

        while (true) {
            printMenu();
            int command = scanner.nextInt();

            if (command == 1) {
                System.out.println("Что хотите отобразить?");
                System.out.println("1 - Задачи, 2 - Эпики, 3 - Подзадачи");
                int taskType = scanner.nextInt();

                if (taskType == 1) {
                    System.out.println(taskManager.showAllTask());
                } else if (taskType == 2) {
                    System.out.println(taskManager.showAllEpic());
                } else if (taskType == 3) {
                    System.out.println(taskManager.showAllSubtask());
                } else {
                    System.out.println("Вводите корректные значения");
                }

            } else if (command == 2) {
                System.out.println("Что хотите удалить?");
                System.out.println("1 - Задачи, 2 - Эпики, 3 - Подзадачи");
                int taskType = scanner.nextInt();

                if (taskType == 1) {
                    taskManager.deleteAllTask();
                } else if (taskType == 2) {
                    taskManager.deleteAllEpic();
                } else if (taskType == 3) {
                    taskManager.deleteAllSubtask();
                } else {
                    System.out.println("Вводите корректные значения");
                }

            } else if (command == 3) {
                System.out.println("Что хотите отобразить?");
                System.out.println("1 - Задачи, 2 - Эпики, 3 - Подзадачи");
                int taskType = scanner.nextInt();

                System.out.println("Введите идентификатор");
                int id = scanner.nextInt();

                if (taskType == 1) {
                    System.out.println(taskManager.showTaskById(id));
                } else if (taskType == 2) {
                    taskManager.showEpicById(id);
                } else if (taskType == 3) {
                    taskManager.showSubtaskById(id);
                } else {
                    System.out.println("Вводите корректные значения");
                }

            } else if (command == 4) {
                String test = scanner.nextLine();
                System.out.println("Введите название задачи");
                String taskName = scanner.nextLine();

                System.out.println("Какой это тип задачи?");
                System.out.println("1 - Задача");
                System.out.println("2 - Эпик");
                System.out.println("3 - Подзадача");
                int taskType = scanner.nextInt();

                String test1 = scanner.nextLine();
                System.out.println("Опишите вашу задачу");
                String taskDescription = scanner.nextLine();

                if (taskType == 1) {
                    Task task = new Task(taskName, taskDescription, "NEW");
                    taskManager.newTask(task);
                } else if (taskType == 2) {
                    Epic epic = new Epic(taskName, taskDescription, "NEW");
                    taskManager.newEpic(epic);
                } else {
                    System.out.println("В рамках какого эпика выполняется задача?");
                    int subtaskEpicId = scanner.nextInt();
                    HashMap<Integer, Epic> epicList = taskManager.getEpicList();
                    Subtask subtask = new Subtask(taskName, taskDescription, "NEW", subtaskEpicId);
                    if (!epicList.containsKey(subtask.getSubtaskEpicId())) {
                        System.out.println("Такого Эпика в списке нет!");
                    } else {
                        Epic subtaskIdInEpic = epicList.get(subtaskEpicId);
                        subtaskIdInEpic.setSubtaskId(taskManager.getTaskId());
                        taskManager.newSubtask(subtask);
                    }
                }

            } else if (command == 5) {
                System.out.println("Что будем обновлять?");
                System.out.println("1 - Задачу, 2 - Эпик, 3 - Подзадачу");
                int taskType = scanner.nextInt();

                String nePonimayu = scanner.nextLine();
                System.out.println("Введите название задачи");
                String taskName = scanner.nextLine();

                System.out.println("Опишите вашу задачу");
                String taskDescription = scanner.nextLine();

                System.out.println("Какой статус у задачи?");
                System.out.println("NEW");
                System.out.println("IN_PROGRESS");
                System.out.println("DONE");
                String taskStatus = scanner.nextLine();

                System.out.println("Введите идентификатор задачи");
                int id = scanner.nextInt();

                if (taskType == 1) {
                    HashMap<Integer, Task> tasksList = taskManager.getTasks();
                    if (!tasksList.containsKey(id)) {
                        System.out.println("Такой задачи нет в списке");
                    } else {
                        Task task = tasksList.get(id);
                        task.setTaskName(taskName);
                        task.setTaskDescription(taskDescription);
                        task.setTaskStatus(taskStatus);
                        taskManager.updateTask(task);
                    }

                } else if (taskType == 2) {
                    HashMap<Integer, Epic> epicList = taskManager.getEpicList();
                    if (!epicList.containsKey(id)) {
                        System.out.println("Такого эпика нет в списке");
                    } else {
                        Epic epic = epicList.get(id);
                        epic.setTaskName(taskName);
                        epic.setTaskDescription(taskDescription);
                        epic.setTaskStatus(taskStatus);
                        taskManager.updateEpic(epic);
                    }

                } else if (taskType == 3) {
                    HashMap<Integer, Subtask> subtaskList = taskManager.getSubtaskList();
                    if (!subtaskList.containsKey(id)) {
                        System.out.println("Такой подзадачи нет в списке");
                    } else {
                        Subtask subtask = subtaskList.get(id);
                        subtask.setTaskName(taskName);
                        subtask.setTaskDescription(taskDescription);
                        subtask.setTaskStatus(taskStatus);
                        taskManager.updateSubtask(subtask);
                    }

                } else {
                    System.out.println("Вводите корректные значения");
                }

            } else if (command == 6) {
                System.out.println("Что будем удалять?");
                System.out.println("1 - Задачу, 2 - Эпик, 3 - Подзадачу");
                int taskType = scanner.nextInt();

                System.out.println("Введите идентификатор задачи");
                int id = scanner.nextInt();

                if (taskType == 1) {
                    taskManager.deleteTaskId(id);
                } else if (taskType == 2) {
                    taskManager.deleteEpicId(id);
                } else if (taskType == 3) {
                    taskManager.deleteSubtaskId(id);
                } else {
                    System.out.println("Вводите корректные значения");
                }

            } else if (command == 7) {
                String daChtoTakoeTo = scanner.nextLine(); // Не понимаю почему в этом месте сканер перепрыгивает на
                System.out.println("Какой Эпик Вас интересует? Введите его айди"); //следующую строку, или дополни -
                int epicName = scanner.nextInt(); // - тельно требуется нажать Enter

                System.out.println(taskManager.showAllSubtaskInEpic(epicName));
            } else if (command == 8) {
                taskManager.getHistory();
            } else if (command == 0) {
                break;
            } else {
                System.out.println("Введите корректное число");
            }
        }
    }

                public static void printMenu() {
                    System.out.println("1 - Показать все Задачи/Эпики/Подзадачи ");
                    System.out.println("2 - Удалить все Задачи/Эпики/Подзадачи");
                    System.out.println("3 - Показать Задачи/Эпики/Подзадачи по идентефикатору");
                    System.out.println("4 - Создать Задачу/Эпик/Подзадачу");
                    System.out.println("5 - Обновить Задачу/Эпик/Подзадачу");
                    System.out.println("6 - Удалить Задачи/Эпики/Подзадачи по идентефикатору");
                    System.out.println("7 - Получение списка всех подзадач определённого эпика");
                    System.out.println("8 - Посмотреть историю запросов");
                    System.out.println("0 - Закрыть менеджер задач");
                    System.out.println("Введите команду");
                }
        }
