package com.guce.service.impl;

import com.guce.service.IExamplePluginService;
import org.springframework.stereotype.Component;

/**
 * @Author chengen.gce
 * @DATE 2021/9/21 9:05 上午
 */
@Component
public class ExamplePlugins3ServiceImpl implements IExamplePluginService {
    @Override
    public String helloPlugin() {
        return "hello plugins3";
    }
}
