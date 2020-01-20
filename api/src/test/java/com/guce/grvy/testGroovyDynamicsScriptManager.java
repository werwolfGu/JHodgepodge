package com.guce.grvy;

import com.guce.groovy.manager.GroovyDynamicsScriptManager;
import com.guce.groovy.pool.GrvyEngineThreadPool;
import com.guce.grvy.service.IGrvy;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author guchengen495
 * @date 2020-01-20 10:12
 * @description
 */
public class testGroovyDynamicsScriptManager {

    @Test
    public void testCacheGrvyScript(){
        String clazzpath = "/Users/guchengen495/workspace/github/JHodgepodge/api/src/test/java/com/guce/grvy/service/impl/GrvyImpl.groovy";
        try {
            long totalTime = System.currentTimeMillis() ;
            GroovyDynamicsScriptManager.getInstance().getGrvyScriptMapper().put("grvy",clazzpath);
            List<CompletableFuture> list = new ArrayList<>(10000);
            for (int i = 0 ; i < 1000 ; i++ ){
                CompletableFuture future = CompletableFuture.runAsync( ()->{
                    long start = System.currentTimeMillis();

                    GroovyDynamicsScriptManager<IGrvy> grvyManager = GroovyDynamicsScriptManager.getInstance();
                    IGrvy iGrvy = null;
                    try {
                        iGrvy = grvyManager.getGrvyClassCache().get("grvy");
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    String name = iGrvy.printGrvy(" grvy classloader");
                    long time = System.currentTimeMillis() - start;
                    System.out.println("name :" + name + " cost time:{}" + time);
                }, GrvyEngineThreadPool.getPoolExecutor());
                list.add(future);
            }
            CompletableFuture[] futureArr = list.toArray(new CompletableFuture[list.size()]);
            CompletableFuture future = CompletableFuture.allOf(futureArr);
            System.out.println("total time: " + (System.currentTimeMillis() - totalTime));
            future.get();
            System.out.println("total time: " + (System.currentTimeMillis() - totalTime));
            //IGrvy iGrvy = GrvyClassLoader.loaderInstance(clazzpath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
