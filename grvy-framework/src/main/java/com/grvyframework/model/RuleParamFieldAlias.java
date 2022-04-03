package com.grvyframework.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author chengen.gce
 * @DATE 2022/4/3 12:06 PM
 */
@Setter
@Getter
public class RuleParamFieldAlias {

    String beanPath;
    String fieldName;
    String alias;

    public String getParamName(){
        return alias == null || "".equals(alias) ? fieldName : alias;
    }

}
