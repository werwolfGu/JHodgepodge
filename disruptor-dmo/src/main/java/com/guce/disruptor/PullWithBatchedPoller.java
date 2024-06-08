package com.guce.disruptor;

import com.google.common.collect.Lists;
import com.guce.event.ObjectEvent;
import com.lmax.disruptor.EventPoller;
import com.lmax.disruptor.RingBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author chengen.gce
 * @DATE 2022/5/11 15:33
 */
public class PullWithBatchedPoller<T> {

    private final EventPoller<ObjectEvent<T>> poller;
    private final BatchedData<T> polledData;
    private int batchSize;

    private AtomicBoolean endFlag;

    private int magicHandle ;
    private int totalHandles;
    private int batchCache ;

    public PullWithBatchedPoller(final RingBuffer<ObjectEvent<T>> ringBuffer, final int batchSize,int batchCache,
                                 final AtomicBoolean endFlag,int magicHandle , int totalHandles)
    {
        this.poller = ringBuffer.newPoller();
        ringBuffer.addGatingSequences(poller.getSequence());
        this.polledData = new BatchedData<>(batchSize);
        this.batchSize = batchSize;
        this.endFlag = endFlag;
        this.magicHandle = magicHandle;
        this.totalHandles = totalHandles;
        this.batchCache = batchCache;
    }

    public List<T> poll() throws Exception
    {
        List<T> result = new ArrayList<>();
        if (polledData.getMsgCount() > 0) {
            // we just fetch from our local
            T o = polledData.pollMessage();
                /*String str = (String) o;
                int val = Integer.valueOf(str);
                if (val % totalHandles != magicHandle){
                    continue;
                }*/
            result.add(o);
        }
        // we try to load from the ring
        loadNextValues(poller, polledData);
        return polledData.getMsgCount() > 0 ? Lists.newArrayList(polledData.pollMessage()) : result;
        //return result;
    }

    private EventPoller.PollState
            loadNextValues(final EventPoller<ObjectEvent<T>> poller, final BatchedData<T> batch)
            throws Exception {

        return poller.poll((event, sequence, endOfBatch) -> {
            T item = event.copyOfData();
            return item != null ? batch.addDataItem(item) : false;
        });
    }

    private static class BatchedData<T>
    {
        private int msgHighBound;
        private final int capacity;
        private final T[] data;
        private int cursor;

        @SuppressWarnings("unchecked")
        BatchedData(final int size)
        {
            this.capacity = size;
            data = (T[]) new Object[this.capacity];
        }

        private void clearCount()
        {
            msgHighBound = 0;
            cursor = 0;
        }

        public int getMsgCount()
        {
            return msgHighBound - cursor;
        }

        public boolean addDataItem(final T item) throws IndexOutOfBoundsException
        {
            if (msgHighBound >= capacity)
            {
                return false;
            }

            data[msgHighBound++] = item;
            return msgHighBound < capacity;
        }

        public T pollMessage()
        {
            T rtVal = null;
            if (cursor < msgHighBound)
            {
                rtVal = data[cursor++];
            }
            if (cursor > 0 && cursor >= msgHighBound)
            {
                clearCount();
            }
            return rtVal;
        }
    }
}
