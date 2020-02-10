package com.grvyframework.model;

import com.grvyframework.handle.IGrvyScriptResultHandler;
import lombok.Data;

import javax.script.Bindings;
import java.util.Map;

/**
 * @author guchengen495
 * @date 2020-01-21 15:38
 * @description
 */
@Data
public class GrvyRuleExecParam {

    /**
     * 规则表达式
     */
    private String script;
    /**
     * 输出结果计算处理
     */
    private IGrvyScriptResultHandler grvyScriptResultHandler;

    /**
     * engine 规则表达式计算参数集
     */
    private Bindings bindings;

    /**
     * 规则表达式计算参数
     */
    private Map<String,Object> proMap;

    /**
     * 输出结果计算参数
     */
    private BaseScriptEvalResultCalculateParam calculateParam;

    /**
     * grvy脚本结果处理类
     */
    private String grvyResultClazzPath;
}
