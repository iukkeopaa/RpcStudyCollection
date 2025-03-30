package org.wqz.rpczero.serialize;

import com.alibaba.fastjson.JSON;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/3/30 下午2:42
 */
public class FastJsonSerializer implements Serializer{
    @Override
    public byte[] serialize(Object obj) {
        String jsonStr = JSON.toJSONString(obj);
        return jsonStr.getBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(new String(bytes),clazz);
    }
}
