package com.guce.client;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author chengen.gce
 * @DATE 2021/11/26 10:15 下午
 */
public class OkHttpClientUtils {

    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) { return true; }
    }


    public static final MediaType JSON_TYPE
            = MediaType.get("application/json; charset=utf-8");

    public static final MediaType FROM_TYPE
            = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");

    public static String test(Map<String,Object> map) throws IOException {


        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts())
                .hostnameVerifier(new TrustAllHostnameVerifier()).build();

        //修改成自己的url
        String url = "https://www.douyin.com/aweme/v1/web/commit/item/digg/?";

        StringBuilder param = new StringBuilder();
        map.entrySet().stream().forEach(entry -> {
            param.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        });

        url = url + param.substring(1);
        System.out.println("url ->  " + url);
        FormBody formBody = new FormBody.Builder().add("aweme_id","7035241464883957030")
                .add("type","0").build();


        Request request = new Request.Builder()
                //.header("Referrer Policy","strict-origin-when-cross-origin")
                .addHeader("cookie","ttcid=38f54ab78f9d4d569b033cafb16c819377; douyin.com; AB_LOGIN_GUIDE_TIMESTAMP=1637932609726; MONITOR_WEB_ID=a1590567-b56b-402d-b7af-8daf5f10fef0; s_v_web_id=verify_kwgesb1j_UiQB9jHe_HhyZ_4sW0_AtVo_KGLv39v5sWkg; passport_csrf_token_default=befafcbd50f2faf9852f40aa24440237; passport_csrf_token=befafcbd50f2faf9852f40aa24440237; n_mh=epegdoZkWqSxeYCgwvbRr4Hue7hCwUCv0dPq7Mi0RYA; sso_uid_tt=1b35f42077c5b15f8ccb48293427f84d; sso_uid_tt_ss=1b35f42077c5b15f8ccb48293427f84d; toutiao_sso_user=5f939389498c3d77709f37333227feb5; toutiao_sso_user_ss=5f939389498c3d77709f37333227feb5; odin_tt=6f01a0a83d5c75c8f62b5383b4b9067a3108c50dde8c655d9c36b57e555d902e03454bdb040076807da0f6a07a7f58a1; passport_auth_status_ss=0a5398c12ab84556dd12d5584e1ef78d%2C; sid_guard=f33d5c30c8f7c5fae3862b5f494417e6%7C1637932694%7C5183999%7CTue%2C+25-Jan-2022+13%3A18%3A13+GMT; uid_tt=a7822bf9258b25ee72dd26e488f9f937; uid_tt_ss=a7822bf9258b25ee72dd26e488f9f937; sid_tt=f33d5c30c8f7c5fae3862b5f494417e6; sessionid=f33d5c30c8f7c5fae3862b5f494417e6; sessionid_ss=f33d5c30c8f7c5fae3862b5f494417e6; sid_ucp_v1=1.0.0-KDQ1ZGNiYTMxYjExNjk4NWZhZGRmNzViOTFiMDFlNjJlNjRmZTJjYzUKFQi8meHE3gIQlr2DjQYY7zE4BkD0BxoCbHEiIGYzM2Q1YzMwYzhmN2M1ZmFlMzg2MmI1ZjQ5NDQxN2U2; ssid_ucp_v1=1.0.0-KDQ1ZGNiYTMxYjExNjk4NWZhZGRmNzViOTFiMDFlNjJlNjRmZTJjYzUKFQi8meHE3gIQlr2DjQYY7zE4BkD0BxoCbHEiIGYzM2Q1YzMwYzhmN2M1ZmFlMzg2MmI1ZjQ5NDQxN2U2; passport_auth_status=0a5398c12ab84556dd12d5584e1ef78d%2C; csrf_session_id=3b44994a3563bc80cd05289f805d6eaf; ttwid=1%7Cf5bh1W6LLoRRzjs2raSS6GU-ElSne1n2xp8WHZLtbY4%7C1638013445%7Ca0b1260c08ff462437b65f612a5f357d8eb7a0b90016b2eba37e377d1d03d8ae; _tea_utm_cache_1243=undefined; _tea_utm_cache_2018=undefined; MONITOR_DEVICE_ID=af639286-dd6d-41a0-bc9b-a7f87fea0967; THEME_STAY_TIME=299997; IS_HIDE_THEME_CHANGE=1; FOLLOW_NUMBER_YELLOW_POINT_INFO=MS4wLjABAAAA4kz5vJhH42zALcj6nHFyT6Yak5DNN-SMjZTS9PFS5jY%2F1638547200000%2F0%2F1638533001884%2F0; _tea_utm_cache_6383=undefined; _tea_utm_cache_1300=undefined; __ac_nonce=061ab27120035c88bf9e1; __ac_signature=_02B4Z6wo00f01cwnPagAAIDCWPFmpW-h38HMAzkAABKk7rp3DGWrr49tHb3tNVYLH-Ao9UyVWtRoWiQ6fAZ85zzodrLp16EwOUqY6iEjwlixXsYUwN2hf-nEMj3Xsa0wLeDjSih.304SgTAC39; home_can_add_dy_2_desktop=1; tt_scid=r2jHH62JWjCQR8YJbHTgfnvgGhh24RIwwYRyvcQNV0qZi8vUgs845Vr7-a3jfrJf3f7e; pwa_guide_count=2; msToken=7rU729IqCzoTI3SzJoQpoDaeCFPomwggO5kmJOlptuyKwFImmNtIN9041CZol6HGfoPshluw7odhEf-tl0xsAx9TNWtf2pTXnT7o1i1U_zmuZGD8mhyruuNk; msToken=yXIS8JKcg-M6HxvZccxyOCanurxI3kVYYCMdF8bcVXXTXBJO9KhCdsTdI7exX6bT41TUL6KkiIQz0EBy8hYYrUFAmVg6wUTgQpeLmfx_2eog3bSaEvXfv-v6g4w=")
                .addHeader("referer","https://www.douyin.com/video/7035241464883957030")
                .addHeader("origin","https://www.douyin.com")
                .addHeader("x-secsdk-csrf-token","000100000001c61bf855e1bf04dda611499b33c890b7faa42185901d6c3ba09a20606bc33cbe16bd80652cdead17")
                .post(formBody)
                .url(url).build();
        Call call = client.newCall(request);

        Response response = call.execute();
        String result = null;
        if(response.body() != null)
        {
            result = response.body().string();
            //处理result
        }
        return result;
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }


    public static void main(String[] args) {
        Map<String,Object> param = new HashMap<>();

        param.put("device_platform","webapp");
        param.put("aid",6383);
        param.put("channel","channel_pc_web");
        param.put("version_code",170400);
        param.put("version_name","17.4.0");
        param.put("cookie_enabled",true);
        param.put("screen_width",1440);
        param.put("screen_height",900);
        param.put("browser_language","zh-CN");
        param.put("browser_name","Chrome");
        param.put("browser_version","96.0.4664.55");
        param.put("browser_online",true);
        param.put("engine_name","Blink");
        param.put("engine_version","96.0.4664.55");
        param.put("os_name","Mac+OS");
        param.put("os_version","10.14.6");
        param.put("cpu_core_num",8);
        param.put("device_memory",8);
        param.put("platform","PC");
        param.put("downlink",10);
        param.put("effective_type","4g");
        param.put("round_trip_time",50);
        param.put("msToken","IwiXZtCzySmYrnJcxHkQE4JDt1bTAA7HsSaRm03IAxGyyxdw3pZ--EdhZY1_7-FpZshao6X3QUqMJlM2UCHJhVzPBLqzkFhMT2OeTDc14AROsdCIt-NPW33K6Q==");
        param.put("_signature","_02B4Z6wo00001hppUcgAAIDBjr8KxRYFReoabVVAAOc.o9dEe.5zhH6oxix9jEsb.yKKBY8fgV0nBkLV5eveWnuQKN6qICgQZLMs1yfglbiMvkkJ4RzTnecZ4QL3SIja6NJDRPrf6SOPgCBqb0");

        try {
            System.out.println(test(param));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
