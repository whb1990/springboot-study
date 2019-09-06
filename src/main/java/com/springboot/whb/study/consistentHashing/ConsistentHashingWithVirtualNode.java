package com.springboot.whb.study.consistentHashing;

import com.springboot.whb.study.common.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author: whb
 * @date: 2019/9/6 10:08
 * @description: 一致性Hash算法--带虚拟节点的实现
 */
public class ConsistentHashingWithVirtualNode {
    /**
     * 待添加到Hash环的服务器
     */
    private static String[] servers = {"127.0.0.1:7000", "127.0.0.1:7001", "127.0.0.1:7002", "127.0.0.1:7003", "127.0.0.1:7004"};

    /**
     * 真实节点列表，考虑到服务器的上线、下线，即添加、删除的场景会比较频繁
     */
    private static List<String> realNodes = new LinkedList<>();

    /**
     * 虚拟节点，Key表示虚拟节点的Hash值，value表示虚拟节点的名称
     */
    private static SortedMap<Integer, String> virtualNodes = new TreeMap<>();

    /**
     * 虚拟节点的数目
     */
    private static final int VIRTUAL_NODES = 4;

    /**
     * 初始化，加入真实节点及虚拟节点
     */
    static {
        //先将原始的服务器添加到真实节点列表
        for (int i = 0; i < servers.length; i++) {
            realNodes.add(servers[i]);
        }
        //添加虚拟节点，遍历真实节点列表
        for (String str : realNodes) {
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                String virtualNodeName = str + "&&VN" + String.valueOf(i);
                setServer(virtualNodeName);
            }
        }
        System.out.println();
    }

    /**
     * 添加虚拟节点
     *
     * @param virtualNodeName
     */
    private static void setServer(String virtualNodeName) {
        setServer(virtualNodeName, null);
    }

    /**
     * 添加虚拟节点
     *
     * @param virtualNodeName
     * @param hash
     */
    private static void setServer(String virtualNodeName, Integer hash) {
        hash = hash != null ? HashUtils.getHash(hash.toString()) : HashUtils.getHash(virtualNodeName);
        if (StringUtils.isBlank(virtualNodes.get(hash))) {
            virtualNodes.put(hash, virtualNodeName);
            System.out.println("虚拟节点[" + virtualNodeName + "]被添加, hash值为：" + hash);
        } else {
            //解决Hash碰撞
            setServer(virtualNodeName, hash);
        }
    }

    /**
     * 得到应当路由到的结点
     *
     * @param key
     * @return
     */
    private static String getServer(String key) {
        //得到该key的hash值
        int hash = HashUtils.getHash(key);
        // 得到大于该Hash值的所有Map
        SortedMap<Integer, String> subMap = virtualNodes.tailMap(hash);
        String virtualNode;
        if (subMap.isEmpty()) {
            //如果没有比该key的hash值大的，则从第一个node开始
            Integer i = virtualNodes.firstKey();
            //返回对应的服务器
            virtualNode = virtualNodes.get(i);
        } else {
            //第一个Key就是顺时针过去离node最近的那个结点
            Integer i = subMap.firstKey();
            //返回对应的服务器
            virtualNode = subMap.get(i);
        }
        return virtualNode;
    }

    public static void main(String[] args) {
        String[] keys = {"斗帝", "斗圣", "斗尊", "斗宗", "斗皇", "斗王", "斗灵", "大斗师", "斗师", "斗者"};
        for (String key : keys) {
            String server = getServer(key);
            String realServer = server.split("&&VN")[0];
            System.out.println("[" + key + "]的hash值为" +
                    HashUtils.getHash(key) + ", 被路由到虚拟结点[" + server + "]，被路由到真实节点[" + realServer + "]");
        }
    }
}
