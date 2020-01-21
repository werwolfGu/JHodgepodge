package com.guce.groovy.executor;


import com.google.common.base.Stopwatch;
import com.guce.groovy.engine.GrvyScriptEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Bindings;
import java.util.concurrent.TimeUnit;

/**
 * @author chengen.gce
 * @date 2020-01-20 11:07
 * @description
 */
public abstract class GrvyScriptEngineExecutor {

    private static Logger logger = LoggerFactory.getLogger(GrvyScriptEngineExecutor.class);

    public <T> T executor(String script) throws Exception {

        Stopwatch watch = Stopwatch.createStarted();
        T result = null;
        try{

            GrvyScriptEngine.getInstance().bindingEngineScopeMapper(bindings());

            result = GrvyScriptEngine.getInstance().eval(script);
            long time = watch.elapsed(TimeUnit.MILLISECONDS);
            logger.info("executor script :{} ; cost time:{} ms.",script,time);

            return result;
        }catch (Throwable ex){

            logger.error("grvy executor error script-> :{}" ,script,ex);
            doException(ex);
        }finally {
            GrvyScriptEngine.getInstance().clearCurrEngineBinding();
            watch.stop();
        }
        return result;
    }

    protected abstract Bindings  bindings() throws Exception;

    /**
     * 异常处理方法
     * @param ex
     */
    public void doException(Throwable ex){

    }
}
