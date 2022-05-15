package com.guce.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @Author chengen.gce
 * @DATE 2022/5/8 17:41
 */
@Slf4j
public class StrZipUtil {
    private static final String charsetName = "ISO-8859-1";

    /**
     * zip压缩
     * @param str
     * @return
     */
    public static String zip(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        GZIPOutputStream gzip = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            gzip = new GZIPOutputStream(bos);
            gzip.write(str.getBytes());
            gzip.close();
            return bos.toString(charsetName);
        } catch (IOException e) {
            log.error("string gzip compress exception : {}" ,str ,e);
        }
        return null;
    }

    public static String unzip(String compressStr) throws IOException {
        if (compressStr == null || compressStr.length() <=0 ){
            return null;
        }
        ByteArrayOutputStream bos = null;
        ByteArrayInputStream bis = null;
        GZIPInputStream gzip = null;
        try{
            bos = new ByteArrayOutputStream();
            bis = new ByteArrayInputStream(compressStr.getBytes(charsetName));
            gzip = new GZIPInputStream(bis);
            byte[] buffer = new byte[256];
            int len;
            while ((len = gzip.read(buffer)) >= 0){
                bos.write(buffer,0,len);
            }
            return bos.toString("UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            log.error("unzip exception : {}" ,compressStr,e);
        }finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String str = "Hello snappy-java! Snappy-java is a JNI-based wrapper of  Snappy, a fast compresser/decompresser." +
                "fiogfhwoiefseoigseroijgwgljsfobmrthojtrdbmthpkdpsgmsroigjafjeigosrugjw45;rghreogwoirgjreigijaeiorgjseorija" +
                "i4y8yt5urf vu  83uhj545git0siovnqroiwrghq'fks-0ghngmadhvwbgntfbjtrihnmtn9rfnqwrgwirgnfvrb" +
                "jw89rghwoihnt98gbh ogwrt09hb8nqrvgogmrofhvwgjbvboignrsflv\n" +
                "f4hgwrign5te9ybh   iu3rhf2t5th390yu-gfburij-jnj mihbk 0uhgbtglv    d[quvnsfk hwbg iv\n" +
                "guwhgwigj98gqwtby92bg outr8rh3opfru9htqv9hnglkk p  bowgjghiog\n" +
                "t7385hg2iuprhv78rb2riwqenkhirhtgnjr ihgwrbnihvr bgirjohrushgisugjvoigrb\n" +
                "firwh9358gwrgntuhbyhjqrgwkgotgb  ijvosrniof biujigowrovsjg tjuogwrnvjs " +
                "iut9854th53ntiubhrgbn2wbqbgnvifdjngrfjwgnrhvingifuwktjbwjgarsgnsoihstjbigb vfjgwrjg54wnen  birnvre\n";
        String compressVal = StrZipUtil.zip(str);
        try {
            System.out.println(compressVal.length());
            String value = StrZipUtil.unzip(compressVal);
            System.out.println(str.length());
            System.out.println(value.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
