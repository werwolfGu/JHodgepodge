package com.guce.groovy.engine;

import lombok.Getter;

import javax.script.*;
import java.util.Optional;

/**
 *  http://docs.groovy-lang.org/latest/html/documentation/guide-integrating.html#_groovyscriptengine
 *  https://www.w3cschool.cn/groovy/groovy_strings.html
 * @author chengen.gce
 * @date 2020-01-19 13:46
 * @description
 */
public class GrvyScriptEngine {

    private final static ScriptEngineManager factory = new ScriptEngineManager();

    private static volatile GrvyScriptEngine  grvyScriptEngine;

    @Getter
    private ScriptEngine scriptEngine;


    /**
     * script engine binding 为线程不安全；
     */
    private static ThreadLocal<ScriptEngine> scriptEngineHolder = new ThreadLocal<ScriptEngine>(){

        @Override
        public ScriptEngine initialValue(){
            ScriptEngine scriptEngine = factory.getEngineByName("groovy");
            return scriptEngine;
        }
    };


    public static GrvyScriptEngine getInstance(){
        if ( grvyScriptEngine == null){
            synchronized (GrvyScriptEngine.class){
                if (grvyScriptEngine == null){
                    grvyScriptEngine = new GrvyScriptEngine();
                }
            }
        }
        return grvyScriptEngine;
    }

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
    public GrvyScriptEngine bindingEngineScopeMapper(String key,Object orgField){
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

        Bindings bindings = Optional.ofNullable(scriptEngineHolder.get().getContext())
                .map( context -> context.getBindings(ScriptContext.ENGINE_SCOPE))
                .orElse(null);
        if (bindings != null){
            bindings.clear();
        }
    }
}
