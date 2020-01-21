package com.grvyframework.grvy;

import com.google.common.base.Stopwatch;
import com.grvyframework.config.GrvyExecutorConfig;
import com.grvyframework.exception.GrvyExceptionEnum;
import com.grvyframework.exception.GrvyExecutorException;
import com.grvyframework.executor.ThreadPoolFactory;
import com.grvyframework.grvy.engine.GrvyScriptEngine;
import com.grvyframework.handle.IGrvyScriptResultHandler;
import com.grvyframework.handle.impl.DefaultGrvyScriptResultHandler;
import com.grvyframework.model.BaseScriptEvalResult;
import com.grvyframework.model.GrvyRequest;
import com.grvyframework.model.GrvyResponse;
import com.grvyframework.spring.container.SpringApplicationBean;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chengen.gu
 * @date 2020-01-20 16:13
 * @description
 */
@Component
public class GrvyScriptEngineExecutor implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(GrvyScriptEngineExecutor.class);

    @Getter
    private ThreadPoolExecutor tpe ;

    @Autowired
    private GrvyScriptEngine grvyScriptEngine;

    @Autowired
    private GrvyExecutorConfig config ;

    public CompletableFuture<BaseScriptEvalResult> executor(GrvyRequest request , GrvyResponse response) throws Exception {


        String script = request.getEvalScript();
        if (script == null){

            throw new GrvyExecutorException(GrvyExceptionEnum.GRVY_SCRIPT_EMPTY);
        }

        CompletableFuture future = CompletableFuture.supplyAsync( () -> {

            Stopwatch watch = Stopwatch.createStarted();
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
            try{
                IGrvyScriptResultHandler grvyScriptResultHandler = Optional.ofNullable(request.getGrvyScriptResultHandler())
                        .orElseGet( () -> SpringApplicationBean.getBean(DefaultGrvyScriptResultHandler.class));

                Object scriptResult = grvyScriptEngine.eval(script);

                BaseScriptEvalResult evalResult = grvyScriptResultHandler.dealResult(scriptResult,request.getCalculateParam());

                response.setScriptEvalResult(evalResult);

                if (logger.isInfoEnabled()){
                    logger.info("executor script :{{}} ; evalResult:{} ;  cost time:{} ms."
                            ,script,evalResult,watch.elapsed(TimeUnit.MILLISECONDS));

                }
                return evalResult;
            } catch (ScriptException e) {

                logger.error("script exception: script:{{}} ; param:{} ; "
                        ,script,grvyScriptEngine.getCurrentGrvyScriptEngine().getBindings(ScriptContext.ENGINE_SCOPE),e);
                throw new GrvyExecutorException(GrvyExceptionEnum.GRVY_EXECUTOR_ERROR);

            } catch (IllegalArgumentException e){

                logger.error("script exception: script:{{}} ; param:{} ; "
                        ,script,grvyScriptEngine.getCurrentGrvyScriptEngine().getBindings(ScriptContext.ENGINE_SCOPE),e);
                throw new GrvyExecutorException(GrvyExceptionEnum.Grvy_ILLEGAL_ARGUMMENT_ERROR);
            } catch (Exception e){

                logger.error("script exception: script:{{}} ; param:{} ; "
                        ,script,grvyScriptEngine.getCurrentGrvyScriptEngine().getBindings(ScriptContext.ENGINE_SCOPE),e);
                throw new GrvyExecutorException(GrvyExceptionEnum.GRVY_EXECUTOR_UNKNOWN_ERROR);

            }finally {

                grvyScriptEngine.clearCurrEngineBinding();
                if (watch != null){
                    watch.stop();
                }
                watch = null;
            }
            } ,tpe);

        return future;

    }

    @Override
    public void afterPropertiesSet() throws Exception {

        tpe = ThreadPoolFactory.getThreadPoolExecutor(config);
    }
}
