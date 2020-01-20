package com.grvyframework.handle;

import com.google.common.base.Stopwatch;
import com.grvyframework.config.GrvyExecutorConfig;
import com.grvyframework.executor.ThreadPoolFactory;
import com.grvyframework.grvy.engine.GrvyScriptEngine;
import com.grvyframework.model.GrvyRequest;
import com.grvyframework.model.GrvyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author guchengen495
 * @date 2020-01-20 16:13
 * @description
 */
@Component
public class GrvyScriptEngineExecutor implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(GrvyScriptEngineExecutor.class);

    private ThreadPoolExecutor executor ;

    @Autowired
    private GrvyScriptEngine grvyScriptEngine;

    public Object executor(GrvyRequest request , GrvyResponse response) throws Exception {

        Stopwatch watch = Stopwatch.createStarted();
        String script = request.getEvalScript();
        if (script == null){
            return null;
        }
        Object result = null;
        try{
            Optional.ofNullable(request)
                    .map(req -> req.getBindings())
                    .map( bindings -> {
                        grvyScriptEngine.bindingEngineScopeMapper(bindings);
                        return true;
                    });
            Optional.of(request).map(req -> req.getProMap())
                    .map( map -> {
                        for (Map.Entry<String,Object> entry : map.entrySet()){
                            grvyScriptEngine.bindingEngineScopeMapper(entry.getKey(),entry.getValue());
                        }
                        return true;
                    });

            Object scriptResult = grvyScriptEngine.eval(script);
            IGrvyScriptResultHandler grvyScriptResultHandler = request.getGrvyScriptResultHandler();
            result = grvyScriptResultHandler.dealResult(scriptResult);
            long time = watch.elapsed(TimeUnit.MILLISECONDS);
            logger.info("executor script :{} ; result:{} ;  cost time:{} ms.",script,result,time);
            return result;
        }catch (Throwable ex){

            logger.error("grvy executor error script-> :{}" ,script,ex);
            throw ex;

        }finally {
            grvyScriptEngine.clearCurrEngineBinding();
            watch.stop();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        GrvyExecutorConfig config = new GrvyExecutorConfig();
        executor = ThreadPoolFactory.getThreadPoolExecutor(config);
    }
}
