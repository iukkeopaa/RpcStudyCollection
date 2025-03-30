package org.wqz.rpczero.zk;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wqz.rpczero.enums.LoadBalanceEnum;
import org.wqz.rpczero.enums.RpcErrorMessageEnum;
import org.wqz.rpczero.exception.RpcException;
import org.wqz.rpczero.extension.ExtensionLoader;
import org.wqz.rpczero.loadbalance.LoadBalance;
import org.wqz.rpczero.protocol.RpcRequest;
import org.wqz.rpczero.provider.impl.ZkServiceProviderImpl;
import org.wqz.rpczero.registry.ServiceDiscovery;
import org.wqz.rpczero.utils.CollectionUtil;
import org.wqz.rpczero.utils.CuratorUtils;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/3/29 下午4:42
 */
public class ZkServiceDiscoveryImpl implements ServiceDiscovery{


    private static final Logger log = LoggerFactory.getLogger(ZkServiceDiscoveryImpl.class);

    private final LoadBalance loadBalance;

    public ZkServiceDiscoveryImpl() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(LoadBalanceEnum.LOADBALANCE.getName());
    }
    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (CollectionUtil.isEmpty(serviceUrlList)) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        // load balancing
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcRequest);
        log.info("Successfully found the service address:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
