package com.guce.utils;

import com.alibaba.fastjson.JSON;
import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * @Author chengen.gce
 * @DATE 2022/5/7 18:40
 */
public class SnappyUtil {
    public static void main(String[] args) {
        String input = "我哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈";
        byte[] compressed ;
        try {
            System.out.println("原字符串长度: " + input.length());
            compressed = Snappy.compress(input.getBytes("UTF-8"));
            //二进制存到数据库中
            String compressedStr = JSON.toJSONString(compressed);
            System.out.println("compress 后： " + compressedStr.length());
            compressed = JSON.parseObject(compressedStr,byte[].class);
            byte[] uncompressed = Snappy.uncompress(compressed);
            String result = new String(uncompressed, "UTF-8");
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
