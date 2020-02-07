package com.gce;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @Author chengen.gu
 * @DATE 2020/2/5 5:10 下午
 * http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/
 */
/*@Warmup(iterations = 10)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)*/

@Slf4j
@BenchmarkMode(Mode.AverageTime)// 测试方法平均执行时间
@OutputTimeUnit(TimeUnit.SECONDS)// 输出结果的时间粒度为微秒
@State(Scope.Benchmark) // 每个测试线程一个实例
public class JMHTestServiceBenchmark {

    private List<Integer> numbers;

    @Param({"25", "50", "100", "200", "500", "1000"})
    private int length;

    @Setup
    public void prepare() {
        numbers = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            numbers.add(ThreadLocalRandom.current().nextInt());
        }
    }

    private void doSomething() {
        Collections.shuffle(numbers);
        Collections.sort(numbers);
    }
    @Benchmark
    @Threads(1)
    public void testSingleThreadDirectly() {
        doSomething();
    }

    public static void main(String[] args) throws Exception {
        // 可以通过注解
        Options opt = new OptionsBuilder()
                .include(JMHTestServiceBenchmark.class.getSimpleName())
                .warmupIterations(3) // 预热3次
                .measurementIterations(2).measurementTime(TimeValue.valueOf("1s")) // 运行5次，每次10秒
                .threads(1) // 10线程并发
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
