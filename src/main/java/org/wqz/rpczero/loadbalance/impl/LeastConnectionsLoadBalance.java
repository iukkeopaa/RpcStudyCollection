package org.wqz.rpczero.loadbalance.impl;

import org.wqz.rpczero.loadbalance.AbstractLoadBalance;
import org.wqz.rpczero.protocol.RpcRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 最少连接负载均衡策略
 */
public class LeastConnectionsLoadBalance extends AbstractLoadBalance {
    // 用于记录每个服务器的连接数
    private final Map<String, Integer> connectionCount = new HashMap<>();

    @Override
    protected String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest) {
        // 初始化最少连接数为最大整数
        int leastConnections = Integer.MAX_VALUE;
        String selectedServer = null;

        for (String server : serviceAddresses) {
            // 如果该服务器还没有记录连接数，初始化为 0
            connectionCount.putIfAbsent(server, 0);
            int currentConnections = connectionCount.get(server);
            if (currentConnections < leastConnections) {
                leastConnections = currentConnections;
                selectedServer = server;
            }
        }

        // 选中的服务器连接数加 1
        if (selectedServer != null) {
            connectionCount.put(selectedServer, leastConnections + 1);
        }
        return selectedServer;
    }
}