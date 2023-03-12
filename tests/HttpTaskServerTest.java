import com.google.gson.Gson;
import interfaces.TaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import managers.FileBackedTasksManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {

    static HttpTaskServer httpTaskServer;

    @BeforeAll
    public static void beforeAll() {
        try {
            httpTaskServer = new HttpTaskServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String url = "http://localhost:8080";

    File file;

    FileBackedTasksManager taskManager;

    TaskManager fileBackTaskManager;

    @BeforeEach
    public void beforeEach() {
        file = new File("saveTasks.csv");
        fileBackTaskManager = new FileBackedTasksManager(file);
        taskManager = HttpTaskServer.getFileBackedTasksManager();
        taskManager.resetTaskId();
        taskManager.deleteAllTask();
        taskManager.deleteAllEpic();
        taskManager.deleteAllSubtask();
    }

    @Test
    public void TaskHandlerGetAllTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task task = new Task("NameTest", "DescriptionTest", TaskStatus.NEW);
        task.setDuration(30);
        LocalDateTime startTime = LocalDateTime.of(2023, 3, 10, 12, 30);
        task.setStartTime(startTime);
        taskManager.newTask(task);

        URI uri = URI.create(url + "/tasks/task");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void EpicHandlerGetAllTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("NameTest", "DescriptionTest", TaskStatus.NEW);
        taskManager.newEpic(epic);

        URI uri = URI.create(url + "/tasks/epic");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void SubtaskHandlerGetAllTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("NameTest", "DescriptionTest", TaskStatus.NEW);
        taskManager.newEpic(epic);

        Subtask subtask1 = new Subtask("NameTest", "DescriptionTest", 1, TaskStatus.NEW);
        taskManager.newSubtask(subtask1);

        URI uri = URI.create(url + "/tasks/subtask");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println("123");
    }

    @Test
    public void TaskHandlerGetIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task task = new Task("NameTest", "DescriptionTest", TaskStatus.NEW);
        task.setDuration(30);
        LocalDateTime startTime = LocalDateTime.of(2023, 3, 10, 12, 30);
        task.setStartTime(startTime);
        taskManager.newTask(task);

        URI uri = URI.create(url + "/tasks/task/?id=1");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void EpicHandlerGetIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("NameTest", "DescriptionTest", TaskStatus.NEW);
        taskManager.newEpic(epic);

        URI uri = URI.create(url + "/tasks/epic/?id=1");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void SubtaskHandlerGetIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("NameTest", "DescriptionTest", TaskStatus.NEW);
        taskManager.newEpic(epic);

        Subtask subtask1 = new Subtask("NameTest", "DescriptionTest", 1, TaskStatus.NEW);
        taskManager.newSubtask(subtask1);

        URI uri = URI.create(url + "/tasks/subtask/?id=2");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void TaskHandlerUpdateTaskTest() throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpClient client = HttpClient.newHttpClient();

        Task task = new Task("TaskTest", "DescriptionTest", TaskStatus.NEW);
        task.setDuration(30);
        LocalDateTime startTime = LocalDateTime.of(2023, 3, 10, 12, 40);
        task.setStartTime(startTime);
        taskManager.newTask(task);

        String jsonTask = gson.toJson(task);

        HttpRequest.BodyPublisher bodyPublishers = HttpRequest.BodyPublishers.ofString(jsonTask);

        URI uri = URI.create(url + "/tasks/task");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyPublishers)
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        /*serialize
        {"taskId":1,"taskName":"NameTest","taskDescription":"DescriptionTest","taskStatus":"NEW","duration":30,
                "startTime":{"date":{"year":2023,"month":3,"day":10},"time":{"hour":12,"minute":30,"second":0,"nano":0}}}*/
    }

    @Test
    public void EpicHandlerUpdateEpicTest() throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("NameTest", "DescriptionTest", TaskStatus.NEW);
        taskManager.newEpic(epic);

        String jsonTask = gson.toJson(epic);

        System.out.println(jsonTask);

        HttpRequest.BodyPublisher bodyPublishers = HttpRequest.BodyPublishers.ofString(jsonTask);

        URI uri = URI.create(url + "/tasks/epic");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyPublishers)
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        /*serialize
        {"subtaskId":[2],"taskId":1,"taskName":"EpicTest"
                ,"taskDescription":"DescriptionTest","taskStatus":"NEW","duration":0}*/
    }

    @Test
    public void SubtaskHandlerUpdateSubtaskTest() throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("EpicTest", "DescriptionTest", TaskStatus.NEW);
        taskManager.newEpic(epic);

        Subtask subtask1 = new Subtask("SubtaskTest", "DescriptionTest", 1, TaskStatus.NEW);
        taskManager.newSubtask(subtask1);

        String jsonTask = gson.toJson(subtask1);

        String jsonEpic = gson.toJson(epic);

        System.out.println(jsonTask);
        System.out.println(jsonEpic);

        HttpRequest.BodyPublisher bodyPublishers = HttpRequest.BodyPublishers.ofString(jsonTask);

        URI uri = URI.create(url + "/tasks/subtask");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyPublishers)
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        /*serialize epic
        {"subtaskId":[2],"taskId":1,"taskName":"EpicTest"
                ,"taskDescription":"DescriptionTest","taskStatus":"NEW","duration":0}*/

        /*serialize subtask
        {"subtaskEpicId":1,"subtaskId":[],"taskId":2,"taskName":"SubtaskTest"
                ,"taskDescription":"DescriptionTest","taskStatus":"NEW","duration":0}*/
    }

    @Test
    public void TaskHandlerNewTaskTest() throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpClient client = HttpClient.newHttpClient();

        Task task = new Task("NameTest", "DescriptionTest", TaskStatus.NEW);
        task.setDuration(30);
        LocalDateTime startTime = LocalDateTime.of(2023, 3, 10, 12, 40);
        task.setStartTime(startTime);
        taskManager.newTask(task);

        task.setTaskId(0);

        String jsonTask = gson.toJson(task);

        HttpRequest.BodyPublisher bodyPublishers = HttpRequest.BodyPublishers.ofString(jsonTask);

        URI uri = URI.create(url + "/tasks/task");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyPublishers)
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        /*serialize
        {"taskId":1,"taskName":"NameTest","taskDescription":"DescriptionTest","taskStatus":"NEW","duration":30,
                "startTime":{"date":{"year":2023,"month":3,"day":10},"time":{"hour":12,"minute":30,"second":0,"nano":0}}}*/
    }

    @Test
    public void EpicHandlerNewEpicTest() throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("NameTest", "DescriptionTest", TaskStatus.NEW);
        taskManager.newEpic(epic);

        epic.setTaskId(0);

        String jsonTask = gson.toJson(epic);

        HttpRequest.BodyPublisher bodyPublishers = HttpRequest.BodyPublishers.ofString(jsonTask);

        URI uri = URI.create(url + "/tasks/epic");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyPublishers)
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void SubtaskHandlerNewSubtaskTest() throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("NameTest", "DescriptionTest", TaskStatus.NEW);
        taskManager.newEpic(epic);

        Subtask subtask = new Subtask("NameTest", "DescriptionTest", 1, TaskStatus.NEW);
        taskManager.newSubtask(subtask);

        subtask.setTaskId(0);

        String jsonTask = gson.toJson(subtask);

        HttpRequest.BodyPublisher bodyPublishers = HttpRequest.BodyPublishers.ofString(jsonTask);

        URI uri = URI.create(url + "/tasks/subtask");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyPublishers)
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void TaskHandleDeleteIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task task = new Task("NameTest", "DescriptionTest", TaskStatus.NEW);
        task.setDuration(30);
        LocalDateTime startTime = LocalDateTime.of(2023, 3, 10, 12, 30);
        task.setStartTime(startTime);
        taskManager.newTask(task);

        URI uri = URI.create(url + "/tasks/task/?id=1");

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void EpicHandlerDeleteIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("NameTest", "DescriptionTest", TaskStatus.NEW);
        taskManager.newEpic(epic);

        URI uri = URI.create(url + "/tasks/epic/?id=1");

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void SubtaskHandlerDeleteIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("NameTest", "DescriptionTest", TaskStatus.NEW);
        taskManager.newEpic(epic);

        Subtask subtask1 = new Subtask("NameTest", "DescriptionTest", 1, TaskStatus.NEW);
        taskManager.newSubtask(subtask1);

        URI uri = URI.create(url + "/tasks/subtask/?id=2");

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void TaskHandleDeleteAllTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task task = new Task("NameTest", "DescriptionTest", TaskStatus.NEW);
        task.setDuration(30);
        LocalDateTime startTime = LocalDateTime.of(2023, 3, 10, 12, 30);
        task.setStartTime(startTime);
        taskManager.newTask(task);

        URI uri = URI.create(url + "/tasks/task");

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void EpicHandlerDeleteAllTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("NameTest", "DescriptionTest", TaskStatus.NEW);
        taskManager.newEpic(epic);

        URI uri = URI.create(url + "/tasks/epic");

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void SubtaskHandlerDeleteAllTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic = new Epic("NameTest", "DescriptionTest", TaskStatus.NEW);
        taskManager.newEpic(epic);

        Subtask subtask1 = new Subtask("NameTest", "DescriptionTest", 1, TaskStatus.NEW);
        taskManager.newSubtask(subtask1);

        URI uri = URI.create(url + "/tasks/subtask");

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
}
