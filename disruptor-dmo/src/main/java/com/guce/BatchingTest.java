package com.guce;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.locks.LockSupport;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @Author chengen.gce
 * @DATE 2022/5/11 20:45
 */
public class BatchingTest {
    public static Stream<Arguments> generateData()
    {
        return Stream.of(arguments(ProducerType.MULTI), arguments(ProducerType.SINGLE));
    }

    private static class ParallelEventHandler implements EventHandler<LongEvent>
    {
        private final long mask;
        private final long ordinal;
        private final int batchSize = 10;

        private long eventCount;
        private long batchCount;
        private long publishedValue;
        private long tempValue;
        private volatile long processed;

        ParallelEventHandler(final long mask, final long ordinal)
        {
            this.mask = mask;
            this.ordinal = ordinal;
        }

        @Override
        public void onEvent(final LongEvent event, final long sequence, final boolean endOfBatch) throws Exception
        {
            if ((sequence & mask) == ordinal)
            {
                eventCount++;
                tempValue = event.get();
            }

            if (endOfBatch || ++batchCount >= batchSize)
            {
                publishedValue = tempValue;
                batchCount = 0;
            }
            else
            {
                LockSupport.parkNanos(1);
            }

            processed = sequence;
        }
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @MethodSource("generateData")
    public void shouldBatch(final ProducerType producerType) throws Exception
    {
        Disruptor<LongEvent> d = new Disruptor<>(
                LongEvent.FACTORY, 2048, DaemonThreadFactory.INSTANCE,
                producerType, new SleepingWaitStrategy());

        ParallelEventHandler handler1 = new ParallelEventHandler(1, 0);
        ParallelEventHandler handler2 = new ParallelEventHandler(1, 1);

        d.handleEventsWith(handler1, handler2);

        RingBuffer<LongEvent> buffer = d.start();

        EventTranslator<LongEvent> translator = (event, sequence) -> event.set(sequence);

        int eventCount = 10000;
        for (int i = 0; i < eventCount; i++) {
            buffer.publishEvent(translator);
        }

        while (handler1.processed != eventCount - 1 ||
                handler2.processed != eventCount - 1) {
            Thread.sleep(1);
        }
        System.out.println("handle1 " + handler1.publishedValue);
        System.out.println("handle1 " + handler1.eventCount);
        System.out.println(handler2.publishedValue);
        System.out.println(handler2.eventCount);

        /*assertThat(handler1.publishedValue, CoreMatchers.is((long) eventCount - 2));
        assertThat(handler1.eventCount, CoreMatchers.is((long) eventCount / 2));
        assertThat(handler2.publishedValue, CoreMatchers.is((long) eventCount - 1));
        assertThat(handler2.eventCount, CoreMatchers.is((long) eventCount / 2));*/
    }
}
