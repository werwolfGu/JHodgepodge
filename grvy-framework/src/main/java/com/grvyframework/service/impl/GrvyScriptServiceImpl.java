package com.grvyframework.service.impl;

import com.grvyframework.grvy.engine.GrvyScriptEngineClient;
import com.grvyframework.service.IGrvyScriptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author chengen.gu
 * @DATE 2020/2/14 3:48 下午
 */
@Service("grvyScriptService")
public class GrvyScriptServiceImpl implements IGrvyScriptService {

    private Logger logger = LoggerFactory.getLogger(GrvyScriptServiceImpl.class);

    @Resource(name = "grvyScriptEngineClient")
    private GrvyScriptEngineClient engineClient;
    @Override
    public String refreshGrvyScriptInfo(String grvyKey) {

        if (Objects.isNull(grvyKey)){
            logger.warn("grvy key is empty!");
        }

       /* String keyArr[] = grvyKey.split(",");
        List<String> keyList = Lists.newArrayList(keyArr);*/
        engineClient.flushLoadingCache(grvyKey);

        return "success";
    }
}
