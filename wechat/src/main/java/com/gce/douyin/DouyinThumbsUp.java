package com.gce.douyin;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 抖音点赞
 * @Author chengen.gce
 * @DATE 2022/1/9 10:57 下午
 */
public class DouyinThumbsUp {


    public static String post(String url , Map<String,Object> queryMap,Map<String,Object> headMap) {
        HttpResponse response = HttpRequest.post(url).addHeaders(addHeaders()).form(queryMap).execute();
        System.out.println(response);
        return response.body();
    }


    public static Map<String,String> addHeaders() {

        Map<String,String> headersMap = new HashMap<>();
        headersMap.put("origin","https://www.douyin.com");
        headersMap.put("accept","application/json, text/plain, */*");
        headersMap.put("content-type","application/x-www-form-urlencoded; charset=UTF-8");


        headersMap.put("x-secsdk-csrf-token","00010000000176191904ddc6991265ec31a90f4c8df29f06e87b8bbc2e8982234da980c44e5616c8a23bcf9efdfe");
        headersMap.put("cookie","ttcid=38f54ab78f9d4d569b033cafb16c819377; MONITOR_WEB_ID=a1590567-b56b-402d-b7af-8daf5f10fef0; passport_csrf_token_default=befafcbd50f2faf9852f40aa24440237; passport_csrf_token=befafcbd50f2faf9852f40aa24440237; n_mh=epegdoZkWqSxeYCgwvbRr4Hue7hCwUCv0dPq7Mi0RYA; sso_uid_tt=1b35f42077c5b15f8ccb48293427f84d; sso_uid_tt_ss=1b35f42077c5b15f8ccb48293427f84d; toutiao_sso_user=5f939389498c3d77709f37333227feb5; toutiao_sso_user_ss=5f939389498c3d77709f37333227feb5; odin_tt=6f01a0a83d5c75c8f62b5383b4b9067a3108c50dde8c655d9c36b57e555d902e03454bdb040076807da0f6a07a7f58a1; sid_guard=f33d5c30c8f7c5fae3862b5f494417e6%7C1637932694%7C5183999%7CTue%2C+25-Jan-2022+13%3A18%3A13+GMT; uid_tt=a7822bf9258b25ee72dd26e488f9f937; uid_tt_ss=a7822bf9258b25ee72dd26e488f9f937; sid_tt=f33d5c30c8f7c5fae3862b5f494417e6; sessionid=f33d5c30c8f7c5fae3862b5f494417e6; sessionid_ss=f33d5c30c8f7c5fae3862b5f494417e6; sid_ucp_v1=1.0.0-KDQ1ZGNiYTMxYjExNjk4NWZhZGRmNzViOTFiMDFlNjJlNjRmZTJjYzUKFQi8meHE3gIQlr2DjQYY7zE4BkD0BxoCbHEiIGYzM2Q1YzMwYzhmN2M1ZmFlMzg2MmI1ZjQ5NDQxN2U2; ssid_ucp_v1=1.0.0-KDQ1ZGNiYTMxYjExNjk4NWZhZGRmNzViOTFiMDFlNjJlNjRmZTJjYzUKFQi8meHE3gIQlr2DjQYY7zE4BkD0BxoCbHEiIGYzM2Q1YzMwYzhmN2M1ZmFlMzg2MmI1ZjQ5NDQxN2U2; ttwid=1%7Cf5bh1W6LLoRRzjs2raSS6GU-ElSne1n2xp8WHZLtbY4%7C1638013445%7Ca0b1260c08ff462437b65f612a5f357d8eb7a0b90016b2eba37e377d1d03d8ae; MONITOR_DEVICE_ID=af639286-dd6d-41a0-bc9b-a7f87fea0967; _tea_utm_cache_6383=undefined; douyin.com; _tea_utm_cache_1300=undefined; pwa_guide_count=3; csrf_session_id=08dcb3526fb7b2eea383de1eca751b08; THEME_STAY_TIME=299426; IS_HIDE_THEME_CHANGE=1; __ac_nonce=061daf88d00c8b6a091f5; __ac_signature=_02B4Z6wo00f0114S3VAAAIDAysSGX25bscteNtnAALZpbUSo2xqV1Q4YqUAAwocjI2RKg7593zFWmVO2vRXcvSocf3V2ApyMynqc.gnJm.aK2OhcAgjq0w9Krp6qwBk9jnRusldyZfNh6oUn8c; home_can_add_dy_2_desktop=1; tt_scid=0KpnzhgoT65a3u.zOEMzCAlhQLXZ6fl9wlm4E44UV6QODVaAYsv0DOuKPQY1WVCu141a; msToken=ZZFytk8Y2Gea4Rh7dGWc3MLgihtEMMXwh2PKIZDmqfgOlXna2n-cr66GM4MH3JwwFAkkjdAfVCAfp3l3zDcoT1jFdwYa-9ad3U2lE1D5ACSf_OLUOEsbHVVKvA==; msToken=B6f7bNLdReHjEL5JK5SoCpfGdjZa2XHZuAlzpbv3nIDryR9F2N9928wpsWzwcXyy_u0zxhUcM9tGxjKhJfZoVgAdtNg5BbL97iirYM6WO86YaLiy1kX_i0C4Tbs=");
        return headersMap;

    }

    public static void main(String[] args) {
        String url ="https://www.douyin.com/aweme/v1/web/commit/item/digg/?device_platform=webapp&aid=6383&channel=channel_pc_web&version_code=170400&version_name=17.4.0&cookie_enabled=true&screen_width=1440&screen_height=900&browser_language=zh-CN&browser_platform=MacIntel&browser_name=Chrome&browser_version=96.0.4664.110&browser_online=true&engine_name=Blink&engine_version=96.0.4664.110&os_name=Mac+OS&os_version=10.14.6&cpu_core_num=8&device_memory=8&platform=PC&downlink=10&effective_type=4g&round_trip_time=150&webid=7034866987474748969&msToken=ZZFytk8Y2Gea4Rh7dGWc3MLgihtEMMXwh2PKIZDmqfgOlXna2n-cr66GM4MH3JwwFAkkjdAfVCAfp3l3zDcoT1jFdwYa-9ad3U2lE1D5ACSf_OLUOEsbHVVKvA==&X-Bogus=DFSzsdVunAUxHVwsSoVeEN7TlqeN&_signature=_02B4Z6wo00001RaQnnAAAIDCgkbFfFi429kWlJrAACSB9YtH2XHgRRVuFOr9Ca-VD.WDgAbp5n8aeze2uzAI0atvvXHeKovh7iCHXbAVXw1MoJc4IHjRK0DUVZLaoRCK7MJGlkpduAGnlqnlc4";
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("aweme_id","7035241464883957030");
        queryMap.put("type","1");

        System.out.println(post(url,queryMap,null));
    }
}
