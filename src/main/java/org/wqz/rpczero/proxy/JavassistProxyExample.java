package org.wqz.rpczero.proxy;

import javassist.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// 定义一个接口
interface MyInterface {
    void doSomething();
}

// 实现接口的类
class MyClass implements MyInterface {
    @Override
    public void doSomething() {
        System.out.println("Doing something...");
    }
}

// 代理处理器
class MyInvocationHandler implements InvocationHandler {
    private final Object target;

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before method call");
        Object result = method.invoke(target, args);
        System.out.println("After method call");
        return result;
    }
}

// 使用 Javassist 生成代理类
public class JavassistProxyExample {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {
        // 获取类池
        ClassPool pool = ClassPool.getDefault();

        // 获取接口的 CtClass 对象
        CtClass myInterfaceCtClass = pool.get(MyInterface.class.getName());

        // 创建代理类
        CtClass proxyCtClass = pool.makeClass("MyProxy");
        proxyCtClass.addInterface(myInterfaceCtClass);

        // 创建构造函数
        CtConstructor constructor = new CtConstructor(new CtClass[]{pool.get(Object.class.getName())}, proxyCtClass);
        constructor.setBody("{this.target = $1;}");
        proxyCtClass.addConstructor(constructor);

        // 添加 target 字段
        CtField targetField = new CtField(pool.get(Object.class.getName()), "target", proxyCtClass);
        proxyCtClass.addField(targetField);

        // 实现接口方法
        CtMethod doSomethingMethod = CtNewMethod.make("public void doSomething() { target.getClass().getMethod(\"doSomething\").invoke(target); }", proxyCtClass);
        proxyCtClass.addMethod(doSomethingMethod);

        // 生成类
        Class<?> proxyClass = proxyCtClass.toClass();

        // 创建目标对象
        MyClass target = new MyClass();

        // 创建代理处理器
        MyInvocationHandler handler = new MyInvocationHandler(target);

        // 创建代理实例
        MyInterface proxyInstance = (MyInterface) Proxy.newProxyInstance(
                MyInterface.class.getClassLoader(),
                new Class<?>[]{MyInterface.class},
                handler
        );

        // 调用代理方法
        proxyInstance.doSomething();
    }
}    