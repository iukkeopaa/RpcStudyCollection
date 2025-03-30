package org.wqz.contentdemo.proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationTargetException;

// 定义一个接口
interface MyInterface {
    void doSomething();
}

// 实现接口的目标类
class MyClass implements MyInterface {
    @Override
    public void doSomething() {
        System.out.println("Doing something...");
    }
}

// 代理类，用于在目标方法执行前后添加额外逻辑
class MyInterceptor {
    public static Object intercept(Object obj, java.lang.reflect.Method method, Object[] args) throws Throwable {
        System.out.println("Before method execution");
        Object result = method.invoke(obj, args);
        System.out.println("After method execution");
        return result;
    }
}

public class ByteBuddyProxyExample {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // 使用 Byte Buddy 创建代理类
        Class<? extends MyInterface> dynamicType = new ByteBuddy()
               .subclass(MyClass.class)
               .method(ElementMatchers.named("doSomething"))
               .intercept(MethodDelegation.to(MyInterceptor.class))
               .make()
               .load(ByteBuddyProxyExample.class.getClassLoader())
               .getLoaded();

        // 创建代理类的实例
        MyInterface proxy = dynamicType.getDeclaredConstructor().newInstance();

        // 调用代理类的方法
        proxy.doSomething();
    }
}    