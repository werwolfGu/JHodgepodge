package com.grvyframework.grvy.engine;

import com.grvyframework.bootstrap.SpringbootStartup;
import com.grvyframework.handle.GrvyScriptEngineExecutor;
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
import java.util.Arrays;
import java.util.List;

/**
 * @author chengen.gu
 * @date 2020-01-20 15:55
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={SpringbootStartup.class})// 指定启动类
public class GrvyScriptEngineTest {

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

        GrvyRequest request = new GrvyRequest();
        GrvyResponse response = new GrvyResponse();
        String script = " if (卡集合.contains(卡) ) return 卡 ";
        request.setEvalScript(script);
        Bindings bindings = new SimpleBindings();
        List<String> card = Arrays.asList("1","2","3");
        bindings.put("卡集合",card);
        bindings.put("卡","2");
        request.setBindings(bindings);
        Class clazz = Thread.currentThread().getContextClassLoader().loadClass("com.grvyframework.grvy.engine.handle.DefaultGrvyScriptResulthandler");
        IGrvyScriptResultHandler handle = (IGrvyScriptResultHandler) SpringApplicationBean.getBean(clazz);
        request.setGrvyScriptResultHandler(handle);
        grvyScriptEngineExecutor.executor(request,response);
    }
}
