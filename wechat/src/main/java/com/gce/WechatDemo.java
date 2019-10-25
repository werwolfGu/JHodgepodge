package com.gce;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengen.gu on 2019/10/1.
 */
public class WechatDemo {
    transient int size;

    public void test(){
        System.out.println(++size);
    }

    public static void main(String[] args) {
        Map<String,Object> param = new HashMap<>();

        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Content-Type","application/x-www-form-urlencoded");
        headerMap.put("X-WECHAT-KEY","e04149065c01b4e97eccbc8dd0a8387108d0b9a10bd4e39d800842c50706047ffb6befc75da9b1850f0f8bc56216be34e3a30b9ea9a075e3f77b9a98dbd7b03f203121fcc171cacfb9d0a19692a20d8c");
        headerMap.put("Connection","keep-alive");
        headerMap.put("X-WECHAT-UIN","MjU5ODA3MzYw");
        String url = "http://mp.weixin.qq.com/mp/getappmsgext?f=json&mock=&fasttmplajax=1&f=json&uin=&key=&pass_ticket=dAl5GUAXP8MkKWqUjvH2kiXAyH9BTVoQAqeQMCWaDFc%25253D&wxtoken=&devicetype=iOS12.4.1&clientversion=17000823&__biz=MjM5MTg4MjcwMA%3D%3D&appmsg_token=&x5=0&f=json&wx_header=1&pass_ticket=dAl5GUAXP8MkKWqUjvH2kiXAyH9BTVoQAqeQMCWaDFc%3D";
        param.put("f","json");
        param.put("fasttmplajax","1");
        param.put("pass_ticket","dAl5GUAXP8MkKWqUjvH2kiXAyH9BTVoQAqeQMCWaDFc%25253D");
        param.put("devicetype","iOS12.4.1%25253D");
        param.put("clientversion","17000823");
        param.put("__biz","MjM5MTg4MjcwMA%3D%3D");
        param.put("wx_header","1");
        param.put("wx_header","1");

        String content = null;
        try {
//            content = HttpClientUtils.doGet(url,headerMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(content);

        try {
//            content = HttpClientUtils.doGet("http://extshort.weixin.qq.com/mmtls/5f4e04d7");
            System.out.println(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WechatDemo wechatDemo = new WechatDemo ();
        wechatDemo.test();

    }
}
