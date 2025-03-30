package org.wqz.rpcregistry;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/3/30 下午2:10
 */
public class ZooKeeperServiceDiscovery implements ServiceDiscovery{


    private static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeperServiceDiscovery.class);


    private String zkAddress;


    public ZooKeeperServiceDiscovery(String zkAddress){
        this.zkAddress=zkAddress;
    }
    @Override
    public String discover(String serviceName) {

        ZkClient zkClient =new ZkClient(zkAddress,Constant.ZK_SESSION_TIMEOUT,Constant.ZK_CONNECTION_TIMEOUT);

        LOGGER.debug("connect zookeeper");

        try {
            String servicePath = Constant.ZK_REGISTRY_PATH + "/" + serviceName;


            if (!zkClient.exists(servicePath)){
                throw new RuntimeException(String.format("cannot find any service node on path :%s",servicePath));
            }
            List<String> addressList = zkClient.getChildren(servicePath);
            if (CollectionUtil.isEmpty(addressList)) {
                throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
            }


            String address;

            int size=addressList.size();
            if (size==1){

                address=addressList.get(0);

                LOGGER.debug("get only address node: {}", address);
            }else {
                address=addressList.get(ThreadLocalRandom.current().nextInt(size));
                LOGGER.debug("get random address node: {}", address);
            }

            String addressPath = servicePath + "/" + address;
            return zkClient.readData(addressPath);
        }finally {
            zkClient.close();
        }

    }
}
