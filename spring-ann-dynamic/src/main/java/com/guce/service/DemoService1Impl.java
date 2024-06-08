package com.guce.service;

import lombok.extern.slf4j.Slf4j;

import javax.xml.soap.Node;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @Author chengen.gce
 * @DATE 2021/7/18 1:21 下午
 */
@Slf4j
public class DemoService1Impl implements IDemoService{


    @Override
    public void helloWorld() {
        Deque<Node> queue = new ArrayDeque<>();
        log.info("hello world!");
    }
}
