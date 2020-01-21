package com.guce.grvy;

import com.guce.groovy.engine.GrvyScriptEngine;
import com.guce.groovy.executor.GrvyScriptEngineExecutor;
import com.guce.groovy.pool.GrvyEngineThreadPoolExecutor;
import org.junit.Test;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author chengen.gce
 * @date 2020-01-20 11:19
 * @description
 */
public class GrvyScriptEngineExecutorTest {

    @Test
    public void testExecutor() throws Exception {

        String script = " if (卡集合.contains(卡) ) return 卡 ";;
        String result = new GrvyScriptEngineExecutor() {
            @Override
            protected Bindings bindings() throws Exception {
                List<String> card = Arrays.asList("1","2","3");
                GrvyScriptEngine.getInstance().bingingGlobalScopeMapper("卡集合",card);
                GrvyScriptEngine.getInstance().bindingEngineScopeMapper("卡","2");
                Bindings bindings = new SimpleBindings();
                bindings.put("卡集合",card);
                bindings.put("卡","2");
                return bindings;
            }
        }.executor(script);

        System.out.println("result :" + result );

    }

    @Test
    public void testMutliThreadExecutor() throws ExecutionException, InterruptedException {
        List<String> mccList = new ArrayList<>();

        long start = System.currentTimeMillis();
        final int idx = 10000;
        for (int i = 0 ; i < idx ; i++ ){
            Integer mcc = ThreadLocalRandom.current().nextInt(idx);
            mccList.add(mcc.toString());
        }
        List<CompletableFuture> futureList = new ArrayList<>(idx);

        for (int i = 0 ; i < idx ; i++ ){

            CompletableFuture future = CompletableFuture.supplyAsync( () -> {

                GrvyScriptEngine.getInstance().bingingGlobalScopeMapper("普卡集合",mccList);
                String scrpit = " if (普卡集合.contains(卡类型)) return 卡类型";
                String result = null;
                try {
                    result = new GrvyScriptEngineExecutor(){
                        @Override
                        protected Bindings bindings() throws Exception {
                            Integer id = ThreadLocalRandom.current().nextInt(idx);
                            Bindings bindings = new SimpleBindings();
                            bindings.put("卡类型",id.toString());
                            return bindings;
                        }
                    }.executor(scrpit);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;

            },GrvyEngineThreadPoolExecutor.getPoolExecutor());
            futureList.add(future);
        }
        CompletableFuture[] arr = futureList.toArray(new CompletableFuture[futureList.size()]);
        CompletableFuture future = CompletableFuture.allOf(arr);

        future.get();

        for (int i = 0 ; i < futureList.size() ; i++ ){
            CompletableFuture future1 = futureList.get(i);
            System.out.println( i + " -> " + future1.get());
        }
        System.out.println("total time : " + (System.currentTimeMillis() - start));
    }
}
