package org.wqz.rpczero.loadbalance.impl;

import org.wqz.rpczero.loadbalance.AbstractLoadBalance;
import org.wqz.rpczero.protocol.RpcRequest;

import java.util.*;

/**
 * 加权轮询负载均衡策略
 */
public class WeightedRoundRobinLoadBalance extends AbstractLoadBalance {
    private final Map<String, Integer> weights = new HashMap<>();
    private int currentIndex = 0;
    private int currentWeight = 0;
    private int maxWeight = 0;
    private int gcdWeight = 0;

    public WeightedRoundRobinLoadBalance(Map<String, Integer> weights) {
        this.weights.putAll(weights);
        List<Integer> weightList = new ArrayList<>(weights.values());
        maxWeight = Collections.max(weightList);
        gcdWeight = gcd(weightList);
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private int gcd(List<Integer> numbers) {
        int result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            result = gcd(result, numbers.get(i));
        }
        return result;
    }

    @Override
    protected String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest) {
        while (true) {
            currentIndex = (currentIndex + 1) % serviceAddresses.size();
            if (currentIndex == 0) {
                currentWeight = currentWeight - gcdWeight;
                if (currentWeight <= 0) {
                    currentWeight = maxWeight;
                }
            }
            String server = serviceAddresses.get(currentIndex);
            if (weights.get(server) >= currentWeight) {
                return server;
            }
        }
    }
}