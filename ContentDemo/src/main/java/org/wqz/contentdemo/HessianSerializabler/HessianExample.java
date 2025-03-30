package org.wqz.contentdemo.HessianSerializabler;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// 定义一个简单的可序列化类
class Person implements java.io.Serializable {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}

public class HessianExample {
    public static void main(String[] args) {
        // 创建一个 Person 对象
        Person person = new Person("John", 30);

        try {
            // 序列化对象
            byte[] serializedData = serialize(person);
            System.out.println("Serialized data length: " + serializedData.length);

            // 反序列化对象
            Person deserializedPerson = (Person) deserialize(serializedData);
            System.out.println("Deserialized person: " + deserializedPerson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 序列化方法
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(bos);
        ho.writeObject(obj);
        return bos.toByteArray();
    }

    // 反序列化方法
    public static Object deserialize(byte[] data) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        HessianInput hi = new HessianInput(bis);
        return hi.readObject();
    }
}    