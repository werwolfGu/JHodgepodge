package com.guce.redis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by chengen.gu on 2018/10/11.
 */
public class JedisClusterPipelineTest {

    @Test
    public void test() {
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        nodes.add(new HostAndPort("127.0.0.1", 6379));
        nodes.add(new HostAndPort("127.0.0.1", 6380));

        JedisCluster jedisCluster = new JedisCluster(nodes);

        long s = System.currentTimeMillis();

        JedisClusterPipeline jedisPipeline = JedisClusterPipeline.pipelined(jedisCluster);
        jedisPipeline.refreshCluster();
        List<Object> batchResult = null;
        try {
            // batch write
            for (int i = 0; i < 10000; i++) {
                jedisPipeline.set("k" + i, "v1" + i);
            }
            jedisPipeline.sync();

            // batch read
            for (int i = 0; i < 10000; i++) {
                jedisPipeline.get("k" + i);
            }
            batchResult = jedisPipeline.syncAndReturnAll();
        } finally {
            jedisPipeline.close();
        }

        // output time
        long t = System.currentTimeMillis() - s;
        System.out.println(t);

        System.out.println(batchResult.size());

        // 实际业务代码中，close要在finally中调，这里之所以没这么写，是因为懒
        try {
            jedisCluster.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
