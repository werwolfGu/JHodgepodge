package com.guce.spring.bootstrap;

import com.guce.spring.autoconfigurature.AutoConfigurature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @Author chengen.gu
 * @DATE 2020/2/13 2:59 下午
 */
@SpringBootApplication
@Import(value = AutoConfigurature.class)
public class SpringbootStartup {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootStartup.class, args);
    }
}
