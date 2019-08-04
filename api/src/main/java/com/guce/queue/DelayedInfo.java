package com.guce.queue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by chengen.gu on 2018/10/31.
 */
public class DelayedInfo<T> implements Delayed {

    private long delayed ;
    private long expire;
    private long now;

    /**
     * Sequence number to break scheduling ties, and in turn to guarantee FIFO order among tied
     * entries.
     */
    private static final AtomicLong sequencer = new AtomicLong(0);

    /** Sequence number to break ties FIFO */
    private final long sequenceNumber;

    private final T item;

    public DelayedInfo(T item,long delayed){
        this.delayed = delayed;
        now = System.currentTimeMillis();
        expire = now + delayed;
        this.item = item;
        this.sequenceNumber = sequencer.getAndIncrement();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long delay = unit.convert(expire - System.currentTimeMillis() ,TimeUnit.MILLISECONDS);
        return delay;
    }

    @Override
    public int compareTo(Delayed other) {

        if(other == this)  return 0;

        if(other instanceof DelayedInfo){
            DelayedInfo x = (DelayedInfo) other;

            long diffTime = expire - x.expire;
            long diffSequence = sequenceNumber - x.sequenceNumber;
            return diffTime > 0 ? 1 : (diffTime < 0 ? -1 : (diffSequence > 0 ? 1 : (diffSequence < 0 ? -1 : 0)) );
        }

        long d = (getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    public T getItem() {
        return item;
    }
}
