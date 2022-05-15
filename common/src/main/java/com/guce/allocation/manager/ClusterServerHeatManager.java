package com.guce.allocation.manager;

import com.guce.common.utils.NetUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author chengen.gce
 * @DATE 2022/5/12 15:56
 */
@Slf4j
@Component
public class ClusterServerHeatManager implements InitializingBean , DisposableBean {

    private RedissonClient redissonClient;
    private final static String SERVER_HEAT_ROUTE_MAP_KEY = "cluster_server_heat_route_key";
    private final static String CLUSTER_SERVER_ROUTE_INFO_TOPIC = "cluster_server_route_info_topic";
    private ScheduledExecutorService scheduledExecutorService ;
    private static final String host ;

    static {
        host = NetUtils.getLocalHost();
    }

    /**
     * 服务器心跳管理  发送心跳服务
     */
    class ClusterServerHeatbeat implements Runnable {
        @Override
        public void run() {
            try{
                sendHeatbeat();
            }catch (Exception e){
                log.warn("host {}  publish to redis connection exception (retry later)", host, e);
            }
        }
    }

    /**
     * 发送心跳信息
     */
    public void sendHeatbeat() {
        Long value = System.currentTimeMillis();
        RMap<String,Long> rMap = redissonClient.getMap(SERVER_HEAT_ROUTE_MAP_KEY);
        rMap.put(host,value);
    }

    /**
     * 刷新路由信息
     */
    class ReflushRouteInfo implements Runnable {

        @Override
        public void run() {
            updateRouteInfo();
        }
    }

    public void updateRouteInfo () {
        long curr = System.currentTimeMillis();
        try{
            Map<String,Long> routeMap = redissonClient.getMap(SERVER_HEAT_ROUTE_MAP_KEY);
            List<String> clusterServerList = ClusterAllocationManager.getClusterServerList();
            boolean updateRoute = false;

            if (routeMap == null) {
                updateRoute = true;
                clusterServerList = new ArrayList<>();
            } else {
                List<String> tmpSet = new ArrayList<>();
                for (Map.Entry<String,Long> entry : routeMap.entrySet()) {
                    String key = entry.getKey();
                    Long updateTime = entry.getValue();
                    if (Math.abs(curr - updateTime) > 6000) {
                        ////删除下线服务器
                        routeMap.remove(key);
                        log.warn("host {} is down,please check it! " , key);
                        continue;
                    }
                    tmpSet.add(key);
                    if (!clusterServerList.contains(key)){
                        updateRoute = true;
                    }
                }
                if (updateRoute || tmpSet.size() != clusterServerList.size()){
                    clusterServerList = tmpSet;
                }
            }
            if (updateRoute) {
                ClusterAllocationManager.setClusterServerList(clusterServerList);
                log.warn("refresh server route info : {}" , clusterServerList);
            }
        }catch (Exception e) {
            log.error("refresh route info exception ;" ,e);
        }
    }

    public void publishRouteInfo() {
        RTopic routeInfoTopic = redissonClient.getTopic(CLUSTER_SERVER_ROUTE_INFO_TOPIC);
        routeInfoTopic.addListener(String.class, (CharSequence channel, String msg) -> {
            /////发送心跳，同时更新路由信息
            sendHeatbeat();
            updateRouteInfo();
        });
        routeInfoTopic.publish(host);
    }

    @Override
    public void destroy() throws Exception {
        RMap<String,Long> routeMap = redissonClient.getMap(SERVER_HEAT_ROUTE_MAP_KEY);
        routeMap.remove(host);
        log.info("spring destroy，将host {} 从路由列表中剔除 , current route info : {}" , host,routeMap);
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        int CPU_CORE = Runtime.getRuntime().availableProcessors();
        scheduledExecutorService = new ScheduledThreadPoolExecutor(CPU_CORE);
        publishRouteInfo();
        scheduledExecutorService
                .scheduleAtFixedRate(new ClusterServerHeatbeat(),0,1, TimeUnit.SECONDS);

        scheduledExecutorService
                .scheduleAtFixedRate(new ReflushRouteInfo(),3,3, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook( new Thread(() -> {
            RMap<String,Long> routeMap = redissonClient.getMap(SERVER_HEAT_ROUTE_MAP_KEY);
            routeMap.remove(host);
            log.info("服务关闭，将host {} 从路由列表中剔除 , current route info : {}" , host,routeMap);

        }));
    }

}
