package com.grvyframework.grvy.engine;

import com.grvyframework.bootstrap.SpringbootStartup;
import com.grvyframework.grvy.GrvyScriptEngineExecutor;
import com.grvyframework.handle.IGrvyScriptResultHandler;
import com.grvyframework.model.GrvyRequest;
import com.grvyframework.model.GrvyResponse;
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
import java.util.concurrent.*;

/**
 * @author chengen.gu
 * @date 2020-01-20 15:55
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={SpringbootStartup.class})// 指定启动类
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

        grvyScriptEngine.bingingGlobalScopeMapper("卡集合",card);
        grvyScriptEngine.bindingEngineScopeMapper("卡","2");
        //GrvyScriptEngine.getInstance().addThreadlocalEngineFieldMapper("卡集合",card);

        String script = " if (卡集合.contains(卡) ) return 卡 ";
        Object obj = grvyScriptEngine.eval(script);
        System.out.println(obj);
        assert "2".equals(obj);
    }

    @Test
    public void testGrvyScriptEngineExecutor() throws Exception {

         List<String> cardList = new ArrayList<>();
         for (int i = 0 ; i < 1000 ; i++ ){
             cardList.add(String.valueOf(i));
         }
         List<CompletableFuture> list = new ArrayList<>();
         for (int i = 0 ; i < 1000 ; i++ ){

             CompletableFuture future = CompletableFuture.runAsync( () -> {
                 GrvyRequest request = new GrvyRequest();
                 GrvyResponse response = new GrvyResponse();
                 String script = " if (卡集合.contains(卡) ) return 卡 ";
                 request.setEvalScript(script);
                 Bindings bindings = new SimpleBindings();
                 bindings.put("卡集合",cardList);
                 Integer idx = ThreadLocalRandom.current().nextInt(1000);
                 bindings.put("卡",idx.toString());
                 request.setBindings(bindings);
                 try {
                     Class clazz = Thread.currentThread().getContextClassLoader().loadClass("com.grvyframework.grvy.engine.handle.DefaultGrvyScriptResulthandler");
                     IGrvyScriptResultHandler handle = (IGrvyScriptResultHandler) SpringApplicationBean.getBean(clazz);
                     request.setGrvyScriptResultHandler(handle);
                     grvyScriptEngineExecutor.asynExecutor(request,response);
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
