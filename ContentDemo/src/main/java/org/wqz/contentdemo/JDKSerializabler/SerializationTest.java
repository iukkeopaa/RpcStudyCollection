package org.wqz.contentdemo.JDKSerializabler;

import java.io.*;

public class SerializationTest {
    public static void main(String[] args) {
        SerializableClass obj = new SerializableClass("Alice", 25);
        String filePath = "serialized.ser";

        // 序列化对象
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(obj);
            System.out.println("对象已序列化并保存到 " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 反序列化对象
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            SerializableClass deserializedObj = (SerializableClass) ois.readObject();
            System.out.println("从 " + filePath + " 反序列化得到的对象: " + deserializedObj);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}    