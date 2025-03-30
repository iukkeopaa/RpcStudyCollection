package org.wqz.rpczero.compress;


import org.wqz.rpczero.extension.SPI;

/**
 * @author wjh
 * @createTime on 2025/3/30
 */

@SPI
public interface Compress {

    byte[] compress(byte[] bytes);


    byte[] decompress(byte[] bytes);
}
