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

    private Bindings bindings;

    private String evalScript;
    private Map<String,Object> proMap;

    private IGrvyScriptResultHandler grvyScriptResultHandler;

}
