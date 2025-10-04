package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import tasks.NotFoundException;
import tasks.Task;

import java.io.IOException;
import java.net.URI;

public class TaskHandler extends BaseHttpHandler {
    private TaskManager taskManager;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(gson);
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String query = uri.getQuery();
        Integer id = null;
        if (query != null && query.startsWith("id=")) {
            id = Integer.parseInt(query.substring(3));
        }
        try {
            switch (method) {
                case "GET" -> handleGetTasks(exchange, id);
                case "POST" -> handlePostTask(exchange);
                case "DELETE" -> handleDeleteById(exchange, id);
            }
        } catch (NotFoundException e) {
            code500(exchange);
        }
    }

    private void handleGetTasks(HttpExchange exchange, Integer id) throws IOException {
        try {
            if (id == null) {
                if (taskManager.getTasks().isEmpty()) {
                    code404(exchange);
                } else {
                    code200(exchange, gson.toJson(taskManager.getTasks()));
                }
            } else {
                Task task = taskManager.getTaskById(id);
                if (task == null) {
                    code404(exchange);
                } else {
                    code200(exchange, gson.toJson(task));
                }
            }
        } catch (NotFoundException e) {
            code500(exchange);
        }
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        try {
            String body = readRequestBody(exchange);
            Task task = gson.fromJson(body, Task.class);
            if (task == null) {
                sendResponse(exchange, 404, "Ошибка. Задача пуста");
                return;
            }
            if (task.getId() == 0) {
                taskManager.createTask(task);
                code201(exchange, "Задача добавлена");
            } else {
                taskManager.updateTask(task);
                code201(exchange, "Задача обновлена");
            }
        } catch (InMemoryTaskManager.TaskValidationException e) {
            code406(exchange);
        } catch (NotFoundException e) {
            code500(exchange);
        }

    }

    private void handleDeleteById(HttpExchange exchange, int id) throws IOException {
        try {
            taskManager.deleteTaskById(id);
            code200(exchange, "Задача с id" + id + "удалена");
        } catch (NotFoundException e) {
            code500(exchange);
        }
    }
}