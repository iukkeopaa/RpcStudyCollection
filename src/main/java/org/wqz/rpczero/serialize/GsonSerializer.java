package org.wqz.rpczero.serialize;


import com.google.gson.Gson;
import org.wqz.rpczero.serialize.Serializer;

import java.nio.charset.StandardCharsets;

public class GsonSerializer implements Serializer {
    private final Gson gson = new Gson();

    @Override
    public byte[] serialize(Object obj) {
        String json = gson.toJson(obj);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        String json = new String(bytes, StandardCharsets.UTF_8);
        return gson.fromJson(json, clazz);
    }
}