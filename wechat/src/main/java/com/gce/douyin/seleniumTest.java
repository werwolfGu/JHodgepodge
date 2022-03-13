package com.gce.douyin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * https://blog.csdn.net/qq_29817481/article/details/101052012#Java_5
 * @Author chengen.gce
 * @DATE 2022/1/12 9:33 下午
 */
public class seleniumTest {

    public static List<String> headers(){

        List<String> headerList = new ArrayList<>();
        headerList.add("accept=application/json, text/plain, */*");
        headerList.add("accept-encoding=gzip, deflate, br");
        headerList.add("accept-language=zh-CN,zh;q=0.9");
        headerList.add("content-length=35");
        headerList.add("content-type=application/x-www-form-urlencoded; charset=UTF-8");
        headerList.add("origin=https://www.douyin.com");
        headerList.add("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36");
        headerList.add("x-secsdk-csrf-token=00010000000130b27b250bb075fa64bda60df2039f5ed1015b9c8d5206b54c9f7d4dd1826f1c16c98adf0f39c584");
        headerList.add("cookie=ttcid=38f54ab78f9d4d569b033cafb16c819377; MONITOR_WEB_ID=a1590567-b56b-402d-b7af-8daf5f10fef0; passport_csrf_token_default=befafcbd50f2faf9852f40aa24440237; passport_csrf_token=befafcbd50f2faf9852f40aa24440237; n_mh=epegdoZkWqSxeYCgwvbRr4Hue7hCwUCv0dPq7Mi0RYA; sso_uid_tt=1b35f42077c5b15f8ccb48293427f84d; sso_uid_tt_ss=1b35f42077c5b15f8ccb48293427f84d; toutiao_sso_user=5f939389498c3d77709f37333227feb5; toutiao_sso_user_ss=5f939389498c3d77709f37333227feb5; odin_tt=6f01a0a83d5c75c8f62b5383b4b9067a3108c50dde8c655d9c36b57e555d902e03454bdb040076807da0f6a07a7f58a1; sid_guard=f33d5c30c8f7c5fae3862b5f494417e6%7C1637932694%7C5183999%7CTue%2C+25-Jan-2022+13%3A18%3A13+GMT; uid_tt=a7822bf9258b25ee72dd26e488f9f937; uid_tt_ss=a7822bf9258b25ee72dd26e488f9f937; sid_tt=f33d5c30c8f7c5fae3862b5f494417e6; sessionid=f33d5c30c8f7c5fae3862b5f494417e6; sessionid_ss=f33d5c30c8f7c5fae3862b5f494417e6; sid_ucp_v1=1.0.0-KDQ1ZGNiYTMxYjExNjk4NWZhZGRmNzViOTFiMDFlNjJlNjRmZTJjYzUKFQi8meHE3gIQlr2DjQYY7zE4BkD0BxoCbHEiIGYzM2Q1YzMwYzhmN2M1ZmFlMzg2MmI1ZjQ5NDQxN2U2; ssid_ucp_v1=1.0.0-KDQ1ZGNiYTMxYjExNjk4NWZhZGRmNzViOTFiMDFlNjJlNjRmZTJjYzUKFQi8meHE3gIQlr2DjQYY7zE4BkD0BxoCbHEiIGYzM2Q1YzMwYzhmN2M1ZmFlMzg2MmI1ZjQ5NDQxN2U2; ttwid=1%7Cf5bh1W6LLoRRzjs2raSS6GU-ElSne1n2xp8WHZLtbY4%7C1638013445%7Ca0b1260c08ff462437b65f612a5f357d8eb7a0b90016b2eba37e377d1d03d8ae; MONITOR_DEVICE_ID=af639286-dd6d-41a0-bc9b-a7f87fea0967; douyin.com; csrf_session_id=08dcb3526fb7b2eea383de1eca751b08; __ac_signature=_02B4Z6wo00f01IpDdbQAAIDDHpUuu8VqdeyKZ3EAAENSQMh6EPR.9YNZU.Em9H5nw4Daf8S8XIEyrNLWygvL..GgbNSXo2YF.6uKObPYm.9VrRVvLZiaKTi.AhnauZ7lrKj6w5hxdEX7VnDafa; _tea_utm_cache_6383=undefined; _tea_utm_cache_1300=undefined; pwa_guide_count=1; THEME_STAY_TIME=299478; IS_HIDE_THEME_CHANGE=1; msToken=8ZtoZyBAQkMK_NzPsaTcE3fooc68Ew1iVvsJGSFo0yEy3NYiqGUgBF021fyJoRu_VlWANvj4lQ8DwzFMqBk9SJGTaiF1Uo-VR9UmqJt8043xZYsoCi9LV1AQHaA=; tt_scid=-eZ1mjrsWSQy0OTdIDhsC0UBlGVNC.N6RKlzcFp0drVg7jP9dMSfbb0F226rG-WO05ed; msToken=SlIORm6mtUOHrXhrCwlhNblen2fD5H9oLzgvAMUwODXBslraXeKcpERUs7bG5tPhHEd2DzFvyh9W1a4sK3q8PrQ3F3vyA_Nb5Q_FQcHHqyjsIHFlGU4IN-HCQ5I_9eWeGTN4; home_can_add_dy_2_desktop=1; __ac_nonce=061dedfc1008716165915");
        return headerList;
    }

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver","/Users/chengen.gu/chrome-driver/chromedriver");

        ChromeOptions options = new ChromeOptions();
        //下面2个参数是不让浏览器弹出，不然浏览器闪出来一下
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments(headers());

        WebDriver driver = new ChromeDriver(options);
        driver.get("https://www.douyin.com/aweme/v1/web/commit/item/digg/?device_platform=webapp&aid=6383&channel=channel_pc_web&version_code=170400&version_name=17.4.0&cookie_enabled=true&screen_width=1440&screen_height=900&browser_language=zh-CN&browser_platform=MacIntel&browser_name=Chrome&browser_version=97.0.4692.71&browser_online=true&engine_name=Blink&engine_version=97.0.4692.71&os_name=Mac+OS&os_version=10.14.6&cpu_core_num=8&device_memory=8&platform=PC&downlink=10&effective_type=4g&round_trip_time=50&webid=7034866987474748969&msToken=7vKEcJUkN6SdQjS9q7AjeKOEcbnLF9np7rMS6DSJWXMcPs5PpCXknK12HTDZq-ixQcGrSiDoUQB6NGzsJyC4f-mDGXIhPDDFdm8L8s6MaxvXHwW-HzPZz6laEBWSMZX-1HN0&X-Bogus=DFSzsdVuMXbxHrXvSoYbCr7TlqCq&_signature=_02B4Z6wo00001lV1K5gAAIDBwaNwlg4Qex5VcS8AAPSSxGcYWl4MCVLmiEw9ZE5d27t-WEUcwS7rDAbKcYoryMmK5Y.x6Btxf-7ZXxm9IGeusgKsZr2ogPK0I6w4hRhYk1bRypCB8eSdlYl1f1");


        String title = driver.getTitle();
        System.out.println(driver.getPageSource());
        System.out.println(title);
        driver.close();
    }
}
