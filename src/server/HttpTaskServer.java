package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static FileBackedTaskManager taskManager;
    private final Gson gson;
    private HttpServer server;

    public HttpTaskServer(FileBackedTaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder().registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
    }

    public Gson getGson() {
        return gson;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/tasks", new TaskHandler(taskManager, gson));
        //server.createContext("/epics", new EpicsHandler(taskManager, gson));
        //server.createContext("/subtasks", new SubtasksHandler(taskManager, gson));
        //server.createContext("/history", new HistoryHandler(taskManager, gson));
        //server.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));

        server.start();
        System.out.println("HTTP Task Server запущен на порту " + PORT);
    }

    public void stop() {
        if (server != null) {
            server.stop(1);
            System.out.println("HTTP Task Server остановлен");
        }
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer(Managers.getDefaultManager());
        server.start();
        try {

            Task testTask = new Task(0, "Выбросить мусор", "Весь",
                    Duration.ofMinutes(10), LocalDateTime.of(2000, 1, 1, 1, 0));
            String jsonTask = server.getGson().toJson(testTask);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/tasks"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                    .build();
            Task testTask1 = new Task(1, "Помыть окна", "Все",
                    Duration.ofMinutes(10), LocalDateTime.of(2000, 1, 1, 1, 0));
            String jsonTask1 = server.getGson().toJson(testTask1);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Статус ответа: " + response.statusCode());
            System.out.println("Тело ответа: " + response.body());
            System.out.println(taskManager.getTasks());

            HttpRequest request1 = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/tasks"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonTask1))
                    .build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

            System.out.println("Статус ответа: " + response1.statusCode());
            System.out.println("Тело ответа: " + response1.body());
            System.out.println(taskManager.getTasks());

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
