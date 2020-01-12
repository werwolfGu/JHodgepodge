package com.guce.javassist;

import com.guce.javassist.demo.AbstractPointRule;
import com.guce.javassist.demo.HelloJavassist;
import com.guce.javassist.demo.PointsModel;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author chengen.gu
 * @DATE 2019/12/11 8:19 下午
 */
public class JavassistClassFactory {

    // 动态代码创建的例子
    // 下面例子使用 Javassist 的 API成功组织出代理类的一个子类，可以看出 添加构造函数，添加属性，
    // 添加方法，内容 都是通过字符串类型完成即可。 通过 Javassist 强大的字节生成能力可以达到动态
    // 增加类和实现动态代理的功能.
    public static void testJavassistDefineClass() throws Exception  {
        // 创建类池，true 表示使用默认路径
        ClassPool classPool = new ClassPool(true);

        String className = HelloJavassist.class.getName();
        // 创建一个类 RayTestJavassistProxy
        CtClass ctClass = classPool.makeClass(className + "JavassistProxy_1");


        // 添加超类
        // 设置 RayTestJavassistProxy 的父类是 RayTest.
        ctClass.setInterfaces(new CtClass[]{classPool.get(HelloJavassist.class.getName())});

        // 添加默认构造函数
        ctClass.addConstructor(CtNewConstructor.defaultConstructor(ctClass));

        // 添加属性
        /*ctClass.addField(CtField.make("public " + className + " real = new " +
                className + "();", ctClass));*/
        String exp = "1 == 1";
        // 添加方法，里面进行动态代理 logic
        ctClass.addMethod(CtNewMethod.make("public String say() { " +
                        "if ("+ exp +"){return \"hello javassit\";}" +
                        "return \"before \";}",
                ctClass));
        Class testClass = ctClass.toClass();
        HelloJavassist rayTest = (HelloJavassist) testClass.newInstance();
        String exe = rayTest.say();
        System.out.println(exe);


    }

    public static AbstractPointRule getPointRuleInstance() throws Exception {
        // 创建类池，true 表示使用默认路径
        ClassPool classPool = new ClassPool(true);

        String className = AbstractPointRule.class.getName();
        // 创建一个类 RayTestJavassistProxy
        CtClass ctClass = classPool.makeClass(className + "JavassistProxy");

        classPool.importPackage("com.guce.javassist.demo.PointsModel");
        // 添加超类
        // 设置 RayTestJavassistProxy 的父类是 RayTest.
        ctClass.setSuperclass(classPool.get(AbstractPointRule.class.getName()));
        // 添加默认构造函数
        ctClass.addConstructor(CtNewConstructor.defaultConstructor(ctClass));


        // 添加属性
        /*ctClass.addField(CtField.make("public " + className + " real = new " +
                className + "();", ctClass));*/
        String exp = "list != null && list.contains(str)";
        // 添加方法，里面进行动态代理 logic
        ctClass.addMethod(CtNewMethod.make("public boolean filter(PointsModel pointsModel){\n" +
                        "Boolean x = false;\n" +
                        "System.out.println(\"a\");" +
                        "        if (list != null && list.contains(pointsModel.getProductId())){\n" +
                        "            return true;\n" +
                        "        }\n" +
                        "        return false;\n" +
                        "    }",
                ctClass));
        Class testClass = ctClass.toClass();
        AbstractPointRule pointRule = (AbstractPointRule) testClass.newInstance();
        return pointRule;
    }

    public static AbstractPointRule getPointRuleInstanceB() throws Exception {
        // 创建类池，true 表示使用默认路径
        ClassPool classPool = new ClassPool(true);

        String className = AbstractPointRule.class.getName();
        // 创建一个类 RayTestJavassistProxy
        CtClass ctClass = classPool.makeClass(className + "JavassistProxy_2");

        classPool.importPackage("com.guce.javassist.demo.PointsModel");
        classPool.importPackage("java.lang.*");

        // 添加超类
        // 设置 RayTestJavassistProxy 的父类是 RayTest.
        ctClass.setSuperclass(classPool.get(AbstractPointRule.class.getName()));
        // 添加默认构造函数
        ctClass.addConstructor(CtNewConstructor.defaultConstructor(ctClass));


        // 添加属性
        /*ctClass.addField(CtField.make("public " + className + " real = new " +
                className + "();", ctClass));*/
        String exp = "list != null && list.contains(str)";
        // 添加方法，里面进行动态代理 logic
        ctClass.addMethod(CtNewMethod.make("public boolean filter(PointsModel pointsModel){\n" +
                        "boolean flag = false; \n" +
                        "PointsModel model = null;\n" +
                        "     System.out.println(\"b\");   " +
                        "if (list != null && list.contains(pointsModel.getProductId())){\n" +
                        "            return true;\n" +
                        "        }\n" +
                        "        return flag;\n" +
                        "    }",
                ctClass));
        Class testClass = ctClass.toClass();
        AbstractPointRule pointRule = (AbstractPointRule) testClass.newInstance();
        return pointRule;
    }

    private static Map<String,AbstractPointRule> ruleMap = new ConcurrentHashMap<>();
    public static void main(String[] args) {


        try {
            //JavassistClassFactory.testJavassistDefineClass();
            AbstractPointRule rule = JavassistClassFactory.getPointRuleInstance();
            ruleMap.put("a",rule);

            AbstractPointRule ruleB = JavassistClassFactory.getPointRuleInstanceB();
            List<String> list =  Arrays.asList("a","b","c");

            rule.setList(list);
            String loop = "abcdefghijkabcdccccccccccccccccddddddddddaaaaaaaaaaacccccccccbbbbbbbbbbbbbbffffffeddfjdacb";
            for (int i = 0 ; i < loop.length() ; i++ ){
                int finalI = i;

                new Thread(){
                    @Override
                    public void run() {
                        long start = System.currentTimeMillis();
                        Character ch = loop.charAt(finalI);
                        PointsModel pointsModel = new PointsModel();
                        pointsModel.setProductId(ch.toString());
                        AbstractPointRule pointRule = ruleMap.get("a");
                        System.out.println(pointRule.filter(pointsModel) + " cost time :" + (System.currentTimeMillis() - start));
                    }
                }.start();

                if (i ==10){
                    new Thread(){
                        @Override
                        public void run() {
                            ruleMap.put("a",ruleB);
                        }
                    }.start();
                }
            }
            System.out.println("===================");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
