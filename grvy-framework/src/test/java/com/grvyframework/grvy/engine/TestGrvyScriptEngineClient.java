package com.grvyframework.grvy.engine;

import com.grvyframework.spring.autoconfigure.GrvyAutoConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.collections.Maps;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author chengen.gu
 * @DATE 2020/1/23 6:52 下午
 */
public class TestGrvyScriptEngineClient {

    private static Logger logger = LoggerFactory.getLogger(TestGrvyScriptEngineClient.class);

    private AnnotationConfigApplicationContext context;


    @Before
    public void before() {
        this.context = new AnnotationConfigApplicationContext();
        context.register(GrvyAutoConfiguration.class);
        context.refresh();
    }

    @After
    public void tearDown() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void testScriptEngine() throws ScriptException, ExecutionException {
        GrvyScriptEngineClient client = this.context.getBean(GrvyScriptEngineClient.class);
        String script = " def list = [\"1101\",\"1411\",\"1121\",\"1131\"]\n" +
                "    def channels = [\"NET\",\"NCUP\"]\n" +
                "    if (list.contains(交易码) && channels.contains(渠道))\n" +
                "        return true ";
        Map<String,Object> map = Maps.newHashMap();
        map.put("交易码","1101");
        map.put("渠道","NET");
        ScriptContext scriptContext = new SimpleScriptContext();
        Bindings binding = new SimpleBindings();
        binding.putAll(map);
        scriptContext.setBindings(binding,ScriptContext.ENGINE_SCOPE);
        Boolean flag = client.eval(script,scriptContext);
        System.out.println("flag-> " + flag);
    }

    @Test
    public void testMutliScriptEngine() throws ExecutionException, InterruptedException {
        GrvyScriptEngineClient client = this.context.getBean(GrvyScriptEngineClient.class);
        List<String> scrpList = new ArrayList<>();
        scrpList.add(" def list = [\"1101\",\"1411\",\"1121\",\"1131\"]\n" +
                "    def channels = [\"NET\",\"NCUP\"]\n" +
                "    if (list.contains(交易码) && channels.contains(渠道))\n" +
                "        return true ");

        scrpList.add( " def list = [\"1101\",\"1411\",\"1121\",\"1131\"]\n" +
                "    def channels = [\"unknow\",\"NCUP\"]\n" +
                "    if (list.contains(交易码) && channels.contains(渠道))\n" +
                "        return true ");

        scrpList.add( " def list = [\"1101\",\"1411\",\"1121\",\"1131\"]\n" +
                "    def channels = [\"unknow\",\"xyz\"]\n" +
                "    if (list.contains(交易码) && channels.contains(渠道))\n" +
                "        return true ");
        scrpList.add( " def list = [\"1101\",\"1411\",\"1121\",\"1131\"]\n" +
                "    def channels = [\"NET\",\"xyz\"]\n" +
                "    if (list.contains(交易码) && channels.contains(渠道))\n" +
                "        return true ");
        List<String> list = Arrays.asList("NET","unknow" ,"NCUP2","xyz3");

        List<CompletableFuture> futures = new ArrayList<>();
        for (int i = 0 ; i < 200 ; i++ ){

            CompletableFuture future = CompletableFuture.runAsync( () -> {

                long start = System.currentTimeMillis();
                Map<String,Object> map = Maps.newHashMap();
                map.put("交易码","1101");
                int idx = ThreadLocalRandom.current().nextInt(4);
                String param = list.get(idx);
                map.put("渠道",param);
                ScriptContext scriptContext = new SimpleScriptContext();
                Bindings binding = new SimpleBindings();
                binding.putAll(map);
                scriptContext.setBindings(binding,ScriptContext.ENGINE_SCOPE);
                String script = scrpList.get(idx);

                try {
                    Object obj = client.eval(script,scriptContext);
                    logger.info(" script:{} ; param:{} ; \n result: {}  ; idx :{}; cost time:{}"
                            ,script,map,obj,idx , System.currentTimeMillis() - start);

                } catch (ScriptException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
            futures.add(future);
        }

        CompletableFuture[] arr = futures.toArray(new CompletableFuture[0]);
        CompletableFuture future = CompletableFuture.allOf(arr);
        future.get();
        logger.info("=========cpu core :{}==============",Runtime.getRuntime().availableProcessors());
    }
}
