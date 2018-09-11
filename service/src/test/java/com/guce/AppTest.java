package com.guce;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    private Map<String,Object> localCache = new ConcurrentHashMap<>();

    public Object get(String key){
        Object result = null;
        if(localCache.containsKey(key)){
            result = localCache.get(key);
        }else{
            Object obj = getInfo4DB();
            //todo  组装成 result 结果
            result = wrapperResult(obj);
            localCache.put(key,result);
        }
        return result;
    }

    public Object wrapperResult(Object obj){
        //todo

        return obj;
    }

    public Object getInfo4DB(){

        return new Object();
    }
}
