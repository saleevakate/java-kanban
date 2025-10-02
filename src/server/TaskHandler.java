package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.InMemoryTaskManager;
import tasks.NotFoundException;
import tasks.Task;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler {
    private final InMemoryTaskManager taskManager;

    public TaskHandler(InMemoryTaskManager manager, Gson gson) {
        super(gson);
        this.taskManager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if (path.equals("/tasks")) {
                if (method.equals("GET")) {
                    handleGetTasks(exchange);
                } else if (method.equals("POST")) {
                    handlePostTask(exchange);
                }
            } else if (path.matches("/tasks/\\d+")) {
                int id = Integer.parseInt(path.substring("/tasks/".length()));
                if (method.equals("GET")) {
                    handleGetTaskById(exchange, id);
                } else if (method.equals("DELETE")) {
                    handleDeleteById(exchange, id);
                }
            } else {
                code404(exchange);
            }
        } catch (NotFoundException e) {
            code404(exchange);
        }
    }

    // tasks GET getTasks код 200
    private void handleGetTasks(HttpExchange exchange) throws IOException {
        try {
            code200(exchange, gson.toJson(taskManager.getTasks()));
        } catch (NotFoundException e) {
            code404(exchange);
        }
    }

    // tasks/id GET getTaskById код 200
    private void handleGetTaskById(HttpExchange exchange, int id) throws IOException {
        try {
            code200(exchange, gson.toJson(taskManager.getTaskById(id)));
        } catch (NotFoundException e) {
            code404(exchange);
        }
    }

    // tasks POST createTask(если id не указан) код 201
                //updateTask(если id указан)    код 201
    private void handlePostTask(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Task task = gson.fromJson(body, Task.class);
        try {
            if (task.getId() == 0) {
                try {
                    taskManager.createTask(task);
                    code201(exchange, "Задача создана");
                } catch (InMemoryTaskManager.TaskVlidationExeption e) {
                    code406(exchange);
                }
            } else {
                taskManager.updateTask(task);
                code201(exchange, "Задача обновлена");
            }
        } catch (NotFoundException e) {
            code404(exchange);
        }
    }

    // tasks/id DELETE deleteTask  код 200
    private void handleDeleteById(HttpExchange exchange, int id) throws IOException {
        try {
            taskManager.deleteTaskById(id);
            code200(exchange, "Задача с id" + id + "удалена");
        } catch (NotFoundException e) {
            code404(exchange);
        }
    }
}