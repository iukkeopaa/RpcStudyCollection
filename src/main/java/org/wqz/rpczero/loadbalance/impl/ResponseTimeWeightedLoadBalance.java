package org.wqz.rpczero.loadbalance.impl;

import org.wqz.rpczero.loadbalance.AbstractLoadBalance;
import org.wqz.rpczero.protocol.RpcRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 响应时间加权负载均衡策略
 */
public class ResponseTimeWeightedLoadBalance extends AbstractLoadBalance {
    // 用于记录每个服务器的平均响应时间
    private final Map<String, Double> responseTimes = new HashMap<>();

    @Override
    protected String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest) {
        double totalWeight = 0;
        Map<String, Double> weights = new HashMap<>();

        for (String server : serviceAddresses) {
            double responseTime = getResponseTime(server);
            responseTimes.put(server, responseTime);
            // 响应时间越短，权重越高
            double weight = 1.0 / responseTime;
            weights.put(server, weight);
            totalWeight += weight;
        }

        double randomValue = new Random().nextDouble() * totalWeight;
        double currentWeight = 0;
        for (Map.Entry<String, Double> entry : weights.entrySet()) {
            currentWeight += entry.getValue();
            if (currentWeight >= randomValue) {
                return entry.getKey();
            }
        }
        return null;
    }

    private double getResponseTime(String server) {
        // 实际实现中需要通过监控获取服务器的响应时间
        // 这里简单返回一个示例值
        return responseTimes.getOrDefault(server, 1.0);
    }
}
