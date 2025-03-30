package org.wqz.rpczero.loadbalance.impl;

import org.wqz.rpczero.loadbalance.AbstractLoadBalance;
import org.wqz.rpczero.protocol.RpcRequest;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡策略
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance {
    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    protected String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest) {
        int size = serviceAddresses.size();
        int currentIndex = index.getAndIncrement() % size;
        return serviceAddresses.get(currentIndex);
    }
}