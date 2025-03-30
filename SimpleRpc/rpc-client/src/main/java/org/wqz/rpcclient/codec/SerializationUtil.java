package org.wqz.rpcclient.codec;

import ch.qos.logback.classic.spi.EventArgUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 序列化工具
 * @Author: wjh
 * @Date: 2025/3/30 下午2:02
 */
public class SerializationUtil {

   private static Map<Class<?>, Schema<?>> cachedScheme = new ConcurrentHashMap<>();

   private static Objenesis objenesis = new ObjenesisStd(true);

   private SerializationUtil(){}

    public static <T> byte[] serialize(T obj) {
        // 获取对象的类类型
        Class<T> cls = (Class<T>) obj.getClass();
        // 分配一个默认大小的缓冲区，用于存储序列化过程中的数据
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            // 获取对象对应的模式
            Schema<T> schema = getSchema(cls);
            // 使用 Protostuff 工具将对象序列化为字节数组
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            // 若序列化过程中出现异常，抛出包含异常信息的 IllegalStateException
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            // 清空缓冲区，释放资源
            buffer.clear();
        }
    }

    /**
     * 反序列化（字节数组 -> 对象）
     */
    public static <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            // 使用 Objenesis 创建对象实例
            T message = objenesis.newInstance(cls);
            // 获取对象对应的模式,获取对象对应的模式就是为了让 Protostuff 知晓对象的结构，从而高效、准确地完成序列化和反序列化操作。也就是获得对象的结构消息以来加块序列化
            Schema<T> schema = getSchema(cls);
            // 使用 Protostuff 工具将字节数组反序列化为对象
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e) {
            // 若反序列化过程中出现异常，抛出包含异常信息的 IllegalStateException
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> cls) {
        // 从缓存中获取对象对应的模式
        Schema<T> schema = (Schema<T>) cachedScheme.get(cls);
        if (schema == null) {
            // 若缓存中不存在该模式，则使用运行时创建模式
            schema = RuntimeSchema.createFrom(cls);
            // 将新创建的模式存入缓存
            cachedScheme.put(cls, schema);
        }
        return schema;
    }
}
