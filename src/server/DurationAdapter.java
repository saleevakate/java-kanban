package server;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {

    @Override
    public JsonElement serialize(Duration src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getSeconds());
    }

    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        long seconds = json.getAsLong();
        return Duration.ofSeconds(seconds);
    }
}