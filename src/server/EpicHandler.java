package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.FileBackedTaskManager;
import tasks.Epic;
import tasks.NotFoundException;

import java.io.IOException;
import java.net.URI;

public class EpicHandler extends BaseHttpHandler {
    private final FileBackedTaskManager taskManager;

    public EpicHandler(FileBackedTaskManager taskManager, Gson gson) {
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
                case "GET" -> handleGetEpic(exchange, id);
                case "POST" -> handlePostEpic(exchange);
                case "DELETE" -> handleDeleteById(exchange, id);
            }
        } catch (NotFoundException e) {
            code500(exchange);
        }

    }

    private void handleGetEpic(HttpExchange exchange, Integer id) throws IOException {
        try {
            if (id == null) {
                if (taskManager.getEpics().isEmpty()) {
                    code404(exchange);
                } else {
                    code200(exchange, gson.toJson(taskManager.getEpics()));
                }
            } else {
                Epic epic = taskManager.getEpicById(id);
                if (epic == null) {
                    code404(exchange);
                } else {
                    code200(exchange, gson.toJson(epic));
                }
            }

        } catch (NotFoundException e) {
            code500(exchange);
        }
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {
        try {
            String body = readRequestBody(exchange);
            Epic epic = gson.fromJson(body, Epic.class);
            if (epic == null) {
                sendResponse(exchange, 404, "Ошибка. Задача пуста");
                return;
            }
            if (epic.getId() == 0) {
                taskManager.createEpic(epic);
                code201(exchange, "Эпик добавлен");
            } else if (epic.getId() != 0) {
                taskManager.updateEpic(epic);
                code201(exchange, "Эпик обновлен");
            }

        } catch (NotFoundException e) {
            code500(exchange);
        }
    }

    private void handleDeleteById(HttpExchange exchange, int id) throws IOException {
        try {
            taskManager.deleteEpicById(id);
            code200(exchange, "Задача с id" + id + "удалена");
        } catch (NotFoundException e) {
            code500(exchange);
        }
    }
}
