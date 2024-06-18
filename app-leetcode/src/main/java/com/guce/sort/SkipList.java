package com.guce.sort;

/**
 * @Author chengen.gce
 * @DATE 2024/6/16 22:33
 */
import java.util.Random;

public class SkipList {

    // 定义最大层数
    private static final int MAX_LEVEL = 16;

    // 定义跳跃表节点类
    private static class Node {
        int value;
        Node[] forward;

        Node(int level, int value) {
            this.value = value;
            this.forward = new Node[level];
        }
    }

    private Node head; // 头节点
    private int level; // 当前层数
    private Random random; // 随机数生成器

    public SkipList() {
        this.head = new Node(MAX_LEVEL, Integer.MIN_VALUE); // 初始化头节点
        this.level = 1;
        this.random = new Random();
    }

    // 随机生成层数
    private int randomLevel() {
        int level = 1;
        while (random.nextInt(2) == 0 && level < MAX_LEVEL) {
            level++;
        }
        return level;
    }

    // 插入元素
    public void insert(int value) {
        Node[] update = new Node[MAX_LEVEL];
        Node current = head;

        for (int i = level - 1; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].value < value) {
                current = current.forward[i];
            }
            update[i] = current;
        }

        int newLevel = randomLevel();
        if (newLevel > level) {
            for (int i = level; i < newLevel; i++) {
                update[i] = head;
            }
            level = newLevel;
        }

        Node newNode = new Node(newLevel, value);
        for (int i = 0; i < newLevel; i++) {
            newNode.forward[i] = update[i].forward[i];
            update[i].forward[i] = newNode;
        }
    }

    // 删除元素
    public void delete(int value) {
        Node[] update = new Node[MAX_LEVEL];
        Node current = head;

        for (int i = level - 1; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].value < value) {
                current = current.forward[i];
            }
            update[i] = current;
        }

        current = current.forward[0];
        if (current != null && current.value == value) {
            for (int i = 0; i < level; i++) {
                if (update[i].forward[i] != current) {
                    break;
                }
                update[i].forward[i] = current.forward[i];
            }

            while (level > 1 && head.forward[level - 1] == null) {
                level--;
            }
        }
    }

    // 查找元素
    public boolean search(int value) {
        Node current = head;

        for (int i = level - 1; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].value < value) {
                current = current.forward[i];
            }
        }

        current = current.forward[0];
        return current != null && current.value == value;
    }

    // 打印跳跃表
    public void printSkipList() {
        for (int i = 0; i < level; i++) {
            Node node = head.forward[i];
            System.out.print("Level " + (i + 1) + ": ");
            while (node != null) {
                System.out.print(node.value + " ");
                node = node.forward[i];
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        SkipList skipList = new SkipList();
        skipList.insert(3);
        skipList.insert(6);
        skipList.insert(7);
        skipList.insert(9);
        skipList.insert(12);
        skipList.insert(19);
        skipList.insert(17);
        skipList.insert(26);
        skipList.insert(21);
        skipList.insert(25);

        skipList.printSkipList();

        System.out.println("Search 19: " + skipList.search(19));
        System.out.println("Search 15: " + skipList.search(15));

        skipList.delete(19);
        System.out.println("After deleting 19:");
        skipList.printSkipList();
    }
}
