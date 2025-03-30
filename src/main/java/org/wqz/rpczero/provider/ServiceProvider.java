package org.wqz.rpczero.provider;

import org.wqz.rpczero.config.RpcServiceConfig;

public interface ServiceProvider {

    void addService(RpcServiceConfig rpcServiceConfig);


    Object getService(String rpcServiceName);

    void pulishService(RpcServiceConfig rpcServiceConfig);
}
