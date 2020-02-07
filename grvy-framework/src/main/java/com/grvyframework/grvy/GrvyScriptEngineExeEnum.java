package com.grvyframework.grvy;

/**
 * @Author chengen.gu
 * @DATE 2020/2/3 2:05 下午
 */
public enum  GrvyScriptEngineExeEnum {

    SCRIPT_ENGINE("1","script脚本引擎执行") ,
    SCRIPT_COMPILER("2","script 脚本编译保存脚本类");

    private String code ;
    private String msg;

    GrvyScriptEngineExeEnum(String code,String msg){
        this.code =code;
        this.msg = msg;
    }
}
