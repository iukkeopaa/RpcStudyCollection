package org.wqz.rpczero.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;

class Employee {
    private String name;
    private int age;

    public Employee(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

public class JacksonSerializationExample {
    public static void main(String[] args) {
        Employee employee = new Employee("Bob", 30);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 序列化
            String json = objectMapper.writeValueAsString(employee);
            System.out.println("Serialized JSON: " + json);

            // 反序列化
            Employee deserializedEmployee = objectMapper.readValue(json, Employee.class);
            System.out.println("Deserialized Employee - Name: " + deserializedEmployee.getName());
            System.out.println("Deserialized Employee - Age: " + deserializedEmployee.getAge());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}