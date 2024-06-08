package com.guce.disruptor;

import com.guce.event.ObjectEvent;
import com.lmax.disruptor.RingBuffer;

/**
 * @Author chengen.gce
 * @DATE 2022/5/11 12:26
 */
public class DisruptorProducer<D>  {
    
    private final RingBuffer<ObjectEvent> ringBuffer;

    public DisruptorProducer(RingBuffer<ObjectEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }
    public void onData(D data){
        long sequence = ringBuffer.next();

        try{
            ObjectEvent objectEvent = ringBuffer.get(sequence);
            objectEvent.setData(data);
            //System.out.println("生产数据：" + data);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ringBuffer.publish(sequence);
        }
    }
}
