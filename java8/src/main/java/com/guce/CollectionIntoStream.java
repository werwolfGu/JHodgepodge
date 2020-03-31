package com.guce;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @Author chengen.gce
 * @DATE 2020/3/29 12:13 上午
 */
public class CollectionIntoStream {

    public static void main(String[] args) {

        List<String> strings = Stream.generate( () -> RandomStringUtils.randomAscii(5))
                .limit(10)
                .collect(Collectors.toList());
        strings.forEach(System.out::println);
        // Convert to a Stream for many more options:
        String result = strings.stream()
                .map(String::toUpperCase)
                .map(s -> s.substring(2))
                .reduce(":", (s1, s2) -> s1 + s2);
        System.out.println(result);

        List<Integer> x = IntStream.range(0, 30)
                .peek(e -> System.out.println(e + ": " +Thread.currentThread()
                        .getName()))
                .limit(10)
                .parallel()
                .boxed()
                .collect(Collectors.toList());
        System.out.println(x);
    }
}
