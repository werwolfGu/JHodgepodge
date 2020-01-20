package com.grvyframework.grvy.engine;

import com.grvyframework.handle.GrvyScriptEngineExecutor;
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
    public void testGrvyScriptEngine() throws ScriptException {

        List<String> card = Arrays.asList("1","2","3");
        GrvyScriptEngine grvyScriptEngine = this.context.getBean(GrvyScriptEngine.class);
        grvyScriptEngine.bingingGlobalScopeMapper("卡集合",card);
        grvyScriptEngine.bindingEngineScopeMapper("卡","2");

        String script = " if (卡集合.contains(卡) ) return 卡 ";
        Object obj = grvyScriptEngine.eval(script);
        assert "2".equals(obj);
    }

    @Test
    public void testGrvyScriptEngineExecutor() throws Exception {

        List<CompletableFuture> list = new ArrayList<>();
        for (int i = 0 ; i < 1000 ; i++ ){
            CompletableFuture future = CompletableFuture.runAsync(() -> {GrvyRequest request = new GrvyRequest();
                GrvyResponse response = new GrvyResponse();
                String script = " if (卡集合.contains(卡) ) return 卡 ";
                request.setEvalScript(script);
                Bindings bindings = new SimpleBindings();
                List<String> card = Arrays.asList("1","2","3");
                bindings.put("卡集合",card);
                bindings.put("卡","2");
                request.setBindings(bindings);
                GrvyScriptEngineExecutor grvyScriptEngineExecutor = null;
                try {
                    Class clazz = Thread.currentThread().getContextClassLoader().loadClass("com.grvyframework.grvy.engine.handle.DefaultGrvyScriptResulthandler");
                    grvyScriptEngineExecutor = this.context.getBean(GrvyScriptEngineExecutor.class);
                    IGrvyScriptResultHandler handle = (IGrvyScriptResultHandler) SpringApplicationBean.getBean(clazz);
                    request.setGrvyScriptResultHandler(handle);
                    grvyScriptEngineExecutor.executor(request,response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            list.add(future);
        }
        CompletableFuture[] arr = list.toArray(new CompletableFuture[list.size()]);
        CompletableFuture future = CompletableFuture.allOf(arr);
        future.get();
    }
}
