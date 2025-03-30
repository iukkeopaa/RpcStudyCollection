package org.wqz.rpczero.extension;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


public class ExtensionLoaderTest<T> {
    private static final String SERVICES_DIRECTORY = "META-INF/services/";
    private final Class<T> type;
    private final Map<String, Class<?>> cachedClasses = new HashMap<>();
    private final Map<String, T> cachedInstances = new HashMap<>();


    private ExtensionLoaderTest(Class<T> type) {
        this.type = type;
        loadExtensionClasses();
    }


    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type == null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type must be an interface");
        }
        if (type.getAnnotation(SPI.class) == null) {
            throw new IllegalArgumentException("Extension type must be annotated with @SPI");
        }
        return new ExtensionLoader<>(type);
    }


    public T getExtension(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Extension name == null");
        }
        T instance = cachedInstances.get(name);
        if (instance == null) {
            synchronized (cachedInstances) {
                instance = cachedInstances.get(name);
                if (instance == null) {
                    instance = createExtension(name);
                    cachedInstances.put(name, instance);
                }
            }
        }
        return instance;
    }


    private T createExtension(String name) {
        Class<?> clazz = cachedClasses.get(name);
        if (clazz == null) {
            throw new IllegalArgumentException("No such extension: " + name);
        }
        try {
            return type.cast(clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create extension instance: " + name, e);
        }
    }


    private void loadExtensionClasses() {
        String fullName = SERVICES_DIRECTORY + type.getName();
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader != null) {
                urls = classLoader.getResources(fullName);
            } else {
                urls = ClassLoader.getSystemResources(fullName);
            }
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    loadResources(url);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load extension classes", e);
        }
    }


    private void loadResources(URL url) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int commentIndex = line.indexOf('#');
                if (commentIndex >= 0) {
                    line = line.substring(0, commentIndex);
                }
                line = line.trim();
                if (line.length() > 0) {
                    try {
                        int index = line.indexOf('=');
                        if (index > 0) {
                            String name = line.substring(0, index).trim();
                            String className = line.substring(index + 1).trim();
                            if (name.length() > 0 && className.length() > 0) {
                                Class<?> clazz = Class.forName(className);
                                cachedClasses.put(name, clazz);
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("Class not found: " + line, e);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read extension resources", e);
        }
    }
}
