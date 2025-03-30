package org.wqz.rpczero.compress;



import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author wjh
 * @createTime on 2025/3/30
 */
public class Bzip2Compress implements Compress {

    private static final int BUFFER_SIZE = 1024 * 4;

    @Override
    public byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             BZip2CompressorOutputStream bzip2Out = new BZip2CompressorOutputStream(bos)) {
            bzip2Out.write(bytes);
            bzip2Out.finish();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("bzip2 compress error", e);
        }
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             BZip2CompressorInputStream bzip2In = new BZip2CompressorInputStream(bis);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = bzip2In.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("bzip2 decompress error", e);
        }
    }
}