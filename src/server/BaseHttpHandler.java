package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {

    protected final Gson gson;

    public BaseHttpHandler(Gson gson) {
        this.gson = gson;
    }


    public abstract void handle(HttpExchange exchange) throws IOException;

    //код 200 сервер корректно выполнил запрос и вернул данные
    protected void code200(HttpExchange exchange, String text) throws IOException {
        sendResponse(exchange, 200, text);
    }

    //код 201 запрос выполнен успешно, но возвращать данные нет необходимости
    protected void code201(HttpExchange exchange, String text) throws IOException {
        sendResponse(exchange, 201, text);
    }

    //код 404 пользователь обратился к несуществующему ресурсу (например, попытался получить задачу, которой нет)
    protected void code404(HttpExchange exchange) throws IOException {
        String text = "Ошибка : Not Found";
        sendResponse(exchange, 404, text);
    }

    //код 406 если добавляемая задача пересекается с существующими
    protected void code406(HttpExchange exchange) throws IOException {
        String text = "Ошибка : Not Acceptable";
        sendResponse(exchange, 406, text);
    }

    //код 500 если произошла ошибка при обработке запроса (например, при сохранении данных менеджера в файл)
    protected void code500(HttpExchange exchange) throws IOException {
        String text = "Ошибка : Internal Server Error";
        sendResponse(exchange, 500, text);
    }


    protected void sendResponse(HttpExchange exchange, int code, String response) throws IOException {
        byte[] resp = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(code, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected String readRequestBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
}