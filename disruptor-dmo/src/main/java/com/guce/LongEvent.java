package com.guce;

import com.lmax.disruptor.EventFactory;

/**
 * @Author chengen.gce
 * @DATE 2022/5/11 20:45
 */
public class LongEvent {
    private long value;

    public void set(final long value)
    {
        this.value = value;
    }

    public long get()
    {
        return value;
    }

    public static final EventFactory<LongEvent> FACTORY = () -> new LongEvent();
}
