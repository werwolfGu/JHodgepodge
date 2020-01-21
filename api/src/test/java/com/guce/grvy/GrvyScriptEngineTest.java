package com.guce.grvy;

import com.guce.groovy.engine.GrvyScriptEngine;
import com.guce.groovy.pool.GrvyEngineThreadPoolExecutor;
import org.junit.Test;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author chengen.gce
 * @date 2020-01-19 16:05
 * @description
 */
public class GrvyScriptEngineTest {

    
    @Test
    public void testGlobalBindingGrvyScript() throws ScriptException {
        List<String> card = Arrays.asList("1","2","3");

        GrvyScriptEngine.getInstance().bingingGlobalScopeMapper("卡集合",card);
        GrvyScriptEngine.getInstance().bindingEngineScopeMapper("卡","2");
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
            GrvyScriptEngine.getInstance().bingingGlobalScopeMapper("卡集合",card);
            GrvyScriptEngine.getInstance().bindingEngineScopeMapper("卡","4");
            //GrvyScriptEngine.getInstance().addThreadlocalEngineFieldMapper("卡集合",card);
            System.out.println("thread local script engine:" + GrvyScriptEngine.getInstance().getCurrentGrvyScriptEngine());
            String script = " if (卡集合.contains(卡) ||  \"4\".equals(卡)) return 卡 ";
            Object obj = null;
            try {
                obj = GrvyScriptEngine.getInstance().eval(script);
                System.out.println(obj);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
            assert "4".equals(obj);
        }, GrvyEngineThreadPoolExecutor.getPoolExecutor());
        CompletableFuture<Object> future1 = CompletableFuture.supplyAsync( () -> {
            GrvyScriptEngine.getInstance().bingingGlobalScopeMapper("卡集合",card);
            GrvyScriptEngine.getInstance().bindingEngineScopeMapper("卡","2");
            System.out.println("thread local script engine:" + GrvyScriptEngine.getInstance().getCurrentGrvyScriptEngine());

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
        }, GrvyEngineThreadPoolExecutor.getPoolExecutor());
        future.get();
        System.out.println("future1 : " + future1.get());
    }

    @Test
    public void complexExpressTest(){
        List<String > cardList = Arrays.asList("1","2","3","4");
        GrvyScriptEngine.getInstance().bingingGlobalScopeMapper("卡集合",cardList);

        GrvyScriptEngine.getInstance().bindingEngineScopeMapper("卡","4");
        GrvyScriptEngine.getInstance().bindingEngineScopeMapper("渠道","NET");
        GrvyScriptEngine.getInstance().bindingEngineScopeMapper("amt",7);
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

    @Test
    public void grvyExpressExec() throws ScriptException, ExecutionException, InterruptedException {
        List<String> mcc = Arrays.asList("1","2","3");


        StringBuilder str = new StringBuilder();
        str.append("卡类型.contains(\"2\") ");
        str.append(" && 卡logo.contains(\"123\") ");
        List<String> textList = new ArrayList<>();
        textList.add(str.toString());
        String text = "卡logo.contains(\"456\")";
        textList.add(text);

        text = "卡类型.contains(\"5\")";
        textList.add(text);

        text = "卡类型.contains(\"1\")";
        textList.add(text);

        text = "卡类型.contains(\"3\")";
        textList.add(text);

        text = "卡logo.contains(\"46\")";
        textList.add(text);

        List<CompletableFuture> futures = new ArrayList<>();
        for (int i = 0 ; i < 1000 ; i++ ){
            CompletableFuture future = CompletableFuture.runAsync(() -> {
                List mccList = Arrays.asList("1","2","3","5");
                GrvyScriptEngine.getInstance().bindingEngineScopeMapper("卡类型",mccList);
                List<String> cardLogoList = Arrays.asList("123","456");
                GrvyScriptEngine.getInstance().bindingEngineScopeMapper("卡logo",cardLogoList);
                long start = System.currentTimeMillis();

                int idx = ThreadLocalRandom.current().nextInt(textList.size());
                String script = textList.get(idx);
                Object obj = null;
                try {
                    obj = GrvyScriptEngine.getInstance().getCurrentGrvyScriptEngine().eval(script);
                } catch (ScriptException e) {
                    e.printStackTrace();
                }
                long time = System.currentTimeMillis() - start;

                System.out.println("script : " + script + "   obj: "+obj+" cost time:" +time);
            }, GrvyEngineThreadPoolExecutor.getPoolExecutor());
            futures.add(future);
        }
        CompletableFuture[] arr = futures.toArray(new CompletableFuture[futures.size()]);
        CompletableFuture allof = CompletableFuture.allOf(arr);
        allof.get();
    }

    @Test
    public void testMulitThreadExec() throws IOException {
        List<String> mccList = new ArrayList<>();
        GrvyScriptEngine.getInstance().bingingGlobalScopeMapper("普卡集合",mccList);

        int idx = 1000;
        for (int i = 0 ; i < idx ; i++ ){
            Integer mcc = ThreadLocalRandom.current().nextInt(idx);
            mccList.add(mcc.toString());
        }
        for (int i = 0 ; i < idx ; i++ ){
            Thread th = new MultiThread();
            GrvyEngineThreadPoolExecutor.getPoolExecutor().execute(th);
        }
    }
    public static class MultiThread extends Thread{

        @Override
        public void run() {

            String mccStr = "3";

            String scrpit = " if (普卡集合.contains(卡类型)) return 卡类型";
            try {
                Integer idx = ThreadLocalRandom.current().nextInt(1000);
               // GrvyScriptEngine.getInstance().getCurrentGrvyScriptEngine().put("普卡集合",mcc);
                GrvyScriptEngine.getInstance().getCurrentGrvyScriptEngine().put("卡类型",idx.toString());

                long start = System.currentTimeMillis();
                String flag = GrvyScriptEngine.getInstance().eval(scrpit);
                System.out.println("script engine:" + GrvyScriptEngine.getInstance().getCurrentGrvyScriptEngine()
                        + " thread name : " + Thread.currentThread().getName() + " flag : " + flag + " idx->" + idx
                        + "  cost time :" + (System.currentTimeMillis() - start));
                //flag = GrvyScriptEngine.getInstance().eval(scrpit);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                GrvyScriptEngine.getInstance().clearCurrEngineBinding();
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
            System.out.println("thread name : " + Thread.currentThread().getName()+" script engine:" + GrvyScriptEngine.getInstance().getCurrentGrvyScriptEngine());
            for (int i = 0 ; i < 100 ; i++ ){
                try {
                    GrvyScriptEngine.getInstance().bindingEngineScopeMapper("普卡集合",mcc);
                    GrvyScriptEngine.getInstance().getCurrentGrvyScriptEngine().put("卡类型",mccStr);

                    long start = System.currentTimeMillis();
                    flag = GrvyScriptEngine.getInstance().getCurrentGrvyScriptEngine().eval(scrpit);
                    System.out.println("thread name : " + Thread.currentThread().getName() + " flag : " + flag +"  cost time :" + (System.currentTimeMillis() - start));
                    //flag = GrvyScriptEngine.getInstance().eval(scrpit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    GrvyScriptEngine.getInstance().clearCurrEngineBinding();
                }

            }


        }
    }

    public static class MultiModifyValueClazz implements Runnable{

        @Override
        public void run() {
            System.out.println("script engine:" + GrvyScriptEngine.getInstance().getCurrentGrvyScriptEngine());
            GrvyScriptEngine.getInstance().getCurrentGrvyScriptEngine().put("卡类型","2");
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MultiThread multiThread = new MultiThread();

        MultiThread1 multiThread1 = new MultiThread1();
        MultiModifyValueClazz multiModifyValueClazz = new MultiModifyValueClazz();
        GrvyEngineThreadPoolExecutor.getPoolExecutor().execute(multiThread);
        GrvyEngineThreadPoolExecutor.getPoolExecutor().execute(multiThread1);
        GrvyEngineThreadPoolExecutor.getPoolExecutor().execute(multiModifyValueClazz);

        GrvyScriptEngineTest test = new GrvyScriptEngineTest();
        test.scriptEngineMultiThreadTest();
    }
}
