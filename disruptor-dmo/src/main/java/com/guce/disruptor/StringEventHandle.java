package com.guce.disruptor;

import com.guce.event.ObjectEvent;
import com.lmax.disruptor.EventHandler;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2022/5/11 14:49
 */
public class StringEventHandle implements  EventHandler<ObjectEvent<String>> {

    private int magicHandleNum ;
    private int totalHandleNum ;
    List<String> processList = new ArrayList<>(100) ;

    public StringEventHandle(int magicHandle,int totalHandle){
        this.magicHandleNum = magicHandle;
        this.totalHandleNum = totalHandle;
    }
    @Override
    public void onEvent(ObjectEvent<String> event, long sequence, boolean endOfBatch) throws Exception {
        String value = event.getData();
        int n = Integer.valueOf(value);

        if (n % totalHandleNum == magicHandleNum){
            processList.add(value);
        }
        //System.out.println(value);
        if (processList.size() <= 100 && !endOfBatch){
            return ;
        }
        Thread.sleep(100);
        handle(endOfBatch, processList);

        processList.clear();
    }

    public void handle(boolean endOfBatch , List<String> list){
        if (CollectionUtils.isEmpty(list)){
            return ;
        }
        System.out.println(endOfBatch + " -> threadname-> " + Thread.currentThread().getName()
                +" comsumer....." + list);
    }
}
