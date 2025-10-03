package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.FileBackedTaskManager;
import tasks.NotFoundException;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {
    private final FileBackedTaskManager taskManager;

    public PrioritizedHandler(FileBackedTaskManager taskManager, Gson gson) {
        super(gson);
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String metod = exchange.getRequestMethod();
            if (!metod.equals("GET")) {
                code404(exchange);
            }
            List<Task> priorityTask = taskManager.getPrioritizedTasks();
            code200(exchange, gson.toJson(priorityTask));
        } catch (NotFoundException e) {
            code500(exchange);
        }

    }
}
