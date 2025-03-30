package org.wqz.rpczero.zk;

import org.apache.curator.framework.CuratorFramework;
import org.wqz.rpczero.registry.ServiceRegistry;
import org.wqz.rpczero.utils.CuratorUtils;

import java.net.InetSocketAddress;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/3/29 下午4:47
 */
public class ZkServiceRegistryImpl implements ServiceRegistry {
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createPersistentNode(zkClient, servicePath);

    }
}
