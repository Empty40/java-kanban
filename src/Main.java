import managers.FileBackedTasksManager;
import models.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        File file = new File("saveTasks.csv");
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileBackedTasksManager taskManagerData = FileBackedTasksManager.loadFromFile(file);
        //FileBackedTasksManager taskManagerData = new FileBackedTasksManager(file);


        while (true) {
            printMenu();
            int command = scanner.nextInt();

            if (command == 1) {
                System.out.println("Что хотите отобразить?");
                System.out.println("1 - Задачи, 2 - Эпики, 3 - Подзадачи");
                int taskType = scanner.nextInt();

                if (taskType == 1) {
                    System.out.println(taskManagerData.showAllTask());
                } else if (taskType == 2) {
                    System.out.println(taskManagerData.showAllEpic());
                } else if (taskType == 3) {
                    System.out.println(taskManagerData.showAllSubtask());
                } else {
                    System.out.println("Вводите корректные значения");
                }

            } else if (command == 2) {
                System.out.println("Что хотите удалить?");
                System.out.println("1 - Задачи, 2 - Эпики, 3 - Подзадачи");
                int taskType = scanner.nextInt();

                if (taskType == 1) {
                    taskManagerData.deleteAllTask();
                } else if (taskType == 2) {
                    taskManagerData.deleteAllEpic();
                } else if (taskType == 3) {
                    taskManagerData.deleteAllSubtask();
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
                    System.out.println(taskManagerData.showTaskById(id));
                } else if (taskType == 2) {
                    System.out.println(taskManagerData.showEpicById(id));
                } else if (taskType == 3) {
                    System.out.println(taskManagerData.showSubtaskById(id));
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
                    Task task = new Task(taskName, taskDescription, TaskStatusAndType.NEW);
                    //taskManagerData.newTask(task);
                    taskManagerData.newTask(task);
                } else if (taskType == 2) {
                    Epic epic = new Epic(taskName, taskDescription, TaskStatusAndType.NEW);
                    taskManagerData.newEpic(epic);
                } else {
                    System.out.println("В рамках какого эпика выполняется задача?");
                    int subtaskEpicId = scanner.nextInt();
                    HashMap<Integer, Epic> epicList = taskManagerData.getEpicList();
                    Subtask subtask = new Subtask(taskName, taskDescription, subtaskEpicId, TaskStatusAndType.NEW);
                    if (!epicList.containsKey(subtask.getSubtaskEpicId())) {
                        System.out.println("Такого Эпика в списке нет!");
                    } else {
                        taskManagerData.newSubtask(subtask);
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
                System.out.println("1 - NEW");
                System.out.println("2 - IN_PROGRESS");
                System.out.println("3 - DONE");
                int taskStatus = scanner.nextInt();

                System.out.println("Введите идентификатор задачи");
                int id = scanner.nextInt();

                if (taskType == 1) {
                    HashMap<Integer, Task> tasksList = taskManagerData.getTasks();
                    if (!tasksList.containsKey(id)) {
                        System.out.println("Такой задачи нет в списке");
                    } else {
                        Task task = tasksList.get(id);
                        task.setTaskName(taskName);
                        task.setTaskDescription(taskDescription);
                        if (taskStatus == 1) {
                            task.setTaskStatus(TaskStatusAndType.NEW);
                        } else if (taskStatus == 2) {
                            task.setTaskStatus(TaskStatusAndType.IN_PROGRESS);
                        } else {
                            task.setTaskStatus(TaskStatusAndType.DONE);
                        }
                        taskManagerData.updateTask(task);
                    }

                } else if (taskType == 2) {
                    HashMap<Integer, Epic> epicList = taskManagerData.getEpicList();
                    if (!epicList.containsKey(id)) {
                        System.out.println("Такого эпика нет в списке");
                    } else {
                        Epic epic = epicList.get(id);
                        epic.setTaskName(taskName);
                        epic.setTaskDescription(taskDescription);
                        if (taskStatus == 1) {
                            epic.setTaskStatus(TaskStatusAndType.NEW);
                        } else if (taskStatus == 2) {
                            epic.setTaskStatus(TaskStatusAndType.IN_PROGRESS);
                        } else {
                            epic.setTaskStatus(TaskStatusAndType.DONE);
                        }
                        taskManagerData.updateEpic(epic);
                    }

                } else if (taskType == 3) {
                    HashMap<Integer, Subtask> subtaskList = taskManagerData.getSubtaskList();
                    if (!subtaskList.containsKey(id)) {
                        System.out.println("Такой подзадачи нет в списке");
                    } else {
                        Subtask subtask = subtaskList.get(id);
                        subtask.setTaskName(taskName);
                        subtask.setTaskDescription(taskDescription);
                        if (taskStatus == 1) {
                            subtask.setTaskStatus(TaskStatusAndType.NEW);
                        } else if (taskStatus == 2) {
                            subtask.setTaskStatus(TaskStatusAndType.IN_PROGRESS);
                        } else {
                            subtask.setTaskStatus(TaskStatusAndType.DONE);
                        }
                        taskManagerData.updateSubtask(subtask);
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
                    taskManagerData.deleteTaskId(id);
                } else if (taskType == 2) {
                    taskManagerData.deleteEpicId(id);
                } else if (taskType == 3) {
                    taskManagerData.deleteSubtaskId(id);
                } else {
                    System.out.println("Вводите корректные значения");
                }

            } else if (command == 7) {
                String daChtoTakoeTo = scanner.nextLine(); // Не понимаю почему в этом месте сканер перепрыгивает на
                System.out.println("Какой Эпик Вас интересует? Введите его айди"); //следующую строку, или дополни -
                int epicName = scanner.nextInt(); // - тельно требуется нажать Enter

                System.out.println(taskManagerData.showAllSubtaskInEpic(epicName));
            } else if (command == 8) {
                System.out.println(taskManagerData.getHistory());
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
