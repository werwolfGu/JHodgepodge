package com.grvyframework.grvy.engine;

import com.grvyframework.bootstrap.GrvySpringbootStartup;
import com.grvyframework.executor.GrvyScriptEngineExecutor;
import com.grvyframework.handle.IGrvyScriptResultHandler;
import com.grvyframework.model.GrvyRequest;
import com.grvyframework.model.GrvyResponse;
import com.grvyframework.model.GrvyRuleConfigEntry;
import com.grvyframework.reduce.Reduce;
import com.grvyframework.spring.container.SpringApplicationBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.script.Bindings;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chengen.gu
 * @date 2020-01-20 15:55
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={GrvySpringbootStartup.class})// 指定启动类
public class GrvyScriptEngineTest {

    private static ThreadPoolExecutor pool;

    static {
        int cpuCore = Runtime.getRuntime().availableProcessors() + 2;
        pool = new ThreadPoolExecutor(cpuCore,cpuCore * 2,3, TimeUnit.MINUTES,new LinkedBlockingQueue<>(10000));
    }
    @Autowired
    private GrvyScriptEngine grvyScriptEngine;

    @Autowired
    private GrvyScriptEngineExecutor grvyScriptEngineExecutor;

    @Test
    public void testGrvyScriptEngine() throws ScriptException {

        List<String> card = Arrays.asList("1","2","3");

        grvyScriptEngine.bingingGlobalScopeMapper("渠道","NET");
        grvyScriptEngine.bindingEngineScopeMapper("交易码","1101");
        grvyScriptEngine.bindingEngineScopeMapper("name","name is grvy");
        //GrvyScriptEngine.getInstance().addThreadlocalEngineFieldMapper("卡集合",card);


        String script = "    def list = [\"1101\",\"1411\",\"1121\",\"1131\"]\n" +
                        "    def channels = [\"NET\",\"NCUP\"]\n" +
                //"    def str = com.grvyframework.grvy.GrvyInvokeInterface.test(name)\n" +
                //"    println( \"str:->\" + str)\n" +
                        "    if (list.contains(交易码) && channels.contains(渠道)){\n" +
                        "        return 2\n" +
                        "    } ";
        for (int i = 0; i < 10; i++) {
            long time = System.currentTimeMillis();
            Object obj = grvyScriptEngine.eval(script);
            System.out.println(obj);
            System.out.println(Objects.isNull(obj));   ///  true
            System.out.println("cost time: " + (System.currentTimeMillis() - time));
        }

        // assert "2".equals(obj);
    }


    @Test
    public void testGrvyScriptEngineExecutor() throws Exception {

         List<String> cardList = new ArrayList<>();
         for (int i = 0 ; i < 1000 ; i++ ){
             cardList.add(String.valueOf(i));
         }
         List<CompletableFuture> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

             CompletableFuture future = CompletableFuture.runAsync( () -> {
                 GrvyRequest request = new GrvyRequest();
                 GrvyResponse response = new GrvyResponse();
                 String script = "def reg1 = ~'he*llo' \n" +
                         "def name = \"hello\" \n" +
                         "if (reg1.matcher(name) ) return true ";
                 List<GrvyRuleConfigEntry> grvyRuleInfoList = new ArrayList<>();


                 Bindings bindings = new SimpleBindings();
                 bindings.put("卡集合",cardList);
                 Integer idx = ThreadLocalRandom.current().nextInt(1000);
                 bindings.put("卡",idx.toString());
                 request.setBindings(bindings);
                 try {
                     request.setGrvyRuleInfoList(grvyRuleInfoList);
                     GrvyRuleConfigEntry ruleInfo = new GrvyRuleConfigEntry();
                     grvyRuleInfoList.add(ruleInfo);
                     Class clazz = Thread.currentThread().getContextClassLoader().loadClass("com.grvyframework.grvy.engine.handle.DefaultGrvyScriptResulthandler");
                     IGrvyScriptResultHandler handle = (IGrvyScriptResultHandler) SpringApplicationBean.getBean(clazz);                 ruleInfo.setGrvyScriptResultHandler(handle);
                     ruleInfo.setScript(script);
                     grvyScriptEngineExecutor.parallelExecutor(request,response, Reduce.firstOf(Objects::nonNull));
                 } catch (ClassNotFoundException e) {
                     e.printStackTrace();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }

             },pool);
             list.add(future);
             CompletableFuture[] arr = list.toArray(new CompletableFuture[list.size()]);
             CompletableFuture future1 =CompletableFuture.allOf(arr);
             future1.get();
         }

    }
}
