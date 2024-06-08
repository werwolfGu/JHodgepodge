package com.guce;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static freemarker.template.Configuration.VERSION_2_3_31;

/**
 * @Author chengen.gce
 * @DATE 2022/9/16 21:07
 */
public class FreeMarkerDemo {

    public String test() {
        Configuration configuration = new Configuration(VERSION_2_3_31);
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        File file = new File(path);
        try {
            configuration.setDirectoryForTemplateLoading(file);
            // 第三步：设置模板文件使用的字符集。一般就是utf-8.
            configuration.setDefaultEncoding("utf-8");

            // 第四步：加载一个模板，创建一个模板对象。
            Template template = configuration.getTemplate("template.ftl");

            // 第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
            Map<String,Object> dataModel = new HashMap();
            //向数据集中添加数据
            dataModel.put("hello", "this is my first FreeMarker test.");

            // 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
            //Writer out = new FileWriter(new File("/hello.html"));

            // 第七步：调用模板对象的process方法输出文件。
            //template.process(dataModel, out);
            // 第八步：关闭流。
            //out.close();

            StringWriter stringWriter = new StringWriter();
            template.process(dataModel, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        FreeMarkerDemo demo = new FreeMarkerDemo();
        System.out.println(demo.test());
    }
}
