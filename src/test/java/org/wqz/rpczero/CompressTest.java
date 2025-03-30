package org.wqz.rpczero;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.wqz.rpczero.compress.Bzip2Compress;
import org.wqz.rpczero.compress.Compress;
import org.wqz.rpczero.compress.ZlibCompress;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/3/29 下午4:59
 */

@SpringBootTest
public class CompressTest {
    public static void main(String[] args) {
        String testData = "This is a test string for compression.";
        byte[] data = testData.getBytes();

        // 使用Zlib压缩
        Compress zlibCompress = new ZlibCompress();
        byte[] compressedZlib = zlibCompress.compress(data);
        byte[] decompressedZlib = zlibCompress.decompress(compressedZlib);
        System.out.println("Zlib decompressed data: " + new String(decompressedZlib));

        // 使用Bzip2压缩
        Compress bzip2Compress = new Bzip2Compress();
        byte[] compressedBzip2 = bzip2Compress.compress(data);
        byte[] decompressedBzip2 = bzip2Compress.decompress(compressedBzip2);
        System.out.println("Bzip2 decompressed data: " + new String(decompressedBzip2));
    }

}
