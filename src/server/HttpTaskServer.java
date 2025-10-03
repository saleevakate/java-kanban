package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTaskManager;
import manager.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
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
        server.createContext("/epics", new EpicHandler(taskManager, gson));
        server.createContext("/subtasks", new SubtaskHandler(taskManager, gson));
        server.createContext("/history", new HistoryHandler(taskManager, gson));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));

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
    }
}
