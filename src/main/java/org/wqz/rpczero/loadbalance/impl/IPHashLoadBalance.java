package org.wqz.rpczero.loadbalance;


import org.wqz.rpczero.protocol.RpcRequest;

import java.util.List;

/**
 * IP 哈希负载均衡策略
 */
public class IPHashLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest) {
        // 获取客户端 IP 地址，这里假设从请求中可以获取到
        String clientIP = getClientIP(rpcRequest);
        int hash = clientIP.hashCode();
        int index = Math.abs(hash) % serviceAddresses.size();
        return serviceAddresses.get(index);
    }

    private String getClientIP(RpcRequest rpcRequest) {
        // 实际实现中需要从 rpcRequest 里提取客户端 IP
        // 这里简单返回一个示例 IP
        return "127.0.0.1";
    }
}