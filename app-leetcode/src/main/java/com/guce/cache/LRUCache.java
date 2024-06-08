package com.guce.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 实现LRU内存淘汰算法
 * @Author chengen.gce
 * @DATE 2022/4/25 21:40
 */
public class LRUCache<K ,V> {

    ////缓存内容
    private ConcurrentMap<K,ListNode> cache;

    private ListNode head ;
    private ListNode tail ;

    private volatile int MAX_SIZE = 5;

    public LRUCache(){
        cache = new ConcurrentHashMap<>();
        head = new ListNode();
        tail = new ListNode();
       head.next = tail;
       tail.pre = head;
    }

    /**
     * 链表节点
     */
    class ListNode {
        K key ;
        V value ;
        ListNode pre ;
        ListNode next;
        ListNode(){
        }
        ListNode(K key,V value){
            this.key = key;
            this.value = value;
        }

    }
    public void remove(){
        ListNode removeNode = head.next;
        if (removeNode.key == null) {
            return ;
        }
        head.next = removeNode.next;
        removeNode.next.pre = head;
        removeNode.pre = null;
        removeNode.next = null;
        cache.remove(removeNode.key);

    }
    public synchronized V add(K key ,V value) {

        if (cache.size() >= MAX_SIZE){
            remove();
        }

        ListNode currNode = cache.get(key);
        if (currNode != null) {
            V oldValue = currNode.value;
            currNode.value = value;
            return oldValue;

        } else {
            ListNode node = new ListNode(key, value);
            node.next = head.next;
            node.pre = head;
            head.next.pre = node;
            head.next = node;
            cache.put(key,node);
        }
        return value;
    }

    public V get(K key) {
        ListNode currNode = cache.get(key);
        if (currNode == null){
            return null;
        }

        ///当前节点脱离出来
        currNode.next.pre = currNode.pre;
        currNode.pre.next = currNode.next;

        ////将当前节点加入到链表尾部
        currNode.next = tail;
        currNode.pre = tail.pre;
        tail.pre.next = currNode;
        tail.pre = currNode;

        return currNode.value;
    }

    public static void main(String[] args) {
        LRUCache lru = new LRUCache<String,Integer>();
        lru.add("key1","v1");
        lru.add("key2","v2");

        String value = (String) lru.get("key1");
        System.out.println(value);
        lru.add("key3","v3");
        lru.add("key4","v4");
        value = (String) lru.get("key3");
        System.out.println(value);

        lru.add("key5","v5");
        lru.add("key6","v6");
        System.out.println(lru.get("key5"));
        System.out.println(lru.get("key6"));

    }
}
