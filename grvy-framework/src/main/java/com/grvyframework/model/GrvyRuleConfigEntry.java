package com.grvyframework.model;

import com.grvyframework.handle.IGrvyScriptResultHandler;
import lombok.Data;

/**
 * @author guchengen495
 * @date 2020-01-21 15:20
 * @description
 */
@Data
public class GrvyRuleConfigEntry {

    /**
     * 规则表达式
     */
    private String script;
    /**
     * 输出结果计算处理
     */
    private IGrvyScriptResultHandler grvyScriptResultHandler;

    /**
     * grvy脚本结果处理类
     */
    private String grvyResultClazzPath;

    /**
     * 规则类型
     */
    private String type;
}
