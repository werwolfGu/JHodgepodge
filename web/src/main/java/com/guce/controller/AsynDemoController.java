package com.guce.controller;

import com.guce.annotation.LogAnnotation;
import com.guce.chain.ChainServiceDemo1;
import com.guce.chain.executor.ChainExecutor;
import com.guce.scope.CustomScopeServiceTest;
import com.guce.scope.CustomSope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;

@LogAnnotation
@RestController
@RequestMapping("/async")
public class AsynDemoController implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(AsynDemoController.class);
    /*@Resource(name = "demoService")
    private DemoService demoService ;

    @RequestMapping(value = "/hello")
    public CompletableFuture<String> asynHelloworld(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thread.sleep(3000);

        return CompletableFuture.completedFuture(demoService.helloService("name2",null));

    }*/

    @Autowired
    private ChainServiceDemo1 chainServiceDemo1;

    @Autowired
    private ChainExecutor chainExecutor;


    @Autowired
    private CustomScopeServiceTest customScopeServiceTest;

    @Autowired
    private CustomSope customSope;

    @RequestMapping(value = "/helloV1")
    public DeferredResult<String> asynHelloworld1() {
        DeferredResult<String> deferredResult = new DeferredResult<String>();

        try {
            logger.info("=============" + customScopeServiceTest);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("hellov1ddddddddd========");
        deferredResult.setResult("hello world ->" + System.currentTimeMillis());

        return deferredResult;
    }

    @RequestMapping(value = "/refreshScope")
    public String refreshScope(String name) {
        return customSope.remove(name).toString();
    }

    @RequestMapping(value = "/chainService")
    public DeferredResult<String> chainServiceDemo() {
        DeferredResult<String> deferredResult = new DeferredResult<String>();

        chainExecutor.execute("serviceDemo", null, null);
        chainExecutor.execute("annChainSerivce", null, null);

        deferredResult.setResult("hello world ->" + System.currentTimeMillis());

        return deferredResult;
    }

    @PostConstruct
    public void init() {
        logger.warn("+++++++++++++++++++init {}  ; {}", chainExecutor, chainServiceDemo1);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.warn("+++++++++++++++++++afterPropertiesSet {} ; {}", chainExecutor, chainServiceDemo1);
    }
}
