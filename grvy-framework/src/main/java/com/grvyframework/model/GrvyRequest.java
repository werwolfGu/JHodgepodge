package com.grvyframework.model;

import com.grvyframework.reduce.Reduce;
import lombok.Data;

import javax.script.Bindings;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author chengen.gu
 * @date 2020-01-20 17:02
 * @description
 */
@Data
public class GrvyRequest implements Serializable {
    private static final long serialVersionUID = 6200156302595905868L;

    /**
     * engine 规则表达式计算参数集
     */
    private Bindings bindings;

    /**
     * 规则表达式计算参数
     */
    private Map<String,Object> proMap;

    /**
     *规则配置信息
     */
    private List<GrvyRuleConfigEntry> grvyRuleInfoList;

    /**
     * 输出结果计算参数
     */
    private BaseScriptEvalResultCalculateParam calculateParam;

    /**
     * 结果集处理条件
     */
    private Reduce reduce;



}
