package com.gce.bootstrap;

import com.gce.config.AopConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @Author chengen.gu
 * @DATE 2020/2/4 3:19 下午
 */
@SpringBootApplication
@Import({AopConfiguration.class})
public class SpringApplicationStartup {
    public static void main(String[] args) {

        SpringApplication.run(SpringApplicationStartup.class, args);

    }
}
