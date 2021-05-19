package com.guce.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author guchengen495
 * @date 2020-05-11 13:58
 * @description
 */
public class CompressUtil {

    private static Logger logger = LoggerFactory.getLogger(CompressUtil.class);

    private static String GZ_POSTFIX = "gz";


    /**
     * gz文件压缩
     * @param inFileName
     */
    public static void doCompressGzFile(String inFileName){

        GZIPOutputStream out = null;

        FileInputStream in = null;

        try{
            logger.info("开始压缩文件:{}",inFileName);
            in = new FileInputStream(inFileName);

            String outFileName = inFileName + "." + GZ_POSTFIX;
            out = new GZIPOutputStream(new FileOutputStream(outFileName));

            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            logger.info("文件压缩完成，生成新文件名：{}",outFileName);
        }catch (Exception e){
            logger.error("do compress gz file error;",e);
        }finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    /**
     * gz文件解压缩
     * @param inFileName
     */
    public static String doUncompressGZFile(String inFileName){

        if (StringUtils.isEmpty(inFileName) || !getExtension(inFileName).equalsIgnoreCase(GZ_POSTFIX)){
            logger.warn("该文件非gz结尾的文件");
            return null;
        }

        logger.info("开始解压文件：{}",inFileName);

        GZIPInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new GZIPInputStream(new FileInputStream(inFileName));
            String outFileName = getFileName(inFileName);

            out = new FileOutputStream(outFileName);
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            logger.info("文件解压完成，文件名:{}",outFileName);
            return outFileName;
        } catch (IOException e) {
            logger.error("File not fousnd.",e);
            return null;
        }finally {

            try {
                if (in != null) {
                    in.close();
                }
                if (out != null){
                    out.close();
                }
            } catch (IOException e) {
                logger.error("关闭文件流失败");
            }

        }

    }


    /**
     * 校验后缀是否准确
     * @param f
     * @return
     */
    public static String getExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');

        if (i > 0 &&  i < f.length() - 1) {
            ext = f.substring(i+1);
        }
        return ext;
    }

    /**
     * 计算最新文件名
     * @param f
     * @return
     */
    public static String getFileName(String f) {
        String fname = "";
        int i = f.lastIndexOf('.');

        if (i > 0 &&  i < f.length() - 1) {
            fname = f.substring(0,i);
        }
        return fname;
    }

    public static void main(String[] args) {
        doUncompressGZFile("/Users/guchengen495/Downloads/阿里巴巴Java开发手册（华山版）.pdf.gz");
    }
}
