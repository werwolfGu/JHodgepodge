package com.guce.groovy

import com.gce.model.PointsRequest
import com.gce.model.PointsResponse
import groovy.text.StreamingTemplateEngine

class PointsRuleTemplate {

    PointsRequest request
    PointsResponse response

    def 规则集 = [
            '卡类型':[
                    '白金卡':[
                            '普通': {
                                '$amt'
                            }()
                    ]
            ]
    ]

    def ruleTemplate(PointsRequest request, PointsResponse response, String eval){

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
        PointsRequest request = new PointsRequest();
        PointsResponse response = new PointsResponse();
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
