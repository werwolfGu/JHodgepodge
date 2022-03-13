package com.guce.service.impl;

import com.guce.service.IExamplePluginService;
import org.springframework.stereotype.Service;

/**
 * @Author chengen.gce
 * @DATE 2021/9/18 9:25 上午
 */
@Service
public class ExamplePluginServiceImpl implements IExamplePluginService {

    @Override
    public String helloPlugin() {
        return "hello plugin";
    }
}
