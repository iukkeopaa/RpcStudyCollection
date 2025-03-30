package org.wqz.rpczero.compress;

import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * @author wjh
 * @createTime on 2025/3/30
 */
public class SnappyCompress implements Compress {

    @Override
    public byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        try {
            return Snappy.compress(bytes);
        } catch (IOException e) {
            throw new RuntimeException("snappy compress error", e);
        }
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        try {
            return Snappy.uncompress(bytes);
        } catch (IOException e) {
            throw new RuntimeException("snappy decompress error", e);
        }
    }
}