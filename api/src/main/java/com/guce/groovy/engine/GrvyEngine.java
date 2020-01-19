package com.guce.groovy.engine;


import lombok.Getter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * http://docs.groovy-lang.org/latest/html/documentation/guide-integrating.html#_groovyscriptengine
 *
 * https://www.w3cschool.cn/groovy/groovy_strings.html
 */
public class GrvyEngine {

    @Getter
    private ScriptEngine engine ;

    private static volatile  GrvyEngine grvyEngine ;


    public static GrvyEngine getInstance(){
        if (grvyEngine == null){
            synchronized (GrvyEngine.class){
                if (grvyEngine == null){
                    grvyEngine = new GrvyEngine();
                }
            }
        }

        return grvyEngine;
    }
    public GrvyEngine(){
        ScriptEngineManager factory = new ScriptEngineManager();
        engine = factory.getEngineByName("groovy");

    }

    public Object eval(String script) throws ScriptException {
        return   engine.eval(script);
    }

    public void grvyExpressExec() throws ScriptException {
        List<String> mcc = Arrays.asList("1","2","3");

        GrvyEngine.getInstance().getEngine().put("卡类型",mcc);
        List<String> cardLogoList = Arrays.asList("123","456");
        GrvyEngine.getInstance().getEngine().put("卡logo",cardLogoList);
        StringBuilder str = new StringBuilder();
        str.append("卡类型.contains(\"2\") ");
        str.append(" && 卡logo.contains(\"123\") ");
        List<String> textList = new ArrayList<>();
        textList.add(str.toString());
        String text = "卡logo.contains(\"456\")";
        textList.add(text);

        text = "卡类型.contains(\"5\")";
        textList.add(text);

        text = "卡类型.contains(\"1\")";
        textList.add(text);

        text = "卡类型.contains(\"3\")";
        textList.add(text);

        text = "卡logo.contains(\"46\")";
        textList.add(text);

        long totalTime = 0 ;
        for (int i = 0 ; i < 100 ; i++ ){
            mcc = Arrays.asList("1","2","3","5");
            GrvyEngine.getInstance().getEngine().put("卡类型",mcc);
            long start = System.currentTimeMillis();

            int idx = ThreadLocalRandom.current().nextInt(textList.size());
            String script = textList.get(idx);
            Object obj = GrvyEngine.getInstance().getEngine().eval(script);
            long time = System.currentTimeMillis() - start;
            totalTime += time;
            System.out.println("script : " + script + "   obj: "+obj+" cost time:" +time);



        }
        System.out.println(" grvy engine total time: " + totalTime);
    }

    public void grvyExpressExecReturn() throws ScriptException {
        List<String> mcc = Arrays.asList("1","2","3");
        String mccStr = "3";
        engine.put("卡类型",mcc);
        engine.put("卡",mccStr);
        List<String> cardLogoList = Arrays.asList("123","456");
        engine.put("卡logo",cardLogoList);
        StringBuilder str = new StringBuilder();
        str.append("def list = [\"1\", 2, \"3\"];");
        str.append("if (list.contains(卡) && 卡类型.contains(卡) && 卡logo.contains(\"123\") )return list.get(1) \r\n " +
                "return list.get(0)");
        String script = str.toString();
        /*for (int i = 0 ; i < 100 ; i++ ){
            long start = System.currentTimeMillis();
            Object obj = engine.eval(script);
            long time = System.currentTimeMillis() - start;
            System.out.println("script : " + script + "   obj: "+obj+" cost time:" +time);
        }*/

        long start = System.currentTimeMillis();
        Object obj = engine.eval(script);
        long time = System.currentTimeMillis() - start;
        System.out.println("script : " + script + "   obj: "+obj+" cost time:" +time);

    }

    public static void main(String[] args) throws ScriptException {
        for (int i = 0 ; i< 100 ; i++){
            GrvyEngine engine = new GrvyEngine();
            engine.grvyExpressExecReturn();
        }


    }
}
