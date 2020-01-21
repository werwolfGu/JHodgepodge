package com.grvyframework.grvy;

import com.google.common.base.Stopwatch;
import com.grvyframework.config.GrvyExecutorConfig;
import com.grvyframework.exception.GrvyExceptionEnum;
import com.grvyframework.exception.GrvyExecutorException;
import com.grvyframework.executor.ThreadPoolFactory;
import com.grvyframework.grvy.engine.GrvyScriptEngine;
import com.grvyframework.handle.IGrvyScriptResultHandler;
import com.grvyframework.handle.impl.DefaultGrvyScriptResultHandler;
import com.grvyframework.model.*;
import com.grvyframework.reduce.Reduce;
import com.grvyframework.spring.container.SpringApplicationBean;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.util.*;
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

    public List<BaseScriptEvalResult> syncExecutor(GrvyRequest request , GrvyResponse response ,Reduce reduce) throws ScriptException {

        List<BaseScriptEvalResult> resultList = new ArrayList<>();

        List<GrvyRuleConfigEntry> ruleList = Optional.ofNullable(request)
                .map(GrvyRequest::getGrvyRuleInfoList)
                .orElse(null);

        CompletableFuture.supplyAsync( () -> {
            List<BaseScriptEvalResult> results = new ArrayList<>();
            for (GrvyRuleConfigEntry ruleInfo : ruleList ){

                GrvyRuleExecParam param = new GrvyRuleExecParam();
                param.setProMap(request.getProMap());
                param.setBindings(request.getBindings());
                param.setCalculateParam(request.getCalculateParam());
                param.setScript(ruleInfo.getScript());
                param.setGrvyScriptResultHandler(ruleInfo.getGrvyScriptResultHandler());
                BaseScriptEvalResult result = executor(param);

                if (reduce.execute(result)){
                    break;
                }
            }
            return results;
        },tpe);

        resultList.addAll(reduce.getResult());
        return resultList;
    }

    public List<BaseScriptEvalResult> asynExecutor(GrvyRequest request , GrvyResponse response,Reduce reduce) throws Exception {

        List<BaseScriptEvalResult> resultList = new ArrayList<>();

        List<GrvyRuleConfigEntry> ruleList = Optional.ofNullable(request)
                .map(GrvyRequest::getGrvyRuleInfoList)
                .orElse(null);
        List<CompletableFuture<BaseScriptEvalResult>> futureList = new ArrayList<>();

        ruleList.forEach( ruleInfo -> {

            CompletableFuture<BaseScriptEvalResult> future = CompletableFuture.supplyAsync( () -> {
                GrvyRuleExecParam param = new GrvyRuleExecParam();
                param.setProMap(request.getProMap());
                param.setBindings(request.getBindings());
                param.setCalculateParam(request.getCalculateParam());
                param.setScript(ruleInfo.getScript());
                param.setGrvyScriptResultHandler(ruleInfo.getGrvyScriptResultHandler());
                BaseScriptEvalResult result = executor(param);
                BaseScriptEvalResult evalResult = executor(param);
                return evalResult;

            } ,tpe);
            futureList.add(future);
        });
        CompletableFuture[] arr = futureList.toArray(new CompletableFuture[futureList.size()]);
        CompletableFuture future = CompletableFuture.allOf(arr);
        future.get();
        for (CompletableFuture<BaseScriptEvalResult> resultFuture : futureList){
            BaseScriptEvalResult value = resultFuture.get();
            if (reduce.execute(value)){
                break;
            }
        }

        resultList.addAll(reduce.getResult());

        return resultList;

    }

    private BaseScriptEvalResult executor(GrvyRuleExecParam ruleExecParam) {


        Stopwatch watch = Stopwatch.createStarted();
        BaseScriptEvalResult evalResult = null;

        String script = ruleExecParam.getScript();
        if (script == null){

            throw new GrvyExecutorException(GrvyExceptionEnum.GRVY_SCRIPT_EMPTY);
        }

        try{

            Optional.ofNullable(ruleExecParam)
                    .map(req -> req.getBindings())
                    .map( bindings -> {
                        grvyScriptEngine.bindingEngineScopeMapper(bindings);
                        return true;
                    });
            Optional.of(ruleExecParam).map(req -> req.getProMap())
                    .map( map -> {
                        for (Map.Entry<String,Object> entry : map.entrySet()){
                            grvyScriptEngine.bindingEngineScopeMapper(entry.getKey(),entry.getValue());
                        }
                        return true;
                    });

            IGrvyScriptResultHandler grvyScriptResultHandler = Optional.ofNullable(ruleExecParam.getGrvyScriptResultHandler())
                    .orElseGet(() -> SpringApplicationBean.getBean(DefaultGrvyScriptResultHandler.class));

            Object scriptResult = grvyScriptEngine.eval(script);

            evalResult = grvyScriptResultHandler.dealResult(scriptResult, ruleExecParam.getCalculateParam());


            if (logger.isInfoEnabled()) {
                logger.info("executor script :{{}} ; evalResult:{} ; cost time:{}."
                        , script, evalResult,watch.elapsed(TimeUnit.MILLISECONDS));

            }
            return evalResult;
        }catch (ScriptException e) {

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
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        tpe = ThreadPoolFactory.getThreadPoolExecutor(config);
    }
}
