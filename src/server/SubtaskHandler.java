package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.FileBackedTaskManager;
import tasks.NotFoundException;
import tasks.Subtask;

import java.io.IOException;
import java.net.URI;

public class SubtaskHandler extends BaseHttpHandler {
    private final FileBackedTaskManager taskManager;

    public SubtaskHandler(FileBackedTaskManager taskManager, Gson gson) {
        super(gson);
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            URI uri = exchange.getRequestURI();
            String query = uri.getQuery();
            Integer id = null;
            if (query != null && query.startsWith("id=")) {
                id = Integer.parseInt(query.substring(3));
            }
            switch (method) {
                case "GET" -> handleGetSubtask(exchange, id);
                case "POST" -> handlePostSubtask(exchange);
                case "DELETE" -> handleDeleteById(exchange, id);
            }
        } catch (NotFoundException e) {
            code500(exchange);
        }
    }

    private void handleGetSubtask(HttpExchange exchange, Integer id) throws IOException {
        try {
            if (id == null) {
                if (taskManager.getSubtasks().isEmpty()) {
                    code404(exchange);
                } else {
                    code200(exchange, gson.toJson(taskManager.getSubtasks()));
                }
            } else {
                Subtask subtask = taskManager.getSubtaskById(id);
                if (subtask == null) {
                    code404(exchange);
                } else {
                    code200(exchange, gson.toJson(subtask));
                }
            }
        } catch (NotFoundException e) {
            code500(exchange);
        }
    }

    private void handlePostSubtask(HttpExchange exchange) throws IOException {
        try {
            String body = readRequestBody(exchange);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            if (subtask == null) {
                sendResponse(exchange, 404, "Ошибка. Задача пуста");
                return;
            }
            if (subtask.getId() == 0) {
                taskManager.createSubtask(subtask);
                code201(exchange, "Сабтаск добавлен");
            } else if (subtask.getId() != 0) {
                taskManager.updateSubtask(subtask);
                code201(exchange, "Сабтаск обновлен");
            }

        } catch (NotFoundException e) {
            code500(exchange);
        }
    }

    private void handleDeleteById(HttpExchange exchange, int id) throws IOException {
        try {
            taskManager.deleteSubtaskById(id);
            code200(exchange, "Задача с id" + id + "удалена");
        } catch (NotFoundException e) {
            code500(exchange);
        }
    }
}
