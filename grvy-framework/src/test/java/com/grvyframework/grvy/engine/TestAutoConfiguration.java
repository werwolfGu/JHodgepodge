package com.grvyframework.grvy.engine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.grvyframework.executor.GrvyScriptEngineExecutor;
import com.grvyframework.handle.IGrvyScriptResultHandler;
import com.grvyframework.handle.impl.DefaultGrvyScriptResultHandler;
import com.grvyframework.model.BaseScriptEvalResult;
import com.grvyframework.model.BaseScriptEvalResultCalculateParam;
import com.grvyframework.model.GrvyRequest;
import com.grvyframework.model.GrvyResponse;
import com.grvyframework.model.GrvyRuleConfigEntry;
import com.grvyframework.reduce.Reduce;
import com.grvyframework.spring.autoconfigure.GrvyAutoConfiguration;
import com.grvyframework.spring.container.SpringApplicationBean;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.script.Bindings;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author chengen.gu
 * @DATE 2020/1/20 8:11 下午
 */
public class TestAutoConfiguration {

    private AnnotationConfigApplicationContext context;

    @After
    public void tearDown() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Before
    public void before() {
        this.context = new AnnotationConfigApplicationContext();
        context.register(GrvyAutoConfiguration.class);
        context.refresh();
    }

    @Test
    public void testGrvyScriptEngine() throws ScriptException, ExecutionException, InterruptedException {

        List<String> card = Arrays.asList("1", "2", "3");
        GrvyScriptEngine grvyScriptEngine = this.context.getBean(GrvyScriptEngine.class);
        grvyScriptEngine.bingingGlobalScopeMapper("交易码", "1101");
        grvyScriptEngine.bindingEngineScopeMapper("渠道", "NET");
        System.out.println(grvyScriptEngine.getCurrentGrvyScriptEngine().getBindings(100));
        String script = " def list = [\"1101\",\"1411\",\"1121\",\"1131\"]\n" +
                "    def channels = [\"NET\",\"NCUP\"]\n" +
                "    if (list.contains(交易码) && channels.contains(渠道))\n" +
                "        return true ";
        Object obj = grvyScriptEngine.eval(script);
        System.out.println("obj:" + obj);

    }

    @Test
    public void testGrvyScriptEngineExecutor() throws Exception {

        List<String> mccList = new ArrayList<>();
        int seed = 10;
        for (int i = 0; i < seed; i++) {
            mccList.add(String.valueOf(i));
        }

        GrvyResponse response = new GrvyResponse();

        GrvyRequest request = new GrvyRequest();

        List<GrvyRuleConfigEntry> grvyRuleInfoList = getRuleConfigInfos();
        request.setGrvyRuleInfoList(grvyRuleInfoList);

        Bindings bindings = new SimpleBindings();
        bindings.put("交易码", "1101");
        bindings.put("渠道", "NET");
        request.setBindings(bindings);
        BaseScriptEvalResultCalculateParam calculateParam = new BaseScriptEvalResultCalculateParam();
        calculateParam.setAmt(2L);
        request.setCalculateParam(calculateParam);

        GrvyScriptEngineExecutor grvyScriptEngineExecutor = this.context.getBean(GrvyScriptEngineExecutor.class);
        List<BaseScriptEvalResult> evalResults = grvyScriptEngineExecutor.serialExecutor(request, response, Reduce.allof(Objects::nonNull));

        System.out.println(evalResults);
        assert "2".equals(evalResults.get(0).getAmt().toString());

    }

    @Test
    public void testAsynGrvyScriptEngineExecutor() throws Exception {

        List<CompletableFuture> list = new ArrayList<>();
        List<String> mccList = new ArrayList<>();
        int seed = 1000;
        for (int i = 0; i < seed; i++) {
            mccList.add(String.valueOf(i));
        }
        for (int i = 0; i < seed; i++) {

            GrvyRequest request = new GrvyRequest();
            GrvyResponse response = new GrvyResponse();
            String script = " def list = [\"1101\",\"1411\",\"1121\",\"1131\"]\n" +
                    "    def channels = [\"NET\",\"NCUP\"]\n" +
                    "    if (list.contains(交易码) && channels.contains(渠道))\n" +
                    "        return true ";
            List<GrvyRuleConfigEntry> grvyRuleInfoList = new ArrayList<>();

            request.setGrvyRuleInfoList(grvyRuleInfoList);
            GrvyRuleConfigEntry ruleInfo = new GrvyRuleConfigEntry();
            grvyRuleInfoList.add(ruleInfo);
            IGrvyScriptResultHandler handle = SpringApplicationBean.getBean(DefaultGrvyScriptResultHandler.class);
            ruleInfo.setGrvyScriptResultHandler(handle);
            ruleInfo.setScript(script);
            Bindings bindings = new SimpleBindings();
            bindings.put("交易码", "1101");
            bindings.put("渠道", "NET");
            Integer idx = ThreadLocalRandom.current().nextInt(seed);
            bindings.put("卡", idx.toString());
            request.setBindings(bindings);
            try {

                GrvyScriptEngineExecutor grvyScriptEngineExecutor = this.context.getBean(GrvyScriptEngineExecutor.class);

                BaseScriptEvalResultCalculateParam calculateParam = new BaseScriptEvalResultCalculateParam();

                calculateParam.setAmt(Long.valueOf(idx));
                request.setCalculateParam(calculateParam);
                List<BaseScriptEvalResult> evalResults = grvyScriptEngineExecutor.parallelExecutor(request, response, Reduce.firstOf(Objects::nonNull));
                System.out.println(evalResults);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static List<GrvyRuleConfigEntry> getRuleConfigInfos() {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("ruleconfigInfo.json");

        List<GrvyRuleConfigEntry> list = new ArrayList<>();
        try {
            String str = IOUtils.toString(in, "UTF-8");
            JSONArray arr = JSON.parseArray(str);

            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                GrvyRuleConfigEntry entry = new GrvyRuleConfigEntry();
                entry.setGrvyResultClazzPath(obj.getString("result_handle"));
                entry.setScript(obj.getString("script"));
                list.add(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
