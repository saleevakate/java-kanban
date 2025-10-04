package server;

import manager.Managers;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

class HttpTaskServerTest {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static HttpTaskServer server;

    @BeforeEach
    void startServer() throws IOException {
        server = new HttpTaskServer(Managers.getDefaultManager());
        server.start();
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }

    @Test
    void testTaskOperations() throws IOException, InterruptedException {
        Thread.sleep(100);
        Task testTask = new Task(0, "Выбросить мусор", "Весь", Duration.ofMinutes(10),
                LocalDateTime.of(2000, 1, 1, 1, 0));
        String jsonTask = server.getGson().toJson(testTask);

        Task testTask1 = new Task(1, "Помыть окна", "Все", Duration.ofMinutes(10),
                LocalDateTime.of(2000, 2, 2, 2, 0));
        String jsonTask1 = server.getGson().toJson(testTask1);

        //POST /tasks
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                 .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        //GET /tasks
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response1.statusCode());

        //POST /tasks (updateTask)
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks?id=1"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask1))
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response2.statusCode());

        //GET /tasks?id=1
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks?id=1"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response3.statusCode());

        //DELETE /tasks?id=1
        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks?id=1"))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response4.statusCode());
    }

    @Test
    void testEpicOperations() throws IOException, InterruptedException {
        Thread.sleep(100);
        Epic testEpic = new Epic(0, "Выбросить мусор", "Весь", Duration.ofMinutes(10),
                LocalDateTime.of(2000, 1, 1, 1, 0));
        String jsonEpic = server.getGson().toJson(testEpic);

        Epic testEpic1 = new Epic(1, "Помыть окна", "Все", Duration.ofMinutes(10),
                LocalDateTime.of(2000, 2, 2, 2, 0));
        String jsonEpic1 = server.getGson().toJson(testEpic1);

        // POST /epics
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        // GET /epics
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response1.statusCode());

        // POST /epics (updateEpic)
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics?id=1"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic1))
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response2.statusCode());

        // GET /epics?id=1
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics?id=1"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response3.statusCode());

        // DELETE /epics?id=1
        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics?id=1"))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response4.statusCode());
    }

    @Test
    void testSubtaskOperations() throws IOException, InterruptedException {
        Thread.sleep(100);
        Epic testEpic = new Epic(0, "Выбросить мусор", "Весь", Duration.ofMinutes(10),
                LocalDateTime.of(2000, 1, 1, 1, 0));
        String jsonEpic = server.getGson().toJson(testEpic);

        Subtask testSubtask = new Subtask(0, "Выбросить мусор", "Весь", 1, Duration.ofMinutes(10),
                LocalDateTime.of(2000, 2, 2, 2, 0));
        String jsonSubtask = server.getGson().toJson(testSubtask);

        Subtask testSubtask1 = new Subtask(2, "Помыть окна", "Все", 1, Duration.ofMinutes(10),
                LocalDateTime.of(2000, 2, 2, 2, 0));
        String jsonSubtask1 = server.getGson().toJson(testSubtask1);

        //создание эпика для сабтаска
        HttpRequest requestEpic = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .build();
        HttpResponse<String> responseEpic = client.send(requestEpic, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseEpic.statusCode());

        // POST /subtasks
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        // GET /subtasks
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response1.statusCode());

        // POST /subtasks (updateSubtask)
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks?id=2"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask1))
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response2.statusCode());

        // GET /subtasks?id=1
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks?id=2"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response3.statusCode());

        // DELETE /subtasks?id=1
        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks?id=2"))
                .header("Content-String", "application/json")
                .DELETE()
                .build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response4.statusCode());
    }
}