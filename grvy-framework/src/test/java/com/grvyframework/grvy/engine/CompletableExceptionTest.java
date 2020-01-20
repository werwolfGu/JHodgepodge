package com.grvyframework.grvy.engine;

import com.grvyframework.exception.GrvyExceptionEnum;
import com.grvyframework.exception.GrvyExecutorException;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * @Author chengen.gu
 * @DATE 2020/1/20 10:55 下午
 */
public class CompletableExceptionTest {

    @Test
    public void testException(){
        try{
            CompletableFuture future = CompletableFuture.runAsync(() -> {
                throw new GrvyExecutorException(GrvyExceptionEnum.GRVY_SCRIPT_EMPTY);
            });
            future.get();
        }catch (Exception ex){
            System.out.println("cuptor :" + ex);
            ex.printStackTrace();
        }


    }
}
