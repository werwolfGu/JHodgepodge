package com.guce.allocation;

import com.guce.disruptor.DisruptorProducer;
import com.guce.disruptor.PullWithBatchedPoller;
import com.guce.disruptor.StringEventHandle;
import com.guce.event.ObjectEvent;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author chengen.gce
 * @DATE 2022/5/11 14:59
 */
public class OrderHandleAllocation {

    private Disruptor<ObjectEvent<String>> disruptor ;

    DisruptorProducer producer;

    List<PullWithBatchedPoller<String>> pollerList = new ArrayList<>(10);

    private volatile AtomicBoolean endFlag = new AtomicBoolean(false);

    private AtomicInteger threadNumber = new AtomicInteger(0);
    public void init (){
        disruptor = new Disruptor<ObjectEvent<String>>(() -> new ObjectEvent(),1024, thread -> {
            Thread t = new Thread(thread);
            t.setName("disruptor->" + threadNumber.getAndIncrement());
            if (t.isDaemon()){
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY){
                t.setPriority(Thread.NORM_PRIORITY);

            }
            return t;
        } );
        int concurrentNumber = 4;
        StringEventHandle[] eventHandles = new StringEventHandle[4];
        for (int i= 0 ; i < concurrentNumber ; i++){
            eventHandles[i] = new StringEventHandle(i,concurrentNumber);
        }
        ////每个handle都会处理一遍 queue的消息
        disruptor.handleEventsWith(eventHandles);
        ///共享queue的消息；
        ///disruptor.handleEventsWithWorkerPool(eventHandles);
        disruptor.start();
        RingBuffer ringBuffer = disruptor.getRingBuffer();
        producer = new DisruptorProducer(ringBuffer);
    }

    public void allocation(){
        try {
            for (int i = 0 ; i < 1000 ;i++) {
                producer.onData("" + i);
                Thread.sleep(1);
            }
            System.out.println("disruptor shutdown......");
            disruptor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Deprecated
    public void initBatchConsumer(){
        disruptor = new Disruptor<ObjectEvent<String>>(() -> new ObjectEvent(),1024, thread -> {
            Thread t = new Thread(thread);
            t.setName("disruptor->" + threadNumber.getAndIncrement());
            if (t.isDaemon()){
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY){
                t.setPriority(Thread.NORM_PRIORITY);

            }
            return t;
        } );
        disruptor.start();
        RingBuffer ringBuffer = disruptor.getRingBuffer();
        int concurrentNumber = 1;
        for (int i = 0 ; i < concurrentNumber ; i++ ){
            PullWithBatchedPoller<String> poller =
                    new PullWithBatchedPoller<>(ringBuffer, 10,100,endFlag,i,concurrentNumber);
            pollerList.add(poller);
        }
        producer = new DisruptorProducer(ringBuffer);
    }

    @Deprecated
    public void batchConsumer (){
        pollerList.stream().forEach( poller -> {
            new Thread( () -> {
                while (true) {
                    try {
                        List list = poller.poll();
                        System.out.println(Thread.currentThread().getName() + "->" + list);
                        Thread.sleep(10);
                        if (list.isEmpty()){
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }).start();

        });
    }


    public static void main(String[] args) {
        OrderHandleAllocation allocation = new OrderHandleAllocation();

        allocation.init();
        //allocation.initBatchConsumer();
        /// 启动消费者
        //allocation.batchConsumer();
        ///启动生产者
        allocation.allocation();

    }
}
