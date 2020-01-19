package com.guce.grvy;

import com.guce.groovy.GrvyEngineThreadPool;
import com.guce.groovy.engine.GrvyScriptEngine;
import org.junit.Test;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author guchengen495
 * @date 2020-01-19 16:05
 * @description
 */
public class GrvyScriptEngineTest {

    @Test
    public void testGlobalBindingGrvyScript() throws ScriptException {
        List<String> card = Arrays.asList("1","2","3");

        GrvyScriptEngine.getInstance().addGlobalFieldMapper("卡集合",card);
        GrvyScriptEngine.getInstance().addThreadlocalEngineFieldMapper("卡","2");
        //GrvyScriptEngine.getInstance().addThreadlocalEngineFieldMapper("卡集合",card);

        String script = " if (卡集合.contains(卡) ) return 卡 ";
        Object obj = GrvyScriptEngine.getInstance().eval(script);
        System.out.println(obj);
        assert "2".equals(obj);
    }

    @Test
    public void scriptEngineMultiThreadTest() throws ExecutionException, InterruptedException {
        List<String> card = Arrays.asList("1","2","3");

        CompletableFuture future = CompletableFuture.runAsync( () -> {
            GrvyScriptEngine.getInstance().addGlobalFieldMapper("卡集合",card);
            GrvyScriptEngine.getInstance().addThreadlocalEngineFieldMapper("卡","4");
            //GrvyScriptEngine.getInstance().addThreadlocalEngineFieldMapper("卡集合",card);

            String script = " if (卡集合.contains(卡) ||  \"4\".equals(卡)) return 卡 ";
            Object obj = null;
            try {
                obj = GrvyScriptEngine.getInstance().eval(script);
                System.out.println(obj);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
            assert "4".equals(obj);
        },GrvyEngineThreadPool.getPoolExecutor());
        CompletableFuture<Object> future1 = CompletableFuture.supplyAsync( () -> {
            GrvyScriptEngine.getInstance().addGlobalFieldMapper("卡集合",card);
            GrvyScriptEngine.getInstance().addThreadlocalEngineFieldMapper("卡","2");
            //GrvyScriptEngine.getInstance().addThreadlocalEngineFieldMapper("卡集合",card);

            String script = " if (卡集合.contains(卡) ) return 卡 ";
            Object obj = null;
            try {
                obj = GrvyScriptEngine.getInstance().eval(script);
                System.out.println(obj);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
            assert "2".equals(obj);
            return obj;
        },GrvyEngineThreadPool.getPoolExecutor());
        future.get();
        System.out.println("future1 : " + future1.get());
    }

    @Test
    public void complexExpressTest(){
        List<String > cardList = Arrays.asList("1","2","3","4");
        GrvyScriptEngine.getInstance().addGlobalFieldMapper("卡集合",cardList);

        GrvyScriptEngine.getInstance().addThreadlocalEngineFieldMapper("卡","4");
        GrvyScriptEngine.getInstance().addThreadlocalEngineFieldMapper("渠道","NET");
        GrvyScriptEngine.getInstance().addThreadlocalEngineFieldMapper("amt",7);
        //GrvyScriptEngine.getInstance().addThreadlocalEngineFieldMapper("卡集合",card);

        String script = " def list = [\"12\",\"34\",\"56\",\"78\",\"90\"]\n" +
                "    def channelList = [\"NET\",\"CUP\"]\n" +
                "    if (list.contains(\"34\") && 卡集合.contains(卡) && channelList.contains(渠道)){\n" +
                "        return 1;\n" +
                "    }";
        String script1 = " def list = [\"12\",\"34\",\"56\",\"78\",\"90\"]\n" +
                "    def channelList = [\"NET\",\"CUP\"]\n" +
                "    if (list.contains(\"34\") && 卡集合.contains(卡) && channelList.contains(渠道)){\n" +
                "        return 1;\n" +
                "    }";
        String script3 = " 4 * amt ";
        Object obj = null;
        long totalTime = 0;
        for (int i = 0 ; i < 100 ;i++) {
            long start = System.currentTimeMillis();
            try {
                obj = GrvyScriptEngine.getInstance().eval(script);
                obj = GrvyScriptEngine.getInstance().eval(script1);
                obj = GrvyScriptEngine.getInstance().eval(script3);
                long costTime = System.currentTimeMillis() - start;
                totalTime += costTime;
                System.out.println("OBJ :" + obj + " cost time:" + costTime);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }
        System.out.println("totalTime :" + totalTime);

    }
    public static class MultiThread extends Thread{

        @Override
        public void run() {
            List<String> mcc = Arrays.asList("1","2","3");
            String mccStr = "3";

            String scrpit = " if (普卡集合.contains(卡类型)) return 1";
            Object flag = null;
            System.out.println("script engine:" + GrvyScriptEngine.getInstance().getThreadLocalGrvyScriptEngine());
            for (int i = 0 ; i < 100 ; i++ ){
                try {
                    GrvyScriptEngine.getInstance().getThreadLocalGrvyScriptEngine().put("普卡集合",mcc);
                    GrvyScriptEngine.getInstance().getThreadLocalGrvyScriptEngine().put("卡类型",mccStr);

                    long start = System.currentTimeMillis();
                    flag = GrvyScriptEngine.getInstance().getThreadLocalGrvyScriptEngine().eval(scrpit);
                    System.out.println("thread name : " + Thread.currentThread().getName() + " flag : " + flag +"  cost time :" + (System.currentTimeMillis() - start));
                    //flag = GrvyScriptEngine.getInstance().eval(scrpit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    GrvyScriptEngine.getInstance().getThreadLocalGrvyScriptEngine().getContext()
                            .setBindings(GrvyScriptEngine.getInstance()
                                    .getThreadLocalGrvyScriptEngine()
                                    .createBindings(), ScriptContext.ENGINE_SCOPE);
                }

            }


        }
    }

    public static class MultiThread1 extends Thread{

        @Override
        public void run() {
            List<String> mcc = Arrays.asList("1","2","3");
            String mccStr = "7";

            String scrpit = " if (普卡集合.contains(卡类型)) return 1";
            Object flag = null;
            System.out.println("thread name : " + Thread.currentThread().getName()+" script engine:" + GrvyScriptEngine.getInstance().getThreadLocalGrvyScriptEngine());
            for (int i = 0 ; i < 100 ; i++ ){
                try {
                    GrvyScriptEngine.getInstance().addThreadlocalEngineFieldMapper("普卡集合",mcc);
                    GrvyScriptEngine.getInstance().getThreadLocalGrvyScriptEngine().put("卡类型",mccStr);

                    long start = System.currentTimeMillis();
                    flag = GrvyScriptEngine.getInstance().getThreadLocalGrvyScriptEngine().eval(scrpit);
                    System.out.println("thread name : " + Thread.currentThread().getName() + " flag : " + flag +"  cost time :" + (System.currentTimeMillis() - start));
                    //flag = GrvyScriptEngine.getInstance().eval(scrpit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    GrvyScriptEngine.getInstance().getThreadLocalGrvyScriptEngine().getContext()
                            .setBindings(GrvyScriptEngine.getInstance()
                                    .getThreadLocalGrvyScriptEngine()
                                    .createBindings(), ScriptContext.ENGINE_SCOPE);
                }

            }


        }
    }

    public static class MultiModifyValueClazz implements Runnable{

        @Override
        public void run() {
            System.out.println("script engine:" + GrvyScriptEngine.getInstance().getThreadLocalGrvyScriptEngine());
            GrvyScriptEngine.getInstance().getThreadLocalGrvyScriptEngine().put("卡类型","2");
        }
    }

    public static void main(String[] args) {
        MultiThread multiThread = new MultiThread();

        MultiThread1 multiThread1 = new MultiThread1();
        MultiModifyValueClazz multiModifyValueClazz = new MultiModifyValueClazz();
        GrvyEngineThreadPool.getPoolExecutor().execute(multiThread);
        GrvyEngineThreadPool.getPoolExecutor().execute(multiThread1);
        GrvyEngineThreadPool.getPoolExecutor().execute(multiModifyValueClazz);
    }
}
