package org.wqz.rpczero.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author wjh
 * @createTime on 2025/3/30
 */
public class ZlibCompress implements Compress {

    private static final int BUFFER_SIZE = 1024 * 4;

    @Override
    public byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        Deflater deflater = new Deflater();
        deflater.setInput(bytes);
        deflater.finish();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("zlib compress error", e);
        } finally {
            deflater.end();
        }
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        Inflater inflater = new Inflater();
        inflater.setInput(bytes);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("zlib decompress error", e);
        } finally {
            inflater.end();
        }
    }
}