package com.springboot.whb.study.rpc.rpc_v2.register;

import com.springboot.whb.study.rpc.rpc_v2.balance.DefaultLoadBalance;
import com.springboot.whb.study.rpc.rpc_v2.balance.LoadBalance;
import com.springboot.whb.study.rpc.rpc_v2.config.BasicConfig;
import com.springboot.whb.study.rpc.rpc_v2.core.RpcRequest;
import com.springboot.whb.study.rpc.rpc_v2.domain.ServiceType;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author: whb
 * @date: 2019/8/19 9:59
 * @description: Zookeeper服务注册实现类
 */
@Slf4j
public class ZkServiceRegister implements ServiceRegister {

    private CuratorFramework client;

    private static final String ROOT_PATH = "whb/demo-rpc";

    private LoadBalance loadBalance = new DefaultLoadBalance();

    public ZkServiceRegister() {
        //重试策略
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);

        this.client = CuratorFrameworkFactory
                .builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(50000)
                .retryPolicy(policy)
                .namespace(ROOT_PATH)
                .build();
        // 业务的根路径是 /whb/demo-rpc ,其他的都会默认挂载在这里

        this.client.start();
        System.out.println("zk启动正常");
    }

    /**
     * 服务注册
     *
     * @param config
     */
    @Override
    public void register(BasicConfig config) {
        String interfacePath = "/" + config.getInterfaceName();
        try {
            if (this.client.checkExists().forPath(interfacePath) == null) {
                // 创建 服务的永久节点
                this.client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(interfacePath);
            }

            config.getMethods().forEach(method -> {
                String methodPath = null;
                try {
                    ServiceType serviceType = config.getType();
                    if (serviceType == ServiceType.PROVIDER) {
                        // 服务提供方，需要暴露自身的ip、port信息，而消费端则不需要
                        String address = getServiceAddress(config);
                        methodPath = String.format("%s/%s/%s/%s", interfacePath, serviceType.getType(), method.getMethodName(), address);
                    } else {
                        methodPath = String.format("%s/%s/%s", interfacePath, serviceType.getType(), method.getMethodName());
                    }
                    log.info("zk path: [" + ROOT_PATH + methodPath + "]");
                    // 创建临时节点，节点包含了服务提供段的信息
                    this.client.create()
                            .creatingParentsIfNeeded()
                            .withMode(CreateMode.EPHEMERAL)
                            .forPath(methodPath, "0".getBytes());
                } catch (Exception e) {
                    log.error("创建临时节点[" + methodPath + "]失败，error:{}", e);
                }
            });
        } catch (Exception e) {
            log.error("创建服务节点失败，error:{}", e);
        }
    }

    /**
     * 服务发现
     *
     * @param request
     * @param nodeType
     * @return
     */
    @Override
    public InetSocketAddress discovery(RpcRequest request, ServiceType nodeType) {
        String path = String.format("/%s/%s/%s", request.getClassName(), nodeType.getType(), request.getMethodName());
        try {
            List<String> addressList = this.client.getChildren().forPath(path);
            // 采用负载均衡的方式获取服务提供方信息,不过并没有添加watcher监听模式
            String address = loadBalance.balance(addressList);
            if (address == null) {
                return null;
            }
            return parseAddress(address);
        } catch (Exception e) {
            log.error("服务发现接口异常，error:{}", e);
        }
        return null;
    }

    /**
     * 获取服务地址
     *
     * @param config
     * @return
     */
    private String getServiceAddress(BasicConfig config) {
        String hostInfo = new StringBuilder()
                .append(config.getHost())
                .append(":")
                .append(config.getPort())
                .toString();
        return hostInfo;
    }

    /**
     * 封装端口
     *
     * @param address
     * @return
     */
    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.valueOf(result[1]));
    }

    /**
     * 设置负载均衡策略
     *
     * @param loadBalance
     */
    public void setLoadBalance(LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }
}
