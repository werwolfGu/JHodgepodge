package com.guce.hystrix;

import org.junit.Test;

import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

/**
 * Created by chengen.gu on 2018/11/1.
 */
public class HystrixUnitTest {


    @Test
    public void testSynchronous() {
        CommandHelloFailure helloFailure = new CommandHelloFailure("world");
        String value = helloFailure.execute();
        System.out.println(value);
        assertEquals("Hello Failure Bob!", new CommandHelloFailure("Bob").execute());
    }



    @Test
    public void testAsynchronous1() throws Exception {
        assertEquals("Hello Failure World!", new CommandHelloFailure("World").queue().get());
        assertEquals("Hello Failure Bob!", new CommandHelloFailure("Bob").queue().get());
    }

    @Test
    public void testAsynchronous2() throws Exception {

        Future<String> fWorld = new CommandHelloFailure("World").queue();
        Future<String> fBob = new CommandHelloFailure("Bob").queue();

        assertEquals("Hello Failure World!", fWorld.get());
        assertEquals("Hello Failure Bob!", fBob.get());
    }
}
