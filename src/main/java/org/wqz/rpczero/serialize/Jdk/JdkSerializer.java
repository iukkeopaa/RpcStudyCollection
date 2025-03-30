package org.wqz.rpczero.serialize.Jdk;

import org.wqz.rpczero.serialize.Serializer;

import java.io.*;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/3/30 下午2:40
 */
public class JdkSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        byte[] data = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(os);
            output.writeObject(obj);
            output.flush();
            output.close();
            data = os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream input = new ObjectInputStream(is);
            Object result = input.readObject();
            return ((T) result);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
