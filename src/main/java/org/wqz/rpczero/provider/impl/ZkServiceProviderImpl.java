package org.wqz.rpczero.provider.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wqz.rpczero.config.RpcServiceConfig;
import org.wqz.rpczero.enums.RpcErrorMessageEnum;
import org.wqz.rpczero.enums.ServiceRegistryEnum;
import org.wqz.rpczero.exception.RpcException;
import org.wqz.rpczero.extension.ExtensionLoader;
import org.wqz.rpczero.provider.ServiceProvider;
import org.wqz.rpczero.registry.ServiceRegistry;
import org.wqz.rpczero.transport.netty.server.NettyRpcServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/3/29 下午4:24
 */


public class ZkServiceProviderImpl implements ServiceProvider {

    private static final Logger log = LoggerFactory.getLogger(ZkServiceProviderImpl.class);
    private final Map<String,Object> serviceMap;


    private final Set<String> registeredService;


    private final ServiceRegistry serviceRegistry;

    public ZkServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension(ServiceRegistryEnum.ZK.getName());
    }
    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {


        String rpcServiceName = rpcServiceConfig.getServiceName();
        if(registeredService.contains(rpcServiceName)){
            return;
        }
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());

        log.info("Add service: {} and interfaces:{}", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());



    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);

        if (null == service) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void pulishService(RpcServiceConfig rpcServiceConfig) {

        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            this.addService(rpcServiceConfig);
            serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), new InetSocketAddress(host, NettyRpcServer.PORT));
        } catch (UnknownHostException e) {
            log.error("occur exception when getHostAddress", e);
        }

    }
}
