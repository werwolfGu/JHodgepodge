package com.guce.interceptor;

import com.guce.chain.executor.ChainExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InterceptorAsyncHandler implements AsyncHandlerInterceptor{

    private final Logger logger = LoggerFactory.getLogger(InterceptorAsyncHandler.class);

    @Autowired
    private ChainExecutor chainExecutor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //异步请求分2步
        //  1、请求接口数据但是返回的数据是 CompletableFuture...
        //  2、获取接口返回的结果目前spring获取接口返回结果时也会调用 Interceptor；
        WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
        chainExecutor.execute("serviceDemo", null, null);
        if(asyncManager.hasConcurrentResult()){
            return true;
        }

        logger.info("preHandle uri:{};  request:{} ; Thread name:{}"
                ,request.getRequestURI(),request,Thread.currentThread().getName());
        return true;
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger.info( " asyn Handler : " + System.currentTimeMillis() + "  " + Thread.currentThread());
    }
}
