package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.NotFoundException;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager, Gson gson) {
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
            List<Task> history = taskManager.getHistory();
            code200(exchange, gson.toJson(history));
        } catch (NotFoundException e) {
            code500(exchange);
        }

    }
}
