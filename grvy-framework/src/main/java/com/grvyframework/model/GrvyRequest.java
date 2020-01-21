package com.grvyframework.model;

import com.grvyframework.handle.IGrvyScriptResultHandler;
import lombok.Data;

import javax.script.Bindings;
import java.util.Map;

/**
 * @author chengen.gu
 * @date 2020-01-20 17:02
 * @description
 */
@Data
public class GrvyRequest {

    /**
     * engine 规则表达式计算参数集
     */
    private Bindings bindings;

    /**
     * 规则表达式计算参数
     */
    private Map<String,Object> proMap;

    /**
     *规则表达式
     */
    private String evalScript;

    /**
     * 输出结果计算参数
     */
    private BaseScriptEvalResultCalculateParam calculateParam;

    /**
     * 输出结果计算处理
     */
    private IGrvyScriptResultHandler grvyScriptResultHandler;

}
