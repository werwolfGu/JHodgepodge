package com.guce.configuration;

import com.gitee.starblues.integration.AutoIntegrationConfiguration;
import com.gitee.starblues.integration.application.AutoPluginApplication;
import com.gitee.starblues.integration.user.PluginUser;
import com.guce.listener.AppPluginListener;
import com.guce.listener.Pf4jPluginListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author chengen.gce
 * @DATE 2021/9/17 10:44 下午
 */
@Configuration
@Import(AutoIntegrationConfiguration.class)
public class PluginConfig {

    @Bean
    public AutoPluginApplication pluginApplication(){
        // 实例化自动初始化插件的PluginApplication
        AutoPluginApplication pluginApplication = new AutoPluginApplication();
        pluginApplication.addPf4jStateListener(Pf4jPluginListener.class);
        pluginApplication.addListener(AppPluginListener.class);
        return pluginApplication;
    }

    @Bean
    public PluginUser pluginUser() {

        AutoPluginApplication application = pluginApplication();
        return application.getPluginUser();
    }

}
