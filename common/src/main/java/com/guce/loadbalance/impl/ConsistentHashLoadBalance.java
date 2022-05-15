package com.guce.loadbalance.impl;

import com.guce.loadbalance.LoadBalance;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 一致性hash 负载均衡算法
 * @Author chengen.gce
 * @DATE 2022/5/10 15:41
 */
@Slf4j
public class ConsistentHashLoadBalance implements LoadBalance {

    public final String NAME = "consistenthash";

    private final ConcurrentMap<String, ConsistentHashSelector<?>> selectors = new ConcurrentHashMap<>();

    @Override
    public <T> T select(List<T> invokers, String invocation) {
        String key = NAME;
        int identityHashCode = System.identityHashCode(invokers);
        ConsistentHashSelector<T> selector = (ConsistentHashSelector<T>) selectors.get(key);
        if (selector == null || selector.identityHashCode != identityHashCode) {
            synchronized (NAME) {
                selector = (ConsistentHashSelector<T>) selectors.get(key);
                if (selector == null || selector.identityHashCode != identityHashCode) {
                    log.info("刷新路由信息：{}" ,invokers);
                    invokers = invokers.stream().sorted().collect(Collectors.toList());
                    selectors.put(key, new ConsistentHashSelector<T>(invokers, identityHashCode));
                    selector = (ConsistentHashSelector<T>) selectors.get(key);
                }

            }

        }
        return (T)selector.select(invocation);
    }

    private static final class ConsistentHashSelector<T> {

        private final TreeMap<Long, T> virtualInvokers;

        private final int replicaNumber;

        private final int identityHashCode;

        ConsistentHashSelector(List<T> invokers,  int identityHashCode) {
            this.virtualInvokers = new TreeMap<Long, T>();
            this.identityHashCode = identityHashCode;
            this.replicaNumber = 160;

            for (T invoker : invokers) {
                String address = invoker.toString();
                for (int i = 0; i < replicaNumber / 4; i++) {
                    byte[] digest = md5(address + i);
                    for (int h = 0; h < 4; h++) {
                        long m = hash(digest, h);
                        virtualInvokers.put(m, invoker);
                    }
                }
            }
        }

        public T select(String invocation) {
            byte[] digest = md5(invocation);
            return selectForKey(hash(digest, 0));
        }

        private T selectForKey(long hash) {
            Map.Entry<Long, T> entry = virtualInvokers.ceilingEntry(hash);
            if (entry == null) {
                entry = virtualInvokers.firstEntry();
            }
            return entry.getValue();
        }

        private long hash(byte[] digest, int number) {
            return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                    | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                    | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                    | (digest[number * 4] & 0xFF))
                    & 0xFFFFFFFFL;
        }

        private byte[] md5(String value) {
            MessageDigest md5;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
            md5.reset();
            byte[] bytes;
            try {
                bytes = value.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
            md5.update(bytes);
            return md5.digest();
        }

    }

    public static void main(String[] args) {
        List<String> invokers = new ArrayList<>();
        invokers.add("192.168.1.1");
        invokers.add("192.168.1.2");
        invokers.add("192.168.1.3");
        invokers.add("192.168.1.4");
        invokers.add("192.168.1.5");
        invokers.add("192.168.1.6");
        invokers.add("192.168.1.7");
        invokers.add("192.168.1.8");
        invokers.add("192.168.1.9");
        invokers.add("192.168.1.20");
        invokers.add("192.168.31.196");

        LoadBalance balance = new ConsistentHashLoadBalance();
        System.out.println(balance.select(invokers,"1323354651"));
        System.out.println(balance.select(invokers,"1323354652"));
        System.out.println(balance.select(invokers,"1323354653"));
        System.out.println(balance.select(invokers,"1323354644"));
        System.out.println(balance.select(invokers,"13233532645"));
        System.out.println(balance.select(invokers,"1323332649"));
        System.out.println(balance.select(invokers,"13232333464"));
        System.out.println(balance.select(invokers,"1328882333464"));
        System.out.println(balance.select(invokers,"132328334649"));
        System.out.println(balance.select(invokers,"13232333469"));
        System.out.println(balance.select(invokers,"1323233346899"));
        System.out.println(balance.select(invokers,"1323233346899"));
        System.out.println(balance.select(invokers,"1323233346899"));
        System.out.println(balance.select(invokers,"1323233346899"));
        System.out.println(balance.select(invokers,"1323233346899"));
        System.out.println(balance.select(invokers,"1323233346899"));
        
    }
}
