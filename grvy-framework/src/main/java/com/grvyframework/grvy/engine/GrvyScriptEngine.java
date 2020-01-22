package com.grvyframework.grvy.engine;

import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.util.Optional;

/**
 * @author chengen.gu
 * @date 2020-01-20 15:52
 * @description
 */
@Component("grvyScriptEngine")
public class GrvyScriptEngine {

    private final static ScriptEngineManager factory = new ScriptEngineManager();

    @Getter
    private final ScriptEngine scriptEngine;

    private static ThreadLocal<ScriptEngine> scriptEngineHolder =
            ThreadLocal.withInitial(() -> factory.getEngineByName("groovy"));

    private GrvyScriptEngine(){
        this.scriptEngine = factory.getEngineByName("groovy");
    }

    /**
     * 执行脚本
     * @param script
     * @param <T>
     * @return
     * @throws ScriptException
     */
    public <T> T eval(String script) throws ScriptException {

        Object result = scriptEngineHolder.get().eval(script);
        return (T)result;
    }

    /**
     * 写入全局 scope Binding
     * @param key
     * @param orgField
     * @return
     */
    public GrvyScriptEngine bingingGlobalScopeMapper(String key,Object orgField){
        Bindings bindings = scriptEngine.getBindings(ScriptContext.GLOBAL_SCOPE);
        if (bindings == null){

            synchronized (scriptEngine){
                if (bindings == null){
                    bindings = scriptEngine.createBindings();
                    scriptEngine.setBindings(bindings,ScriptContext.GLOBAL_SCOPE);
                }
            }

        }
        bindings.put(key,orgField);

        return this;
    }

    public ScriptEngine getCurrentGrvyScriptEngine(){
        return scriptEngineHolder.get();
    }

    /**
     * 写入Engine  scope Binding
     * @param key
     * @param orgField
     * @return
     */
    public  GrvyScriptEngine bindingEngineScopeMapper(String key,Object orgField){
        scriptEngineHolder.get().put(key,orgField);
        return this;
    }
    /**
     * 写入Engine  scope Binding
     * @param bindings
     * @return
     */
    public GrvyScriptEngine bindingEngineScopeMapper(Bindings bindings){
        scriptEngineHolder.get().setBindings(bindings,ScriptContext.ENGINE_SCOPE);
        return this;
    }

    public  void clearCurrEngineBinding(){

        /*Optional.ofNullable(scriptEngineHolder.get().getContext())
                .map(context -> context.getBindings(ScriptContext.ENGINE_SCOPE)).ifPresent(Map::clear);*/
        /**
         * 前面传的是应用 如果clear的话 会导致执行第二个规则时 拿不到binding值 原因 binding值已经被clear了
         */
        Optional.ofNullable(scriptEngineHolder.get().getContext())
                .map(context -> {
                    context.setBindings(new SimpleBindings(),ScriptContext.ENGINE_SCOPE);
                    return true;
                });
    }
}
