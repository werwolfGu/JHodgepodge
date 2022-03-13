package com.ecg;

import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.log.RecordLog;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

/**
 * @Author chengen.gce
 * @DATE 2021/11/19 8:34 下午
 */
public abstract class RedisDataSource<T> extends AbstractDataSource<String, T> {

    private RedissonClient redisClient ;
    private final String ruleKey;

    public RedisDataSource(RedissonClient redissonClient , String ruleKey , String channel ,Converter<String, T> parser) {

        super(parser);
        this.redisClient = redissonClient;
        this.ruleKey = ruleKey;
        loadInitialConfig();
        subscribeFromChannel(channel);

    }

    /**
     * 初始化规则配置信息
     */
    private void loadInitialConfig() {
        try {
            T newValue = loadConfig();
            if (newValue == null) {
                RecordLog.warn("[RedisDataSource] WARN: initial config is null, you may have to check your data source");
            }
            getProperty().updateValue(newValue);
        } catch (Exception ex) {
            RecordLog.warn("[RedisDataSource] Error when loading initial config", ex);
        }
    }

    private void subscribeFromChannel(String channel) {

        RTopic topic = redisClient.getTopic(channel);
        topic.addListener(String.class ,(type,message) -> {

            getProperty().updateValue(parser.convert(message));
        });
    }


    @Override
    public String readSource() {
        if (this.redisClient == null ) {
            throw new IllegalStateException("Redis client or Redis Cluster client has not been initialized or error occurred");
        }

        RBucket<String> bucket = redisClient.getBucket(ruleKey);
        String source = bucket.get();
        if (StringUtils.isBlank(source)){
            source = defaultReadSrouce();
        }
        return source;
    }

    /**
     * 从redis中没读取到数据的话，可能就需要从别的地方去读取数据了 比如数据库；
     * @return
     */
    public abstract String defaultReadSrouce();

    @Override
    public void close() {
        if (redisClient != null) {
            redisClient.shutdown();
        }
    }

}
