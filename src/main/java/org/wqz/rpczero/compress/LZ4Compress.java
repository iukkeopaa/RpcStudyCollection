package org.wqz.rpczero.compress;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

import java.io.IOException;


/**
 * @author wjh
 * @createTime on 2025/3/30
 */
public class LZ4Compress implements Compress {

    @Override
    public byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        LZ4Factory factory = LZ4Factory.fastestInstance();
        LZ4Compressor compressor = factory.fastCompressor();
        byte[] compressed = new byte[compressor.maxCompressedLength(bytes.length)];
        int compressedLength = compressor.compress(bytes, 0, bytes.length, compressed, 0);
        byte[] result = new byte[compressedLength];
        System.arraycopy(compressed, 0, result, 0, compressedLength);
        return result;
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        LZ4Factory factory = LZ4Factory.fastestInstance();
        LZ4FastDecompressor decompressor = factory.fastDecompressor();
        // 这里假设知道原始数据长度，实际应用中可能需要额外处理
        int originalLength = 1024; 
        byte[] decompressed = new byte[originalLength];
        try {
            decompressor.decompress(bytes, 0, decompressed, 0, originalLength);
        } catch (Exception e) {
            throw new RuntimeException("lz4 decompress error", e);
        }
        return decompressed;
    }
}