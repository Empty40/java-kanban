package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import interfaces.TaskManager;
import managers.FileBackedTasksManager;
import models.Epic;
import models.Subtask;
import models.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static FileBackedTasksManager getFileBackedTasksManager() {
        return fileBackedTasksManager;
    }

    private static FileBackedTasksManager fileBackedTasksManager;

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        fileBackedTasksManager = new FileBackedTasksManager();

        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks/task", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/epic", new EpicHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/subtask", new SubtaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/history", new HistoryHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks", new HistoryHandler(fileBackedTasksManager));
        httpServer.start(); // запускаем сервер
    }

    public HttpTaskServer() throws IOException {
        HttpServer httpServer = HttpServer.create();
        fileBackedTasksManager = new FileBackedTasksManager();

        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks/task", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/epic", new EpicHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/subtask", new SubtaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/history", new HistoryHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks", new HistoryHandler(fileBackedTasksManager));
        httpServer.start(); // запускаем сервер
    }

    static class TaskHandler implements HttpHandler {

        Gson gson = new Gson();

        TaskManager taskManager;

        public TaskHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка GET /tasks/task запроса от клиента.");
            String method = httpExchange.getRequestMethod();

            System.out.println(method);
            String jsonString = null;

            System.out.println(httpExchange.getRequestURI().toString());

            switch (method) {
                case "GET":
                    if (httpExchange.getRequestURI().toString().contains("?id=")) {
                        String parseId = httpExchange.getRequestURI().getQuery();
                        int symbol = parseId.indexOf("=");
                        int id = Integer.parseInt(parseId.substring(symbol + 1));
                        jsonString = gson.toJson(taskManager.showTaskById(id));
                        if (!jsonString.equals("[]")) {
                            httpExchange.sendResponseHeaders(200, 0);
                        } else if (id > 0) {
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Список задач пустой";
                        }
                    } else {
                        jsonString = gson.toJson(taskManager.showAllTask());
                        if (!jsonString.equals("[]")) {
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Список задач пустой";
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Task taskFromJson = gson.fromJson(body, Task.class);
                    System.out.println(taskFromJson);
                    if (taskFromJson.getTaskId() == 0) {
                        taskManager.newTask(taskFromJson);
                        if (taskManager.showTaskById(taskFromJson.getTaskId()).equals(taskFromJson)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            jsonString = "Задача успешно добавлена";
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Задача не добавлена";
                        }
                    } else {
                        taskManager.updateTask(taskFromJson);
                        if (taskManager.showTaskById(taskFromJson.getTaskId()).equals(taskFromJson)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            jsonString = "Задача успешно обновлена";
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Возникла ошибка при обновлении задачи";
                        }
                    }
                    System.out.println("Тело запроса:\n" + body);
                    System.out.println(taskFromJson);
                    System.out.println(taskFromJson.getTaskId());
                    break;
                case "DELETE":
                    if (httpExchange.getRequestURI().toString().contains("?id=")) {
                        String parseId = httpExchange.getRequestURI().getQuery();
                        int sumbol = parseId.indexOf("=");
                        int id = Integer.parseInt(parseId.substring(sumbol + 1));
                        taskManager.deleteTaskId(id);
                        if (taskManager.showTaskById(id) == null) {
                            httpExchange.sendResponseHeaders(200, 0);
                            jsonString = "Задача успешно удалена по id";
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Возникла ошибка при обработке запроса";
                        }
                    } else {
                        taskManager.deleteAllTask();
                        if (taskManager.showAllTask().isEmpty()) {
                            httpExchange.sendResponseHeaders(200, 0);
                            jsonString = "Все задачи успешно удалены";
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Возникла ошибка при обработке запроса";
                        }
                    }
                    break;
                default:

            }

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(jsonString.getBytes());
            }
        }
    }

    static class EpicHandler implements HttpHandler {
        Gson gson = new Gson();

        TaskManager taskManager;

        public EpicHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка GET /tasks/epic запроса от клиента.");
            String method = httpExchange.getRequestMethod();

            String jsonString = null;

            switch (method) {
                case "GET":
                    if (httpExchange.getRequestURI().toString().contains("?id=")) {
                        String parseId = httpExchange.getRequestURI().getQuery();
                        int sumbol = parseId.indexOf("=");
                        int id = Integer.parseInt(parseId.substring(sumbol + 1));
                        System.out.println(gson.toJson(taskManager.showEpicById(id)));
                        jsonString = gson.toJson(taskManager.showEpicById(id));
                        if (!jsonString.equals("[]")) {
                            httpExchange.sendResponseHeaders(200, 0);
                        } else if (id > 0) {
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Список задач пустой";
                        }
                    } else {
                        jsonString = gson.toJson(taskManager.showAllEpic());
                        if (!jsonString.equals("[]")) {
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Список задач пустой";
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Epic taskFromJson = gson.fromJson(body, Epic.class);
                    if (taskFromJson.getTaskId() == 0) {
                        taskManager.newEpic(taskFromJson);
                        if (taskManager.showEpicById(taskFromJson.getTaskId()).equals(taskFromJson)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            jsonString = "Эпик успешно добавлен";
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Эпик не добавлен";
                        }
                    } else {
                        taskManager.updateEpic(taskFromJson);
                        if (taskManager.showEpicById(taskFromJson.getTaskId()).equals(taskFromJson)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            jsonString = "Эпик успешно обновлён";
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Возникла ошибка при обработке запроса";
                        }
                    }
                    System.out.println("Тело запроса:\n" + body);
                    System.out.println(taskFromJson);
                    System.out.println(taskFromJson.getTaskId());
                    break;
                case "DELETE":
                    if (httpExchange.getRequestURI().toString().contains("?id=")) {
                        String parseId = httpExchange.getRequestURI().getQuery();
                        int sumbol = parseId.indexOf("=");
                        int id = Integer.parseInt(parseId.substring(sumbol + 1));
                        taskManager.deleteEpicId(id);
                        if (taskManager.showTaskById(id) == null) {
                            httpExchange.sendResponseHeaders(200, 0);
                            jsonString = "Эпик удалён по id";
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Возникла ошибка при обработке запроса";
                        }
                    } else {
                        taskManager.deleteAllEpic();
                        if (taskManager.showAllEpic().isEmpty()) {
                            httpExchange.sendResponseHeaders(200, 0);
                            jsonString = "Все эпики удалены";
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Возникла ошибка при обработке запроса";
                        }
                    }
                    break;
                default:

            }

            try (
                    OutputStream os = httpExchange.getResponseBody()) {
                os.write(jsonString.getBytes());
            }
        }
    }

    static class SubtaskHandler implements HttpHandler {
        Gson gson = new Gson();

        TaskManager taskManager;

        public SubtaskHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка GET /tasks/subtask запроса от клиента.");
            String method = httpExchange.getRequestMethod();

            String jsonString = null;

            switch (method) {
                case "GET":
                    if (httpExchange.getRequestURI().toString().contains("?id=")) {
                        String parseId = httpExchange.getRequestURI().getQuery();
                        int sumbol = parseId.indexOf("=");
                        int id = Integer.parseInt(parseId.substring(sumbol + 1));
                        jsonString = gson.toJson(taskManager.showSubtaskById(id));
                        if (!jsonString.equals("[]")) {
                            httpExchange.sendResponseHeaders(200, 0);
                        } else if (id > 0) {
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Список задач пустой";
                        }
                    } else {
                        jsonString = gson.toJson(taskManager.showAllSubtask());
                        if (!jsonString.equals("[]")) {
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Список задач пустой";
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Subtask taskFromJson = gson.fromJson(body, Subtask.class);
                    if (taskFromJson.getTaskId() == 0) {
                        taskManager.newSubtask(taskFromJson);
                        if (taskManager.showSubtaskById(taskFromJson.getTaskId()).equals(taskFromJson)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            jsonString = "Сабтаск успешно добавлен";
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Сабтаск не добавлен";
                        }
                    } else {
                        taskManager.updateSubtask(taskFromJson);
                        if (taskManager.showSubtaskById(taskFromJson.getTaskId()).equals(taskFromJson)) {
                            httpExchange.sendResponseHeaders(200, 0);
                            jsonString = "Сабтаск успешно обновлён";
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Возникла ошибка при обработке запроса";
                        }
                    }
                    break;
                case "DELETE":
                    if (httpExchange.getRequestURI().toString().contains("?id=")) {
                        String parseId = httpExchange.getRequestURI().getQuery();
                        int sumbol = parseId.indexOf("=");
                        int id = Integer.parseInt(parseId.substring(sumbol + 1));
                        taskManager.deleteSubtaskId(id);
                        if (taskManager.showTaskById(id) == null) {
                            httpExchange.sendResponseHeaders(200, 0);
                            jsonString = "Сабтаск успешно удалён по id";
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Возникла ошибка при обработке запроса";
                        }
                    } else {
                        taskManager.deleteAllSubtask();
                        if (taskManager.showAllSubtask().isEmpty()) {
                            httpExchange.sendResponseHeaders(200, 0);
                            jsonString = "Все сабтаски успешно удалены";
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            jsonString = "Возникла ошибка при обработке запроса";
                        }
                    }
                    break;
                default:
            }

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(jsonString.getBytes());
            }
        }
    }

    static class HistoryHandler implements HttpHandler {

        TaskManager taskManager;

        public HistoryHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка GET /tasks запроса от клиента.");
        }
    }
}