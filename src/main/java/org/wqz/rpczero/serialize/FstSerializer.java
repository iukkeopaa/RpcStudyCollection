package org.wqz.rpczero.serialize;


import org.nustaq.serialization.FSTConfiguration;

public class FstSerializer implements Serializer {
    private static final FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

    @Override
    public byte[] serialize(Object obj) {
        return conf.asByteArray(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return (T) conf.asObject(bytes);
    }
}