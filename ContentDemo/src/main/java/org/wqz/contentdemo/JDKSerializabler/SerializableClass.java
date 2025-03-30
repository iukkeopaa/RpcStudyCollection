package org.wqz.contentdemo.JDKSerializabler;

import java.io.*;




// 实现 Serializable 接口以支持序列化
class SerializableClass implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;

    public SerializableClass(String name, int age) {
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
        return "SerializableClass{name='" + name + "', age=" + age + "}";
    }
}    