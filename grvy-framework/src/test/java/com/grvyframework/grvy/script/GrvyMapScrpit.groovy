package com.grvyframework.grvy.script

def GrvyScrpit (Integer i){

    def list = ["1101","1411","1121","1131"]
    def channels = ["NET","NCUP"];
    def map = [2:800,
               3:1000,
               4:1200]
    for (e in map) {
        println "key = ${e.key}, value = ${e.value}"
    }
    println(map.getAt(i))
}

GrvyScrpit(2)
