package com.grvyframework.executor;

import com.google.common.base.Stopwatch;
import com.grvyframework.config.GrvyExecutorConfig;
import com.grvyframework.exception.GrvyExceptionEnum;
import com.grvyframework.exception.GrvyExecutorException;
import com.grvyframework.grvy.GrvyScriptEngineExeEnum;
import com.grvyframework.grvy.engine.GrvyScriptEngine;
import com.grvyframework.grvy.engine.GrvyScriptEngineClient;
import com.grvyframework.handle.IGrvyScriptResultHandler;
import com.grvyframework.handle.impl.DefaultGrvyScriptResultHandler;
import com.grvyframework.model.BaseScriptEvalResult;
import com.grvyframework.model.GrvyRequest;
import com.grvyframework.model.GrvyResponse;
import com.grvyframework.model.GrvyRuleConfigEntry;
import com.grvyframework.model.GrvyRuleExecParam;
import com.grvyframework.pool.ThreadPoolFactory;
import com.grvyframework.reduce.Reduce;
import com.grvyframework.spring.container.SpringApplicationBean;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author chengen.gu
 * @date 2020-01-20 16:13
 * @description
 */
@Component("grvyScriptEngineExecutor")
public class GrvyScriptEngineExecutor implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(GrvyScriptEngineExecutor.class);

    @Getter
    private ExecutorService tpe ;

    @Resource(name = "grvyScriptEngine")
    private GrvyScriptEngine grvyScriptEngine;

    @Resource(name = "grvyExecutorConfig")
    private GrvyExecutorConfig config ;

    @Resource(name = "grvyScriptEngineClient")
    private GrvyScriptEngineClient grvyScriptEngineClient;

    private final static long DEFAULT_ASYNC_TIMEOUT = 3;

    private final static long DEFAULT_SERIAL_ASYNC_TIMEOUT = 5;

    /**
     * 串行执行规则
     *
     * @param request
     * @param response
     * @param reduce
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public List<BaseScriptEvalResult> serialExecutor(GrvyRequest request, GrvyResponse response
            , Reduce<BaseScriptEvalResult> reduce) throws ExecutionException, InterruptedException {

        return serialExecutor(request, response, GrvyScriptEngineExeEnum.SCRIPT_COMPILER, reduce);
    }
    /**
     * 串行执行规则
     * @param request
     * @param response
     * @param reduce
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public List<BaseScriptEvalResult> serialExecutor(GrvyRequest request, GrvyResponse response, GrvyScriptEngineExeEnum grvyScriptEngineExeEnum
            , Reduce<BaseScriptEvalResult> reduce) throws InterruptedException, ExecutionException {

        List<BaseScriptEvalResult> resultList = new ArrayList<>();

        List<GrvyRuleConfigEntry> ruleList = Optional.ofNullable(request)
                .map(GrvyRequest::getGrvyRuleInfoList)
                .orElse(null);

        if (CollectionUtils.isEmpty(ruleList)){
            logger.error(" List<GrvyRuleConfigEntry> ruleList 规则参数不能为空！");
            throw new GrvyExecutorException(GrvyExceptionEnum.Grvy_ILLEGAL_ARGUMMENT_ERROR);
        }
        //默认取第一个有效值
        if (reduce == null){
            reduce = Reduce.firstOf(Objects::nonNull);
        }
        final Reduce<BaseScriptEvalResult> finalReduce = reduce;

        CompletableFuture future = CompletableFuture.runAsync( () -> {

            for (GrvyRuleConfigEntry ruleInfo : ruleList ){
                GrvyRuleExecParam param = wrapperGrvyRuleParam(request,ruleInfo);
                try {
                    BaseScriptEvalResult result = this.executor(param, grvyScriptEngineExeEnum);

                    if (result != null && finalReduce.execute(result)){
                        break;
                    }
                }catch(GrvyExecutorException ex){
                    logger.warn("####grvy脚本执行异常:{}",param,ex);
                }

            }
        },tpe);

        try {
            future.get(DEFAULT_SERIAL_ASYNC_TIMEOUT,TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            logger.error("GrvyScript executor time out",e);
            return null;
        }
        resultList.addAll(finalReduce.getResult());
        return resultList;
    }

    /**
     * 并行执行规则
     * @param request
     * @param response
     * @param reduce
     * @return
     * @throws Exception
     */
    public List<BaseScriptEvalResult> parallelExecutor(GrvyRequest request , GrvyResponse response , Reduce<BaseScriptEvalResult> reduce) throws Exception {

        List<BaseScriptEvalResult> resultList = new ArrayList<>();

        List<GrvyRuleConfigEntry> ruleList = Optional.ofNullable(request)
                .map(GrvyRequest::getGrvyRuleInfoList)
                .orElse(null);

        List<CompletableFuture<BaseScriptEvalResult>> futureList = new ArrayList<>();

        assert ruleList != null;

        ruleList.forEach(ruleInfo -> {

            CompletableFuture<BaseScriptEvalResult> future = CompletableFuture.supplyAsync( () -> {

                GrvyRuleExecParam param = wrapperGrvyRuleParam(request,ruleInfo);
                return this.executor(param);

            } ,tpe);
            futureList.add(future);
        });
        CompletableFuture[] arr = futureList.toArray(new CompletableFuture[0]);
        CompletableFuture future = CompletableFuture.allOf(arr);
        future.get(DEFAULT_ASYNC_TIMEOUT,TimeUnit.SECONDS);

        for (CompletableFuture<BaseScriptEvalResult> resultFuture : futureList){
            try {
                BaseScriptEvalResult value = resultFuture.get();
                if (reduce.execute(value)){
                    break;
                }
            }catch(GrvyExecutorException ex){
                logger.warn("####grvy脚本执行异常",ex);
            }

        }

        resultList.addAll(reduce.getResult());

        return resultList;

    }

    private GrvyRuleExecParam wrapperGrvyRuleParam(GrvyRequest request,GrvyRuleConfigEntry ruleEntitry){
        GrvyRuleExecParam param = new GrvyRuleExecParam();
        param.setProMap(request.getProMap());
        param.setBindings(request.getBindings());
        param.setCalculateParam(request.getCalculateParam());
        param.setScript(ruleEntitry.getScript());
        param.setGrvyScriptResultHandler(ruleEntitry.getGrvyScriptResultHandler());
        param.setGrvyResultClazzPath(ruleEntitry.getGrvyResultClazzPath());
        return param;
    }

    private BaseScriptEvalResult executor(GrvyRuleExecParam ruleExecParam){
        return executor(ruleExecParam, GrvyScriptEngineExeEnum.SCRIPT_COMPILER);
    }

    private BaseScriptEvalResult executor(GrvyRuleExecParam ruleExecParam, GrvyScriptEngineExeEnum executorEnum) {


        Stopwatch watch = Stopwatch.createStarted();
        BaseScriptEvalResult evalResult ;

        String script = Optional.ofNullable(ruleExecParam)
                .map(GrvyRuleExecParam::getScript)
                .orElse(null);
        if (script == null){

            throw new GrvyExecutorException(GrvyExceptionEnum.GRVY_SCRIPT_EMPTY);
        }

        try{

            ScriptContext context = new SimpleScriptContext();

            Optional.of(ruleExecParam)
                    .map(GrvyRuleExecParam::getBindings)
                    .map(bindings -> {
                        grvyScriptEngine.bindingEngineScopeMapper(bindings);
                        context.setBindings(bindings,ScriptContext.ENGINE_SCOPE);
                        return true;
                    });

            Optional.of(ruleExecParam).map(GrvyRuleExecParam::getProMap)
                    .map( map -> {
                        for (Map.Entry<String,Object> entry : map.entrySet()){
                            grvyScriptEngine.bindingEngineScopeMapper(entry.getKey(),entry.getValue());

                            context.setAttribute(entry.getKey(),entry.getValue(),ScriptContext.ENGINE_SCOPE);
                        }
                        return true;
                    });

            IGrvyScriptResultHandler grvyScriptResultHandler = Optional.of(ruleExecParam)
                    .map(GrvyRuleExecParam::getGrvyScriptResultHandler)

                    .orElseGet(() -> {

                        IGrvyScriptResultHandler handler = null;
                        try {
                            if (ruleExecParam.getGrvyResultClazzPath() != null){

                                Class clazz = Thread.currentThread().getContextClassLoader()
                                        .loadClass(ruleExecParam.getGrvyResultClazzPath());

                                handler = (IGrvyScriptResultHandler) SpringApplicationBean.getBean(clazz);
                                if (handler == null){
                                    logger.warn("该类不在spring container里 直接实例化此类 ：{}",ruleExecParam.getGrvyResultClazzPath());
                                    handler = (IGrvyScriptResultHandler) clazz.newInstance();
                                }
                            }
                        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                            logger.error("######无法找到结果处理类，将使用默认grvy结果处理类; classpath:{}"
                                    ,ruleExecParam.getGrvyResultClazzPath(),e);
                        }
                        if (handler == null){
                            logger.warn("######grvy result 使用默认grvy结果处理类;");
                            handler=  SpringApplicationBean.getBean(DefaultGrvyScriptResultHandler.class);

                        }
                        return handler;
                    } );

            Object scriptResult;
            if (GrvyScriptEngineExeEnum.SCRIPT_COMPILER == executorEnum){
                scriptResult = grvyScriptEngineClient.eval(script,context);
            }else {
                scriptResult = grvyScriptEngine.eval(script, context);

            }

            evalResult = grvyScriptResultHandler.dealResult(scriptResult, ruleExecParam.getCalculateParam());

            if (Objects.isNull(evalResult)) {
                grvyScriptResultHandler.dealResult(scriptResult, ruleExecParam.getCalculateParam()
                        , context.getBindings(ScriptContext.ENGINE_SCOPE));
            }

            if (logger.isInfoEnabled()) {

                logger.info("executor script :\n{{}}  ; ######evalResult : {} ; cost time : {}."
                        , script, evalResult,watch.elapsed(TimeUnit.MILLISECONDS));

            }
            return evalResult;
        }catch (ScriptException e) {

            logger.error("script exception: script:\n {{}} ; param:{} ; "
                    ,script,Optional.
                            ofNullable(grvyScriptEngine.getCurrentGrvyScriptEngine().getBindings(ScriptContext.ENGINE_SCOPE))
                            .map(Map::entrySet).orElse(null),e);
            throw new GrvyExecutorException(GrvyExceptionEnum.GRVY_EXECUTOR_ERROR,e);

        } catch (IllegalArgumentException e){

            logger.error("script exception: script:{{}} ; param:{} ; "
                    ,script,grvyScriptEngine.getCurrentGrvyScriptEngine().getBindings(ScriptContext.ENGINE_SCOPE),e);
            throw new GrvyExecutorException(GrvyExceptionEnum.Grvy_ILLEGAL_ARGUMMENT_ERROR,e);
        } catch (Exception e){

            logger.error("script exception: script:{{}} ; param:{} ; "
                    ,script,grvyScriptEngine.getCurrentGrvyScriptEngine().getBindings(ScriptContext.ENGINE_SCOPE),e);
            throw new GrvyExecutorException(GrvyExceptionEnum.GRVY_EXECUTOR_UNKNOWN_ERROR,e);

        }finally {
            grvyScriptEngine.clearCurrEngineBinding();
            watch.stop();
        }
    }

    @Override
    public void afterPropertiesSet() {

        tpe = ThreadPoolFactory.getThreadPoolExecutor(config);
    }
}
