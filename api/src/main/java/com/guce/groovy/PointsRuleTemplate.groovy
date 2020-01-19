package com.guce.groovy

import com.guce.groovy.model.RequestEntry
import com.guce.groovy.model.ResponseEntry
import groovy.text.StreamingTemplateEngine

class PointsRuleTemplate {

    RequestEntry request
    ResponseEntry response

    def 规则集 = [
            '卡类型':[
                    '白金卡':[
                            '普通': {
                                1
                            }()
                    ]
            ]
    ]

    def ruleTemplate(RequestEntry request, ResponseEntry response, String eval){

        this.request = request
        def engine = new StreamingTemplateEngine()
        def str = this.规则集.卡类型.白金卡.普通
        def text =  "${this.规则集.卡类型.白金卡.普通}"
        def template = engine.createTemplate(text)
        Map map = new HashMap()
        map.put("amt",request.getAmt())
        map.put("xxxxx",request.getAmt())
        def value = template.make(map).toString()
        value = value.toLong() * 2;

        response.setAmt( value)

    }

    public static void main(String[] args) {
        def ruleTemplate = new PointsRuleTemplate()
        RequestEntry request = new RequestEntry();
        ResponseEntry response = new ResponseEntry();
        ruleTemplate.request = request;
        ruleTemplate.response = response
        request.setAmt(1000L)
        println(request)
        ruleTemplate.ruleTemplate(request, response,"")

        // println "基础信息.是否90后  =  ${ruleTemplate.规则集.卡类型.白金卡.普通}"
        /*for (int i = 0 ; i < 10 ; i++) {
            long start = System.currentTimeMillis()
            ruleTemplate.ruleTemplate(request, response,"")
            println(System.currentTimeMillis() - start)
        }*/

        println(response)
    }


}
