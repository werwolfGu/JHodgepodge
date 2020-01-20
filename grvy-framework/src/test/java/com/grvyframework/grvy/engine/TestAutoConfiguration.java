package com.grvyframework.grvy.engine;

import com.grvyframework.grvy.GrvyScriptEngineExecutor;
import com.grvyframework.handle.IGrvyScriptResultHandler;
import com.grvyframework.model.GrvyRequest;
import com.grvyframework.model.GrvyResponse;
import com.grvyframework.spring.autoconfigure.GrvyAutoConfiguration;
import com.grvyframework.spring.container.SpringApplicationBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.script.Bindings;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author chengen.gu
 * @DATE 2020/1/20 8:11 下午
 */
public class TestAutoConfiguration {

    private AnnotationConfigApplicationContext context;

    @After
    public void tearDown() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Before
    public void before() {
        this.context = new AnnotationConfigApplicationContext();
        context.register(GrvyAutoConfiguration.class);
        context.refresh();
    }

    @Test
    public void testGrvyScriptEngine() throws ScriptException, ExecutionException, InterruptedException {

        List<String> card = Arrays.asList("1","2","3");
        GrvyScriptEngine grvyScriptEngine = this.context.getBean(GrvyScriptEngine.class);
        grvyScriptEngine.bingingGlobalScopeMapper("卡集合",card);
        grvyScriptEngine.bindingEngineScopeMapper("卡","2");
        System.out.println(grvyScriptEngine.getCurrentGrvyScriptEngine().getBindings(100));
        String script = " if (卡集合.contains(卡) ) return 卡 ";
        Object obj = grvyScriptEngine.eval(script);

        assert "2".equals(obj);
    }

    @Test
    public void testGrvyScriptEngineExecutor() throws Exception {

        List<String> mccList = new ArrayList<>();
        int seed = 10;
        for (int i = 0 ; i < seed ; i++ ){
            mccList.add(String.valueOf(i));
        }
        GrvyRequest request = new GrvyRequest();
        GrvyResponse response = new GrvyResponse();
        String script = " if (卡集合.contains(卡) ) return 卡 ";
        request.setEvalScript(script);
        Bindings bindings = new SimpleBindings();
        bindings.put("卡集合",mccList);
        Integer idx = ThreadLocalRandom.current().nextInt(seed);
        bindings.put("卡","2");
        request.setBindings(bindings);
        Class clazz = Thread.currentThread().getContextClassLoader().loadClass("com.grvyframework.grvy.engine.handle.DefaultGrvyScriptResulthandler");
        GrvyScriptEngineExecutor grvyScriptEngineExecutor = this.context.getBean(GrvyScriptEngineExecutor.class);
        IGrvyScriptResultHandler handle = (IGrvyScriptResultHandler) SpringApplicationBean.getBean(clazz);
        request.setGrvyScriptResultHandler(handle);
        CompletableFuture future = grvyScriptEngineExecutor.executor(request,response);

        assert "2".equals(future.get());
    }

    @Test
    public void testAsynGrvyScriptEngineExecutor() throws Exception {

        List<CompletableFuture> list = new ArrayList<>();
        List<String> mccList = new ArrayList<>();
        int seed = 1000;
        for (int i = 0 ; i < seed ; i++ ){
            mccList.add(String.valueOf(i));
        }
        for (int i = 0 ; i < seed ; i++ ){

            GrvyRequest request = new GrvyRequest();
            GrvyResponse response = new GrvyResponse();
            String script = " if (卡集合.contains(卡) ) return 卡 ";
            request.setEvalScript(script);
            Bindings bindings = new SimpleBindings();
            bindings.put("卡集合",mccList);
            Integer idx = ThreadLocalRandom.current().nextInt(seed);
            bindings.put("卡",idx.toString());
            request.setBindings(bindings);
            try {
                Class clazz = Thread.currentThread().getContextClassLoader().loadClass("com.grvyframework.grvy.engine.handle.DefaultGrvyScriptResulthandler");
                GrvyScriptEngineExecutor grvyScriptEngineExecutor = this.context.getBean(GrvyScriptEngineExecutor.class);
                IGrvyScriptResultHandler handle = (IGrvyScriptResultHandler) SpringApplicationBean.getBean(clazz);
                request.setGrvyScriptResultHandler(handle);
                CompletableFuture future = grvyScriptEngineExecutor.executor(request,response);
                list.add(future);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        CompletableFuture[] arr = list.toArray(new CompletableFuture[list.size()]);
        CompletableFuture future = CompletableFuture.allOf(arr);
        future.get();
    }
}
