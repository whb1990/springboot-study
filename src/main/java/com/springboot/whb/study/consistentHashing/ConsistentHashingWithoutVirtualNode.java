package com.springboot.whb.study.consistentHashing;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author: whb
 * @date: 2019/9/6 9:59
 * @description: 一致性Hash算法--不带虚拟节点的实现
 */
public class ConsistentHashingWithoutVirtualNode {
    /**
     * 待添加到Hash环的服务器
     */
    private static String[] servers = {"127.0.0.1:7000", "127.0.0.1:7001", "127.0.0.1:7002", "127.0.0.1:7003", "127.0.0.1:7004"};
    /**
     * key表示服务器的Hash值，value表示服务器
     */
    private static SortedMap<Integer, String> sortedMap = new TreeMap<>();

    /**
     * 初始化，将所有服务器放入sortedMap
     */
    static {
        for (int i = 0; i < servers.length; i++) {
            int hash = HashUtils.getHash(servers[i]);
            System.out.println("[" + servers[i] + "]加入集合中, 其Hash值为：" + hash);
            sortedMap.put(hash, servers[i]);
        }
        System.out.println();
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
        //得到大于该Hash值的所有Map
        SortedMap<Integer, String> subMap = sortedMap.tailMap(hash);
        if (subMap.isEmpty()) {
            //如果没有比该key的hash值大的，则从第一个node开始
            Integer i = sortedMap.firstKey();
            //返回对应的服务器
            return sortedMap.get(i);
        } else {
            //第一个Key就是顺时针过去离node最近的那个结点
            Integer i = subMap.firstKey();
            //返回对应的服务器
            return subMap.get(i);
        }
    }

    public static void main(String[] args) {
        String[] keys = {"斗帝", "斗圣", "斗尊", "斗宗", "斗皇", "斗王", "斗灵", "大斗师", "斗师", "斗者"};
        for (String key : keys) {
            System.out.println("[" + key + "]的hash值为：" + HashUtils.getHash(key)
                    + ", 被路由到结点[" + getServer(key) + "]");
        }
    }
}
