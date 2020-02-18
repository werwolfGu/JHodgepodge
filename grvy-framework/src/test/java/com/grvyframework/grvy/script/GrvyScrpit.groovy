package com.grvyframework.grvy.script

def GrvyScrpit (){

    def list = ["1101","1411","1121","1131"]
    def channels = ["NET","NCUP"]
    def str = com.grvyframework.grvy.GrvyInvokeInterface.test()
    println( "str:->" + str)
    if (list.contains(交易码) && channels.contains(渠道)){
        return true
    }
}

GrvyScrpit();
