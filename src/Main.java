import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();

        while (true) {
            printMenu();
            int command = scanner.nextInt();

            if (command == 1) {
                System.out.println("Что хотите отобразить?");
                System.out.println("1 - Задачи, 2 - Эпики, 3 - Подзадачи");
                int taskType = scanner.nextInt();

                if (taskType == 1) {
                    manager.showAllTask();
                } else if (taskType == 2) {
                    manager.showAllEpic();
                } else if (taskType == 3) {
                    manager.showAllSubtask();
                } else {
                    System.out.println("Вводите корректные значения");
                }

            } else if (command == 2) {
                System.out.println("Что хотите удалить?");
                System.out.println("1 - Задачи, 2 - Эпики, 3 - Подзадачи");
                int taskType = scanner.nextInt();

                if (taskType == 1) {
                    manager.deleteAllTask();
                } else if (taskType == 2) {
                    manager.deleteAllEpic();
                } else if (taskType == 3) {
                    manager.deleteAllSubtask();
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
                    System.out.println(manager.showTaskId(id));
                } else if (taskType == 2) {
                    manager.showEpicId(id);
                } else if (taskType == 3) {
                    manager.showSubtaskId(id);
                } else {
                    System.out.println("Вводите корректные значения");
                }

            } else if (command == 4) {
                String hz = scanner.nextLine();
                System.out.println("Введите название задачи");
                String taskName = scanner.nextLine();

                System.out.println("Какой это тип задачи?");
                System.out.println("1 - Задача");
                System.out.println("2 - Эпик");
                System.out.println("3 - Подзадача");
                int taskType = scanner.nextInt();

                String pochemyTak = scanner.nextLine();
                System.out.println("Опишите вашу задачу");
                String taskDescription = scanner.nextLine();

                if (taskType == 1) {
                    Task task = new Task(taskName, taskDescription, "NEW");
                    manager.newTask(task);
                } else if (taskType == 2) {
                    Epic epic = new Epic(taskName, taskDescription, "NEW");
                    manager.newEpic(epic);
                } else {
                    System.out.println("В рамках какого эпика выполняется задача?");
                    int subtaskEpicId = scanner.nextInt();
                    Subtask subtask = new Subtask(taskName, taskDescription, "NEW", subtaskEpicId);
                    manager.newSubtask(subtask);
                    Epic test = manager.epicList.get(subtaskEpicId);
                    test.subtaskId.add(manager.taskId);
                    System.out.println("Задаче присвоен идентификатор " + manager.taskId);
                    manager.taskId++;
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
                    Task task = manager.tasks.get(id);
                    task.taskName = taskName;
                    task.taskDescription = taskDescription;
                    task.taskStatus = taskStatus;
                    manager.updateTask(task);

                } else if (taskType == 2) {
                    Epic epic = manager.epicList.get(id);
                    epic.taskName = taskName;
                    epic.taskDescription = taskDescription;
                    epic.taskStatus = taskStatus;
                    manager.updateEpic(epic);

                } else if (taskType == 3) {
                    Subtask subtask = manager.subtaskList.get(id);
                    subtask.taskName = taskName;
                    subtask.taskDescription = taskDescription;
                    subtask.taskStatus = taskStatus;
                    manager.updateSubtask(subtask);

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
                    manager.deleteTaskId(id);
                } else if (taskType == 2) {
                    manager.deleteEpicId(id);
                } else if (taskType == 3) {
                    manager.deleteSubtaskId(id);
                } else {
                    System.out.println("Вводите корректные значения");
                }

            } else if (command == 7) {
                String daChtoTakoeTo = scanner.nextLine(); // Не понимаю почему в этом месте сканер перепрыгивает на
                System.out.println("Какой Эпик Вас интересует? Введите его название"); //следующую строку, или дополни -
                String epicName = scanner.nextLine(); // - тельно требуется нажать Enter

                manager.showAllSubtaskInEpic(epicName);
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
                    System.out.println("0 - Закрыть менеджер задач");
                    System.out.println("Введите команду");
                }
        }
